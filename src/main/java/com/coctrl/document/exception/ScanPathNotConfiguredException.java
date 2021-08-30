package com.coctrl.document.exception;

/**
 * 类 PropertiesNotFundException 功能描述：
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2021/08/25 10:08
 */
public class ScanPathNotConfiguredException extends RuntimeException {

    public ScanPathNotConfiguredException(String msg) {
        super(msg);
    }
    public ScanPathNotConfiguredException(String msg, Throwable t) {
        super(msg, t);
    }
}
