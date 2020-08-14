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
import com.platform.modules.qkjvip.entity.QkjvipMemberOrderEntity;
import com.platform.modules.qkjvip.service.MemberBasicService;
import com.platform.modules.qkjvip.service.QkjvipMemberOrderService;
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
@Component("qrtzMemberOrder")
@Slf4j
public class QrtzMemberOrderController extends AbstractController {
    @Autowired
    private QkjvipMemberOrderService qkjvipMemberOrderService;

    /**
     * 会员定时任务
     *
     */
    @SysLog("会员订单读取定时任务")
    @RequestMapping("/getMembersOrder")
    @Test
    public void getMembersOrder(String params) throws IOException, NoSuchAlgorithmException {
        long start, end2;
        start = System.currentTimeMillis();
        String url = "http://open.api.zhongjiu.cn/DashboardOpenAPI/ShopOrderDataSync";
        String timeStamp = DateUtils.getTimeStamp();  //时间戳
        SortedMap<String, String> map = new TreeMap<>();
        String urlParam = "";
        String sign = "";
        String resultPost = "";  //返回结果
        List<MemberBasicEntity> mbList = new ArrayList<MemberBasicEntity>();
        map.clear();
        String startime="";
        String endtime="";
        if (!StringUtils.isEmpty(params) && params.split(",").length == 2) {//同步指定时间
            String[] paramArr = new String[params.split(",").length];
            paramArr = params.split(",");
            startime=paramArr[0].trim();
            endtime=paramArr[1].trim();
        }else{//半小时同步一次
            Date nowDate = new Date();
            endtime = DateUtils.format(nowDate, "yyyy-MM-dd HH:mm:ss");  //现在时间
            startime = DateUtils.format(DateUtils.addDateMinutes(nowDate, -30), "yyyy-MM-dd HH:mm:ss"); //前半小时时间
        }
        Integer listsize=getMemberBasicEntities(url,timeStamp,startime,endtime);//订单生成修改
        end2 = System.currentTimeMillis();
        System.out.println("批量处理"+listsize+"条数据，耗费了" + (end2 - start) + "ms");
    }

    private Integer getMemberBasicEntities(String url, String timeStamp, String starttime, String endtime) throws IOException {
        SortedMap<String, String> map = new TreeMap<>();
        String resultPost = "";  //返回结果
        String sign;
        String urlParam;
        List<QkjvipMemberOrderEntity> addList= new ArrayList<QkjvipMemberOrderEntity>();
        List<QkjvipMemberOrderEntity> mdyList= new ArrayList<QkjvipMemberOrderEntity>();
        //数据库已存在的所有数据
        map.put("starttime", starttime);
        map.put("endtime", endtime);
        map.put("timestamp", timeStamp);
        map.put("app_key", Vars.APP_KEY);
        for(int i=1;i<=1;i++){
            map.put("timetype", i+"");
            sign = MD5Utils.getMD5Sign(map);
            urlParam = "?starttime=" + starttime
                    + "&endtime=" + endtime
                    + "&timetype=" + i+""
                    + "&timestamp=" + timeStamp
                    + "&app_key=" + Vars.APP_KEY
                    + "&sign=" + sign;
            resultPost = HttpClient.doPost(url + urlParam);
            if(i==1){
                addList = JSON.parseArray(resultPost, QkjvipMemberOrderEntity.class);
                qkjvipMemberOrderService.deleteBatchByOrder(addList);
                qkjvipMemberOrderService.saveBatch(addList);
            }else {
                //mdyList = JSON.parseArray(resultPost, QkjvipMemberOrderEntity.class);
                //qkjvipMemberOrderService.updateBatch(mdyList);
            }

        }
        return addList.size()+mdyList.size();
    }


}
