/*
 * 项目名称:platform-plus
 * 类名称:QkjRMaterialController.java
 * 包名称:com.platform.modules.qkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-20 11:15:58        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkj.entity.QkjRMaterialEntity;
import com.platform.modules.qkj.service.QkjRMaterialService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2019-09-20 11:15:58
 */
@RestController
@RequestMapping("qkj/rmaterial")
public class QkjRMaterialController extends AbstractController {
    @Autowired
    private QkjRMaterialService qkjRMaterialService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjRMaterialEntity> list = qkjRMaterialService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkj:rmaterial:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjRMaterialService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param fid 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{fid}")
    @RequiresPermissions("qkj:rmaterial:info")
    public RestResponse info(@PathVariable("fid") String fid) {
        QkjRMaterialEntity qkjRMaterial = qkjRMaterialService.getById(fid);

        return RestResponse.success().put("rmaterial", qkjRMaterial);
    }

    /**
     * 新增
     *
     * @param qkjRMaterial qkjRMaterial
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkj:rmaterial:save")
    public RestResponse save(@RequestBody QkjRMaterialEntity qkjRMaterial) {

        qkjRMaterialService.add(qkjRMaterial);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjRMaterial qkjRMaterial
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkj:rmaterial:update")
    public RestResponse update(@RequestBody QkjRMaterialEntity qkjRMaterial) {

        qkjRMaterialService.update(qkjRMaterial);

        return RestResponse.success();
    }

    /**
     * 根据主键删除
     *
     * @param fids fids
     * @return RestResponse
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("qkj:rmaterial:delete")
    public RestResponse delete(@RequestBody String[] fids) {
        qkjRMaterialService.deleteBatch(fids);

        return RestResponse.success();
    }
}
