/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberActivityController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-03 09:49:29        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjInterface.controller;


import com.platform.common.utils.RestResponse;
import com.platform.modules.accesstoken.entity.AccesstokenEntity;
import com.platform.modules.accesstoken.service.AccesstokenService;
import com.platform.modules.qkjInterface.entity.UserMsgEntity;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysSmsLogService;
import com.platform.modules.sys.service.SysUserService;
import com.platform.modules.tb.service.UserService;
import com.platform.modules.util.DingMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.*;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-09-03 09:49:29
 */
@RestController
@RequestMapping("qkjvip/sendmsgbatch")
public class QkjvipSendController extends AbstractController {
    @Autowired
    private SysSmsLogService sysSmsLogService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AccesstokenService accesstokenService;

    /**
     * 发短信及钉钉消息
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/sendmsg")
    public RestResponse sendmsg(Map<String, Object> params) {
        UserMsgEntity userMsgEntity = new UserMsgEntity();
        userMsgEntity.setMobilelist(params.get("mobilelist")+"");
        userMsgEntity.setDinglist(params.get("dinglist")+"");
        userMsgEntity.setTitle(params.get("title")+"");
        userMsgEntity.setMsg(params.get("msg")+"");
        if (userMsgEntity!=null && userMsgEntity.getMobilelist() != null && !userMsgEntity.getMobilelist().equals("")) {
            // 发短信
            SysSmsLogEntity smsLog = new SysSmsLogEntity();
            smsLog.setContent(userMsgEntity.getMsg());
            smsLog.setMobile(userMsgEntity.getMobilelist());
            SysSmsLogEntity sysSmsLogEntity = sysSmsLogService.sendSmsBach(smsLog);
        }

        if (userMsgEntity!=null && userMsgEntity.getDinglist()!= null && !userMsgEntity.getDinglist().equals("")) {
            //发钉钉消息
            AccesstokenEntity ak=accesstokenService.queryAll(null).get(0);
            DingMsg demo=new DingMsg();
            String result = demo.sendLinkMessageResult(userMsgEntity.getTitle(),userMsgEntity.getMsg(), userMsgEntity.getDinglist(),"",ak.getAccessToken());
            if (!result.equals("0")) { // 失败
                return RestResponse.error(result);
            }
        }

        return RestResponse.success();
    }

    public static void main(String[] args) {

    }


}
