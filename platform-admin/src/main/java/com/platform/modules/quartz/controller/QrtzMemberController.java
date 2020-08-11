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

import com.alibaba.fastjson.JSON;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.StringUtils;
import com.platform.modules.qkjvip.entity.MemberBasicEntity;
import com.platform.modules.qkjvip.service.MemberBasicService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.MD5Utils;
import com.platform.modules.util.Vars;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 * QrtzMemberController
 *
 * @author liuqianru
 * @date 2020/8/6 9:41
 */

@RestController
@Component("qrtzMember")
@Slf4j
public class QrtzMemberController extends AbstractController {
    @Autowired
    private MemberBasicService memberBasicService;

    /**
     * 会员定时任务
     *
     */
    @SysLog("会员读取定时任务")
    @RequestMapping("/getMembers")
    @Test
    public void getMembers(String params) throws IOException, NoSuchAlgorithmException {
        String url = "http://open.api.zhongjiu.cn/DashboardOpenAPI/ShopMeberDataSync";
        String timeStamp = DateUtils.getTimeStamp();  //时间戳
        SortedMap<String, String> map = new TreeMap<>();
        String urlParam = "";
        String sign = "";
        String resultPost = "";  //返回结果
        List<MemberBasicEntity> mbList = new ArrayList<MemberBasicEntity>();
        if (!StringUtils.isEmpty(params) && params.split(",").length == 3) {   //当定时任务有参数时，按照参数指定日期同步数据
            String[] paramArr = new String[params.split(",").length];
            paramArr = params.split(",");
            map.clear();
            map.put("starttime", paramArr[0].trim());
            map.put("endtime", paramArr[1].trim());
            map.put("timetype", paramArr[2].trim());
            map.put("timestamp", timeStamp);
            map.put("app_key", Vars.APP_KEY);
            sign = MD5Utils.getMD5Sign(map);
            urlParam = "?starttime=" + paramArr[0].trim()
                    + "&endtime=" + paramArr[1].trim()
                    + "&timetype=" + paramArr[2].trim()
                    + "&timestamp=" + timeStamp
                    + "&app_key=" + Vars.APP_KEY
                    + "&sign=" + sign;
            resultPost = HttpClient.doPost(url + urlParam);
            mbList = JSON.parseArray(resultPost, MemberBasicEntity.class);
            if ("0".equals(paramArr[2].trim())) {  //注册
                List<MemberBasicEntity> fromDbList = new ArrayList<MemberBasicEntity>();
                if (mbList.size() > 0) {
                    fromDbList = memberBasicService.queryList(mbList);
                    for(MemberBasicEntity mbn : mbList) {
                        for(MemberBasicEntity dbmb : fromDbList) {
                            if (mbn.getCellphone() != null && mbn.getCellphone().equals(dbmb.getCellphone()) && mbn.getShopname() != null && mbn.getShopname().equals(dbmb.getShopname())) {
                                continue;
                            }
                        }
                        memberBasicService.saveOrUpdate(mbn);
                    }
                }
            } else if ("1".equals(paramArr[2].trim())) {
                memberBasicService.updateBatchByCondition(mbList);
            }
        } else {
            Date nowDate = new Date();
            String endtime = DateUtils.format(nowDate, "yyyy-MM-dd HH:mm:ss");  //现在时间
            String starttime = DateUtils.format(DateUtils.addDateMinutes(nowDate, -30), "yyyy-MM-dd HH:mm:ss"); //前半小时时间
            map.clear();
            map.put("starttime", starttime);
            map.put("endtime", endtime);
            map.put("timetype", "0");
            map.put("timestamp", timeStamp);
            map.put("app_key", Vars.APP_KEY);
            sign = MD5Utils.getMD5Sign(map);
            urlParam = "?starttime=" + starttime
                    + "&endtime=" + endtime
                    + "&timetype=0"
                    + "&timestamp=" + timeStamp
                    + "&app_key=" + Vars.APP_KEY
                    + "&sign=" + sign;
            resultPost = HttpClient.doPost(url + urlParam);
            mbList = JSON.parseArray(resultPost, MemberBasicEntity.class);
//            memberBasicService.saveOrUpdate(mbList);   //此时间段注册的会员
            if (mbList.size() > 0) {
                for(MemberBasicEntity mbn : mbList) {
                    memberBasicService.saveOrUpdate(mbn);
                }
            }
            map.clear();
            map.put("starttime", starttime);
            map.put("endtime", endtime);
            map.put("timetype", "1");
            map.put("timestamp", timeStamp);
            map.put("app_key", Vars.APP_KEY);
            sign = MD5Utils.getMD5Sign(map);
            urlParam = "?starttime=" + starttime
                    + "&endtime=" + endtime
                    + "&timetype=1"
                    + "&timestamp=" + timeStamp
                    + "&app_key=" + Vars.APP_KEY
                    + "&sign=" + sign;
            resultPost = HttpClient.doPost(url + urlParam);
            mbList = JSON.parseArray(resultPost, MemberBasicEntity.class);
//            memberBasicService.updateBatchByMobile(mbList);  //此时间段更新的会员
            memberBasicService.updateBatchByCondition(mbList);
        }
    }
}
