package com.lostandfind.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.servlet.http.HttpServletRequest;

public class TransformRequest {
    public static String transform(HttpServletRequest request, String source){
        try {
            if(request.getParameter(source)!=null){
                return new String(request.getParameter(source).getBytes("iso-8859-1"), "utf-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
