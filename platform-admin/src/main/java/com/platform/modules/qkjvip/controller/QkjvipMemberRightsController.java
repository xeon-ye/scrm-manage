/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberRightsController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-04-26 16:38:38        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberRightsEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberRightsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2021-04-26 16:38:38
 */
@RestController
@RequestMapping("qkjvip/memberrights")
public class QkjvipMemberRightsController extends AbstractController {
    @Autowired
    private QkjvipMemberRightsService qkjvipMemberRightsService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:memberrights:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberRightsEntity> list = qkjvipMemberRightsService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:memberrights:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberRightsService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:memberrights:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberRightsEntity qkjvipMemberRights = qkjvipMemberRightsService.getById(id);

        return RestResponse.success().put("memberrights", qkjvipMemberRights);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberRights qkjvipMemberRights
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:memberrights:save")
    public RestResponse save(@RequestBody QkjvipMemberRightsEntity qkjvipMemberRights) {

        qkjvipMemberRightsService.add(qkjvipMemberRights);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberRights qkjvipMemberRights
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:memberrights:update")
    public RestResponse update(@RequestBody QkjvipMemberRightsEntity qkjvipMemberRights) {

        qkjvipMemberRightsService.update(qkjvipMemberRights);

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
    @RequiresPermissions("qkjvip:memberrights:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberRightsService.deleteBatch(ids);

        return RestResponse.success();
    }
}
