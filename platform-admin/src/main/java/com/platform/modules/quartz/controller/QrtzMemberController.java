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
            this.saveOrUpdateMember(mbList, paramArr[2].trim());
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
            this.saveOrUpdateMember(mbList, "0");
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
            this.saveOrUpdateMember(mbList, "1");
        }
    }

    public void saveOrUpdateMember(List<MemberBasicEntity> list, String timeType) {
        List<MemberBasicEntity> fromDbList = new ArrayList<MemberBasicEntity>();
        List<MemberBasicEntity> tmpList = new ArrayList<MemberBasicEntity>();
        if ("0".equals(timeType)) {  //注册
            if (list.size() > 0) {
                fromDbList = memberBasicService.queryList(list);  //检索数据库已存在的会员信息
                if (fromDbList.size() == 0) {  //说明数据库不存在，都要插入
                    tmpList = list;
                } else {
                    boolean flag = false;
                    for(MemberBasicEntity mbn : list) {
                        for(MemberBasicEntity dbmb : fromDbList) {
                            if (mbn.getCellphone() != null && mbn.getCellphone().equals(dbmb.getCellphone()) && mbn.getShopname() != null && mbn.getShopname().equals(dbmb.getShopname())) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            tmpList.add(mbn);
                        }
                    }
                }
                if (tmpList.size() > 0) {
                    memberBasicService.saveOrUpdate(tmpList);  //会员批量插入
                }
            }
        } else if ("1".equals(timeType)) {
            if (list.size() > 0) {
                memberBasicService.updateByCondition(list);
            }
        }
    }
}
