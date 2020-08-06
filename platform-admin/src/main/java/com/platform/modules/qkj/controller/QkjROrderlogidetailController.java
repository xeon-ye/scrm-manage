/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderlogidetailController.java
 * 包名称:com.platform.modules.qkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-11-28 09:16:35        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkj.entity.QkjROrderlogidetailEntity;
import com.platform.modules.qkj.service.QkjROrderlogidetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2019-11-28 09:16:35
 */
@RestController
@RequestMapping("qkj/rorderlogidetail")
public class QkjROrderlogidetailController extends AbstractController {
    @Autowired
    private QkjROrderlogidetailService qkjROrderlogidetailService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkj:rorderlogidetail:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjROrderlogidetailEntity> list = qkjROrderlogidetailService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkj:rorderlogidetail:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjROrderlogidetailService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkj:rorderlogidetail:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjROrderlogidetailEntity qkjROrderlogidetail = qkjROrderlogidetailService.getById(id);

        return RestResponse.success().put("rorderlogidetail", qkjROrderlogidetail);
    }

    /**
     * 新增
     *
     * @param qkjROrderlogidetail qkjROrderlogidetail
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkj:rorderlogidetail:save")
    public RestResponse save(@RequestBody QkjROrderlogidetailEntity qkjROrderlogidetail) {

        qkjROrderlogidetailService.add(qkjROrderlogidetail);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjROrderlogidetail qkjROrderlogidetail
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkj:rorderlogidetail:update")
    public RestResponse update(@RequestBody QkjROrderlogidetailEntity qkjROrderlogidetail) {

        qkjROrderlogidetailService.update(qkjROrderlogidetail);

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
    @RequiresPermissions("qkj:rorderlogidetail:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjROrderlogidetailService.deleteBatch(ids);

        return RestResponse.success();
    }
}
