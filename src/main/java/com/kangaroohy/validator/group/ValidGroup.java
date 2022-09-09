package com.kangaroohy.validator.group;

import javax.validation.groups.Default;

/**
 * 类 ValidGroup 功能描述：
 *
 * @author kangaroo hy
 * @version 0.0.1
 * @date 2021/09/18 11:16
 */
public interface ValidGroup extends Default {
    interface Create extends ValidGroup {
    }

    interface Update extends ValidGroup {
    }

    interface Query extends ValidGroup {
    }

    interface Delete extends ValidGroup {
    }
}
