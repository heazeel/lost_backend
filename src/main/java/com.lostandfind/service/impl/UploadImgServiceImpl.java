package com.lostandfind.service.impl;

import com.lostandfind.utils.OSSClientUtil;

import java.io.InputStream;

public class UploadImgServiceImpl {

    public String uploadImg2OSS(String folder, String fileName, InputStream inputStream){
        String url = null;
        OSSClientUtil ossClient = new OSSClientUtil();
        url = ossClient.uploadImg2OSS(folder, fileName, inputStream);
        return url;
    }

}
