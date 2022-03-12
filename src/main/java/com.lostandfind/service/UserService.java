package com.lostandfind.service;

import com.lostandfind.domain.User;

public interface UserService {

    public Boolean queryAccount(String identityType, String identifier);

    public User login(String identifier, String credential);

    public void register(String userId, String username, String phone, String email, String credential);
}
