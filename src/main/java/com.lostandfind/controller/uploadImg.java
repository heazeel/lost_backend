package com.lostandfind.controller;
import com.alibaba.fastjson.JSON;
import com.lostandfind.domain.User;
import com.lostandfind.service.UserService;
import com.lostandfind.service.impl.ImageSearchServiceimpl;
import com.lostandfind.service.impl.UploadImgServiceImpl;
import com.lostandfind.service.impl.UserServiceImpl;
import com.lostandfind.utils.ResponseJsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lostandfind.utils.TransformRequest;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;


@WebServlet("/upload")
public class uploadImg extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

//        switch (request.getParameter("_method")) {
//            case "PUT" :
//                register(request, response);
//                break;
//            case "DELETE" :
//                break;
//            default:
//                login(request, response);
//
//        }
        //if("PUT".equals(request.getParameter("_method"))){
        uploadImg2Oss(request, response);
        //}
    }

    private void uploadImg2Oss(HttpServletRequest request, HttpServletResponse response){

        String filename = null;
        String url = null;
        Boolean flag = false;
        String folder = null;

        // 系统分隔符
        String dirFlag = System.getProperty("file.separator");

        String saveDirectory = this.getServletContext().getRealPath("") + dirFlag + "img";
        File savedir = new File(saveDirectory);

        Map<String, Object> data = new HashMap<String, Object>();

        try {
            if (!savedir.exists())
                savedir.mkdir();
            int maxPostSize = 10 * 1024 * 1024;
            FileRenamePolicy policy = (FileRenamePolicy) new DefaultFileRenamePolicy();
            MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize, "UTF-8", policy);

            //读取文件
            Enumeration<String> files = multi.getFileNames();
            String name = files.nextElement();
            File f = multi.getFile(name);

            // 重命名文件
            if (f != null) {
                String fname = f.getName();
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String[] names = fname.split("[.]");
                filename = uuid + "." + names[names.length - 1];
            }
            File newfile = new File(saveDirectory+dirFlag+filename);
            f.renameTo(newfile);

            // 获取文件shuruliu
            InputStream in = new FileInputStream(newfile);

            //读取其他参数
            Enumeration params = multi.getParameterNames();
            while (params.hasMoreElements()) {
                String s = (String) params.nextElement();
                String[] str = multi.getParameterValues(s);
                for (int i = 0; i < str.length; i++) {
                    folder = str[i] + "-img/";
                }
            }

            UploadImgServiceImpl upload = new UploadImgServiceImpl();
            url = upload.uploadImg2OSS(folder, filename, in);

            System.out.println("图片上传结果:" + folder + " " + flag + " " + saveDirectory);

            data.put("code", 200);
            data.put("msg", "上传成功！");
            data.put("content", url);
            ResponseJsonUtils.json(response, data);
            // System.out.println(identityType+" "+identifier);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

