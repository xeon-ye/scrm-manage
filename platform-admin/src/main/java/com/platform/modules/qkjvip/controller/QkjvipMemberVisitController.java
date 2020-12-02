/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberVisitController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-12-01 11:39:36        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.service.QkjvipMemberOrderService;
import com.platform.modules.qkjvip.service.QkjvipMemberVisitMaterialService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberVisitEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberVisitService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2020-12-01 11:39:36
 */
@RestController
@RequestMapping("qkjvip/membervisit")
public class QkjvipMemberVisitController extends AbstractController {
    @Autowired
    private QkjvipMemberVisitService qkjvipMemberVisitService;
    @Autowired
    private QkjvipMemberVisitMaterialService qkjvipMemberVisitMaterialService;
    @Autowired
    private QkjvipMemberOrderService qkjvipMemberOrderService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:membervisit:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberVisitEntity> list = qkjvipMemberVisitService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:membervisit:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberVisitService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:membervisit:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberVisitEntity qkjvipMemberVisit = qkjvipMemberVisitService.getById(id);
        //查询物料
        Map params = new HashMap();
        params.put("visitId", id);
        qkjvipMemberVisit.setMaterialList(qkjvipMemberVisitMaterialService.queryAll(params));
        //查询订单
        qkjvipMemberVisit.setOrderList(qkjvipMemberOrderService.queryAll(params));
        return RestResponse.success().put("membervisit", qkjvipMemberVisit);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberVisit qkjvipMemberVisit
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:membervisit:save")
    public RestResponse save(@RequestBody QkjvipMemberVisitEntity qkjvipMemberVisit) {
        qkjvipMemberVisit.setAddUser(getUserId());
        qkjvipMemberVisit.setAddDept(getOrgNo());
        qkjvipMemberVisit.setAddTime(new Date());
        qkjvipMemberVisitService.add(qkjvipMemberVisit);

        // 新增物料
        if (qkjvipMemberVisit.getMaterialList().size() > 0) {
            for (int i = 0; i < qkjvipMemberVisit.getMaterialList().size(); i++) {
                qkjvipMemberVisit.getMaterialList().get(i).setVisitId(qkjvipMemberVisit.getId());
                qkjvipMemberVisit.getMaterialList().get(i).setAddUser(getUserId());
                qkjvipMemberVisit.getMaterialList().get(i).setAddDept(getOrgNo());
                qkjvipMemberVisit.getMaterialList().get(i).setAddTime(new Date());
            }
            qkjvipMemberVisitMaterialService.addBatch(qkjvipMemberVisit.getMaterialList());
        }

        // 新增订单
        if (qkjvipMemberVisit.getOrderList().size() > 0) {
            for(int j = 0; j < qkjvipMemberVisit.getOrderList().size(); j++) {
                qkjvipMemberVisit.getOrderList().get(j).setVisitId(qkjvipMemberVisit.getId());
            }
            qkjvipMemberOrderService.addBatch(qkjvipMemberVisit.getOrderList());
        }

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberVisit qkjvipMemberVisit
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:membervisit:update")
    public RestResponse update(@RequestBody QkjvipMemberVisitEntity qkjvipMemberVisit) {
        qkjvipMemberVisit.setLmUser(getUserId());
        qkjvipMemberVisit.setLmDept(getOrgNo());
        qkjvipMemberVisit.setLmTime(new Date());
        qkjvipMemberVisitService.update(qkjvipMemberVisit);

        // 1.先删除物料表对应的数据
        qkjvipMemberVisitMaterialService.deleteByVisitId(qkjvipMemberVisit.getId());
        // 2.再插入物料
        if (qkjvipMemberVisit.getMaterialList().size() > 0) {
            for (int i = 0; i < qkjvipMemberVisit.getMaterialList().size(); i++) {
                qkjvipMemberVisit.getMaterialList().get(i).setVisitId(qkjvipMemberVisit.getId());
                qkjvipMemberVisit.getMaterialList().get(i).setAddUser(getUserId());
                qkjvipMemberVisit.getMaterialList().get(i).setAddDept(getOrgNo());
                qkjvipMemberVisit.getMaterialList().get(i).setAddTime(new Date());
            }
            qkjvipMemberVisitMaterialService.addBatch(qkjvipMemberVisit.getMaterialList());
        }

        // 1.先删除订单表对应的数据
        qkjvipMemberOrderService.deleteByVisitId(qkjvipMemberVisit.getId());
        // 2.再插入订单
        if (qkjvipMemberVisit.getOrderList().size() > 0) {
            for(int j = 0; j < qkjvipMemberVisit.getOrderList().size(); j++) {
                qkjvipMemberVisit.getOrderList().get(j).setVisitId(qkjvipMemberVisit.getId());
            }
            qkjvipMemberOrderService.addBatch(qkjvipMemberVisit.getOrderList());
        }

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
    @RequiresPermissions("qkjvip:membervisit:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberVisitService.deleteBatch(ids);
        // 删除物料
        qkjvipMemberVisitMaterialService.deleteByVisitIds(ids);
        // 删除订单
        qkjvipMemberOrderService.deleteByVisitIds(ids);
        return RestResponse.success();
    }

    /**
     * 根据主键关闭拜访
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/close/{id}")
    @RequiresPermissions("qkjvip:membervisit:close")
    public RestResponse close(@PathVariable("id") String id) {
        qkjvipMemberVisitService.closeById(id);
        return RestResponse.success();
    }
}
