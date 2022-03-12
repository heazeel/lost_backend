package com.lostandfind.controller;

import com.alibaba.fastjson.JSON;
import com.lostandfind.domain.Goods;
import com.lostandfind.service.GoodsService;
import com.lostandfind.service.impl.GoodsServiceImpl;
import com.lostandfind.service.impl.ImageSearchServiceimpl;
import com.lostandfind.service.impl.UploadImgServiceImpl;
import com.lostandfind.utils.C3P0utils;
import com.lostandfind.utils.ResponseJsonUtils;
import com.lostandfind.utils.TransformRequest;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.*;
import java.util.List;

@WebServlet("/imageSearch")
public class imageSearch extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        imageSearch(request, response);
    }

    public void imageSearch(HttpServletRequest request, HttpServletResponse response){

        String filename = null;
        ArrayList list = new ArrayList();

        // 系统分隔符
        String dirFlag = System.getProperty("file.separator");

        String saveDirectory = this.getServletContext().getRealPath("") + dirFlag + "img";
        File savedir = new File(saveDirectory);

        Map<String, Object> data = new HashMap<String, Object>();

        Connection conn = null;
        QueryRunner queryRunner = new QueryRunner();

        try {
            conn = C3P0utils.getConnection();
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
//            Enumeration params = multi.getParameterNames();
//            while (params.hasMoreElements()) {
//                String s = (String) params.nextElement();
//                String[] str = multi.getParameterValues(s);
//                for (int i = 0; i < str.length; i++) {
//                    folder = str[i] + "-img/";
//                }
//            }

            ImageSearchServiceimpl imageSearch = new ImageSearchServiceimpl();
            list = imageSearch.Search(in);

            StringBuffer sql = new StringBuffer("SELECT username, goods.userId, goodsId, submissionType, title, type, date, description, photos, positionLngLat, positionArea, positionDetail, goods.phone FROM goods LEFT JOIN users ON goods.userId = users.userId WHERE");
            List<Object> sqllist = new ArrayList<Object>();
            List<Goods> goodslist = new ArrayList<Goods>();

            if(list.size() == 0){
                sql.append(" goodsId = 0");
            }
            else{
                sql.append(" goodsId = ?");
                sqllist.add(list.get(0));
                for(int i = 1; i < list.size(); i ++){
                    sql.append(" OR goodsId = ?");
                    sqllist.add(list.get(i));
                }
            }
            goodslist = queryRunner.query(conn, sql.toString(), new BeanListHandler<Goods>(Goods.class), sqllist.toArray());

            data.put("code", 200);
            data.put("msg", "上传成功！");
            data.put("content", goodslist);
            ResponseJsonUtils.json(response, data);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
