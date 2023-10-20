package com.kangaroohy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类 ApiModelEnum 功能描述：<br/>
 *
 * @author hy
 * @version 0.0.1
 * @date 2023/10/20 17:06
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelEnum {
}
