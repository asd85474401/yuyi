package com.kcidea.erms.service.common;

import com.kcidea.erms.model.common.FileModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/26
 **/
public interface UploadService {
    /**
     * 上传表格文件
     *
     * @param file 表格
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/11/26 16:36
     */
    FileModel uploadExcelFile(MultipartFile file);

    /**
     * 上传表格文件
     *
     * @param file 表格
     * @return 文件路径
     * @author yeweiwei
     * @date 2021/11/26 16:36
     */
    String uploadExcelFile(File file);

    /**
     * 上传文件
     *
     * @param file    文件
     * @param maxSize 文件大小限制
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/12/1 13:11
     */
    FileModel uploadFile(MultipartFile file, Long maxSize);
}
