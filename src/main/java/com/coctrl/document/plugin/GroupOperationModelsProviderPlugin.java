package com.coctrl.document.plugin;

import org.springframework.util.ReflectionUtils;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.OperationModelContextsBuilder;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * 类 GroupOperationModelsProviderPlugin 功能描述：
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2021/09/18 11:16
 */
public class GroupOperationModelsProviderPlugin implements OperationModelsProviderPlugin {
    public static final Field CONTEXTS = ReflectionUtils.findField(OperationModelContextsBuilder.class, "contexts");

    {
        CONTEXTS.setAccessible(true);
    }

    public static final Field DOCUMENTATION_TYPE = ReflectionUtils.findField(ModelContext.class, "documentationType");

    {
        DOCUMENTATION_TYPE.setAccessible(true);
    }

    @Override
    public void apply(RequestMappingContext context) {
        // documentationType附加 group信息
        // 查找方法参数group参数
        context.getParameters().stream()
                .forEach(rmp -> {
                    Class[] groups = GroupOperationBuilderPlugin.getGroups(rmp, ResolvedMethodParameter::findAnnotation);
                    if (groups != null) {
                        ((Set<ModelContext>) ReflectionUtils.getField(CONTEXTS, context.operationModelsBuilder())).stream()
                                .filter(mc -> mc.getType().equals(context.alternateFor(rmp.getParameterType())))
                                .forEach(mc -> ReflectionUtils.setField(DOCUMENTATION_TYPE,
                                        mc,
                                        new GroupDocumentationType(mc.getDocumentationType(), groups)));
                    }
                });
        // 查找返回值group参数
        Class[] groups = GroupOperationBuilderPlugin.getGroups(context, RequestMappingContext::findAnnotation);
        if (groups != null) {
            ((Set<ModelContext>) ReflectionUtils.getField(CONTEXTS, context.operationModelsBuilder())).stream()
                    .filter(ModelContext::isReturnType)
                    .forEach(mc -> ReflectionUtils.setField(DOCUMENTATION_TYPE,
                            mc,
                            new GroupDocumentationType(mc.getDocumentationType(), groups)));
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

}