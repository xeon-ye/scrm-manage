/*
 * 项目名称:platform-plus
 * 类名称:SysOrgController.java
 * 包名称:com.platform.modules.sys.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-01-21 11:29:22        李鹏军     初版做成
 *
 * Copyright (c) 2018-2019 微同软件
 */
package com.platform.modules.sys.controller;

import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.sys.dao.SysOrgDao;
import com.platform.modules.sys.entity.SysOrgEntity;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysOrgService;
import com.platform.modules.webservices.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织机构Controller
 *
 * @author 李鹏军
 * @date 2019-01-21 11:29:22
 */
@RestController
@RequestMapping("sys/org")
@Component("quartzorg")
public class SysOrgController extends AbstractController {
    @Autowired
    private SysOrgService sysOrgService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<SysOrgEntity> list = sysOrgService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 根据主键查询详情
     *
     * @param orgNo 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{orgNo}")
    @RequiresPermissions("sys:org:info")
    public RestResponse info(@PathVariable("orgNo") String orgNo) {
        SysOrgEntity sysOrg = sysOrgService.getById(orgNo);

        return RestResponse.success().put("org", sysOrg);
    }

    /**
     * 保存
     *
     * @param sysOrg sysOrg
     * @return RestResponse
     */
    @SysLog("保存机构")
    @RequestMapping("/save")
    @RequiresPermissions("sys:org:save")
    public RestResponse save(@RequestBody SysOrgEntity sysOrg) {
        SysUserEntity user = getUser();
        sysOrg.setCreateUserId(user.getUserId());
        sysOrgService.add(sysOrg);
        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param sysOrg sysOrg
     * @return RestResponse
     */
    @SysLog("修改机构")
    @RequestMapping("/update")
    @RequiresPermissions("sys:org:update")
    public RestResponse update(@RequestBody SysOrgEntity sysOrg) {
        sysOrgService.update(sysOrg);
        return RestResponse.success();
    }

    /**
     * 删除
     *
     * @param orgNo 机构编码
     * @return RestResponse
     */
    @SysLog("删除机构")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:org:delete")
    public RestResponse delete(@RequestBody String orgNo) {
        orgNo = orgNo.replaceAll("\"", "");
        List<SysOrgEntity> sysOrgEntities = sysOrgService.queryListByOrgNo(orgNo);
        if (sysOrgEntities.size() > 0) {
            return RestResponse.error("请先删除子机构");
        } else {
            sysOrgService.delete(orgNo);
        }
        return RestResponse.success();
    }

    /**
     * 组织架构定时任务
     *
     */
    @SysLog("组织架构定时任务")
    @RequestMapping("/quartzOrg")
    public void quartzOrg() {
        Map<String, Object> params=new HashMap<String, Object>();
        List<SysOrgEntity> depts = sysOrgService.queryAll(params);  //表中寄存的部门
        HrmService service = new HrmService();
        try {
            HrmServicePortType clien = service.getHrmServiceHttpPort();
            ArrayOfDepartmentBean arrayDept = clien.getHrmDepartmentInfo("221.207.54.67", "");
            List<DepartmentBean> oaDepts = arrayDept.getDepartmentBean();  //OA的组织架构
            for (DepartmentBean oadept:oaDepts) {
                Boolean flag = true; //部门是否在crm存在标识
                for (SysOrgEntity dept:depts) {
                    if (oadept.getCanceled().getValue() != null && oadept.getCanceled().getValue().equals("1")) {  //OA中废弃的部门
                        if (dept.getOrgNo().equals(oadept.getDepartmentid().getValue()) && dept.getStatus() == 1) {
                            dept.setStatus(0);
                            sysOrgService.update(dept);   //OA中废弃的部门在crm系统改为无效
                            flag = false;
                            System.out.println("部门：" + oadept.getFullname().getValue() + "已修改为无效");
                            break;
                        }
                    } else {
                        if (dept.getOrgNo().equals(oadept.getDepartmentid().getValue())) {
                            if (!dept.getOrgName().equals(oadept.getFullname().getValue()) || !dept.getParentNo().equals(oadept.getSupdepartmentid().getValue())) {
                                SysOrgEntity sysOrg = new SysOrgEntity();
//                                SysUserEntity user = getUser();
                                sysOrg.setOrgNo(oadept.getDepartmentid().getValue());
                                sysOrg.setOrgName(oadept.getFullname().getValue());
                                sysOrg.setParentNo(oadept.getSupdepartmentid().getValue());
                                sysOrg.setStatus(1);
                                sysOrg.setSort((oadept.getShoworder().getValue()==null || "".equals(oadept.getShoworder().getValue())) ? 0 : Integer.parseInt(oadept.getShoworder().getValue()));
//                                sysOrg.setCreateUserId(user.getUserId());
                                sysOrg.setCreateTime(new Date());
                                sysOrgService.update(sysOrg);
                                System.out.println("部门：" + oadept.getFullname().getValue() + "已修改");
                            }
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag == true && !(oadept.getCanceled().getValue() != null && oadept.getCanceled().getValue().equals("1"))) {
                    SysOrgEntity sysOrg = new SysOrgEntity();
//                    SysUserEntity user = getUser();
                    sysOrg.setOrgNo(oadept.getDepartmentid().getValue());
                    sysOrg.setOrgName(oadept.getFullname().getValue());
                    sysOrg.setParentNo(oadept.getSupdepartmentid().getValue());
                    sysOrg.setStatus(1);
                    sysOrg.setSort((oadept.getShoworder().getValue()==null || "".equals(oadept.getShoworder().getValue())) ? 0 : Integer.parseInt(oadept.getShoworder().getValue()));
//                    sysOrg.setCreateUserId(user.getUserId());
                    sysOrg.setCreateTime(new Date());
                    sysOrgService.quartzAdd(sysOrg);
                    System.out.println("部门：" + oadept.getFullname().getValue() + "已添加");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
