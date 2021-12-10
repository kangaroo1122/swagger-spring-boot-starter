package com.coctrl.swagger.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.filter.ProductionSecurityFilter;
import com.github.xiaoymin.knife4j.spring.filter.SecurityBasicAuthFilter;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Autowired
    public SwaggerAutoConfiguration(SwaggerProperties properties){
        this.properties = properties;
    }

    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .groupName(properties.getGroupName() + "-待认证")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex(properties.getAuthc()))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Bean(value = "pubApi")
    public Docket pubApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .groupName(properties.getGroupName() + "-无认证")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.regex(properties.getAnon()))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(SecurityBasicAuthFilter.class)
    @ConditionalOnProperty(name = "coctrl.swagger.certifiable", havingValue = "true")
    public SecurityBasicAuthFilter securityBasicAuthFilter(SwaggerProperties properties) {
        return new SecurityBasicAuthFilter(properties.getCertifiable(), properties.getUsername(), properties.getPassword());
    }

    @Bean
    @ConditionalOnMissingBean(ProductionSecurityFilter.class)
    @ConditionalOnProperty(name = "coctrl.swagger.prod", havingValue = "true")
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
                new ApiKey(properties.getHeader(), properties.getHeader(), "header"));
    }

    private List<SecurityContext> securityContexts() {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.regex(properties.getAuthc()))
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(properties.getHeader(), authorizationScopes));
    }
}
