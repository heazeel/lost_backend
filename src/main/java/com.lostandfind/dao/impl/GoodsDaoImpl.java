package com.lostandfind.dao.impl;

import com.lostandfind.dao.GoodsDao;
import com.lostandfind.domain.Goods;
import com.lostandfind.domain.User;
import com.lostandfind.service.impl.ImageSearchServiceimpl;
import com.lostandfind.utils.C3P0utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoodsDaoImpl implements GoodsDao {
    @Override
    public void create(String goodsId, String userId, String submissionType, String title, String type, String date, String description, String photos, String positionLngLat, String positionArea, String positionDetail, String phone){
        String sql = "INSERT INTO goods(goodsId, userId, submissionType, title, type, date, description, photos, positionLngLat, positionArea, positionDetail, phone, visible) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = C3P0utils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, goodsId);
            pstmt.setString(2, userId);
            pstmt.setString(3, submissionType);
            pstmt.setString(4, title);
            pstmt.setString(5, type);
            pstmt.setString(6, date);
            pstmt.setString(7, description);
            pstmt.setString(8, photos);
            pstmt.setString(9, positionLngLat);
            pstmt.setString(10, positionArea);
            pstmt.setString(11, positionDetail);
            pstmt.setString(12, phone);
            pstmt.setInt(13, 1);
            pstmt.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            C3P0utils.close(conn, pstmt, null);
        }
    }

    @Override
    public List<Goods> search(String submissionType, String type, String description, String positionArea, String time, String userId){
        Connection conn = null;
        QueryRunner queryRunner = new QueryRunner();
        StringBuffer sql = new StringBuffer("SELECT username, goods.userId, goodsId, submissionType, title, type, date, description, photos, positionLngLat, positionArea, positionDetail, goods.phone FROM goods LEFT JOIN users ON goods.userId = users.userId WHERE visible=1");
        List<Object> list = new ArrayList<Object>();

        if(submissionType != null){
            sql.append(" AND submissionType = ?");
            list.add(submissionType);
        }
        if(type != null){
            sql.append(" AND type = ?");
            list.add(type);
        }
        if(description != null){
            sql.append(" AND (type LIKE ? OR description LIKE ? OR title LIKE ? OR positionArea LIKE ? OR positionDetail LIKE ?)");
            list.add("%"+description+"%");
            list.add("%"+description+"%");
            list.add("%"+description+"%");
            list.add("%"+description+"%");
            list.add("%"+description+"%");
        }
        if(positionArea != null){
            sql.append(" AND positionArea like ?");
            list.add("%"+positionArea+"%");
        }
        if(time != null){
            int pretime=0;
            if(time.equals("今天")){
                pretime=0;
            }
            else if(time.equals("3天内")){
                pretime=3;
            }
            else if(time.equals("7天内")){
                pretime=7;
            }
            else if(time.equals("15天内")){
                pretime=15;
            }
            else if(time.equals("1个月内")){
                pretime=31;
            }
            else if(time.equals("3个月内")){
                pretime=93;
            }
            else if(time.equals("6个月内")){
                pretime=186;
            }
            else if(time.equals("1年内")){
                pretime=365;
            }
            sql.append(" AND TIMESTAMPDIFF(DAY,date,CURDATE())<=" + pretime);
        }
        if(userId != null){
            sql.append(" AND goods.userId = ?");
            list.add(userId);
        }

        //QueryRunner提供了两个方法，query、update
        try{
            conn = C3P0utils.getConnection();
            return queryRunner.query(conn, sql.toString(), new BeanListHandler<Goods>(Goods.class), list.toArray());

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            C3P0utils.close(conn, null, null);
        }

        return null;
    }

    @Override
    public void delete(String goodsId){
        String sql = "UPDATE goods SET visible = ? WHERE goodsId = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = C3P0utils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 0);
            pstmt.setString(2, goodsId);
            pstmt.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            C3P0utils.close(conn, pstmt, null);
        }
    }

    @Override
    public void update(String goodsId, String title, String type, String date, String description, String photos, String positionLngLat, String positionArea, String positionDetail, String phone){
        String sql = "UPDATE goods SET title = ?, type = ?, date = ?, description = ?, photos = ?, positionLngLat = ?, positionArea = ?, positionDetail = ?, phone = ? WHERE goodsId = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = C3P0utils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, type);
            pstmt.setString(3, date);
            pstmt.setString(4, description);
            pstmt.setString(5, photos);
            pstmt.setString(6, positionLngLat);
            pstmt.setString(7, positionArea);
            pstmt.setString(8, positionDetail);
            pstmt.setString(9, phone);
            pstmt.setString(10, goodsId);
            pstmt.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            C3P0utils.close(conn, pstmt, null);
        }
    }
}
