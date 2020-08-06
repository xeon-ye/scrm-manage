/*
 * 项目名称:platform-plus
 * 类名称:MD5Sign.java
 * 包名称:com.platform.modules.util
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/8/6 11:26            liuqianru    初版做成
 *
 */
package com.platform.modules.util;

import com.platform.common.utils.StringUtils;
import org.omg.IOP.Encoding;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * MD5Sign
 *
 * @author liuqianru
 * @date 2020/8/6 11:26
 */
public class MD5Sign {
    /// <summary>
    /// 生成接口验签sign值
    /// </summary>
    /// <param name="dictionary">不包括 sign 的所有参数键值对集合</param>
    /// <returns></returns>
    public static String getMD5Sign(SortedMap<String, String> map) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String appSecret = "oasd4igv9afu6exquz12s";//此值为私有不放入参数
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            String value = map.get(key).toString();
            builder.append(key + value);
            System.out.println("key:" + key + " vlaue:" + value);
        }
        String waitsignstr = builder.toString()+ appSecret;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] buffer = md5.digest(waitsignstr.getBytes("utf-8"));

        StringBuilder signbuilder = new StringBuilder(0x20);
        String sTmp = "";
        for (int j = 0; j < buffer.length; j++)
        {
            sTmp = Integer.toHexString(0xFF & buffer[j]);
            if (sTmp.length() < 2) {
                signbuilder.append(0);
            }
            signbuilder.append(sTmp);
        }
        return signbuilder.toString().toUpperCase();
    }
}
