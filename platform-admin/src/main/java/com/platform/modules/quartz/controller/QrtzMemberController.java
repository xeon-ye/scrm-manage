/*
 * 项目名称:platform-plus
 * 类名称:QrtzMemberController.java
 * 包名称:com.platform.modules.quartz.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/8/6 9:41            liuqianru    初版做成
 *
 */
package com.platform.modules.quartz.controller;

import com.platform.common.annotation.SysLog;
import com.platform.modules.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.platform.modules.util.MD5Sign.getMD5Sign;

/**
 * QrtzMemberController
 *
 * @author liuqianru
 * @date 2020/8/6 9:41
 */

@RestController
@Component("qrtzMember")
@Slf4j
public class QrtzMemberController {

    /**
     * 会员定时任务
     *
     */
    @SysLog("会员读取定时任务")
    @RequestMapping("/getMembers")
    public void getMembers() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String url = "http://open.api.zhongjiu.cn/DashboardOpenAPI/ShopMeberDataSync";
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss" );
        String nowTime = sdf.format(new Date());
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("starttime","2020-01-02 00:00:00"));
        list.add(new BasicNameValuePair("endtime","2020-03-02 00:00:00"));
        list.add(new BasicNameValuePair("timetype","1"));
        list.add(new BasicNameValuePair("timestamp", nowTime));
        list.add(new BasicNameValuePair("app_key","open100010fb1n94ev9"));
        SortedMap<String, String> map = new TreeMap<>();
        map.put("starttime", "2020-01-02 00:00:00");
        map.put("endtime", "2020-03-02 00:00:00");
        map.put("timetype", "1");
        map.put("timestamp", nowTime);
        map.put("app_key", "open100010fb1n94ev9");
        map.put("sign", "00000");
        String sign = getMD5Sign(map);
        list.add(new BasicNameValuePair("sign",sign));
        String resultPost = HttpClient.doPost(url,list);
        System.out.println("post请求" + resultPost);
    }
}
