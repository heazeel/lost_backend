package com.lostandfind.service.impl;

import com.aliyun.imagesearch20201214.models.AddImageResponse;
import com.aliyun.imagesearch20201214.models.SearchImageByPicResponse;
import com.aliyun.imagesearch20201214.models.SearchImageByPicResponseBody;
import com.lostandfind.service.ImageSearchService;
import com.lostandfind.utils.ImageSearch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageSearchServiceimpl implements ImageSearchService {
    @Override
    public Boolean Add(String productId, String picName, String customContent, InputStream inputStream){
        ImageSearch imageSearch = new ImageSearch();
        try {
            AddImageResponse response = imageSearch.Add(productId, picName, customContent, inputStream);
            return response.getBody().success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList Search(InputStream inputStream){
        ImageSearch imageSearch = new ImageSearch();
        ArrayList list = new ArrayList<>();
        try {
            SearchImageByPicResponse response = imageSearch.Search(inputStream);

            List<SearchImageByPicResponseBody.SearchImageByPicResponseBodyAuctions> auctions = response.getBody().getAuctions();
            for(SearchImageByPicResponseBody.SearchImageByPicResponseBodyAuctions auction:auctions) {
                list.add(auction.productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
