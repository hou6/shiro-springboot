package com.houliu.config;

/**
 * @author houliu
 * @create 2020-02-18 11:43
 */

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * 定义session监听类
 */
@Component
public class MySessionListener extends SessionListenerAdapter {

    /**
     * session创建时触发
     * @param session
     */
    @Override
    public void onStart(Session session) {
        super.onStart(session);
    }

    /**
     * session停止时触发
     * @param session
     */
    @Override
    public void onStop(Session session) {
        super.onStop(session);
    }

    /**
     * session过期时触发，静默时间超过了过期时间
     * @param session
     */
    @Override
    public void onExpiration(Session session) {
        super.onExpiration(session);
    }
}
