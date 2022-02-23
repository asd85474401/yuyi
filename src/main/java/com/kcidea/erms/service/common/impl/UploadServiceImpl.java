package com.kcidea.erms.service.common.impl;

import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.util.MinioFileUtil;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.model.common.FileModel;
import com.kcidea.erms.service.common.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/26
 **/
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    /**
     * 上传表格文件
     *
     * @param file 表格
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/11/26 16:36
     */
    @Override
    public FileModel uploadExcelFile(MultipartFile file) {
        if (file == null) {
            throw new CustomException("很抱歉，您尚未上传文件");
        }
        //原文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new CustomException("很抱歉，上传的文件名为空,无法进行保存");
        }
        //获取文件类型后缀名,分割出文件名最后一个点号之后的内容
        String type = originalFilename.substring(originalFilename.lastIndexOf(Constant.SplitChar.POINT_CHAR) + 1);
        if (isNotExcel(type)) {
            throw new CustomException("很抱歉，您上传的文件格式错误，请选择符合要求的文件格式进行上传");
        }
        //上传文件
        return uploadFile(file, originalFilename);
    }

    /**
     * 上传表格文件
     *
     * @param file 表格
     * @return 文件路径
     * @author yeweiwei
     * @date 2021/11/26 16:36
     */
    @Override
    public String uploadExcelFile(File file) {
        if (file == null) {
            throw new CustomException("很抱歉，您尚未上传文件");
        }
        //原文件名
        String name = file.getName();
        //获取文件类型后缀名,分割出文件名最后一个点号之后的内容
        String type = name.substring(name.lastIndexOf(Constant.SplitChar.POINT_CHAR) + 1);
        if (isNotExcel(type)) {
            throw new CustomException("很抱歉，您上传的文件格式错误，请选择符合要求的文件格式进行上传");
        }
        //上传文件，返回存储路径
        return MinioFileUtil.uploadFile(Constant.MinIoBucketName.ERMS, file, EnumTrueFalse.否.getValue());
    }

    /**
     * 上传文件
     *
     * @param file    文件
     * @param maxSize 文件大小限制
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/12/1 13:11
     */
    @Override
    public FileModel uploadFile(MultipartFile file, Long maxSize) {
        if (file == null) {
            throw new CustomException("很抱歉，您尚未上传文件");
        }
        //原文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new CustomException("很抱歉，上传的文件名为空,无法进行保存");
        }
        //校验文件大小，最大20MB
        if (file.getSize() > maxSize) {
            throw new CustomException("很抱歉，上传文件最大20MB");
        }

        //通过.分割，使用最后一个作为后缀名
        String[] nameArray = originalFilename.split("\\.");
        //获取后缀名
        String type = "";
        if (nameArray.length > 0) {
            type = nameArray[nameArray.length - 1];
        }

        if (type.equals(Constant.Suffix.EXE)) {
            throw new CustomException("很抱歉，不允许的上传文件格式");
        }
        return uploadFile(file, originalFilename);
    }

    /**
     * 上传文件，返回文件信息
     *
     * @param file             文件
     * @param originalFilename 文件名
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/12/1 13:13
     */
    private FileModel uploadFile(MultipartFile file, String originalFilename) {
        //上传文件，返回存储路径
        String fileName = originalFilename.replace(Constant.SplitChar.SEMICOLON_CHAR, "");
        String path = MinioFileUtil.uploadFile(Constant.MinIoBucketName.ERMS, file, fileName);

        FileModel fileModel = new FileModel();
        fileModel.setOriginalName(fileName);
        fileModel.setPath(path);
        return fileModel;
    }

    /**
     * 检查文件是否为表格类型，如果不是，返回true
     *
     * @param type 文件类型
     * @return boolean
     * @author yeweiwei
     * @date 2021/11/26 16:38
     */
    private boolean isNotExcel(String type) {
        if (Strings.isNullOrEmpty(type)) {
            return true;
        }
        type = type.toLowerCase();
        return !Constant.Suffix.CSV.equals(type) &&
                !Constant.Suffix.XLS.equals(type) && !Constant.Suffix.XLSX.equals(type);
    }
}
