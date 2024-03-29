package com.kangaroohy.swagger.configuration;

import com.kangaroohy.swagger.plugin.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import springfox.documentation.schema.property.bean.AccessorsProvider;
import springfox.documentation.schema.property.field.FieldProvider;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * 类 ValidGroupConfig 功能描述：
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2021/09/18 21:22
 */
@Configuration
@ConditionalOnClass(Docket.class)
public class CustomPluginAutoConfiguration {

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public GroupOperationModelsProviderPlugin groupOperationModelsProviderPlugin() {
        return new GroupOperationModelsProviderPlugin();
    }

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public GroupModelBuilderPlugin groupModelBuilderPlugin() {
        return new GroupModelBuilderPlugin();
    }

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public GroupModelPropertyBuilderPlugin groupModelPropertyBuilderPlugin() {
        return new GroupModelPropertyBuilderPlugin();
    }

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public DisplayEnumPlugin displayEnumPlugin() {
        return new DisplayEnumPlugin();
    }

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public GroupExpandedParameterBuilderPlugin groupExpandedParameterBuilderPlugin() {
        return new GroupExpandedParameterBuilderPlugin();
    }

    @Bean
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public GroupOperationBuilderPlugin groupOperationBuilderPlugin() {
        return new GroupOperationBuilderPlugin();
    }

    @Bean
    @Primary
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public GroupModelAttributeParameterExpander groupModelAttributeParameterExpander(FieldProvider fields, AccessorsProvider accessors, EnumTypeDeterminer enumTypeDeterminer) {
        return new GroupModelAttributeParameterExpander(fields, accessors, enumTypeDeterminer);
    }
}
