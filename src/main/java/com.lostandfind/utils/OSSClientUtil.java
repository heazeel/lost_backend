package com.lostandfind.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OSSClientUtil {

    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAI5tRtyj3jVcbcEEs3FhQS";
    private static String accessKeySecret = "PPuiIORArT6IqStTXsHn3Qddx4ELFP";
    private static String bucketName = "lost-and-find";
    //private static String folder = "自己创建文件夹名称/";
    private static String key = "https://lost-and-find.oss-cn-hangzhou.aliyuncs.com/";
    public OSSClientUtil() {

    }

    public String uploadImg2OSS(String folder, String fileName, InputStream inputStream) {

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(new PutObjectRequest(bucketName, folder + fileName, inputStream));
            return key + folder + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}