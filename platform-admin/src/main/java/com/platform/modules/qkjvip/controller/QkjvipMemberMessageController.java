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
import com.platform.modules.qkjvip.service.*;
import com.platform.modules.quartz.entity.QrtzMemberFansEntity;
import com.platform.modules.quartz.service.QrtzMemberFansService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.service.SysSmsLogService;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.ListToStringUtil;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
    @Autowired
    private QkjvipMemberCponsonService qkjvipMemberCponsonService;
    @Autowired
    private QkjvipMemberCponService qkjvipMemberCponService;
    @Autowired
    private QkjvipMemberActivitymbsService qkjvipMemberActivitymbsService;
    @Autowired
    private QkjvipMemberActivityService qkjvipMemberActivityService;

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
        List<QkjvipMemberMessageUserQueryEntity> selectedUser = new ArrayList<>();
        String userStr = "";
        String openidStr = "";
        //获取公众号列表
        String resultPost = HttpClient.sendGet(Vars.APPID_GETLIST_URL);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("0".equals(resultObject.get("code").toString())) {  //调用成功
            options = JSON.parseArray(resultObject.getString("data"), QkjvipOptionsEntity.class);
            //获取被选人分布在每个公众号的人数
            List<QrtzMemberFansEntity> fansList = new ArrayList<>();
            if (qkjvipMemberMessage.getCategoryType() != null) {
                if ("1".equals(qkjvipMemberMessage.getCategoryType())) {  //活动
                    selectedUser = qkjvipMemberActivitymbsService.queryByActivityId(qkjvipMemberMessage.getCategoryId());
                } else if ("2".equals(qkjvipMemberMessage.getCategoryType())) {  //积分
                    selectedUser = qkjvipMemberIntegraluserService.queryByIntegralId(qkjvipMemberMessage.getCategoryId());
                } else if ("3".equals(qkjvipMemberMessage.getCategoryType())) {  //优惠券
                    selectedUser = qkjvipMemberCponsonService.queryByCponId(qkjvipMemberMessage.getCategoryId());
                }
                Map map = ListToStringUtil.entityToMap(selectedUser);
                fansList = qrtzMemberFansService.queryByMemberMessageQuery(map);
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
    @Transactional(rollbackFor = Exception.class)
    public RestResponse sendMsg(@RequestBody QkjvipMemberMessageEntity qkjvipMemberMessage) throws IOException {
        Map map = new HashMap();
        List<QrtzMemberFansEntity> fansList = new ArrayList<>();
        if (qkjvipMemberMessage.getCategoryType() != null) {
            String[] appidAttr = qkjvipMemberMessage.getChannels().split(",");
            List<String> appidList = Arrays.asList(appidAttr);
            appidList.remove("012345678987654321");
            qkjvipMemberMessage.setAppidList(appidList);
            String appidstr = ListToStringUtil.listToString(appidList);
            List<QkjvipMemberMessageUserQueryEntity> selectedUser = new ArrayList<>();
            String memberidstr = "";
            String openidStr = "";
            String msg = "";

            if ("1".equals(qkjvipMemberMessage.getCategoryType())) {  //活动
                QkjvipMemberActivityEntity qkjvipMemberCpon=new QkjvipMemberActivityEntity();
                qkjvipMemberCpon = qkjvipMemberActivityService.getById(qkjvipMemberMessage.getCategoryId());
                qkjvipMemberCpon.setStatus(2);
                qkjvipMemberActivityService.update(qkjvipMemberCpon);

                selectedUser = qkjvipMemberActivitymbsService.queryByActivityId(qkjvipMemberMessage.getCategoryId());

                if (qkjvipMemberMessage.getChannels().contains("012345678987654321")) {  //包含短信
                    //查询所有邀约人员
                    List<QkjvipMemberActivitymbsEntity> mbs=new ArrayList<>();
                    map.clear();
                    map.put("activityId",qkjvipMemberMessage.getCategoryId());
                    mbs=qkjvipMemberActivitymbsService.queryAll(map);
                    if(mbs.size()>0){
                        for(QkjvipMemberActivitymbsEntity a:mbs){
                            if(a!=null&&a.getMobile()!=null&&!a.getMobile().equals("")){
                                //发短信
                                msg = qkjvipMemberMessage.getDxContent() + "请在微信里打开以下链接：" + qkjvipMemberMessage.getUrl();
                                this.sendMobileMsg(msg, a.getMobile());
                            }
                        }
                    }
                }
            } else if ("2".equals(qkjvipMemberMessage.getCategoryType())) {  //积分
                QkjvipMemberIntegralEntity qkjvipMemberIntegral = new QkjvipMemberIntegralEntity();
                qkjvipMemberIntegral.setSendStatus(2); //状态修改为通知已发送
                qkjvipMemberIntegral.setId(qkjvipMemberMessage.getCategoryId());
                qkjvipMemberIntegralService.updateStatus(qkjvipMemberIntegral);

                selectedUser = qkjvipMemberIntegraluserService.queryByIntegralId(qkjvipMemberMessage.getCategoryId());

                if (qkjvipMemberMessage.getChannels().contains("012345678987654321")) {  //包含短信
                    map.clear();
                    map.put("integralId", qkjvipMemberMessage.getCategoryId());
                    List<QkjvipMemberIntegraluserEntity> integralusers = new ArrayList<>();
                    integralusers = qkjvipMemberIntegraluserService.queryAll(map);

                    //群发发短信
                    for(QkjvipMemberIntegraluserEntity integraluser : integralusers){
                        if(integraluser != null && integraluser.getMobile() != null && !integraluser.getMobile().equals("")){
                            msg = qkjvipMemberMessage.getDxContent() + "请在微信里打开以下链接：" + qkjvipMemberMessage.getUrl();
                            this.sendMobileMsg(msg, integraluser.getMobile());
                        }
                    }
                }
            } else if ("3".equals(qkjvipMemberMessage.getCategoryType())) {  //优惠券
                QkjvipMemberCponEntity qkjvipMemberCpon=new QkjvipMemberCponEntity();
                qkjvipMemberCpon = qkjvipMemberCponService.getById(qkjvipMemberMessage.getCategoryId());
                qkjvipMemberCpon.setStatus(2);
                qkjvipMemberCponService.update(qkjvipMemberCpon);

                selectedUser = qkjvipMemberCponsonService.queryByCponId(qkjvipMemberMessage.getCategoryId());

                if (qkjvipMemberMessage.getChannels().contains("012345678987654321")) {  //包含短信
                    map.clear();
                    map.put("mainId", qkjvipMemberMessage.getCategoryId());
                    List<QkjvipMemberCponsonEntity> integralusers = new ArrayList<>();
                    integralusers = qkjvipMemberCponsonService.queryAll(map);

                    //群发发短信
                    for(QkjvipMemberCponsonEntity integraluser : integralusers){
                        if(integraluser != null && integraluser.getMobile() != null && !integraluser.getMobile().equals("")){
                            msg = qkjvipMemberMessage.getDxContent() + "请在微信里打开以下链接：" + qkjvipMemberMessage.getUrl();
                            this.sendMobileMsg(msg, integraluser.getMobile());
                        }
                    }
                }

            }

            qkjvipMemberMessage.setAddUser(getUserId());
            qkjvipMemberMessage.setAddDept(getOrgNo());
            qkjvipMemberMessage.setAddTime(new Date());
            qkjvipMemberMessageService.add(qkjvipMemberMessage);  //保存发放记录

            Map queryMap = ListToStringUtil.entityToMap(selectedUser);
            memberidstr = queryMap.get("userStr").toString();
            openidStr = queryMap.get("openidStr").toString();
            if (!"".equals(appidstr) && (!"('')".equals(memberidstr) || !"('')".equals(openidStr))) {
                map.clear();
                map.put("appidstr", appidstr);
                map.put("memberidstr", memberidstr);
                map.put("openidStr", openidStr);
                fansList = qrtzMemberFansService.queryAll(map);
                //调用赵月辉接口
                if (fansList.size() > 0) {
                    this.sendWxMsg(qkjvipMemberMessage, fansList);
                }
            }
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
        smsLog.setMobile(mobile);
//        smsLog.setMobile("13621255469");
        SysSmsLogEntity sysSmsLogEntity = sysSmsLogService.sendSms(smsLog);
    }

    /**
     * 群发微信
     * @param qkjvipMemberMessage
     * @param fansList 微信用户列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendWxMsg(QkjvipMemberMessageEntity qkjvipMemberMessage, List<QrtzMemberFansEntity> fansList) throws IOException {
        Map map = new HashMap();
        List<String> appidList = qkjvipMemberMessage.getAppidList();
        List<Object> list = new ArrayList<>();
        // 测试start
//        sonMap.put("appId", "wx2d52554e706d23ad");
//        List<String> Openids = new ArrayList<>();
//        List<Object> list = new ArrayList<>();
//        Openids.add("ozmFr1S-kFJIlTSIrpj-OnK907jE");
//        Openids.add("ozmFr1S-kFJIlTSIrpj-OnK907jE");
//        sonMap.put("openIds", Openids);
//        list.add(sonMap);
//        map.put("list", list);
        // 测试end
        // 正式start
        for (int i = 0; i < appidList.size(); i++) {
            List<String> openIds = new ArrayList<>();
            Map sonMap = new HashMap();
            sonMap.clear();
            sonMap.put("appId", appidList.get(i));
            for (int j = 0; j < fansList.size(); j++) {
                if (appidList.get(i) != null && appidList.get(i).equals(fansList.get(j).getAppid())) {
                    if (fansList.get(j).getOpenid() != null && !"".equals(fansList.get(j).getOpenid())) {
                        openIds.add(fansList.get(j).getOpenid());
                    }
                }
            }
            if (openIds != null && openIds.size() > 1) {  //一个appid下至少有2个人才可以发送图文消息
                sonMap.put("openIds", openIds);
                list.add(sonMap);
            }
        }
        if (list != null && list.size() > 0) {
            map.put("type", Integer.parseInt(qkjvipMemberMessage.getCategoryType().trim()));
            map.put("title", qkjvipMemberMessage.getTitle());
            map.put("url", qkjvipMemberMessage.getUrl());
            map.put("content", qkjvipMemberMessage.getWxContent());
            map.put("list", list);

            String queryJsonStr = JsonHelper.toJsonString(map);
            String resultPost = HttpClient.sendPost(Vars.MESSAGE_SEND, queryJsonStr);
            JSONObject resultObject = JSON.parseObject(resultPost);
            if (!"0".equals(resultObject.get("code").toString())) {  //调用失败
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        // 正式end
    }
}
