/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberFansController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-09 14:02:22        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.quartz.controller;

import cn.emay.util.AES;
import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platform.common.annotation.SysLog;
import com.platform.modules.quartz.entity.QrtzMemberFansEntity;
import com.platform.modules.quartz.entity.TmpQkjvipMemberFansEntity;
import com.platform.modules.quartz.service.QrtzMemberFansService;
import com.platform.modules.quartz.service.TmpQkjvipMemberFansService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.RandomGUID;
import com.platform.modules.util.Vars;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static cn.emay.util.Md5.md5;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2020-09-09 14:02:22
 */
@RestController
@Component("qrtzMemberFans")
@Slf4j
public class QrtzMemberFansController extends AbstractController {
    @Autowired
    private QrtzMemberFansService qrtzMemberFansService;
    @Autowired
    private TmpQkjvipMemberFansService tmpQkjvipMemberFansService;

    /**
     * 粉丝定时任务
     *
     */
    @SysLog("粉丝读取定时任务")
    @RequestMapping("/getMemberFans")
    @Test
    public void getMemberFans() throws IOException, NoSuchAlgorithmException {
        String url = "http://test.open.zhongjiuyun.com:9000/datapool/userdetail";
        List<QrtzMemberFansEntity> fanList = new ArrayList<QrtzMemberFansEntity>();
        List<TmpQkjvipMemberFansEntity> tmpList = new ArrayList<TmpQkjvipMemberFansEntity>();
        RandomGUID myGUID = new RandomGUID();
        String urlParam = "";
        String resultPost = "";  //返回结果
        Map map = new HashMap();
        Map subMap = new HashMap();
        subMap.put("user", "");
        subMap.put("imei", "12312312312");
        subMap.put("version", 1);
        subMap.put("channel", "00000000");
        subMap.put("phonemodel", "hua wei");
        subMap.put("platform", 0);
        subMap.put("IsApp", 0);
        map.put("UpdateTime", "2018-09-01");
        map.put("OriginId", "gh_82456394f186");
        map.put("meta", subMap);
        JSONObject json = new JSONObject(map);
        String requestJson = JsonHelper.toJsonString(json);
        byte[] bytes = requestJson.getBytes(Vars.ENCODE);
        //AES加密
        byte[] parambytes = AES.encrypt(bytes, Vars.AESKEY.getBytes(Vars.ENCODE), Vars.VECTOR.getBytes(Vars.ENCODE), Vars.ALGORITHM);
        //转base64位字符串
        String res = new BASE64Encoder().encode(parambytes);
        System.out.println("key:data" + " vlaue:" + res);
        String guid = myGUID.toString();
        String sign = md5((res + guid + Vars.SECRETKEY).getBytes(Vars.ENCODE));   //md5加密
        urlParam = "?v=1"
                + "&sign=" + sign
                + "&key=" + guid;
        System.out.println("key:sign" + " vlaue:" + sign);
        System.out.println("key:key" + " vlaue:" + guid);
        resultPost = HttpClient.sendPost(url + urlParam, res);
        JSONObject fanObject = JSONObject.parseObject(resultPost);
        if ("0".equals(fanObject.get("result").toString()) && fanObject.get("UserList").toString().length() > 0) {
            String listStr = fanObject.get("UserList").toString();
            fanList = JSON.parseArray(listStr, QrtzMemberFansEntity.class);
            tmpList = JSON.parseArray(listStr, TmpQkjvipMemberFansEntity.class);
            System.out.println("result:" + fanList);
            List<QrtzMemberFansEntity> updList = new ArrayList<QrtzMemberFansEntity>();
            tmpQkjvipMemberFansService.addBatch(tmpList);  //批量插入粉丝临时表
            updList = qrtzMemberFansService.queryList();  //取出粉丝表与临时表的交集(插入是为重复数据，更新时即为要更新的数据)
            if (updList.size() > 0) {  //更新
                qrtzMemberFansService.updateBatch(updList);
            }
            fanList.removeAll(updList);
            if (fanList.size() > 0) {
                qrtzMemberFansService.addBatch(fanList);  //粉丝批量插入
            }
        }
    }
}
