/*
 * 项目名称:platform-plus
 * 类名称:MyTest2.java
 * 包名称:com.platform
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/9/23 14:03            liuqianru    初版做成
 *
 */
package com.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MyTest2
 *
 * @author liuqianru
 * @date 2020/9/23 14:03
 */
public class MyTest3 {
    @Test
    public static void main(String[] args)
    {
        String[] appidAttr = {};
        List<String> appidList = Arrays.asList(appidAttr);
        List<String> aaa = new ArrayList<String>(appidList);
        aaa.remove("012345678987654321");
        System.out.print(aaa);
    }
}
