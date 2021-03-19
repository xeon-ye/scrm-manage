/*
 * 项目名称:platform-plus
 * 类名称:QkjvipOrderWarehouseController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-03-15 15:49:03        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipOrderWarehouseEntity;
import com.platform.modules.qkjvip.service.QkjvipOrderWarehouseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2021-03-15 15:49:03
 */
@RestController
@RequestMapping("qkjvip/orderwarehouse")
public class QkjvipOrderWarehouseController extends AbstractController {
    @Autowired
    private QkjvipOrderWarehouseService qkjvipOrderWarehouseService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:orderwarehouse:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipOrderWarehouseEntity> list = qkjvipOrderWarehouseService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:orderwarehouse:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipOrderWarehouseService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:orderwarehouse:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipOrderWarehouseEntity qkjvipOrderWarehouse = qkjvipOrderWarehouseService.getById(id);

        return RestResponse.success().put("orderwarehouse", qkjvipOrderWarehouse);
    }

    /**
     * 新增
     *
     * @param qkjvipOrderWarehouse qkjvipOrderWarehouse
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:orderwarehouse:save")
    public RestResponse save(@RequestBody QkjvipOrderWarehouseEntity qkjvipOrderWarehouse) {
        qkjvipOrderWarehouse.setCreator(getUserId());
        qkjvipOrderWarehouse.setCreateon(new Date());
        qkjvipOrderWarehouseService.add(qkjvipOrderWarehouse);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipOrderWarehouse qkjvipOrderWarehouse
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:orderwarehouse:update")
    public RestResponse update(@RequestBody QkjvipOrderWarehouseEntity qkjvipOrderWarehouse) {

        qkjvipOrderWarehouseService.update(qkjvipOrderWarehouse);

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
    @RequiresPermissions("qkjvip:orderwarehouse:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipOrderWarehouseService.deleteBatch(ids);

        return RestResponse.success();
    }
}
