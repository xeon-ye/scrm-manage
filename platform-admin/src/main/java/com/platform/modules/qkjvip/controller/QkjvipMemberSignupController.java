/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberSignupController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-30 09:20:27        孙珊珊     初版做成
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
import com.platform.modules.qkjvip.service.QkjvipMemberActivityService;
import com.platform.modules.qkjvip.service.QkjvipMemberActivitymbsService;
import com.platform.modules.qkjvip.service.QkjvipMemberImportService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.service.QkjvipMemberSignupService;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-09-30 09:20:27
 */
@RestController
@RequestMapping("qkjvip/membersignup")
public class QkjvipMemberSignupController extends AbstractController {
    @Autowired
    private QkjvipMemberSignupService qkjvipMemberSignupService;
    @Autowired
    private QkjvipMemberActivityService qkjvipMemberActivityService;
    @Autowired
    private QkjvipMemberImportService qkjvipMemberImportService;
    @Autowired
    private QkjvipMemberActivitymbsService qkjvipMemberActivitymbsService;


    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberSignupEntity> list = qkjvipMemberSignupService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:membersignup:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberSignupService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:membersignup:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberSignupEntity qkjvipMemberSignup = qkjvipMemberSignupService.getById(id);

        return RestResponse.success().put("membersignup", qkjvipMemberSignup);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberSignup qkjvipMemberSignup
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody QkjvipMemberSignupEntity qkjvipMemberSignup) {
        Map<String, Object> params=new HashMap<>();
        params.put("phone",qkjvipMemberSignup.getPhone());
        params.put("acitvityid",qkjvipMemberSignup.getAcitvityId());
        List<QkjvipMemberSignupEntity> list = qkjvipMemberSignupService.queryAll(params);
        if(list.size()>0){
            //return RestResponse.error("已报名成功，谢谢");
            qkjvipMemberSignup = list.get(0);
        }else{
            List<QkjvipMemberImportEntity> mbs=new ArrayList<>();
            //查询中间表是否有此手机号
            Map<String, Object> map=new HashMap<>();
            map.put("mobile",qkjvipMemberSignup.getPhone());
            mbs=qkjvipMemberImportService.selectMemberByMobile(map);
            QkjvipMemberImportEntity memberImport=new QkjvipMemberImportEntity();
            memberImport.setAddUser(qkjvipMemberSignup.getMemadduser());
            memberImport.setAddDept(qkjvipMemberSignup.getMemadddept());
            memberImport.setOrgUserid(qkjvipMemberSignup.getMemadduser());
            memberImport.setOrgNo(qkjvipMemberSignup.getMemadddept());
            memberImport.setServicename("天佑德青青稞酒");
            memberImport.setAddTime(new Date());
            memberImport.setOfflineflag(2);
            memberImport.setMemberName(qkjvipMemberSignup.getUserName());
            memberImport.setMobile(qkjvipMemberSignup.getPhone());
            memberImport.setOpenid(qkjvipMemberSignup.getOpenid());
            if(mbs==null||mbs.size()<=0){
                //清洗会员
                qkjvipMemberImportService.add(memberImport);  //将数据保存到中间表
            }

            //调用数据清洗接口
            MemberEntity member = new MemberEntity();
            try {
                Object obj = JSONArray.toJSON(memberImport);
                String memberJsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
                System.out.println("报名清洗会员："+memberJsonStr);
                String resultPost = HttpClient.sendPost(Vars.MEMBER_ADD_URL, memberJsonStr);
                //插入会员标签
                JSONObject resultObject = JSON.parseObject(resultPost);
                member.setMemberId(resultObject.get("memberid").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //如果没有邀约记录则补充
            qkjvipMemberActivitymbsService.supadd(qkjvipMemberSignup.getAcitvityId(),member.getMemberId());
            //添加报名记录
            qkjvipMemberSignup.setMemberid(member.getMemberId());
            qkjvipMemberSignupService.add(qkjvipMemberSignup);

        }

        //查询活动情况
        //QkjvipMemberActivityEntity qkjvipMemberActivity = qkjvipMemberActivityService.getById(qkjvipMemberSignup.getAcitvityId());
        return RestResponse.success().put("membersignup",qkjvipMemberSignup);
    }

    /**
     * 修改
     *
     * @param qkjvipMemberSignup qkjvipMemberSignup
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:membersignup:update")
    public RestResponse update(@RequestBody QkjvipMemberSignupEntity qkjvipMemberSignup) {

        qkjvipMemberSignupService.update(qkjvipMemberSignup);

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
    @RequiresPermissions("qkjvip:membersignup:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberSignupService.deleteBatch(ids);

        return RestResponse.success();
    }
}
