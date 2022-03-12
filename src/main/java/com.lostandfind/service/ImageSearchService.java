package com.lostandfind.service;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.InputStream;
import java.util.ArrayList;

public interface ImageSearchService {
    public Boolean Add(String productId, String picName, String customContent, InputStream inputStream);
    public ArrayList Search(InputStream inputStream);
}
