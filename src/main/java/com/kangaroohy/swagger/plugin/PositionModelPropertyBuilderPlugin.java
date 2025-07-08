package com.kangaroohy.swagger.plugin;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 类 PositionModelPropertyBuilderPlugin 功能描述：<br/>
 *
 * @author hy
 * @version 0.0.1
 * @date 2025/7/8 12:24
 */
public class PositionModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

    // 缓存每个类字段顺序
    private static final Map<Class<?>, Map<String, Integer>> fieldOrderCache = new HashMap<>();

    @Override
    public void apply(ModelPropertyContext context) {
        autoSetPosition(context);
    }

    public static void autoSetPosition(ModelPropertyContext context) {
        if (!context.getBeanPropertyDefinition().isPresent()) {
            return;
        }

        String fieldName = context.getBeanPropertyDefinition().get().getName();
        Class<?> declaringClass = context.getBeanPropertyDefinition().get().getPrimaryMember().getDeclaringClass();

        Integer position = getFieldPosition(declaringClass, fieldName);
        if (position != null) {
            context.getBuilder().position(position);
        }
    }

    public static Integer getFieldPosition(Class<?> clazz, String fieldName) {
        Map<String, Integer> fieldOrder = fieldOrderCache.computeIfAbsent(clazz, PositionModelPropertyBuilderPlugin::resolveFieldOrder);
        return fieldOrder.get(fieldName);
    }

    public static Map<String, Integer> resolveFieldOrder(Class<?> clazz) {
        LinkedHashMap<String, Integer> orderMap = new LinkedHashMap<>();

        List<Class<?>> classHierarchy = new ArrayList<>();
        for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
            classHierarchy.add(c);
        }

        Collections.reverse(classHierarchy);

        int index = 0;
        for (Class<?> currentClass : classHierarchy) {
            for (Field field : currentClass.getDeclaredFields()) {
                String name = field.getName();
                if (!orderMap.containsKey(name)) {
                    orderMap.put(name, index++);
                }
            }
        }

        return orderMap;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType == DocumentationType.SWAGGER_2;
    }
}
