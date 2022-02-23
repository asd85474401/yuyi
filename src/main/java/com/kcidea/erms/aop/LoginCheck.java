package com.kcidea.erms.aop;

import com.kcidea.erms.enums.user.EnumLoginAction;

import java.lang.annotation.*;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginCheck {

    EnumLoginAction action() default EnumLoginAction.Normal;

}
