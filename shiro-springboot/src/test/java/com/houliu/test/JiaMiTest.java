package com.houliu.test;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;

import java.util.UUID;

/**
 * @author houliu
 * @create 2020-02-15 17:48
 */

/**
 * 测试shiro自带的加密方式
 */
public class JiaMiTest {

    public static void main(String[] args) {
        /**
         * 测试MD5加密
         */
        String a = "123";  //明文密码
        String s1 = new Md5Hash(a).toBase64();  //简单加密，没有盐和散列次数
        String s2 = new Md5Hash(a).toString();
        System.out.println("s1 = " + s1);
        System.out.println("s2 = " + s2);

        String salt = UUID.randomUUID().toString();
        String s3 = new Md5Hash(a, salt, 10000).toBase64();//加盐，加散列次数
        String s4 = new Md5Hash(a, salt, 10000).toString();//加盐，加散列次数
        System.out.println("s3 = " + s3);
        System.out.println("s4 = " + s4);

        System.out.println("============================================");

        /**
         * 测试 Sha256 加密
         */
        String s5 = new Sha256Hash(a).toBase64();
        String s6 = new Sha256Hash(a).toString();
        System.out.println("s5 = " + s5);
        System.out.println("s6 = " + s6);

        String s7 = new Sha256Hash(a, salt, 10000).toBase64();
        String s8 = new Sha256Hash(a, salt, 10000).toString();
        System.out.println("s7 = " + s7);
        System.out.println("s8 = " + s8);


        /**
         * 测试 Sha512 加密
         */
        String s9 = new Sha512Hash(a).toBase64();
        String s10 = new Sha512Hash(a).toString();
        System.out.println("s9 = " + s9);
        System.out.println("s10 = " + s10);

        String s11 = new Sha512Hash(a, salt, 10000).toBase64();
        String s12 = new Sha512Hash(a, salt, 10000).toString();
        System.out.println("s11 = " + s11);
        System.out.println("s12 = " + s12);

    }
}
