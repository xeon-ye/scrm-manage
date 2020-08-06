package com.platform.modules.util;

import com.dingtalk.open.client.api.model.corp.MessageBody;
import com.dingtalk.open.client.api.model.corp.MessageType;
import com.platform.modules.accesstoken.entity.AccesstokenEntity;
import com.platform.modules.accesstoken.service.AccesstokenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DingMsg {
    @Autowired
    private AccesstokenService accesstokenService;
    public static void main(String[] args)  throws Exception{
        DingMsg d=new DingMsg();
        d.sendLinkMessage("1","01672211281464","01672211281464","7b2338f0a5233db0b1a0dfe95763e3a9");
    }
    public void sendLinkMessage(String msg,String user,String singuuid,String accessToken) throws Exception{
        //发送消息
        MessageBody.LinkBody linkBody = new MessageBody.LinkBody();
        linkBody.setPicUrl("@lALOACZwe2Rk");
        linkBody.setTitle("供应商管理");
        linkBody.setText(msg);
        // 发送微应用消息
        String toUsers = user;
        String toParties = Vars.TO_PARTY;
        String agentId = Vars.AGENT_ID;
        LightAppMessageDelivery lightAppMessageDelivery = new LightAppMessageDelivery(toUsers, toParties, agentId);
        lightAppMessageDelivery.withMessage(MessageType.LINK, linkBody);
        MessageHelper.send(accessToken, lightAppMessageDelivery);
    }
}
