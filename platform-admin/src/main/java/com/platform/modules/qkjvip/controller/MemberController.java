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
import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.RestResponse;
import com.platform.common.validator.ValidatorUtils;
import com.platform.modules.pageCont.pageCount;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.qkjvip.service.MemberService;
import com.platform.modules.qkjvip.service.MemberTagsService;
import com.platform.modules.qkjvip.service.QkjvipMemberImportService;
import com.platform.modules.qkjvip.service.QkjvipTaglibsService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysDictEntity;
import com.platform.modules.sys.service.SysDictService;
import com.platform.modules.sys.service.SysRoleOrgService;
import com.platform.modules.util.ExcelSelectListUtil;
import com.platform.modules.util.ExportExcelUtils;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.Vars;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    @Autowired
    private MemberTagsService memberTagsService;
    @Autowired
    private QkjvipTaglibsService qkjvipTaglibsService;
    @Autowired
    private QkjvipMemberImportService qkjvipMemberImportService;
    @Autowired
    private SysRoleOrgService sysRoleOrgService;

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
     * @param memberQuery 查询参数
     * @return RestResponse
     */
    @PostMapping("/list")
    @RequiresPermissions("qkjvip:member:list")
    public RestResponse list(@RequestBody MemberQueryEntity memberQuery) throws IOException {

//        Page page = memberService.queryPage(params);
//        return RestResponse.success().put("page", page);
        if (getUser().getUserName().contains("admin")) {
            memberQuery.setCurrentmemberid("");
            memberQuery.setListorgno("");
        } else {
            memberQuery.setCurrentmemberid(getUserId());
            memberQuery.setListorgno(sysRoleOrgService.queryOrgNoListByUserId(getUserId()));
        }
        Object obj = JSONArray.toJSON(memberQuery);
        String queryJsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");

        String resultPost = HttpClient.sendPost(Vars.MEMBER_GETLIST_URL, queryJsonStr);
        //插入会员标签
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //调用成功
            List<MemberEntity> memberList = new ArrayList<>();
            memberList = JSON.parseArray(resultObject.getString("listmember"),MemberEntity.class);
            Page page = new Page();
            page.setRecords(memberList);
            page.setTotal(Long.parseLong(resultObject.get("totalcount").toString()));
            page.setSize(memberQuery.getPagesize());
            page.setCurrent(memberQuery.getPageindex());
            return RestResponse.success().put("page", page);
        } else {
            return RestResponse.error(resultObject.get("descr").toString());
        }
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

        //获取会员标签
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        List<MemberTagsEntity> memberTagsEntities = memberTagsService.queryTagsList(params);
        List<MemberTagsQueryEntity> membertags = new ArrayList<>();
        if (memberTagsEntities.size() > 0) {   //会员打了标签的情况下
            for (int i = 0; i < memberTagsEntities.size(); i++) {
                MemberTagsQueryEntity memberTagsQueryEntity = new MemberTagsQueryEntity();
                memberTagsQueryEntity.setTagGroupId(memberTagsEntities.get(i).getTagGroupId());
                memberTagsQueryEntity.setTagGroupName(memberTagsEntities.get(i).getTagGroupName());
                memberTagsQueryEntity.setTagType(memberTagsEntities.get(i).getTagType());
                params.put("tagGroupId", memberTagsEntities.get(i).getTagGroupId());
                List<QkjvipTaglibsEntity> tagList = qkjvipTaglibsService.queryAll(params);
                memberTagsQueryEntity.setTagList(tagList);
                if (memberTagsEntities.get(i).getTagType() != null && memberTagsEntities.get(i).getTagType() == 2) {
                    memberTagsQueryEntity.setTagIdList(Arrays.asList(memberTagsEntities.get(i).getItems().split(",")));
                    memberTagsQueryEntity.setTagValue("");
                } else {
                    List<String> tagIdList = new ArrayList<>();
                    memberTagsQueryEntity.setTagIdList(tagIdList);
                    memberTagsQueryEntity.setTagValue(memberTagsEntities.get(i).getTagValue());
                }
                membertags.add(memberTagsQueryEntity);
            }
        }
        member.setMembertags(membertags);

        return RestResponse.success().put("member", member);
    }

    /**
     * 保存会员信息
     *
     * @param memberImport memberImport
     * @return RestResponse
     */
    @SysLog("保存会员信息")
    @PostMapping("/save")
    @RequiresPermissions("qkjvip:member:save")
    public RestResponse save(@RequestBody QkjvipMemberImportEntity memberImport) {
        memberImport.setAddUser(getUserId());
        memberImport.setAddDept(getOrgNo());
        memberImport.setAddTime(new Date());
        memberImport.setOfflineflag(1);
        qkjvipMemberImportService.add(memberImport);  //将数据保存到中间表
        //调用数据清洗接口
        MemberEntity member = new MemberEntity();
        try {
            Object obj = JSONArray.toJSON(memberImport);
            String memberJsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
            String resultPost = HttpClient.sendPost(Vars.MEMBER_ADD_URL, memberJsonStr);
            //插入会员标签
            JSONObject resultObject = JSON.parseObject(resultPost);
            if ("200".equals(resultObject.get("resultcode").toString())) {  //清洗成功
                member.setMemberId(resultObject.get("memberid").toString());
                member.setMembertags(memberImport.getMembertags());
                memberTagsService.saveOrUpdate(member);
            } else {
                return RestResponse.error(resultObject.get("descr").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        ValidatorUtils.validateEntity(member);

        Map<String, Object> params = new HashMap<>(2);
        params.put("dataScope", getDataScope());
        memberService.update(member, params);
        //修改会员标签
        memberTagsService.saveOrUpdate(member);

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
        //同时删除会员对应的标签
        memberTagsService.deleteBatch(memberIds);
        return RestResponse.success();
    }

    /**
     * 导出会员数据
     */
    @SysLog("导出会员")
    @RequestMapping("/export")
    @RequiresPermissions("qkjvip:member:export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> params) {
//        List<MemberEntity> list = memberService.queryAll(params);
        try {
            List<MemberEntity> list = JSON.parseArray(URLDecoder.decode(params.get("dataStr").toString()), MemberEntity.class);
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
                List<QkjvipMemberImportEntity> list = ExportExcelUtils.importExcel(file, 1, 1,QkjvipMemberImportEntity.class);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setAddUser(getUserId());
                    list.get(i).setAddDept(getOrgNo());
                    list.get(i).setAddTime(new Date());
                    list.get(i).setOfflineflag(1);
                }
                if (list.size() > 0) {
                    qkjvipMemberImportService.addBatch(list); //批量导入临时表

                    //调用数据清洗接口
                    Object objList = JSONArray.toJSON(list);
                    String memberJsonStr = JsonHelper.toJsonString(objList, "yyyy-MM-dd HH:mm:ss");
                    String resultPost = HttpClient.sendPost(Vars.MEMBER_IMPORT_URL, memberJsonStr);

                    JSONObject resultObject = JSON.parseObject(resultPost);
                    if (!"200".equals(resultObject.get("resultcode").toString())) {  //清洗失败
                        return RestResponse.error(resultObject.get("descr").toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return RestResponse.success().put("msg", "导入成功！");
    }
}
