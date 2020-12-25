/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberMessageController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-12-22 11:05:08        李鹏军     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.qkjvip.service.QkjvipMemberIntegralService;
import com.platform.modules.qkjvip.service.QkjvipMemberIntegraluserService;
import com.platform.modules.quartz.entity.QrtzMemberFansEntity;
import com.platform.modules.quartz.service.QrtzMemberFansService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.service.QkjvipMemberMessageService;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.service.SysSmsLogService;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.ListToStringUtil;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Controller
 *
 * @author 李鹏军
 * @date 2020-12-22 11:05:08
 */
@RestController
@RequestMapping("qkjvip/membermessage")
public class QkjvipMemberMessageController extends AbstractController {
    @Autowired
    private QkjvipMemberMessageService qkjvipMemberMessageService;
    @Autowired
    private QkjvipMemberIntegraluserService qkjvipMemberIntegraluserService;
    @Autowired
    private QrtzMemberFansService qrtzMemberFansService;
    @Autowired
    private SysSmsLogService sysSmsLogService;
    @Autowired
    private QkjvipMemberIntegralService qkjvipMemberIntegralService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:membermessage:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberMessageEntity> list = qkjvipMemberMessageService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:membermessage:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberMessageService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:membermessage:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberMessageEntity qkjvipMemberMessage = qkjvipMemberMessageService.getById(id);

