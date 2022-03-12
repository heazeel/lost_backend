package com.lostandfind.dao;


import com.lostandfind.domain.User;

public interface UserDao {

    /**
     * 查询账号是否存在
     * @param identityType 账户类型
     * @param identifier 账户
     * @return Boolean
     */
    public Boolean queryAccount(String identityType, String identifier);

    /**
     * 登录校验
     * @param identifier 账户
     * @param credential 登录凭证
     * @return Boolean
     */
    public User login(String identifier, String credential);

    /**
     * 注册
     * @param username 用户姓名
     * @param phone 手机号
     * @param email 邮件地址
     * @param credential 登录凭证
     */
     public void register(String userId, String username, String phone, String email, String credential);

}
