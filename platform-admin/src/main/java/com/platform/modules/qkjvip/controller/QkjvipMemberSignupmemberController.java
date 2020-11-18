/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberSignupmemberController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-10-26 13:18:34        孙珊珊     初版做成
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
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-10-26 13:18:34
 */
@RestController
@RequestMapping("qkjvip/membersignupmember")
public class QkjvipMemberSignupmemberController extends AbstractController {
    @Autowired
    private QkjvipMemberSignupmemberService qkjvipMemberSignupmemberService;
    @Autowired
    private QkjvipMemberActivitymbsService qkjvipMemberActivitymbsService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private QkjvipMemberSignupService qkjvipMemberSignupService;
    @Autowired
    private QkjvipMemberImportService qkjvipMemberImportService;
    @Autowired
    private QkjvipMemberActivityService qkjvipMemberActivityService;
    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:membersignupmember:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberSignupmemberEntity> list = qkjvipMemberSignupmemberService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:membersignupmember:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberSignupmemberService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:membersignupmember:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberSignupmemberEntity qkjvipMemberSignupmember = qkjvipMemberSignupmemberService.getById(id);

        return RestResponse.success().put("membersignupmember", qkjvipMemberSignupmember);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberSignupmember qkjvipMemberSignupmember
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:membersignupmember:save")
    public RestResponse save(@RequestBody QkjvipMemberSignupmemberEntity qkjvipMemberSignupmember) {

        qkjvipMemberSignupmemberService.add(qkjvipMemberSignupmember);

        return RestResponse.success();
    }

    /**
     * 批量签到
     *
     * @param qkjvipMemberSignupmember qkjvipMemberSignupmember
     * @return RestResponse
     */
    @SysLog("批量签到")
    @RequestMapping("/saveMems")
    public RestResponse saveMems(@RequestBody QkjvipMemberSignupmemberEntity qkjvipMemberSignupmember) {
        if(qkjvipMemberSignupmember.getMemlist().size()>0){
            for(QkjvipMemberActivitymbsEntity mems:qkjvipMemberSignupmember.getMemlist()){
                String bmid="";
                if(mems.getQdstatus()!=null&&mems.getQdstatus().equals(1)){//已签到

                }else{//未签到
                    if(mems.getBmstatus()!=null&&mems.getBmstatus().equals(1)){//已报名
                        bmid=mems.getBmid();
                    }else{//补充报名
                        bmid=qkjvipMemberSignupService.supadd(qkjvipMemberSignupmember.getActivityId(),mems.getMemberId());
                    }
                    //添加签到
                    if(bmid!=""){
                        Date date=new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String date2=sdf.format(date);
                        qkjvipMemberSignupmember.setMemberId(mems.getMemberId());
                        qkjvipMemberSignupmember.setTime(date2);
                        qkjvipMemberSignupmember.setSignupId(bmid);
                        qkjvipMemberSignupmemberService.add(qkjvipMemberSignupmember);
                    }

                }
            }
        }
        //qkjvipMemberSignupmemberService.add(qkjvipMemberSignupmember);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberSignupmember qkjvipMemberSignupmember
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:membersignupmember:update")
    public RestResponse update(@RequestBody QkjvipMemberSignupmemberEntity qkjvipMemberSignupmember) {

        qkjvipMemberSignupmemberService.update(qkjvipMemberSignupmember);

        return RestResponse.success();
    }

    /**
     * 签到
     *
     * @param qkjvipMemberSignupmember qkjvipMemberSignupmember
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/savesign")
    public RestResponse savesign(@RequestBody QkjvipMemberSignupmemberEntity qkjvipMemberSignupmember) {
        // 清洗会员(微信id有则不清洗,无清洗)
        MemberEntity member = new MemberEntity();
        Map<String, Object> params=new HashMap<>();
        String openid="ojipzwPJcEk0DG2Hn6CAjW2pDGYA";//模拟的微信id
        String mobile="18810242427";//模拟的手机号
        List<MemberEntity> ms=new ArrayList<>();
        params.put("openid",openid);
        ms=memberService.queryAll(params);
        if(ms.size()>0){//有此会员
            if(ms.get(0).getMobile()==null){//补充手机号
                MemberEntity newM=new MemberEntity();
                newM=ms.get(0);
                newM.setMobile(mobile);
                params.clear();
                memberService.update(newM,params);
            }
            member=ms.get(0);
        }else{
            //查询活动
            params.clear();
            params.put("id",qkjvipMemberSignupmember.getActivityId());
            List<QkjvipMemberActivityEntity> list =qkjvipMemberActivityService.queryAll(params);
            if(list.size()>0){
                QkjvipMemberActivityEntity newa=new QkjvipMemberActivityEntity();
                //清洗会员
                newa=list.get(0);
                QkjvipMemberImportEntity memberImport=new QkjvipMemberImportEntity();
                memberImport.setAddUser(newa.getAdduser());
                memberImport.setAddDept(newa.getAdddept());
                memberImport.setOrgUserid(newa.getAdduser());
                memberImport.setOrgNo(newa.getAdddept());
                memberImport.setAddTime(new Date());
                memberImport.setOfflineflag(2);
                memberImport.setMemberName("微信昵称");
                memberImport.setMobile("手机号");
                qkjvipMemberImportService.add(memberImport);  //将数据保存到中间表

                //调用数据清洗接口
                try {
                    Object obj = JSONArray.toJSON(memberImport);
                    String memberJsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
                    String resultPost = HttpClient.sendPost(Vars.MEMBER_ADD_URL, memberJsonStr);
                    //插入会员标签
                    JSONObject resultObject = JSON.parseObject(resultPost);
                    member.setMemberId(resultObject.get("memberid").toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        //是否已签到
        params.clear();
        params.put("memberId",member.getMemberId());
        params.put("activityId",qkjvipMemberSignupmember.getActivityId());
        List<QkjvipMemberSignupmemberEntity> list = qkjvipMemberSignupmemberService.queryAll(params);
        if(list.size()>0){//已签到
            //return RestResponse.error("已签到成功，谢谢");
            //已签到显示行程安排、参加的活动记录、用户的积分、积分商城
        } else {
            //邀请补充
            qkjvipMemberActivitymbsService.supadd(qkjvipMemberSignupmember.getActivityId(),member.getMemberId());
            //报名补充
            String bmid=qkjvipMemberSignupService.supadd(qkjvipMemberSignupmember.getActivityId(),member.getMemberId());
            //添加签到
            Date date=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date2=sdf.format(date);
            qkjvipMemberSignupmember.setMemberId(member.getMemberId());
            qkjvipMemberSignupmember.setTime(date2);
            qkjvipMemberSignupmember.setSignupId(bmid);
            qkjvipMemberSignupmemberService.add(qkjvipMemberSignupmember);
        }

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
    @RequiresPermissions("qkjvip:membersignupmember:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberSignupmemberService.deleteBatch(ids);

        return RestResponse.success();
    }
}
