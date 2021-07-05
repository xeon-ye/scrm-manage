/*
 * 项目名称:platform-plus
 * 类名称:QkjvipLotterySettingController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-07-05 10:59:58        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.entity.QkjvipLotteryUsersEntity;
import com.platform.modules.qkjvip.service.QkjvipLotteryUsersService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipLotterySettingEntity;
import com.platform.modules.qkjvip.service.QkjvipLotterySettingService;
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
 * @date 2021-07-05 10:59:58
 */
@RestController
@RequestMapping("qkjvip/lotterysetting")
public class QkjvipLotterySettingController extends AbstractController {
    @Autowired
    private QkjvipLotterySettingService qkjvipLotterySettingService;
    @Autowired
    private QkjvipLotteryUsersService qkjvipLotteryUsersService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:lotterysetting:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipLotterySettingEntity> list = qkjvipLotterySettingService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:lotterysetting:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipLotterySettingService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:lotterysetting:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipLotterySettingEntity qkjvipLotterySetting = qkjvipLotterySettingService.getById(id);
        Map map = new HashMap();
        map.put("mainid", id);
        List<QkjvipLotteryUsersEntity> users = qkjvipLotteryUsersService.queryAll(map);
        qkjvipLotterySetting.setUserlist(users);

        return RestResponse.success().put("lotterysetting", qkjvipLotterySetting);
    }

    /**
     * 新增
     *
     * @param qkjvipLotterySetting qkjvipLotterySetting
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:lotterysetting:save")
    public RestResponse save(@RequestBody QkjvipLotterySettingEntity qkjvipLotterySetting) {
        qkjvipLotterySetting.setAdduser(getUserId());
        qkjvipLotterySetting.setAddtime(new Date());
        qkjvipLotterySettingService.add(qkjvipLotterySetting);

        if (qkjvipLotterySetting.getUserlist().size() > 0) {
            for (QkjvipLotteryUsersEntity user : qkjvipLotterySetting.getUserlist()) {
                user.setMainid(qkjvipLotterySetting.getId());
            }
            qkjvipLotteryUsersService.addBatch(qkjvipLotterySetting.getUserlist());
        }

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipLotterySetting qkjvipLotterySetting
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:lotterysetting:update")
    public RestResponse update(@RequestBody QkjvipLotterySettingEntity qkjvipLotterySetting) {
        qkjvipLotterySettingService.update(qkjvipLotterySetting);
        qkjvipLotteryUsersService.deleteByMainId(qkjvipLotterySetting.getId());
        if (qkjvipLotterySetting.getUserlist().size() > 0) {
            for (QkjvipLotteryUsersEntity user : qkjvipLotterySetting.getUserlist()) {
                user.setMainid(qkjvipLotterySetting.getId());
            }
            qkjvipLotteryUsersService.addBatch(qkjvipLotterySetting.getUserlist());
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
    @RequiresPermissions("qkjvip:lotterysetting:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipLotterySettingService.deleteBatch(ids);

        return RestResponse.success();
    }
}
