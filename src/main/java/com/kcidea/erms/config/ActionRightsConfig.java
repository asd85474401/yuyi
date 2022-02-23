package com.kcidea.erms.config;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.kcidea.erms.aop.BaseActionRightsConfig;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.common.util.RightsUtil;
import com.kcidea.erms.common.util.RsaUtil;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.service.menu.MenuService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Aspect
@Component
public class ActionRightsConfig extends BaseActionRightsConfig {

    @Resource
    private MenuService menuService;

    /**
     * 声明AOP切入点，controller下所有方法都会被拦截
     */
    @Pointcut("execution(* com.kcidea.erms.controller..*.*(..))")
    @Override
    protected void checkRightsAspect() {

    }

    /**
     * 检查用户是否具有当前权限
     * 1. 从token中获得用户的基本信息
     * 2. 从redis中获取角色的全部权限
     * 3. 比较当前角色是否具有这个权限
     *
     * @param token          用户凭证
     * @param menuStr        菜单
     * @param enumUserAction 权限
     * @return java.lang.Boolean
     * @author huxubin
     * @date 2021/5/19 11:33
     **/
    @Override
    protected Boolean checkUserRight(Object token, String menuStr, EnumUserAction enumUserAction) {
        //根据解密后的token从redis中获取用户基本信息
        String tokenStr = token.toString();
        String decryptToken = RsaUtil.decryptDefaultKey(tokenStr);
        String json = RedisUtil.getStringByKey(Constant.RedisKey.TOKEN_REDIS_KEY.concat(decryptToken));
        if (Strings.isNullOrEmpty(json)) {
            return false;
        }
        //转为AdminDetailModel
        UserModel userModel = JSON.parseObject(json, UserModel.class);

        //从redis中获取用户的全部权限
        List<MenuModel> menuList = menuService.findMenuListByRoleId(userModel.getRoleId());

        //是否拥有该权限
        return RightsUtil.checkAdminMenu(menuList, menuStr, enumUserAction);
    }


    /**
     * 从request里拿出用户的Token凭据
     *
     * @param request request请求
     * @author huxubin
     * @date 2021/5/19 14:58
     **/
    @Override
    protected Object getToken(HttpServletRequest request) {
        //从request里面获取token
        String token = request.getHeader(Constant.ERMS_TOKEN_KEY);
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        return token;
    }

}
