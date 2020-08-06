/*
 * 项目名称:platform-plus
 * 类名称:QkjRSonorderController.java
 * 包名称:com.platform.modules.qkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-28 14:44:59        孙珊珊sc     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkj.entity.QkjRSonorderEntity;
import com.platform.modules.qkj.service.QkjRSonorderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
@RestController
@RequestMapping("qkj/rsonorder")
public class QkjRSonorderController extends AbstractController {
    @Autowired
    private QkjRSonorderService qkjRSonorderService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjRSonorderEntity> list = qkjRSonorderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkj:rsonorder:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjRSonorderService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkj:rsonorder:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjRSonorderEntity qkjRSonorder = qkjRSonorderService.getById(id);

        return RestResponse.success().put("rsonorder", qkjRSonorder);
    }

    /**
     * 新增
     *
     * @param qkjRSonorder qkjRSonorder
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkj:rsonorder:save")
    public RestResponse save(@RequestBody QkjRSonorderEntity qkjRSonorder) {

        qkjRSonorderService.add(qkjRSonorder);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjRSonorder qkjRSonorder
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkj:rsonorder:update")
    public RestResponse update(@RequestBody QkjRSonorderEntity qkjRSonorder) {

        qkjRSonorderService.update(qkjRSonorder);

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
    @RequiresPermissions("qkj:rsonorder:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjRSonorderService.deleteBatch(ids);

        return RestResponse.success();
    }
}
