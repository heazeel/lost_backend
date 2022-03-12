package com.lostandfind.service;

import com.lostandfind.domain.Goods;

import java.util.List;

public interface GoodsService {
    public void create(String goodsId, String userId, String submissionType, String title, String type, String date, String description, String photos, String positionLngLat, String positionArea, String positionDetail, String phone);
    public List<Goods> search(String submissionType, String type, String description, String positionArea, String time, String userId);
    public void delete(String goodsId);
    public void update(String goodsId, String title, String type, String date, String description, String photos, String positionLngLat, String positionArea, String positionDetail, String phone);
}
