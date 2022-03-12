package com.lostandfind.controller;

import com.alibaba.fastjson.JSON;
import com.lostandfind.domain.User;
import com.lostandfind.service.UserService;
import com.lostandfind.service.impl.UserServiceImpl;
import com.lostandfind.utils.ResponseJsonUtils;
import com.lostandfind.utils.TransformRequest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user")
public class user extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        queryAccount(request, response);
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
        if("PUT".equals(request.getParameter("_method"))){
            register(request, response);
        }else{
            login(request, response);
        }
    }

    private void queryAccount(HttpServletRequest request, HttpServletResponse response){
        String identityType = request.getParameter("identityType");
        String identifier = request.getParameter("identifier");

        UserService userService = new UserServiceImpl();
        Boolean bool = userService.queryAccount(identityType, identifier);

        Map<String, Object> data = new HashMap<String, Object>();
        if(bool == true){
            data.put("code", 200);
            data.put("content", null);
        }else{
            data.put("code", 210);
            data.put("msg", "账户不存在！");
            data.put("content", null);
        }

        ResponseJsonUtils.json(response, data);
        // System.out.println(identityType+" "+identifier);

    }

    private void login(HttpServletRequest request, HttpServletResponse response){
        try {
            String identifier = new String(request.getParameter("identifier").getBytes("iso-8859-1"), "utf-8");
            String credential = new String(request.getParameter("credential").getBytes("iso-8859-1"), "utf-8");

            UserService userService = new UserServiceImpl();
            User user = userService.login(identifier, credential);
            System.out.println(user);

            Map<String, Object> data = new HashMap<String, Object>();
            if(user!=null){
                data.put("code", 200);
                data.put("content", user);
            }else{
                data.put("code", 210);
                data.put("content", null);
                data.put("msg", "密码错误！");
            }
            ResponseJsonUtils.json(response, data);

            //System.out.println(identifier + " " + credential);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void register(HttpServletRequest request, HttpServletResponse response){
        try{
            String userId = String.valueOf(new Date().getTime());
            String username = TransformRequest.transform(request, "username");
            String phone = TransformRequest.transform(request, "phone");
            String email = TransformRequest.transform(request, "email");
            String credential = TransformRequest.transform(request, "credential");

            UserService userService = new UserServiceImpl();
            userService.register(userId, username, phone, email, credential);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("code", 200);
            data.put("content", userId);
            data.put("msg", "注册成功");
            ResponseJsonUtils.json(response, data);

            //System.out.println(username + " " + phone + " " + email + " " + credential);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
