package com.lostandfind.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0utils {

    //返回dataSource等于以前的JDBC

    //创建C3P0连接池对象
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource("lost_and_find_db");

    //返回一个数据源
    public static ComboPooledDataSource getDataSource(){
        return dataSource;
    }

    //创建连接
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    public static void close(Connection conn, PreparedStatement pst, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(pst!=null){
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
