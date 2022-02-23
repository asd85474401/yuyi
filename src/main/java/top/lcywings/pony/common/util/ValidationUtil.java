package top.lcywings.pony.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据格式校验
 *
 * @author yeweiwei
 * @date 2021/5/7
 * @version 1.0
 **/

public class ValidationUtil {

    /**
     * 手机号格式
     */
    public static String REGEX_MOBILE = "^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$";

    /**
     * 邮件格式
     */
    public static String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([0-9a-z_\\-]*)(\\.(com|cn|inc|org|cc|edu|de)*){1,2}([a-z]{2})?$";

    /**
     * 仅限中英文
     */
    public static final String CN_ENG = "^[a-zA-Z\\u4e00-\\u9fa5]+$";

    /**
     * 仅限中英文和数字
     */
    public static final String CN_ENG_NUM = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";

    /**
     * 仅限英文、数字和符号
     */
    public static final String ENG_NUM_CHA = "^[((?=[\\x21-\\x7e]+)[^A-Za-z0-9])]+$";

    /**
     * 仅限英文和数字
     */
    public static final String ENG_NUM = "^[A-Za-z0-9]+$";

    public static boolean mobile(String str) {
        return validate(str, REGEX_MOBILE);
    }

    public static boolean email(String str) {
        return validate(str, REGEX_EMAIL);
    }

    public static boolean cnEng(String str) { return validate(str, CN_ENG);}

    public static boolean cnEngNum(String str) {return validate(str, CN_ENG_NUM);}

    public static boolean engNumCha(String str) {return validate(str, ENG_NUM_CHA);}

    public static boolean engNum(String str) {return validate(str, ENG_NUM);}

    public static boolean length(String str,int maxLen){
        return (str.length() <= maxLen);
    }

    public static boolean length(String str,int minLength,int maxLen){
        return (str.length()>=minLength && str.length()<=maxLen);
    }

    public static boolean validate(String str, String type) {
        Pattern p = Pattern.compile(type);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
