package com.kangaroohy.swagger.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import com.google.common.base.Predicate;
import com.kangaroohy.enums.PathSelectorsType;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author kangaroo
 * @version 1.0.0
 * @since 2020/02/01
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration {

    private final SwaggerProperties properties;

    private final DefaultListableBeanFactory context;

    @Autowired
    public SwaggerAutoConfiguration(SwaggerProperties properties, DefaultListableBeanFactory context) {
        this.properties = properties;
        this.context = context;
        config();
    }

    private void config() {
        List<SwaggerProperties.Group> groupList = properties.getGroup();
        if (properties.getEnableDefault().equals(Boolean.TRUE)) {
            setDefaultGroup(groupList);
        }

        if (groupList != null) {
            for (int i = 0; i < groupList.size(); i++) {
                SwaggerProperties.Group group = groupList.get(i);
                Docket docket;
                if (!StringUtils.hasText(group.getApis())) {
                    group.setApis(properties.getApis());
                }
                if (group.getAuth().equals(Boolean.TRUE)) {
                    docket = new Docket(DocumentationType.SWAGGER_2)
                            .useDefaultResponseMessages(false)
                            .apiInfo(apiInfo())
                            .groupName(group.getGroupName() + "-需认证")
                            .select()
                            .apis(getApis(group))
                            .paths(getPaths(group))
                            .build()
                            .securitySchemes(securitySchemes())
                            .securityContexts(securityContexts(group));
                } else {
                    docket = new Docket(DocumentationType.SWAGGER_2)
                            .useDefaultResponseMessages(false)
                            .apiInfo(apiInfo())
                            .groupName(group.getGroupName() + "-无认证")
                            .select()
                            .apis(getApis(group))
                            .paths(getPaths(group))
                            .build();
                }
                context.registerSingleton("swaggerGroup" + i, docket);
            }
        }
    }

    @Bean
    @ConditionalOnMissingBean(SecurityBasicAuthFilter.class)
    @ConditionalOnProperty(name = "kangaroohy.swagger.basic-auth", havingValue = "true")
    public SecurityBasicAuthFilter securityBasicAuthFilter(SwaggerProperties properties) {
        return new SecurityBasicAuthFilter(properties.getBasicAuth(), properties.getUsername(), properties.getPassword());
    }

    @Bean
    @ConditionalOnMissingBean(ProductionSecurityFilter.class)
    @ConditionalOnProperty(name = "kangaroohy.swagger.prod", havingValue = "true")
    public ProductionSecurityFilter productionSecurityFilter(SwaggerProperties properties) {
        return new ProductionSecurityFilter(properties.getProd());
    }

    private ApiInfo apiInfo() {
        SwaggerProperties.AppInfo.Contact con = properties.getAppInfo().getContact();
        return new ApiInfoBuilder()
                .title(properties.getAppInfo().getTitle())
                .termsOfServiceUrl(properties.getAppInfo().getTermsOfServiceUrl())
                .description(properties.getAppInfo().getDescription())
                .version(properties.getAppInfo().getVersion())
                .license(properties.getAppInfo().getLicense())
                .licenseUrl(properties.getAppInfo().getLicenseUrl())
                .contact(new Contact(con.getName(), con.getUrl(), con.getEmail()))
                .build();
    }

    private List<ApiKey> securitySchemes() {
        return newArrayList(
                new ApiKey(properties.getAuthHeader(), properties.getAuthHeader(), "header"));
    }

    private List<SecurityContext> securityContexts(SwaggerProperties.Group group) {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(getPaths(group))
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(properties.getAuthHeader(), authorizationScopes));
    }

    private void setDefaultGroup(List<SwaggerProperties.Group> groupList) {
        SwaggerProperties.Group group1 = new SwaggerProperties.Group();
        group1.setPathSelectors(PathSelectorsType.REGEX);
        group1.setPaths("^((?!/pub/).)*$");
        group1.setAuth(true);

        SwaggerProperties.Group group2 = new SwaggerProperties.Group();
        group2.setPathSelectors(PathSelectorsType.REGEX);
        group2.setPaths("^.*/pub/.*$");

        groupList.add(group1);
        groupList.add(group2);
    }

    private Predicate<RequestHandler> getApis(SwaggerProperties.Group group) {
        return StringUtils.hasText(group.getApis()) ? RequestHandlerSelectors.basePackage(group.getApis()) : RequestHandlerSelectors.withClassAnnotation(Api.class);
    }

    private Predicate<String> getPaths(SwaggerProperties.Group group) {
        switch (group.getPathSelectors()) {
            case ANY:
                return PathSelectors.any();
            case NONE:
                return PathSelectors.none();
            case REGEX:
                return StringUtils.hasText(group.getPaths()) ? PathSelectors.regex(group.getPaths()) : PathSelectors.any();
            case ANT:
                return StringUtils.hasText(group.getPaths()) ? PathSelectors.ant(group.getPaths()) : PathSelectors.any();
            default:
                throw new RuntimeException("PathSelectors error");
        }
    }
}
