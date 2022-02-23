package top.lcywings.pony.config;

import top.lcywings.pony.common.constant.Vm;
import top.lcywings.pony.common.result.Result;
import top.lcywings.pony.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/10
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionConfig {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public Result<?> customExceptionHandler(CustomException e, HttpServletResponse response) {
        //如果错误信息是没有登录，设置状态码为403，跳转到登录页
        if (Objects.equals(e.getMessage(), Vm.NO_LOGIN)) {
            response.setStatus(403);
            log.error(Vm.NO_LOGIN);
            return new Result<>().error(403, e.getMessage());
        }
        log.error("请求发生错误，原因：" + e.getMessage(), e);
        return new Result<>().error(500, e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<?> exceptionHandler(Exception ex) {

        log.error("请求发生错误，原因：" + ex.getMessage(), ex);

        //请求参数缺失
        if (ex.getClass() == MissingServletRequestParameterException.class ||
                ex.getClass() == MethodArgumentTypeMismatchException.class) {
            return new Result<>().error(400, Vm.ERROR_PARAMS);
        }

        //请求类型不支持
        if (ex.getClass() == HttpRequestMethodNotSupportedException.class) {
            return new Result<>().error(401, Vm.ERROR_METHOD);
        }

        return new Result<>().error(500, Vm.SYSTEM_ERROR_PLEASE_REFRESH_AND_RETRY);
    }

    /**
     * 参数@Valid校验不通过
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<?> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        log.error("参数校验未通过，原因" + e.getMessage(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return new Result<>().error(400, fieldErrors.get(0).getDefaultMessage());
    }
}
