package com.kcidea.erms.common.exception;

/**
 * 自定义的异常
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public class CustomException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     * 自定义异常
     * @param message 异常的消息
     * @author huxubin
     * @date 2021/11/9 16:40
     */
    public CustomException(String message){
        super(message);
    }

}
