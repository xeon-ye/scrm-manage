/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberCponController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-12-17 14:46:26        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import cn.emay.util.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.entity.QkjvipMemberCponsonEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberCponsonService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberCponEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberCponService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-12-17 14:46:26
 */
@RestController
@RequestMapping("qkjvip/membercpon")
public class QkjvipMemberCponController extends AbstractController {
    @Autowired
    private QkjvipMemberCponService qkjvipMemberCponService;
    @Autowired
    private QkjvipMemberCponsonService qkjvipMemberCponsonService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:membercpon:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberCponEntity> list = qkjvipMemberCponService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:membercpon:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberCponService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:membercpon:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberCponEntity qkjvipMemberCpon = qkjvipMemberCponService.getById(id);

        return RestResponse.success().put("membercpon", qkjvipMemberCpon);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberCpon qkjvipMemberCpon
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:membercpon:save")
    public RestResponse save(@RequestBody QkjvipMemberCponEntity qkjvipMemberCpon) {
        qkjvipMemberCpon.setAddUser(getUserId());
        qkjvipMemberCpon.setAddDept(getOrgNo());
        qkjvipMemberCpon.setAddTime(DateUtil.toString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        List<QkjvipMemberCponsonEntity> sonlists=new ArrayList<>();
        sonlists=qkjvipMemberCpon.getSonlists();
        if(sonlists.size()>0){
            qkjvipMemberCponsonService.batchAdd(sonlists);
        }
        qkjvipMemberCponService.add(qkjvipMemberCpon);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberCpon qkjvipMemberCpon
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:membercpon:update")
    public RestResponse update(@RequestBody QkjvipMemberCponEntity qkjvipMemberCpon) {
        qkjvipMemberCponService.update(qkjvipMemberCpon);

        List<QkjvipMemberCponsonEntity> sonlists=new ArrayList<>();
        sonlists=qkjvipMemberCpon.getSonlists();
        if(sonlists.size()>0){
            //删除
            qkjvipMemberCponsonService.deleteBatchByOrder(qkjvipMemberCpon.getId());
            qkjvipMemberCponsonService.batchAdd(sonlists);
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
    @RequiresPermissions("qkjvip:membercpon:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberCponService.deleteBatch(ids);

        return RestResponse.success();
    }
}
