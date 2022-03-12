package com.lostandfind.service.impl;

import com.lostandfind.dao.UserDao;
import com.lostandfind.dao.impl.UserDaoImpl;
import com.lostandfind.domain.User;
import com.lostandfind.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public Boolean queryAccount(String identityType, String identifier){
        UserDao userDao = new UserDaoImpl();
        Boolean bool = userDao.queryAccount(identityType, identifier);

        return bool;
    }

    @Override
    public User login(String identifier, String credential){
        UserDao userDao = new UserDaoImpl();
        User user = userDao.login(identifier, credential);

        return user;
    }

    @Override
    public void register(String userId, String username, String phone, String email, String credential){
        UserDao userDao = new UserDaoImpl();
        userDao.register(userId, username, phone, email, credential);
    }
}
