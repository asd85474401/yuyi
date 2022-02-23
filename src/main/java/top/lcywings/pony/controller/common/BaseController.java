package top.lcywings.pony.controller.common;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.lcywings.pony.common.constant.Constant;
import top.lcywings.pony.common.util.IpUtil;
import top.lcywings.pony.service.common.DownloadService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
}
