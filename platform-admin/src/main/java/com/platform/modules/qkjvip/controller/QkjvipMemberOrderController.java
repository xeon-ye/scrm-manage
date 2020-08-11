/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberOrderController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-08-11 14:15:23        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberOrderEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-08-11 14:15:23
 */
@RestController
@RequestMapping("qkjvip/memberorder")
public class QkjvipMemberOrderController extends AbstractController {
    @Autowired
    private QkjvipMemberOrderService qkjvipMemberOrderService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:memberorder:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberOrderEntity> list = qkjvipMemberOrderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:memberorder:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberOrderService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:memberorder:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberOrderEntity qkjvipMemberOrder = qkjvipMemberOrderService.getById(id);

        return RestResponse.success().put("memberorder", qkjvipMemberOrder);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberOrder qkjvipMemberOrder
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:memberorder:save")
    public RestResponse save(@RequestBody QkjvipMemberOrderEntity qkjvipMemberOrder) {

        qkjvipMemberOrderService.add(qkjvipMemberOrder);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberOrder qkjvipMemberOrder
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:memberorder:update")
    public RestResponse update(@RequestBody QkjvipMemberOrderEntity qkjvipMemberOrder) {

        qkjvipMemberOrderService.update(qkjvipMemberOrder);

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
    @RequiresPermissions("qkjvip:memberorder:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberOrderService.deleteBatch(ids);

        return RestResponse.success();
    }
}
