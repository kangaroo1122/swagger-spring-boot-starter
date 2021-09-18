package com.coctrl.document.plugin;

import com.google.common.base.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * @author jam
 */
public class GroupOperationBuilderPlugin implements OperationBuilderPlugin {
    public static final Field REQUEST_CONTEXT = ReflectionUtils.findField(OperationContext.class, "requestContext");

    {
        REQUEST_CONTEXT.setAccessible(true);
    }

    public static final Field TYPE = ReflectionUtils.findField(ModelRef.class, "type");

    {
        TYPE.setAccessible(true);
    }

    public static <T, R> Class[] getGroups(T t, BiFunction<T, Class<? extends Annotation>, Optional<R>> findAnnotation) {
        Optional<R> opValidated = findAnnotation.apply(t, Validated.class);
        if (opValidated.isPresent()) {
            return ((Validated) opValidated.get()).value();
        } else {
            Optional<R> opValid = findAnnotation.apply(t, Valid.class);
            if (opValid.isPresent()) {
                return new Class[0];
            }
        }
        return null;
    }

    @Override
    public void apply(OperationContext context) {
        RequestMappingContext requestMappingContext = (RequestMappingContext) ReflectionUtils.getField(REQUEST_CONTEXT, context);

        // 查找方法参数group参数
        Operation operation = context.operationBuilder().build();
        operation.getParameters().stream()
                .filter(parameter -> "body".equals(parameter.getParamType()))
                .forEach(parameter ->
                        requestMappingContext.getParameters().stream()
                                .filter(rmp -> parameter.getName().equals(rmp.defaultName().get()))
                                .forEach(rmp -> {
                                    Class[] groups = GroupOperationBuilderPlugin.getGroups(rmp, ResolvedMethodParameter::findAnnotation);
                                    if (groups != null) {
                                        ReflectionUtils.setField(TYPE, parameter.getModelRef(),
                                                GroupDocumentationType.buildModelRefId(parameter.getModelRef().getType(), groups));
                                    }
                                })
                );
        // 查找返回值group参数
        Class[] groups = GroupOperationBuilderPlugin.getGroups(context, OperationContext::findAnnotation);
        if (groups != null) {
            operation.getResponseMessages().stream().filter(responseMessage -> responseMessage.getCode() == HttpStatus.OK.value())
                    .forEach(responseMessage -> {
                        Optional<ModelReference> item = responseMessage.getResponseModel().itemModel();
                        if (item.isPresent()) {
                            ReflectionUtils.setField(TYPE, item.get(),
                                    GroupDocumentationType.buildModelRefId(item.get().getType(), groups));
                        } else {
                            ReflectionUtils.setField(TYPE, responseMessage.getResponseModel(),
                                    GroupDocumentationType.buildModelRefId(responseMessage.getResponseModel().getType(), groups));
                        }
                    });
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
