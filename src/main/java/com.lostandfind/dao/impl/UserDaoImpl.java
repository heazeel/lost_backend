package com.lostandfind.dao.impl;

import com.lostandfind.dao.UserDao;
import com.lostandfind.domain.User;
import com.lostandfind.utils.C3P0utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class UserDaoImpl implements UserDao {
    @Override
    public Boolean queryAccount(String identityType, String identifier){
        //准备一个QueryRunner核心类
        String sql = "SELECT * FROM user_auths WHERE identityType=? AND identifier=?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn = C3P0utils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, identityType);
            pstmt.setString(2, identifier);
            rs = pstmt.executeQuery();
            rs.last();
            if(rs.getRow() > 0) {
                System.out.println(true);
                return true;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            C3P0utils.close(conn, pstmt ,rs);
        }

        return false;
    }

    @Override
    public User login(String identifier, String credential){
        //准备一个QueryRunner核心类
        Connection conn = null;
        QueryRunner queryRunner = new QueryRunner();
        String sql = "SELECT users.userId, username, phone, email, status FROM users LEFT JOIN user_auths ON users.userId = user_auths.userId WHERE identifier=? AND credential=?";
        Object[] params = {identifier, credential};

        //QueryRunner提供了两个方法，query、update
        try{
            conn = C3P0utils.getConnection();
            User user = queryRunner.query(conn, sql, new BeanHandler<User>(User.class), params);
            return user;

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            C3P0utils.close(conn, null, null);
        }

        return null;
    }

    @Override
    public void register(String userId, String username, String phone, String email, String credential){
        String insertUserSql = "INSERT INTO users(userId, username, phone, email, status) VALUES(" + userId + ", ?, ?, ?, 'effective')";
        String insertUserAuthsSql = "INSERT INTO user_auths(userId, identityType, identifier, credential, ifVerified) VALUES(" + userId + ", ?, ?, ?, 'YES')";

        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        try{
            conn = C3P0utils.getConnection();

            pstmt1 = conn.prepareStatement(insertUserSql);
            pstmt1.setString(1, username);
            pstmt1.setString(2, phone);
            pstmt1.setString(3, email);
            pstmt1.executeUpdate();

            pstmt2 = conn.prepareStatement(insertUserAuthsSql);
            for(int i = 0; i < 2; i++){
                String identityType = (i == 0 ? "email" : "phone");
                String identifier = (i == 0 ? email : phone);
                pstmt2.setString(1, identityType);
                pstmt2.setString(2,identifier);
                pstmt2.setString(3, credential);
                pstmt2.addBatch();
            }
            pstmt2.executeBatch();
            pstmt2.clearBatch();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                pstmt1.close();
                pstmt2.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            C3P0utils.close(conn, null ,null);
        }
    }
}
