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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.service.QkjvipMemberActivitymbsService;
import com.platform.modules.qkjvip.service.QkjvipMemberSignupService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberSignupmemberEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberSignupmemberService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    private QkjvipMemberSignupService qkjvipMemberSignupService;
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

        
        //邀请补充
        //qkjvipMemberActivitymbsService.supadd(qkjvipMemberSignup.getAcitvityId(),member.getMemberId());
        //报名补充
        //qkjvipMemberSignupService.supadd(activityid,memberid);
        //签到
        //已签到显示行程安排、参加的活动记录、用户的积分、积分商城
        qkjvipMemberSignupmemberService.add(qkjvipMemberSignupmember);
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
