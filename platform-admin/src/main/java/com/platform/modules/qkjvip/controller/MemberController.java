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

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.common.validator.group.UpdateGroup;
import com.platform.modules.qkjvip.entity.MemberEntity;
import com.platform.modules.qkjvip.service.MemberService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysDictEntity;
import com.platform.modules.sys.service.SysDictService;
import com.platform.modules.util.ExcelSelectListUtil;
import com.platform.modules.util.ExportExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
    @Autowired
    private SysDictService sysDictService;
//    @Autowired
//    private MemberLabelService memberLabelService;

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
     * @param member 查询参数
     * @return RestResponse
     */
    @PostMapping("/list")
    @RequiresPermissions("qkjvip:member:list")
    public RestResponse list(@RequestBody MemberEntity member) {

        //改为post形式传输后修改以下start
        Map<String, Object> params = new HashMap<>();
        params = JSON.parseObject(JSON.toJSONString(member), Map.class);
        //如需数据权限，在参数中添加DataScope
        params.put("dataScope", getDataScope("m.add_user","m.add_dept", "org_userid"));

        List<String> labelIds = (List<String>) params.get("labelIdList");
        String paramsStr = "";
        if (labelIds != null && labelIds.size() > 0) {
            for (int i = 0; i < labelIds.size(); i++) {
                paramsStr += "m.member_label like '%" + labelIds.get(i) + "%' and ";
            }
            paramsStr += "1=1";
        }
        params.put("paramsStr", paramsStr);
        //改为post形式传输后修改以下end
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
        if (StringUtils.isNotEmpty(member.getMemberLabel())) {
            member.setLabelIdList(Arrays.asList(member.getMemberLabel().split(",")));
        }
        //获取会员标签(主子表形式-目前暂时废弃)
//        List<String> labelList = memberLabelService.queryLabelList(memberId);
//        member.setLabelIdList(labelList);

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
        if (member.getLabelIdList() != null && member.getLabelIdList().size() > 0) {
            member.setMemberLabel(StringUtils.join(member.getLabelIdList().toArray(), ","));
        }
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
        if (member.getLabelIdList() != null && member.getLabelIdList().size() > 0) {
            member.setMemberLabel(StringUtils.join(member.getLabelIdList().toArray(), ","));
        }
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
        Map<String, Object> params = new HashMap<>();
        List<SysDictEntity> dictList = new ArrayList<>();
        String[] dictAttr = null;
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("会员信息表","会员信息"), MemberEntity .class, list);
            //这里是自己加的 带下拉框的代码
            ExcelSelectListUtil.selectList(workbook, 4, 4, new String[]{"男","女","未知"});
            ExcelSelectListUtil.selectList(workbook, 18, 18, new String[]{"是","否"});
            params.clear();
            params.put("code", "MEMBERTYPE"); //会员类型
            dictList = sysDictService.queryByCode(params);
            dictAttr = new String[dictList.size()];
            for (int i = 0; i < dictList.size(); i++) {
                dictAttr[i] = dictList.get(i).getName();
            }
            ExcelSelectListUtil.selectList(workbook, 7, 7, dictAttr);

            params.clear();
            params.put("code", "MEMBERNATURE"); //会员性质
            dictList = sysDictService.queryByCode(params);
            dictAttr = new String[dictList.size()];
            for (int i = 0; i < dictList.size(); i++) {
                dictAttr[i] = dictList.get(i).getName();
            }
            ExcelSelectListUtil.selectList(workbook, 8, 8, dictAttr);

            params.clear();
            params.put("code", "MEMBERLEVEL"); //会员等级
            dictList = sysDictService.queryByCode(params);
            dictAttr = new String[dictList.size()];
            for (int i = 0; i < dictList.size(); i++) {
                dictAttr[i] = dictList.get(i).getName();
            }
            ExcelSelectListUtil.selectList(workbook, 9, 9, dictAttr);

            params.clear();
            params.put("code", "MEMBERSOURCE"); //会员来源
            dictList = sysDictService.queryByCode(params);
            dictAttr = new String[dictList.size()];
            for (int i = 0; i < dictList.size(); i++) {
                dictAttr[i] = dictList.get(i).getName();
            }
            ExcelSelectListUtil.selectList(workbook, 10, 10, dictAttr);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode( "会员信息表." + ExportExcelUtils.ExcelTypeEnum.XLS.getValue(), "UTF-8"));
            workbook.write(response.getOutputStream());
//            ExportExcelUtils.exportExcel(list,"会员信息表","会员信息",MemberEntity.class,"会员信息",response);
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
