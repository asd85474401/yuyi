package com.kcidea.erms.service.common;

import org.springframework.http.ResponseEntity;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/6/9
 **/
public interface DownloadService {

    /**
     * 根据文件路径下载文件
     *
     * @param filePath 文件路径
     * @return ResponseEntity
     * @author majuehao
     * @date 2021/11/30 10:14
     **/
    ResponseEntity<byte[]> downloadFile(String filePath);

    /**
     * 根据文件路径下载文件
     *
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @return ResponseEntity
     * @author majuehao
     * @date 2021/11/30 10:14
     **/
    ResponseEntity<byte[]> downloadFile(String fileName,String filePath);
}
