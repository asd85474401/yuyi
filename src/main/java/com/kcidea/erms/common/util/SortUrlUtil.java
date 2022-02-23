package com.kcidea.erms.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.model.sorturl.SortJsonModel;
import com.kcidea.erms.model.sorturl.SortLinkModel;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.Map;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/13
 **/
@Slf4j
public class SortUrlUtil {

    //默认的短链接域名
    private final static String DEFAULT_DOMAIN = "sourl.cn";

    //默认的分组
    private final static String DEFAULT_GROUP = "pc2g5xvu";

    //请求的api地址
    private final static String DEFAULT_API_URL = "https://api.xiaomark.com/v1/link/create";

    //默认的apiKey
    private final static String DEFAULT_API_KEY = "9a23ed900b220d50c30d5e8b1de35d56";

    //默认的原始链接
    private final static String ORIGIN_URL = "http://test.erms.addup.com.cn/#/dataProvider?apiKey=";

    /**
     * 生成短链接
     *
     * @param apiKey 数据库的apiKey
     * @return 短链接地址
     * @author huxubin
     * @date 2022/1/13 14:19
     */
    public static String createSortUrl(String apiKey) {
        return createSortUrl(DEFAULT_DOMAIN, apiKey);
    }

    /**
     * 更新
     *
     * @param sortUrl 短链接地址
     * @param apiKey  新的apiKey
     * @return 更新后的短链接
     * @author huxubin
     * @date 2022/1/13 15:56
     */
    public static String updateSortUrl(String sortUrl, String apiKey) {
        return "";
    }

    /**
     * 生成自定义域名的短链接
     *
     * @param domain 自定义域名
     * @param apiKey 数据库apiKey
     * @return 短链接地址
     * @author huxubin
     * @date 2022/1/13 14:19
     */
    public static String createSortUrl(String domain, String apiKey) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("domain", domain);
        jsonObject.put("origin_url", ORIGIN_URL.concat(apiKey));
        jsonObject.put("apikey", DEFAULT_API_KEY);
        jsonObject.put("group_sid", DEFAULT_GROUP);
        String jsonData = getJSONData(jsonObject);

        if (Strings.isNullOrEmpty(jsonData)) {
            return null;
        }

        JSONObject object = JSON.parseObject(jsonData);
        if (object == null) {
            return null;
        }

        int code = ConvertUtil.objToInt(object.get("code"), -1);
        if (code != 0) {
            throw new CustomException(getMessageByCode(code));
        }

        SortJsonModel model = JSON.parseObject(FormatUtil.formatValue(object.get("data")), SortJsonModel.class);
        if (model != null && model.getLink() != null) {
            return model.getLink().getSortUrl();
        }
        return null;
    }

    /**
     * 根据返回代码获取对应的消息
     *
     * @param code 返回代码
     * @return 错误的消息
     * @author huxubin
     * @date 2022/1/13 16:17
     */
    public static String getMessageByCode(int code) {
        String message;
        switch (code) {
            case 13: {
                message = "跳转链接的域名不在白名单之内";
                break;
            }
            case 14: {
                message = "一天内短链接创建数量超过50次限制";
                break;
            }
            case 15: {
                message = "自定义域名未绑定或不可用";
                break;
            }
            case 101: {
                message = "apikey错误";
                break;
            }
            case 102: {
                message = "您的api帐号异常";
                break;
            }
            case 103: {
                message = "没有API权限";
                break;
            }
            case 104: {
                message = "没有此API的调用权限";
                break;
            }
            default: {
                message = "请求格式错误";
                break;
            }
        }
        return message;
    }

    /**
     * 获取json数据
     *
     * @param jsonObject 请求参数
     * @return 获取到的结果
     * @author huxubin
     * @date 2022/1/13 15:19
     */
    public static String getJSONData(JSONObject jsonObject) {
        try {
            Connection.Response response = Jsoup.connect(DEFAULT_API_URL)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .timeout(1000 * 60) //超时时间60秒
                    .maxBodySize(0)
                    .requestBody(jsonObject.toJSONString())
                    .header("Connection", "keep-alive")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85" +
                            ".0.4168.2 Safari/537.36")
                    .execute();
            return response.body();
        } catch (Exception exception) {
            log.error("请求API接口出错啦，原因：" + exception.getMessage(), exception);
        }
        return null;
    }
}
