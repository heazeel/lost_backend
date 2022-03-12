package com.lostandfind.service;

import java.io.InputStream;

public interface UploadImgService {

    public String uploadImg2OSS(String folder, String fileName, InputStream inputStream);

}
