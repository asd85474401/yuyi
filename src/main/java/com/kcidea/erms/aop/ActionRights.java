package com.kcidea.erms.aop;

import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;

import java.lang.annotation.*;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Target({ElementType.METHOD}) //目标是方法
@Retention(RetentionPolicy.RUNTIME) //注解会在class中存在，运行时可通过反射获取
@Documented //文档生成时，该注解将被包含在javadoc中，可去掉
public @interface ActionRights {

    /**
     * 访问的权限
     */
    EnumUserAction userAction() default EnumUserAction.查询;

    /**
     * 访问的菜单
     */
    EnumMenu menu() default EnumMenu.首页;

}
