package com.houliu.service.impl;

import com.houliu.common.Constant;
import com.houliu.mapper.UserMapper;
import com.houliu.pojo.User;
import com.houliu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author houliu
 * @create 2020-02-14 20:58
 */

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 新增用户
     * @param user
     */
    @Override
    public void insertUser(User user) {
        user.setId(null);
        String salt = UUID.randomUUID().toString();
        user.setSalt(salt);
        user.setPwd(new Md5Hash(user.getPwd(),salt, Constant.INTERCOUNT).toBase64());
        user.setPerms(null);
        int count = userMapper.insert(user);
        if (count != 1) {
            log.error("新增用户失败。。。。。");
        }
    }

    @Override
    public String queryPermsByName(String username) {
        String perms = userMapper.queryPermsByName(username);
        return perms;
    }
}
