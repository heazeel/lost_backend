package com.lostandfind.service.impl;

import com.lostandfind.dao.GoodsDao;
import com.lostandfind.dao.impl.GoodsDaoImpl;
import com.lostandfind.domain.Goods;
import com.lostandfind.service.GoodsService;

import java.util.List;

public class GoodsServiceImpl implements GoodsService {
    @Override
    public void create(String goodsId, String userId, String submissionType, String title, String type, String date, String description, String photos, String positionLngLat, String positionArea, String positionDetail, String phone){
        GoodsDao goodsDao = new GoodsDaoImpl();
        goodsDao.create(goodsId, userId, submissionType, title, type, date, description, photos, positionLngLat, positionArea, positionDetail, phone);
    }

    @Override
    public List<Goods> search(String submissionType, String type, String description, String positionArea, String time, String userId){
        GoodsDao goodsDao = new GoodsDaoImpl();
        return goodsDao.search(submissionType, type, description, positionArea, time, userId);
    }

    @Override
    public void delete(String goodsId){
        GoodsDao goodsDao= new GoodsDaoImpl();
        goodsDao.delete(goodsId);
    }

    @Override
    public void update(String goodsId, String title, String type, String date, String description, String photos, String positionLngLat, String positionArea, String positionDetail, String phone){
        GoodsDao goodsDao = new GoodsDaoImpl();
        goodsDao.update(goodsId, title, type, date, description, photos, positionLngLat, positionArea, positionDetail, phone);
    }
}
