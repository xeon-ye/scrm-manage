/*
 * 项目名称:platform-plus
 * 类名称:MemberVisitController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/13 10:12            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.common.validator.group.UpdateGroup;
import com.platform.modules.qkjvip.entity.MemberEntity;
import com.platform.modules.qkjvip.entity.MemberVisitEntity;
import com.platform.modules.qkjvip.service.MemberVisitService;
import com.platform.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MemberVisitController
 *
 * @author liuqianru
 * @date 2020/3/13 10:12
 */
@RestController
@RequestMapping("/qkjvip/mvisit")
public class MemberVisitController extends AbstractController {
    @Autowired
    private MemberVisitService memberVisitService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:mvisit:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MemberVisitEntity> list = memberVisitService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 所有拜访列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:mvisit:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {

        //如需数据权限，在参数中添加DataScope
//        params.put("dataScope", getDataScope());

        Page page = memberVisitService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param uuid 主键
     * @return RestResponse
     */
    @GetMapping("/info/{uuid}")
    @RequiresPermissions("qkjvip:mvisit:info")
    public RestResponse info(@PathVariable("uuid") String uuid) {
        MemberVisitEntity mvisit = memberVisitService.getById(uuid);
        return RestResponse.success().put("mvisit", mvisit);
    }

    /**
     * 保存拜访信息
     *
     * @param mvisit mvisit
     * @return RestResponse
     */
    @SysLog("保存拜访记录信息")
    @PostMapping("/save")
    @RequiresPermissions("qkjvip:mvisit:save")
    public RestResponse save(@RequestBody MemberVisitEntity mvisit) {

        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());

        mvisit.setAddUser(getUserId());
        mvisit.setAddDept(getOrgNo());
        mvisit.setAddTime(new Date());
        memberVisitService.add(mvisit, params);

        return RestResponse.success();
    }

    /**
     * 修改拜访信息
     *
     * @param mvisit mvisit
     * @return RestResponse
     */
    @SysLog("修改拜访记录")
    @PostMapping("/update")
    @RequiresPermissions("qkjvip:mvisit:update")
    public RestResponse update(@RequestBody MemberVisitEntity mvisit) {
        ValidatorUtils.validateEntity(mvisit, UpdateGroup.class);

        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());

        memberVisitService.update(mvisit, params);

        return RestResponse.success();
    }

    /**
     * 删除拜访信息
     *
     * @param uuids uuids
     * @return RestResponse
     */
    @SysLog("删除拜访信息")
    @PostMapping("/delete")
    @RequiresPermissions("qkjvip:mvisit:delete")
    public RestResponse delete(@RequestBody String[] uuids) {
        memberVisitService.deleteBatch(uuids);

        return RestResponse.success();
    }


}
