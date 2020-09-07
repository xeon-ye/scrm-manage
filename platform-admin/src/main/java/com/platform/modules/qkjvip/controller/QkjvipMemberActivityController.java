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
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberActivityEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberActivityService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-09-03 09:49:29
 */
@RestController
@RequestMapping("qkjvip/memberactivity")
public class QkjvipMemberActivityController extends AbstractController {
    @Autowired
    private QkjvipMemberActivityService qkjvipMemberActivityService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:memberactivity:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberActivityEntity> list = qkjvipMemberActivityService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:memberactivity:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberActivityService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:memberactivity:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberActivityEntity qkjvipMemberActivity = qkjvipMemberActivityService.getById(id);

        return RestResponse.success().put("memberactivity", qkjvipMemberActivity);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberActivity qkjvipMemberActivity
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:memberactivity:save")
    public RestResponse save(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {

        qkjvipMemberActivityService.add(qkjvipMemberActivity);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberActivity qkjvipMemberActivity
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:memberactivity:update")
    public RestResponse update(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {

        qkjvipMemberActivityService.update(qkjvipMemberActivity);

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
    @RequiresPermissions("qkjvip:memberactivity:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberActivityService.deleteBatch(ids);

        return RestResponse.success();
    }
}
