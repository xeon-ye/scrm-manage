/*
 * 项目名称:platform-plus
 * 类名称:QkjOaLeaveController.java
 * 包名称:com.platform.modules.qkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-04-22 17:25:47        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkj.entity.QkjOaLeaveEntity;
import com.platform.modules.qkj.service.QkjOaLeaveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 注册Controller
 *
 * @author liuqianru
 * @date 2020-04-22 17:25:47
 */
@RestController
@RequestMapping("qkj/oaleave")
public class QkjOaLeaveController extends AbstractController {
    @Autowired
    private QkjOaLeaveService qkjOaLeaveService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkj:oaleave:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjOaLeaveEntity> list = qkjOaLeaveService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询注册
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkj:oaleave:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjOaLeaveService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkj:oaleave:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjOaLeaveEntity qkjOaLeave = qkjOaLeaveService.getById(id);

        return RestResponse.success().put("oaleave", qkjOaLeave);
    }

    /**
     * 新增注册
     *
     * @param qkjOaLeave qkjOaLeave
     * @return RestResponse
     */
    @SysLog("新增注册")
    @RequestMapping("/save")
    @RequiresPermissions("qkj:oaleave:save")
    public RestResponse save(@RequestBody QkjOaLeaveEntity qkjOaLeave) {

        qkjOaLeaveService.add(qkjOaLeave);

        return RestResponse.success();
    }

    /**
     * 修改注册
     *
     * @param qkjOaLeave qkjOaLeave
     * @return RestResponse
     */
    @SysLog("修改注册")
    @RequestMapping("/update")
    @RequiresPermissions("qkj:oaleave:update")
    public RestResponse update(@RequestBody QkjOaLeaveEntity qkjOaLeave) {

        qkjOaLeaveService.update(qkjOaLeave);

        return RestResponse.success();
    }

    /**
     * 根据主键删除注册
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除注册")
    @RequestMapping("/delete")
    @RequiresPermissions("qkj:oaleave:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjOaLeaveService.deleteBatch(ids);

        return RestResponse.success();
    }
}