        return RestResponse.success().put("membermessage", qkjvipMemberMessage);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberMessage qkjvipMemberMessage
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:membermessage:save")
    public RestResponse save(@RequestBody QkjvipMemberMessageEntity qkjvipMemberMessage) {

        qkjvipMemberMessageService.add(qkjvipMemberMessage);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberMessage qkjvipMemberMessage
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:membermessage:update")
    public RestResponse update(@RequestBody QkjvipMemberMessageEntity qkjvipMemberMessage) {

        qkjvipMemberMessageService.update(qkjvipMemberMessage);

        return RestResponse.success();
    }

    /**
     * 根据主键删除
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("qkjvip:membermessage:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberMessageService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 取出所选人分布在每个公众号的人数
     *
     * @param qkjvipMemberMessage 活动/积分/优惠券的
     * @return RestResponse
     */
    @PostMapping("/init")
    public RestResponse init(@RequestBody QkjvipMemberMessageEntity qkjvipMemberMessage) {
        List<QkjvipOptionsEntity> options = new ArrayList<>();
        //获取公众号列表
        String resultPost = HttpClient.sendGet(Vars.APPID_GETLIST_URL);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("0".equals(resultObject.get("code").toString())) {  //调用成功
            options = JSON.parseArray(resultObject.getString("data"), QkjvipOptionsEntity.class);
            //获取被选人分布在每个公众号的人数
            List<QrtzMemberFansEntity> fansList = new ArrayList<>();
            if (qkjvipMemberMessage.getCategoryType() != null) {
                if ("1".equals(qkjvipMemberMessage.getCategoryType())) {  //活动
                    //TODO
                } else if ("2".equals(qkjvipMemberMessage.getCategoryType())) {  //积分
                    List<String> integralusers = new ArrayList<>();
                    integralusers = qkjvipMemberIntegraluserService.queryByIntegralId(qkjvipMemberMessage.getCategoryId());
                    String userStr = ListToStringUtil.listToString(integralusers);
                    if (!"".equals(userStr)) {
                        fansList = qrtzMemberFansService.queryByMemberIdStr(userStr);
                    }
                } else if ("3".equals(qkjvipMemberMessage.getCategoryType())) {  //优惠券
                    //TODO
                }
            }

            for (QkjvipOptionsEntity option : options) {
                for(QrtzMemberFansEntity fans : fansList){
                    if (option.getAppid() != null && option.getAppid().equals(fans.getAppid())) {
                        option.setName(option.getName() + "(" + fans.getMembernum() + "个人)");
                        break;
                    }
                }
            }
            QkjvipOptionsEntity option = new QkjvipOptionsEntity();
            option.setAppid("012345678987654321");
            option.setName("短信");
            options.add(option);
        }

        return RestResponse.success().put("options", options);
    }

    /**
     * 发送消息
     *
     * @param qkjvipMemberMessage 活动/积分/优惠券的
     * @return RestResponse
     */
    @PostMapping("/sendMsg")
    public RestResponse sendMsg(@RequestBody QkjvipMemberMessageEntity qkjvipMemberMessage) throws IOException {
        Map map = new HashMap();
        List<QrtzMemberFansEntity> fansList = new ArrayList<>();
        if (qkjvipMemberMessage.getCategoryType() != null) {
            String[] appidAttr = qkjvipMemberMessage.getChannels().split(",");
            String appidstr = ListToStringUtil.listToString(Arrays.asList(appidAttr));
            List<String> users = qkjvipMemberIntegraluserService.queryByIntegralId(qkjvipMemberMessage.getCategoryId());
            String memberidstr = ListToStringUtil.listToString(users);

            if ("1".equals(qkjvipMemberMessage.getCategoryType())) {  //活动
                //TODO
            } else if ("2".equals(qkjvipMemberMessage.getCategoryType())) {  //积分
                if (qkjvipMemberMessage.getChannels().contains("012345678987654321")) {  //包含短信
                    map.clear();
                    map.put("integralId", qkjvipMemberMessage.getCategoryId());
                    List<QkjvipMemberIntegraluserEntity> integralusers = new ArrayList<>();
                    integralusers = qkjvipMemberIntegraluserService.queryAll(map);

                    //群发发短信
                    for(QkjvipMemberIntegraluserEntity integraluser : integralusers){
                        if(integraluser != null && integraluser.getMobile() != null && !integraluser.getMobile().equals("")){
                            this.sendMobileMsg(qkjvipMemberMessage.getContent(), integraluser.getMobile());
                        }
                    }
                }
                QkjvipMemberIntegralEntity qkjvipMemberIntegral = new QkjvipMemberIntegralEntity();
                qkjvipMemberIntegral.setSendStatus(2); //状态修改为通知已发送
                qkjvipMemberIntegral.setId(qkjvipMemberMessage.getCategoryId());
                qkjvipMemberIntegralService.updateStatus(qkjvipMemberIntegral);
            } else if ("3".equals(qkjvipMemberMessage.getCategoryType())) {  //优惠券
                //TODO
            }

            qkjvipMemberMessageService.add(qkjvipMemberMessage);  //保存发放记录
            map.clear();
            map.put("appidstr", appidstr);
            map.put("memberidstr", memberidstr);
            fansList = qrtzMemberFansService.queryAll(map);
            //调用赵月辉接口
            this.sendWxMsg(qkjvipMemberMessage, fansList);
        }

        return RestResponse.success();
    }

    /**
     * 群发短信
     *
     * @param content 内容
     * @param mobile 手机
     */
    public void sendMobileMsg(String content, String mobile) {
        //发短信
        SysSmsLogEntity smsLog = new SysSmsLogEntity();
        smsLog.setContent(content);
//        smsLog.setMobile(mobile);
        smsLog.setMobile("13621255469");
        SysSmsLogEntity sysSmsLogEntity = sysSmsLogService.sendSms(smsLog);
    }

    /**
     * 群发微信
     * @param qkjvipMemberMessage
     * @param fansList 微信用户列表
     */
    public void sendWxMsg(QkjvipMemberMessageEntity qkjvipMemberMessage, List<QrtzMemberFansEntity> fansList) throws IOException {
        QkjvipMemberIntegralEntity qkjvipMemberIntegral = new QkjvipMemberIntegralEntity();
        Map map = new HashMap();
        StringBuilder param = new StringBuilder();
        param.append("<a href='{site}?appid={appid}&callback=");
        param.append(URLEncoder.encode(qkjvipMemberMessage.getUrl(), "UTF-8"));
        param.append("'></a>");
        String content = qkjvipMemberMessage.getContent().replace("<Link>", param.toString());
        map.put("content", content);
        String[] appidAttr = qkjvipMemberMessage.getChannels().split(",");
        Map sonMap = new HashMap();
        List<String> Openids = new ArrayList<>();
        for (int i = 0; i < appidAttr.length; i++) {
            sonMap.clear();
            sonMap.put("appId", appidAttr[i]);
            for (int j = 0; j < fansList.size(); j++) {
                if (appidAttr[i] != null && appidAttr[i].equals(fansList.get(j).getAppid())) {
                    if (fansList.get(j).getOpenid() != null && !"".equals(fansList.get(j).getOpenid())) {
                        Openids.add(fansList.get(j).getOpenid());
                    }
                }
            }
            sonMap.put("openIds", Openids);
            map.put("list", sonMap);
        }

        String queryJsonStr = JsonHelper.toJsonString(map);
        String resultPost = HttpClient.sendPost(Vars.MESSAGE_SEND, queryJsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if (!"0".equals(resultObject.get("code").toString())) {  //调用失败
            if ("2".equals(qkjvipMemberMessage.getCategoryType())) {
                qkjvipMemberIntegral.setSendStatus(1); //状态修改为积分已发放
                qkjvipMemberIntegral.setId(qkjvipMemberMessage.getCategoryId());
                qkjvipMemberIntegralService.updateStatus(qkjvipMemberIntegral);
            }
            //失败同时删除发放记录
            String[] ids = new String[]{qkjvipMemberMessage.getId()};
            qkjvipMemberMessageService.deleteBatch(ids);
        }
    }
}
