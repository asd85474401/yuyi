package com.kcidea.erms.service.common.impl;

import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.DownloadUtil;
import com.kcidea.erms.common.util.MinioFileUtil;
import com.kcidea.erms.service.common.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/6/9
 **/
@Slf4j
@Service
public class DownloadServiceImpl implements DownloadService {

    /**
     * 根据文件路径下载文件
     *
     * @param filePath 文件路径
     * @return ResponseEntity
     * @author majuehao
     * @date 2021/11/30 10:14
     **/
    @Override
    public ResponseEntity<byte[]> downloadFile(String filePath) {
        return downloadFile(null, filePath);
    }

    /**
     * 根据文件路径下载文件
     *
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @return ResponseEntity
     * @author majuehao
     * @date 2021/11/30 10:14
     **/
    @Override
    public ResponseEntity<byte[]> downloadFile(String fileName, String filePath) {
        if (Strings.isNullOrEmpty(filePath)) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        if (Strings.isNullOrEmpty(fileName)) {
            //下载文件的名称
            fileName = filePath.substring(filePath.lastIndexOf(Constant.SplitChar.FORWARD_SLASH_CHAR) + 1);
        }
        byte[] bytes = new byte[0];
        try {
            InputStream fileInputStream = MinioFileUtil.getFileInputStream(Constant.MinIoBucketName.ERMS, filePath);
            bytes = IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DownloadUtil.getResponseEntityByExcel(bytes, fileName);
    }
}
