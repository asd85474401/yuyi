package com.kcidea.erms.controller.common;

import com.google.common.base.Strings;
import com.kcidea.erms.aop.LoginCheck;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.common.util.IpUtil;
import com.kcidea.erms.common.util.Md5Util;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.common.util.RsaUtil;
import com.kcidea.erms.domain.user.User;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.user.EnumLoginAction;
import com.kcidea.erms.model.menu.MenuModel;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.service.database.DataBaseSortUrlService;
import com.kcidea.erms.service.menu.MenuService;
import com.kcidea.erms.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    @Resource
    private DataBaseSortUrlService dataBaseSortUrlService;

    /**
     * 用户登录
     *
     * @param account  帐号
     * @param password 密码
     * @return 用户的数据
     * @author huxubin
     * @date 2021/11/10 15:52
     */
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Result<UserModel> login(@RequestParam(value = "account") String account,
                                   @RequestParam(value = "password") String password) {
        super.saveInfoLog();

        Result<UserModel> result = new Result<>();
        User user = userService.findOneByAccount(account);
        //用户空或者密码不匹配，都直接返回
        if (null == user || !Objects.equals(user.getPassword(), Md5Util.toMd5(password).toUpperCase())) {
            throw new CustomException(Vm.ERROR_USERNAME_OR_PASSWORD);
        }

        //判断帐号是否禁用
        if (user.getDisableFlag() == EnumTrueFalse.是.getValue()) {
            throw new CustomException(Vm.USER_DISABLE);
        }

        //获取ip地址
        String ipAddr = IpUtil.getIpAddress(request);

        //验证成功，查询用户权限，缓存到redis
        List<MenuModel> menuList = menuService.findMenuListByRoleId(user.getRoleId());

        //保存登录的信息
        UserModel userModel = userService.saveLoginData(user, ipAddr);
        //菜单列表
        userModel.setMenuList(menuList);
        return result.success(userModel);
    }

    /**
     * 退出登录
     *
     * @param request  request
     * @param response response
     * @return 退出登录结果
     * @author yeweiwei
     * @date 2021/11/12 15:06
     */
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public Result<String> loginOut(HttpServletRequest request, HttpServletResponse response) {
        super.saveInfoLog();

        Result<String> result = new Result<>();

        response.setContentType("application/x-www-form-urlencoded");
        //获取token
        String token = request.getHeader(Constant.ERMS_TOKEN_KEY);
        if (Strings.isNullOrEmpty(token)) {
            //没有token的话，直接返回
            result.successMsg(Vm.SUCCESS_MSG);
            return result;
        }

        //删除redis
        //token解密
        String decryptToken = RsaUtil.decryptDefaultKey(token);
        RedisUtil.deleteByKey(Constant.RedisKey.TOKEN_REDIS_KEY.concat(decryptToken));
        result.successMsg(Vm.SUCCESS_MSG);
        return result;
    }

    /**
     * 库商apiKey登录
     *
     * @param apiKey 输入的apiKey
     * @return 用户的数据
     * @author huxubin
     * @date 2022/02/9 15:52
     */
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/apiKeyLogin", method = RequestMethod.POST)
    public Result<String> apiKeyLogin(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new Result<String>().successMsg(dataBaseSortUrlService.saveApiKeyLogin(IpUtil.getIpAddress(request),
                apiKey));
    }
}
