package com.kcidea.erms.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.aop.LoginCheck;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.IpUtil;
import com.kcidea.erms.common.util.RedisUtil;
import com.kcidea.erms.common.util.RsaUtil;
import com.kcidea.erms.enums.user.EnumLoginAction;
import com.kcidea.erms.model.user.UserModel;
import com.kcidea.erms.service.common.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Slf4j
public class BaseController {

    @Autowired(required = false)
    protected HttpServletRequest request;

    @Autowired(required = false)
    protected HttpServletResponse response;

    @Resource
    protected DownloadService downloadService;

    /**
     * 获取用户id
     *
     * @return 用户id
     * @author huxubin
     * @date 2021/11/10 16:19
     */
    protected Long getUserId() {
        UserModel userModel = getLoginUserModel();
        if (null == userModel) {
            return 0L;
        }
        return userModel.getId();
    }

    /**
     * 获取学校id
     *
     * @return 学校id
     * @author huxubin
     * @date 2021/11/10 15:34
     */
    protected Long getSid() {
        UserModel userModel = getLoginUserModel();
        if (null == userModel) {
            return 0L;
        }
        return userModel.getSid();
    }

    /**
     * 获取学校开始年份
     *
     * @return 学校开始年份
     * @author yeweiwei
     * @date 2021/11/16 12:00
     */
    protected Integer getSchoolStartYear() {
        UserModel userModel = getLoginUserModel();
        if (null == userModel) {
            return 0;
        }
        return userModel.getStartYear();
    }

    /**
     * 校验年份不能小于学校开始年份
     *
     * @param vYear 年份
     * @author yeweiwei
     * @date 2021/11/18 9:32
     */
    protected void checkYear(Integer vYear) {
        if (null == vYear) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        //年份不为999时，校验
        if (!vYear.equals(Constant.ALL_INT_VALUE)) {
            Integer schoolStartYear = getSchoolStartYear();
            if (vYear < schoolStartYear) {
                throw new CustomException("很抱歉，选则的年份不能小于学校开始年份！");
            }
            if (vYear > LocalDate.now().getYear() + 1) {
                throw new CustomException("很抱歉，选则的年份不能大于学校截止年份！");
            }
        }
    }

    /**
     * 校验开始年份和截止年份
     *
     * @param startYear 开始年份
     * @param endYear   截止年份
     * @author yeweiwei
     * @date 2021/11/18 9:54
     */
    protected void checkYearRange(Integer startYear, Integer endYear) {

        //开始时间小于截止时间
        if (startYear > endYear) {
            throw new CustomException("很抱歉，开始年份不能大于截止年份！");
        }

        checkYear(startYear);
        checkYear(endYear);
    }

    /**
     * 获取登录用户信息
     *
     * @return 用户信息
     * @author huxubin
     * @date 2021/11/10 15:31
     */
    public UserModel getLoginUserModel() {
        //从request里面获取token
        String token = request.getHeader(Constant.ERMS_TOKEN_KEY);
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }

        //token解密
        String decryptToken;
        try {
            decryptToken = RsaUtil.decryptDefaultKey(token);
        } catch (Exception e) {
            log.error("解密失败，密文" + token + "：" + e.getMessage());
            return null;
        }

        if (Strings.isNullOrEmpty(decryptToken)) {
            return null;
        }

        //拿token去redis查询当前用户是否存在
        String json = RedisUtil.getStringByKey(Constant.RedisKey.TOKEN_REDIS_KEY.concat(decryptToken));

        if (Strings.isNullOrEmpty(json)) {
            return null;
        }

        //adminStr 转对象
        return JSONObject.parseObject(json, UserModel.class);
    }

    /**
     * 保存接口的请求日志
     *
     * @author huxubin
     * @date 2021/11/11 15:54
     */
    protected void saveInfoLog() {
        //请求取参
        String paramsStr = this.getParameterStr(request);
        //打印[INFO]日志
        log.info("访问接口: {}，访问IP: {}，访问参数: {}", request.getRequestURI(), IpUtil.getIpAddress(request), paramsStr);
    }

    /**
     * 获取请求的参数
     *
     * @param request 请求参数
     * @return 请求的内容
     * @author huxubin
     * @date 2021/11/10
     **/
    private String getParameterStr(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getParameterNames();
        List<String> params = Lists.newArrayList();
        if (null != attributeNames) {
            while (attributeNames.hasMoreElements()) {
                String paramName = attributeNames.nextElement();
                params.add(paramName.concat(":[").concat(request.getParameter(paramName)).concat("]"));
            }
        }

        String paramsStr = "";
        if (!CollectionUtils.isEmpty(params)) {
            paramsStr = StringUtils.join(params, Constant.SplitChar.COMMA_CHAR);
        }
        return paramsStr;
    }

    /**
     * 根据文件路径下载模板文件
     *
     * @param filePath 文件路径
     * @return 文件
     * @author majuehao
     * @date 2021/12/21 13:31
     */
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/downloadTemplateByNamePath")
    public ResponseEntity<byte[]> downloadTemplateByNamePath(@RequestParam("filePath") String filePath) {
        this.saveInfoLog();
        return downloadService.downloadFile(filePath);
    }

    /**
     * 根据文件路径、文件名下载文件
     *
     * @param fileName 文件名
     * @param filePath 文件路径
     * @return 文件
     * @author majuehao
     * @date 2021/11/29 14:34
     **/
    @LoginCheck(action = EnumLoginAction.Normal)
    @GetMapping(value = "/downloadFileByNamePath")
    public ResponseEntity<byte[]> downloadFileByNamePath(@RequestParam(value = "fileName") String fileName,
                                                         @RequestParam(value = "filePath") String filePath) {
        this.saveInfoLog();
        return downloadService.downloadFile(fileName, filePath);
    }
}
