package com.coctrl.document.exception;

/**
 * 类 PropertiesNotFoundException 功能描述：
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2021/08/25 10:08
 */
public class PropertiesNotFoundException extends RuntimeException {

    public PropertiesNotFoundException(String msg) {
        super(msg);
    }
    public PropertiesNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
