package com.lostandfind.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lostandfind.domain.Goods;
import com.lostandfind.service.GoodsService;
import com.lostandfind.service.impl.GoodsServiceImpl;
import com.lostandfind.service.impl.ImageSearchServiceimpl;
import com.lostandfind.utils.ResponseJsonUtils;
import com.lostandfind.utils.TransformRequest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/goods")
public class goods extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        goodsSearch(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        if("DELETE".equals(request.getParameter("_method"))){
            goodsDelete(request, response);
        }else if("PUT".equals(request.getParameter("_method"))){
            goodsUpdate(request, response);
        }else{
            goodsCreate(request, response);
        }
    }

    public void goodsCreate(HttpServletRequest request, HttpServletResponse response){
        try{
            String goodsId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            String userId = TransformRequest.transform(request, "userId");
            String submissionType = TransformRequest.transform(request, "submissionType");
            String title = TransformRequest.transform(request, "title");
            String type = TransformRequest.transform(request, "type");
            String date = TransformRequest.transform(request, "date");
            String description = TransformRequest.transform(request, "description");
            String photos = TransformRequest.transform(request, "photos");
            String positionLngLat = TransformRequest.transform(request, "positionLngLat");
            String positionArea = TransformRequest.transform(request, "positionArea");
            String positionDetail = TransformRequest.transform(request, "positionDetail");
            String phone = TransformRequest.transform(request, "phone");

            GoodsService goodsService = new GoodsServiceImpl();
            goodsService.create(goodsId, userId, submissionType, title, type, date, description, photos, positionLngLat, positionArea, positionDetail, phone);

            ImageSearchServiceimpl imageSearch = new ImageSearchServiceimpl();
            String dirFlag = System.getProperty("file.separator");
            String saveDirectory = this.getServletContext().getRealPath("") + dirFlag + "img";

            String[] photosName = photos.split(",");

            for(int i = 0; i < photosName.length; i++){
                String[] photoName = photosName[i].split("/");
                String name = photoName[photoName.length-1];
                File newfile = new File(saveDirectory + dirFlag + name);
                InputStream in = new FileInputStream(newfile);

                Boolean flag = imageSearch.Add(goodsId, name, userId, in);
                System.out.println(flag);
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", 200);
            data.put("content", null);
            data.put("msg", "新建成功");
            ResponseJsonUtils.json(response, data);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goodsSearch(HttpServletRequest request, HttpServletResponse response){
        try{
            String submissionType = request.getParameter("submissionType");
            String type = request.getParameter("type");
            String description = request.getParameter("description");
            String positionArea = request.getParameter("positionArea");
            String time = request.getParameter("time");
            String userId = request.getParameter("userId");


            GoodsService goodsService = new GoodsServiceImpl();
            List<Goods> list = goodsService.search(submissionType, type, description, positionArea, time, userId);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", 200);
            data.put("content", list);
            ResponseJsonUtils.json(response, data);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goodsDelete(HttpServletRequest request, HttpServletResponse response){
        try{
            String goodsId = TransformRequest.transform(request, "goodsId");
            GoodsService goodsService = new GoodsServiceImpl();
            goodsService.delete(goodsId);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", 200);
            data.put("content", null);
            data.put("msg", "删除成功！");
            ResponseJsonUtils.json(response, data);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goodsUpdate(HttpServletRequest request, HttpServletResponse response){
        try{
            String goodsId = TransformRequest.transform(request, "goodsId");
            String userId = TransformRequest.transform(request, "userId");
            String title = TransformRequest.transform(request, "title");
            String type = TransformRequest.transform(request, "type");
            String date = TransformRequest.transform(request, "date");
            String description = TransformRequest.transform(request, "description");
            String photos = TransformRequest.transform(request, "photos");
            String positionLngLat = TransformRequest.transform(request, "positionLngLat");
            String positionArea = TransformRequest.transform(request, "positionArea");
            String positionDetail = TransformRequest.transform(request, "positionDetail");
            String phone = TransformRequest.transform(request, "phone");

            GoodsService goodsService = new GoodsServiceImpl();
            goodsService.update(goodsId, title, type, date, description, photos, positionLngLat, positionArea, positionDetail, phone);

            ImageSearchServiceimpl imageSearch = new ImageSearchServiceimpl();
            String dirFlag = System.getProperty("file.separator");
            String saveDirectory = this.getServletContext().getRealPath("") + dirFlag + "img";

            String[] photosName = photos.split(",");

            for(int i = 0; i < photosName.length; i++){
                String[] photoName = photosName[i].split("/");
                String name = photoName[photoName.length-1];
                File newfile = new File(saveDirectory + dirFlag + name);
                InputStream in = new FileInputStream(newfile);

                Boolean flag = imageSearch.Add(goodsId, name, userId, in);
                System.out.println(flag);
            }

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", 200);
            data.put("content", null);
            data.put("msg", "修改成功");
            ResponseJsonUtils.json(response, data);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
