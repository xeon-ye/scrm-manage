/*
 * 项目名称:platform-plus
 * 类名称:QkjvipContentGroupController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-03-24 15:41:39        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.qkjvip.service.*;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2021-03-24 15:41:39
 */
@RestController
@RequestMapping("qkjvip/contentgroup")
public class QkjvipContentGroupController extends AbstractController {
    @Autowired
    private QkjvipContentGroupService qkjvipContentGroupService;
    @Autowired
    private QkjvipContentPushchannelService qkjvipContentPushchannelService;
    @Autowired
    private QkjvipMemberMessageService qkjvipMemberMessageService;
    @Autowired
    private QkjvipContentService qkjvipContentService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:contentgroup:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipContentGroupEntity> list = qkjvipContentGroupService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:contentgroup:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipContentGroupService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:contentgroup:info")
    public RestResponse info(@PathVariable("id") String id) {
        List<QkjvipContentEntity> contentList = new ArrayList<>();
        List<QkjvipContentPushchannelEntity> pushChannels = new ArrayList<>();
        List<String> appids = new ArrayList<>();
        QkjvipContentGroupEntity qkjvipContentGroup = qkjvipContentGroupService.getById(id);

        contentList = qkjvipContentService.queryByGroupId(id);
        qkjvipContentGroup.setContentList(contentList);

        Map param = new HashMap();
        param.put("groupid", id);
        pushChannels = qkjvipContentPushchannelService.queryAll(param);
        for (QkjvipContentPushchannelEntity pushchannelEntity : pushChannels) {
            appids.add(pushchannelEntity.getAppid());
        }
        qkjvipContentGroup.setAppids(appids);

        return RestResponse.success().put("contentgroup", qkjvipContentGroup);
    }

    /**
     * 新增
     *
     * @param qkjvipContentGroup qkjvipContentGroup
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:contentgroup:save")
    public RestResponse save(@RequestBody QkjvipContentGroupEntity qkjvipContentGroup) {
        qkjvipContentGroup.setAddDept(getOrgNo());
        qkjvipContentGroup.setAddUser(getUserId());
        qkjvipContentGroup.setAddTime(new Date());
        qkjvipContentGroupService.add(qkjvipContentGroup);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipContentGroup qkjvipContentGroup
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:contentgroup:update")
    public RestResponse update(@RequestBody QkjvipContentGroupEntity qkjvipContentGroup) {
        qkjvipContentGroup.setLmUser(getUserId());
        qkjvipContentGroup.setLmDept(getOrgNo());
        qkjvipContentGroup.setLmTime(new Date());
        qkjvipContentGroupService.update(qkjvipContentGroup);

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
    @RequiresPermissions("qkjvip:contentgroup:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipContentGroupService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 推送文章
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/pushToApp/{id}")
    public RestResponse pushToApp(@PathVariable("id") String id) {
        List<QkjvipContentEntity> contentList = new ArrayList<>();
        List<QkjvipContentPushchannelEntity> pushChannels = new ArrayList<>();
        List<String> appids = new ArrayList<>();

        contentList = qkjvipContentService.queryByGroupId(id);

        Map param = new HashMap();
        param.put("groupid", id);
        pushChannels = qkjvipContentPushchannelService.queryAll(param);
        for (QkjvipContentPushchannelEntity pushchannelEntity : pushChannels) {
            appids.add(pushchannelEntity.getAppid());
        }

        // TODO 推送公众号接口

        return RestResponse.success();
    }
}
