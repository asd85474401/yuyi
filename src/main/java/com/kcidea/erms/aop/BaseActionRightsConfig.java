package com.kcidea.erms.aop;

import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.enums.user.EnumLoginAction;
import com.kcidea.erms.enums.user.EnumUserAction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public abstract class BaseActionRightsConfig {

    @Autowired(required = false)
    private HttpServletRequest request;

    /**
     * 声明AOP切入点，controller下所有方法都会被拦截
     */
    protected abstract void checkRightsAspect();


    /**
     * 该标签声明次方法是一个前置通知：在目标方法开始之前执行
     *
     * @param joinPoint joinPoint
     */
    @Before("checkRightsAspect()")
    public void beforeAspect(JoinPoint joinPoint) {

    }

    /**
     * 环绕通知（Around advice） ：包围一个连接点的通知，类似Web中Servlet规范中的Filter的doFilter方法。
     * 可以在方法的调用前后完成自定义的行为，也可以选择不执行。
     *
     * @param joinPoint joinPoint
     */
    @Around("checkRightsAspect()")
    public Object aroundAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        EnumUserAction enumUserAction = this.getUserRightsType(joinPoint);
        String menuStr = this.getMenu(joinPoint);
        Object token = getToken(request);
        //判断是否有token
        if (token != null) {
            //如果是loginCheck注解，就不用判断权限
            if (isLoginCheck(joinPoint)) {
                return joinPoint.proceed();
            }
            //调用核心的检查方法
            Boolean checkRight = this.checkUserRight(token, menuStr, enumUserAction);
            if (checkRight) {
                //继续执行方法
                return joinPoint.proceed();
            }
            //没有权限
            throw new CustomException(MessageFormat.format("很抱歉，您没有{0}权限，无法进行此操作",
                    enumUserAction.getName()));
        } else {
            //判断是否跳过登录验证
            if (loginPassCheck(joinPoint)) {
                //如果跳过登录验证就可以pass的注解，直接放行
                return joinPoint.proceed();
            }
            //尚未登录
            throw new CustomException(Vm.NO_LOGIN);
        }
    }


    /**
     * 检查用户是否具有当前权限
     * 1. 从token中获得用户的基本信息
     * 2. 从redis中获取用户的全部权限
     * 3. 比较当前用户是否具有这个权限
     *
     * @param token          token
     * @param menuStr        菜单
     * @param enumUserAction 权限
     * @return java.lang.Boolean
     * @author huxubin
     * @date 2021/5/19 11:33
     **/
    protected abstract Boolean checkUserRight(Object token, String menuStr, EnumUserAction enumUserAction);

    /**
     * 从request里拿出用户的Token凭据
     *
     * @param request 请求
     * @return token
     * @author huxubin
     * @date 2021/11/9 19:50
     */
    protected abstract Object getToken(HttpServletRequest request);

    /**
     * 获取对应的权限
     *
     * @param joinPoint joinPoint
     * @return 权限
     */
    public EnumUserAction getUserRightsType(JoinPoint joinPoint) {
        EnumUserAction userRightsType = EnumUserAction.查询;

        //获取当前注解
        Method theMethod = this.getMethod(joinPoint);
        if (theMethod != null && theMethod.isAnnotationPresent(ActionRights.class)) {
            //当前访问的模块
            ActionRights annotation = theMethod.getAnnotation(ActionRights.class);
            userRightsType = annotation.userAction();
        }
        return userRightsType;
    }

    /**
     * 获取对应的菜单，根据ActionRights上注解的menu信息来获取，如果没有的话，那就以自己的方法名作为菜单
     *
     * @param joinPoint joinPoint
     * @return 菜单名称
     */
    private String getMenu(JoinPoint joinPoint) {
        String ret = "";
        //获取当前注解
        Method theMethod = this.getMethod(joinPoint);
        if (theMethod != null && theMethod.isAnnotationPresent(ActionRights.class)) {
            //当前访问的模块
            ActionRights annotation = theMethod.getAnnotation(ActionRights.class);
            ret = annotation.menu().getName();
            if (Strings.isNullOrEmpty(ret)) {
                //如果当前模块没有注解自己的菜单，那就用自己的方法名作为菜单返回
                ret = theMethod.getName();
            }
        }
        return ret;
    }

    /**
     * 获取当前执行的方法
     *
     * @param joinPoint joinPoint
     * @return 当前执行的方法
     */
    private Method getMethod(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        try {
            method = target.getClass().getMethod(method.getName(),
                    method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    /**
     * 判断当前方法上是否有LoginPass的注解
     *
     * @param joinPoint joinPoint
     * @return boolean
     */
    private boolean loginPassCheck(JoinPoint joinPoint) {
        //获取当前注解
        Method method = this.getMethod(joinPoint);

        if (method == null) {
            return false;
        }

        //获取对应的类型
        LoginCheck annotation = method.getAnnotation(LoginCheck.class);

        //判断是否验证权限
        return annotation != null && annotation.action() == EnumLoginAction.Skip;
    }

    /**
     * 判断是否有LoginCheck注解
     *
     * @param joinPoint joinPoint
     * @return boolean
     * @author yeweiwei
     * @date 2021/11/12 17:47
     */
    private boolean isLoginCheck(ProceedingJoinPoint joinPoint) {
        //获取当前注解
        Method method = this.getMethod(joinPoint);

        return method != null && method.isAnnotationPresent(LoginCheck.class);
    }

}
