/*
 * 项目名称:platform-plus
 * 类名称:SysUserController.java
 * 包名称:com.platform.modules.sys.controller
 *
 * 修改履历:
 *      日期                修正者      主要内容
 *      2018/11/21 16:04    李鹏军      初版完成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import cn.afterturn.easypoi.util.PoiPublicUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.common.validator.group.UpdateGroup;
import com.platform.modules.qkjvip.entity.MemberEntity;
import com.platform.modules.qkjvip.service.MemberService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.util.ExportExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2020-03-09 14:44:59
 */
@RestController
@RequestMapping("/qkjvip/member")
public class MemberController extends AbstractController {
    @Autowired
    private MemberService memberService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:member:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<MemberEntity> list = memberService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 所有会员列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:member:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {

        //如需数据权限，在参数中添加DataScope
        params.put("dataScope", getDataScope("m.add_user","m.add_dept", "org_userid"));

        Page page = memberService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param memberId 主键
     * @return RestResponse
     */
    @GetMapping("/info/{memberId}")
    @RequiresPermissions("qkjvip:member:info")
    public RestResponse info(@PathVariable("memberId") String memberId) {
        MemberEntity member = memberService.getById(memberId);
        return RestResponse.success().put("member", member);
    }

    /**
     * 保存会员信息
     *
     * @param member member
     * @return RestResponse
     */
    @SysLog("保存会员信息")
    @PostMapping("/save")
    @RequiresPermissions("qkjvip:member:save")
    public RestResponse save(@RequestBody MemberEntity member) {

        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());

        member.setAddUser(getUserId());
        member.setAddDept(getOrgNo());
        member.setAddTime(new Date());
        memberService.add(member, params);

        return RestResponse.success().put("member", member);
    }

    /**
     * 修改用户
     *
     * @param member user
     * @return RestResponse
     */
    @SysLog("修改用户")
    @PostMapping("/update")
    @RequiresPermissions("qkjvip:member:update")
    public RestResponse update(@RequestBody MemberEntity member) {
        ValidatorUtils.validateEntity(member, UpdateGroup.class);

        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());

        memberService.update(member, params);

        return RestResponse.success().put("member", member);
    }

    /**
     * 删除用户
     *
     * @param memberIds memberIds
     * @return RestResponse
     */
    @SysLog("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("qkjvip:member:delete")
    public RestResponse delete(@RequestBody String[] memberIds) {
        memberService.deleteBatch(memberIds);

        return RestResponse.success();
    }

    /**
     * 导出会员数据
     */
    @SysLog("导出会员")
    @RequestMapping("/export")
    @RequiresPermissions("qkjvip:member:export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
        List<MemberEntity> list = memberService.queryAll(params);
        try {
            ExportExcelUtils.exportExcel(list,"会员信息表","会员信息",MemberEntity.class,"会员信息",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出会员数据模板
     */
    @SysLog("导出会员模板")
    @RequestMapping("/exportTpl")
    @RequiresPermissions("qkjvip:member:exportTpl")
    public void exportTplExcel(HttpServletRequest request, HttpServletResponse response) {
        List<MemberEntity> list = new ArrayList<>();
        try {
            ExportExcelUtils.exportExcel(list,"会员信息表","会员信息",MemberEntity.class,"会员信息",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入会员数据
     */
    @SysLog("导入会员")
    @RequestMapping("/import")
    @RequiresPermissions("qkjvip:member:import")
    public RestResponse importExcel(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException("请选择要导入的文件");
        } else {
            try {
                List<MemberEntity> list = ExportExcelUtils.importExcel(file, 1, 1,MemberEntity.class);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setAddUser(getUserId());
                    list.get(i).setAddDept(getOrgNo());
                    list.get(i).setAddTime(new Date());
                }
                memberService.addBatch(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return RestResponse.success().put("msg", "导入成功！");
    }
}
