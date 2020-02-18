package com.houliu.service;

import com.houliu.pojo.User;

/**
 * @author houliu
 * @create 2020-02-14 20:57
 */
public interface UserService {
    void insertUser(User user);

    String queryPermsByName(String username);
}
