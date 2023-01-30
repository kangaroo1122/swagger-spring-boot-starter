package com.kangaroohy.validator.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * 类 EnumNumberValidator 功能描述：
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2021/09/18 11:16
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

