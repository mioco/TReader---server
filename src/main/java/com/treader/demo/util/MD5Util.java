package com.treader.demo.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    public static String getMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(), 0, str.length());
        return new BigInteger(1, md.digest()).toString(16);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(getMD5("zhy331122"));
    }
}
