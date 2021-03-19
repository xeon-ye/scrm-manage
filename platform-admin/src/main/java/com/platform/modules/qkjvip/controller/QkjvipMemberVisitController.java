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
import com.platform.common.utils.StringUtils;
import com.platform.modules.qkjvip.entity.QkjvipMemberIntentionorderEntity;
import com.platform.modules.qkjvip.entity.QkjvipMemberSignupaddressEntity;
import com.platform.modules.qkjvip.entity.QkjvipMemberVisitMaterialEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberIntentionorderService;
import com.platform.modules.qkjvip.service.QkjvipMemberVisitMaterialService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberVisitEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberVisitService;
import org.apache.poi.ss.formula.functions.T;
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
    private QkjvipMemberIntentionorderService qkjvipMemberIntentionorderService;

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
        // 每个人只可查看自己添加的数据和对应部门的数据
        params.put("dataScope", getDataScope("qkjvip:membervisit:list", "T.add_user", "T.add_dept", null));
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
//        QkjvipMemberVisitEntity qkjvipMemberVisit = qkjvipMemberVisitService.getById(id);
        Map params = new HashMap();
        params.put("id", id);
        List<QkjvipMemberVisitEntity> list = qkjvipMemberVisitService.queryAll(params);
        QkjvipMemberVisitEntity qkjvipMemberVisit = new QkjvipMemberVisitEntity();
        if (list.size() > 0) {
            qkjvipMemberVisit = list.get(0);
        }
        //查询物料
        params.clear();
        params.put("visitId", id);
        qkjvipMemberVisit.setMaterialList(qkjvipMemberVisitMaterialService.queryAll(params));
        //查询订单
        qkjvipMemberVisit.setOrderList(qkjvipMemberIntentionorderService.queryAll(params));
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
        Date nowDate = new Date();
        if (qkjvipMemberVisit.getVisitStartDate().after(nowDate)) {
            qkjvipMemberVisit.setVisitStatus(1);  // 计划拜访
        }
        if (qkjvipMemberVisit.getVisitEndDate().before(nowDate)) {
            if (StringUtils.isNotBlank(qkjvipMemberVisit.getContent())) {  // 时间在今天之前并且总结已填为已完成
                qkjvipMemberVisit.setVisitStatus(2);
            } else {
                qkjvipMemberVisit.setVisitStatus(3);  //总结未填为未拜访
            }
        }
        qkjvipMemberVisit.setAddUser(getUserId());
        qkjvipMemberVisit.setAddDept(getOrgNo());
        qkjvipMemberVisit.setAddTime(new Date());
        qkjvipMemberVisitService.add(qkjvipMemberVisit);

        // 新增物料
        if (qkjvipMemberVisit.getMaterialList().size() > 0) {
            for(QkjvipMemberVisitMaterialEntity mvm : qkjvipMemberVisit.getMaterialList()){
                mvm.setVisitId(qkjvipMemberVisit.getId());
                mvm.setAddUser(getUserId());
                mvm.setAddDept(getOrgNo());
                mvm.setAddTime(new Date());
            }
            qkjvipMemberVisitMaterialService.addBatch(qkjvipMemberVisit.getMaterialList());
        }

        // 新增订单
        if (qkjvipMemberVisit.getOrderList().size() > 0) {
            for(QkjvipMemberIntentionorderEntity mio : qkjvipMemberVisit.getOrderList()){
                mio.setVisitId(qkjvipMemberVisit.getId());
                mio.setMemberId(qkjvipMemberVisit.getMemberId());
                mio.setAddUser(getUserId());
                mio.setAddDept(getOrgNo());
                mio.setAddTime(new Date());
            }
            qkjvipMemberIntentionorderService.addBatch(qkjvipMemberVisit.getOrderList());
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
        Date nowDate = new Date();
        if (qkjvipMemberVisit.getVisitStartDate().after(nowDate)) {
            qkjvipMemberVisit.setVisitStatus(1);  // 计划拜访
        }
        if (qkjvipMemberVisit.getVisitEndDate().before(nowDate)) {
            if (StringUtils.isNotBlank(qkjvipMemberVisit.getContent())) {  // 时间在今天之前并且总结已填为已完成
                qkjvipMemberVisit.setVisitStatus(2);
            } else {
                qkjvipMemberVisit.setVisitStatus(3);  //总结未填为未拜访
            }
        }
        qkjvipMemberVisit.setLmUser(getUserId());
        qkjvipMemberVisit.setLmDept(getOrgNo());
        qkjvipMemberVisit.setLmTime(new Date());
        qkjvipMemberVisitService.update(qkjvipMemberVisit);

        // 1.先删除物料表对应的数据
        qkjvipMemberVisitMaterialService.deleteByVisitId(qkjvipMemberVisit.getId());
        // 2.再插入物料
        if (qkjvipMemberVisit.getMaterialList().size() > 0) {
            for(QkjvipMemberVisitMaterialEntity mvm : qkjvipMemberVisit.getMaterialList()){
                mvm.setVisitId(qkjvipMemberVisit.getId());
                mvm.setAddUser(getUserId());
                mvm.setAddDept(getOrgNo());
                mvm.setAddTime(new Date());
            }
            qkjvipMemberVisitMaterialService.addBatch(qkjvipMemberVisit.getMaterialList());
        }

        // 1.先删除订单表对应的数据
        qkjvipMemberIntentionorderService.deleteByVisitId(qkjvipMemberVisit.getId());
        // 2.再插入订单
        if (qkjvipMemberVisit.getOrderList().size() > 0) {
            for(QkjvipMemberIntentionorderEntity mio : qkjvipMemberVisit.getOrderList()){
                mio.setVisitId(qkjvipMemberVisit.getId());
                mio.setMemberId(qkjvipMemberVisit.getMemberId());
                mio.setAddUser(getUserId());
                mio.setAddDept(getOrgNo());
                mio.setAddTime(new Date());
            }
            qkjvipMemberIntentionorderService.addBatch(qkjvipMemberVisit.getOrderList());
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
        qkjvipMemberIntentionorderService.deleteByVisitIds(ids);
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
