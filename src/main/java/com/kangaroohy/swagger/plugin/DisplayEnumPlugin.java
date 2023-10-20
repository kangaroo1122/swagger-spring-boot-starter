package com.kangaroohy.swagger.plugin;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import com.kangaroohy.annotation.ApiModelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.schema.Annotations;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类 DisplayEnumPlugin 功能描述：<br/>
 *
 * @author hy
 * @version 0.0.1
 * @date 2023/10/20 17:07
 */
@Slf4j
public class DisplayEnumPlugin implements ModelPropertyBuilderPlugin {
    @Override
    public void apply(ModelPropertyContext modelPropertyContext) {
        //获取当前字段的类型
        final Class fieldType = modelPropertyContext.getBeanPropertyDefinition().get().getField().getRawType();

        //为枚举字段设置注释
        descForEnumFields(modelPropertyContext, fieldType);
    }

    /**
     * 为枚举字段设置注释
     */
    private void descForEnumFields(ModelPropertyContext context, Class fieldType) {
        Optional<ApiModelProperty> annotation = Optional.absent();

        if (context.getAnnotatedElement().isPresent()) {
            annotation = annotation
                    .or(ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            annotation = annotation.or(Annotations.findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class));
        }

        //没有 @ApiModelProperty 直接返回
        if (!annotation.isPresent()) {
            return;
        }

        Class rawPrimaryType;
        try {
            //@ApiModelProperties中的notes指定的class类型
            if (StringUtils.hasText(annotation.get().notes())) {
                rawPrimaryType = Class.forName(annotation.get().notes());
            } else if (fieldType.isEnum()) {
                rawPrimaryType = fieldType;
            } else {
                return;
            }
        } catch (ClassNotFoundException e) {
            if (fieldType.isEnum()) {
                rawPrimaryType = fieldType;
            } else {
                //如果指定的类型无法转化，直接忽略
                return;
            }
        }

        //如果对应的class是一个@ApiModelEnum修饰的枚举类，获取其中的枚举值
        Object[] subItemRecords = null;
        ApiModelEnum apiModelEnum = AnnotationUtils.findAnnotation(rawPrimaryType, ApiModelEnum.class);
        if (null != apiModelEnum && Enum.class.isAssignableFrom(rawPrimaryType)) {
            subItemRecords = rawPrimaryType.getEnumConstants();
        }
        if (null == subItemRecords) {
            return;
        }

        final List<String> displayValues = Arrays.stream(subItemRecords).filter(Objects::nonNull)
                .map(Object::toString).filter(Objects::nonNull).collect(Collectors.toList());

        String joinText = " (" + String.join("; ", displayValues) + ")";
        try {
            Field mField = ModelPropertyBuilder.class.getDeclaredField("description");
            mField.setAccessible(true);
            joinText = mField.get(context.getBuilder()) + joinText;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        final ResolvedType resolvedType = context.getResolver().resolve(fieldType);
        context.getBuilder().description(joinText).type(resolvedType);
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
