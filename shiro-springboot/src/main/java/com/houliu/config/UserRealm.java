package com.houliu.config;

import com.houliu.mapper.UserMapper;
import com.houliu.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author houliu
 * @create 2020-02-14 17:13
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        //获取当前用户
        User user = (User) subject.getPrincipal();
        simpleAuthorizationInfo.addStringPermission(user.getPerms());  //添加权限
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证");
        //用户名，密码
        UsernamePasswordToken userToken = (UsernamePasswordToken)token;
        User user = userMapper.queryUserByName(userToken.getUsername());
        if (null == user){
            return null;
        }
        //把当前用户设置进入session
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser",user);

        ByteSource byteSource = ByteSource.Util.bytes(user.getSalt());
        return new SimpleAuthenticationInfo(user,user.getPwd(),byteSource,this.getName());
    }
}
