package com.coctrl.document.plugin;

import com.coctrl.validation.constraints.EnumInteger;
import com.coctrl.validation.constraints.EnumString;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 类 GroupModelPropertyBuilderPlugin 功能描述：
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2021/09/18 11:16
 */
public class GroupModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

    @Override
    public void apply(ModelPropertyContext context) {
        // 附加额外配置
        GroupDocumentationType documentationType = (GroupDocumentationType) context.getDocumentationType();
        AnnotatedField field = context.getBeanPropertyDefinition().get().getField();

        apply(field, AnnotatedField::getAnnotation,
                context.getBuilder(), ModelPropertyBuilder::isHidden, ModelPropertyBuilder::required,
                ModelPropertyBuilder::allowableValues, ModelPropertyBuilder::pattern, documentationType.groups);
    }

    public static <A, P> void apply(A annotationObject, BiFunction<A, Class, Annotation> getAnnotation,
                                    P propertyObject, BiConsumer<P, Boolean> isHidden, BiConsumer<P, Boolean> required,
                                    BiConsumer<P, AllowableValues> allowableValues, BiConsumer<P, String> pattern, Class[] groups) {
        Null annotationNull = (Null) getAnnotation.apply(annotationObject, Null.class);
        if (annotationNull != null && inGroups(annotationNull.groups(), groups)) {
            isHidden.accept(propertyObject, true);
        }
        NotNull annotationNotNull = (NotNull) getAnnotation.apply(annotationObject, NotNull.class);
        if (annotationNotNull != null && inGroups(annotationNotNull.groups(), groups)) {
            required.accept(propertyObject, true);
        }
        Size annotationSize = (Size) getAnnotation.apply(annotationObject, Size.class);
        if (annotationSize != null && inGroups(annotationSize.groups(), groups)) {
            allowableValues.accept(propertyObject, new AllowableRangeValues(Integer.toString(annotationSize.min()), Integer.toString(annotationSize.max())));
        }
        Min annotationMin = (Min) getAnnotation.apply(annotationObject, Min.class);
        Max annotationMax = (Max) getAnnotation.apply(annotationObject, Max.class);
        if (annotationMin != null && annotationMax != null
                && inGroups(annotationMin.groups(), groups) && inGroups(annotationMax.groups(), groups)) {
            allowableValues.accept(propertyObject, new AllowableRangeValues(Long.toString(annotationMin.value()), Long.toString(annotationMax.value())));
        } else if (annotationMax != null && inGroups(annotationMax.groups(), groups)) {
            allowableValues.accept(propertyObject, new AllowableRangeValues("0", Long.toString(annotationMax.value())));
        } else if (annotationMin != null && inGroups(annotationMin.groups(), groups)) {
            allowableValues.accept(propertyObject, new AllowableRangeValues(Long.toString(annotationMin.value()), ""));
        }
        Pattern annotationPattern = (Pattern) getAnnotation.apply(annotationObject, Pattern.class);
        if (annotationPattern != null && inGroups(annotationPattern.groups(), groups)) {
            pattern.accept(propertyObject, annotationPattern.regexp());
        }

        EnumInteger annotationEnumInteger = (EnumInteger) getAnnotation.apply(annotationObject, EnumInteger.class);
        if (annotationEnumInteger != null && inGroups(annotationEnumInteger.groups(), groups)) {
            allowableValues.accept(propertyObject, new AllowableListValues(
                    Arrays.stream(annotationEnumInteger.value()).mapToObj(Integer::toString).collect(Collectors.toList()),
                    "int"
            ));
        }

        EnumString annotationEnumString = (EnumString) getAnnotation.apply(annotationObject, EnumString.class);
        if (annotationEnumString != null && inGroups(annotationEnumString.groups(), groups)) {
            allowableValues.accept(propertyObject, new AllowableListValues(
                    Arrays.asList(annotationEnumString.value()),
                    "string"
            ));
        }
    }

    public static boolean inGroups(Class[] groups, Class[] parentGroups) {
        // groups 至少含有 parentGroups 一个元素
        if(groups.length == 0 ){
            if(parentGroups.length == 0){
                return true;
            }
            // 实体类配置默认Default接口，配置bean validation同样生成swagger
            for (Class pg : parentGroups) {
                if(pg.equals(Default.class)){
                    return true;
                }
            }
        }
        for (Class<?> gc : groups) {
            if(gc.isInstance(Default.class)) {
                return true;
            }
            // 当前group 或 父group
            for (Class pgc : parentGroups) {
                if (pgc.equals(gc) || gc.isAssignableFrom(pgc)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType instanceof GroupDocumentationType;
    }
}
