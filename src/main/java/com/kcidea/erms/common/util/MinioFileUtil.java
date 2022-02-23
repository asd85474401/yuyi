package com.kcidea.erms.common.util;

import com.google.common.base.Strings;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
@Slf4j
@Component
public class MinioFileUtil implements CommandLineRunner {

    private static MinioClient minioClient;

    @Value("${my-config.minio.endpoint}")
    private String clientUrl;

    @Value("${my-config.minio.access-key}")
    private String accessKey;

    @Value("${my-config.minio.secret-key}")
    private String secretKey;

    @Value("${my-config.minio.base-bucket}")
    private String baseBucket;

    private static String baseBucketPath;

    @Override
    public void run(String... args) {
        try {
            baseBucketPath = baseBucket;
            minioClient =
                    (MinioClient.builder().endpoint(clientUrl).credentials(accessKey, secretKey).build());
        } catch (Exception e) {
            log.error("文件配置失败：" + e.getMessage(), e);
        }
    }

    /**
     * 创建bucket，如果已存在，不创建可直接使用
     *
     * @param bucketName 桶的名称
     * @author huxubin
     * @date 2021/11/9 19:01
     */
    public static void createBucket(String bucketName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("创建文件桶失败，原因:".concat(e.getMessage()), e);
        }
    }

    /**
     * 根据文件桶的，和文件名称获取文件流
     *
     * @param bucketName 桶的名称
     * @param objectName 文件名
     * @return 文件流
     * @author huxubin
     * @date 2021/11/9 19:02
     */
    public static InputStream getFileInputStream(String bucketName, String objectName) {

        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            log.error("根据文件桶的，和文件名称获取文件失败:".concat(e.getMessage()), e);
        }
        return inputStream;
    }

    /**
     * 上传文件
     *
     * @param bucketName 存储桶
     * @param file       文件
     * @param fileName   原文件名
     * @return 文件路径
     * @author yeweiwei
     * @date 2021/11/26 16:49
     */
    public static String uploadFile(String bucketName, MultipartFile file, String fileName) {
        //返回路径
        String path = "";
        try {
            if (Strings.isNullOrEmpty(bucketName)) {
                bucketName = baseBucketPath;
            }
            // 判断存储桶是否存在
            createBucket(bucketName);

            //构造路径
            path = createPath(fileName);

            try {
                InputStream inputStream = file.getInputStream();
                minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(path)
                        .stream(inputStream, file.getSize(), -1).build());
                inputStream.close();
            } catch (Exception e) {
                log.error("文件上传失败：" + e.getMessage(), e);

            }
        } catch (Exception e) {
            log.error("上传文件失败:" + e.getMessage(), e);
        }
        return path;
    }

    /**
     * 构造文件路径
     *
     * @param fileName 文件名称
     * @return 路径
     * @author yeweiwei
     * @date 2021/11/26 16:49
     */
    private static String createPath(String fileName) {
        StringBuilder path = new StringBuilder();

        //获取年月
        String yearMonthFile = DateTimeUtil.localDateToString(LocalDate.now(), Constant.Pattern.MONTH);

        // 文件名
        // 新的文件名 = 年月文件夹 / 时间戳-原文件名.后缀名
        path.append(yearMonthFile);
        path.append("/");
        path.append(Long.toString(System.currentTimeMillis()).concat(Constant.SplitChar.LINE_CHAR));
        path.append(fileName);

        return path.toString();
    }

    /**
     * 上传文件，根据deleteFlag删除本地文件
     *
     * @param bucketName 存储桶
     * @param file       文件
     * @return 文件路径
     * @author yeweiwei
     * @date 2021/11/26 16:50
     */
    public static String uploadFile(String bucketName, File file, Integer deleteFlag) {
        //返回路径
        String path = "";
        String fileName = file.getName();
        try {
            if (Strings.isNullOrEmpty(bucketName)) {
                bucketName = baseBucketPath;
            }
            // 判断存储桶是否存在
            createBucket(bucketName);

            //构造路径
            path = createPath(fileName);

            try {
                InputStream inputStream = new FileInputStream(file);
                minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(path)
                        .stream(inputStream, file.length(), -1).build());
                inputStream.close();
            } catch (Exception e) {
                log.error("文件上传失败：" + e.getMessage(), e);

            }
        } catch (Exception e) {
            log.error("上传文件失败:" + e.getMessage(), e);
        } finally {
            if (EnumTrueFalse.是.getValue() == deleteFlag) {
                try {
                    if (file.delete()) {
                        log.error("删除{}文件成功", fileName);
                    }
                } catch (Exception e) {
                    log.error("删除文件出错" + e.getMessage());
                }
            }
        }
        return path;
    }

    /**
     * 删除文件
     *
     * @param bucketName 桶名称
     * @param filePath   文件路径
     * @author yeweiwei
     * @date 2021/12/1 14:43
     */
    public static void removeObject(String bucketName, String filePath) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(filePath).build());
    }
}
