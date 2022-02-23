package com.kcidea.erms.common.util;

import com.google.common.base.Strings;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/1/7
 **/
@Slf4j
public class DownloadUtil {

    /**
     * 统一生成返回的错误文件
     *
     * @param message 错误的消息
     * @return 生成的结果
     * @author huxubin
     * @date 2020/9/14 9:43
     */
    public static ResponseEntity<byte[]> getErrorResponseEntity(String message) {
        return getErrorResponseEntity(message, null);
    }

    /**
     * 统一生成返回的错误文件
     *
     * @param message  错误的消息
     * @param fileName 文件名
     * @return 生成的结果
     * @author huxubin
     * @date 2020/9/14 9:43
     */
    public static ResponseEntity<byte[]> getErrorResponseEntity(String message, String fileName) {
        if (Strings.isNullOrEmpty(message)) {
            message = "导出失败";
        }

        if (Strings.isNullOrEmpty(fileName)) {
            fileName = "error.xlsx";
        }

        //处理显示中文文件名的问题
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        //设置请求头内容,告诉浏览器打开下载窗口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(message.getBytes(StandardCharsets.UTF_8),
                headers, HttpStatus.CREATED);
    }

    /**
     * 生成下载的文件流
     *
     * @param bytes    字节流
     * @param fileName 导出的文件名
     * @return 导出的文件流
     * @author huxubin
     * @date 2020/6/28
     **/
    public static ResponseEntity<byte[]> getResponseEntityByCsv(byte[] bytes, String fileName) {
        try {
            //给CSV增加bom头
            byte[] head = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            //处理显示中文文件名的问题
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            //设置请求头内容,告诉浏览器打开下载窗口
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(ConvertUtil.byteMergerAll(head, bytes),
                    headers, HttpStatus.CREATED);

        } catch (Exception ex) {
            log.error("下载文件发生错误，原因：" + ex.getMessage());
        }
        return null;
    }

    /**
     * @param bytes    字节
     * @param fileName 文件名
     * @return 导出的文件流
     * @author huxubin
     * @date 2021/1/8 14:28
     */
    public static ResponseEntity<byte[]> getResponseEntityByExcel(byte[] bytes, String fileName) {
        try {
            //处理显示中文文件名的问题
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            //设置请求头内容,告诉浏览器打开下载窗口
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("下载文件发生错误，原因：" + ex.getMessage());
        }
        return null;
    }

    /**
     * 生成下载的文件流
     * 可删除文件
     *
     * @param filePath   文件路径
     * @param fileName   导出的文件名
     * @param deleteFlag 是否删除，1==删除，0==不删除
     * @return 导出的文件流
     * @author wuliwei
     * @date 2020/6/28
     **/
    public static ResponseEntity<byte[]> getResponseEntityCanDeleteFile(String filePath, String fileName, Integer deleteFlag) {
        ResponseEntity<byte[]> ret = null;
        File file = null;
        try {
            file = new File(filePath);
            if (file.exists()) {
                //处理显示中文文件名的问题
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                //设置请求头内容,告诉浏览器打开下载窗口
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment", fileName);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                ret = new ResponseEntity<>(FileUtils.readFileToByteArray(file),
                        headers, HttpStatus.CREATED);
            }
        } catch (Exception ex) {
            log.error("下载文件发文错误，原因：" + ex.getMessage());
            getErrorResponseEntity("下载文件发文错误，原因：" + ex.getMessage(), fileName);
        } finally {
            if (null != file
                    && EnumTrueFalse.是.getValue() == deleteFlag) {
                try {
                    if (file.delete()) {
                        log.error("删除{}文件成功", fileName);
                    }
                } catch (Exception e) {
                    log.error("删除文件出错" + e.getMessage());
                }
            }
        }
        return ret;
    }
}
