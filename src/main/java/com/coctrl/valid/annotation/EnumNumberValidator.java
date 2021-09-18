package com.coctrl.valid.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jam
 * @date 2021/8/4 4:28 下午
 */
public class EnumNumberValidator implements ConstraintValidator<EnumInteger, Number> {
    private List<Integer> enumStringList;

    @Override
    public void initialize(EnumInteger constraintAnnotation) {
        enumStringList = new ArrayList<>();
        int[] values = constraintAnnotation.value();
        for (int value : values) {
            enumStringList.add(value);
        }
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return enumStringList.contains(value.intValue());
    }
}

