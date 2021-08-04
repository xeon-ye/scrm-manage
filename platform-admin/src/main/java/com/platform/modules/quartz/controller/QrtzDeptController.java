/*
 * 项目名称:platform-plus
 * 类名称:DeptController.java
 * 包名称:com.platform.modules.quartz.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/25 15:39            liuqianru    初版做成
 *
 */
package com.platform.modules.quartz.controller;

import com.platform.common.annotation.SysLog;
import com.platform.modules.cache.CacheFactory;
import com.platform.modules.cache.SysDBCacheLogic;
import com.platform.modules.quartz.entity.QrtzDeptEntity;
import com.platform.modules.quartz.service.QrtzDeptService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysOrgEntity;
import com.platform.modules.sys.service.SysOrgService;
import com.platform.modules.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DeptController
 *
 * @author liuqianru
 * @date 2020/3/25 15:39
 */
@RestController
@Component("qrtzorg")
@Slf4j
public class QrtzDeptController extends AbstractController {
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private QrtzDeptService qrtzDeptService;

    /**
     * 组织架构定时任务
     *
     */
    @SysLog("组织架构定时任务")
    @RequestMapping("/quartzOrg")
    public void quartzOrg() {
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("status", 1);
        List<SysOrgEntity> depts = sysOrgService.queryAll(params);  //表中寄存的部门
        try {
            List<QrtzDeptEntity> oaDepts = qrtzDeptService.queryAll(null);  //OA的组织架构
            for (QrtzDeptEntity oadept:oaDepts) {
                Boolean flag = true; //部门是否在crm存在标识
                for (SysOrgEntity dept:depts) {
                    if (oadept.getCanceled() != null && oadept.getCanceled().equals("1")) {  //OA中废弃的部门
                        if (dept.getOrgNo().equals(oadept.getId().toString())) {
                            dept.setStatus(0);
                            sysOrgService.update(dept);   //OA中废弃的部门在crm系统改为无效
                            flag = false;
                            System.out.println("部门：" + oadept.getDepartmentname() + "已修改为无效");
                            break;
                        }
                    } else {
                        if (dept.getOrgNo().equals(oadept.getId().toString())) {
                            if (!dept.getOrgName().equals(oadept.getDepartmentname()) || !dept.getParentNo().equals(oadept.getSupdepid().toString())) {
                                SysOrgEntity sysOrg = new SysOrgEntity();
                                sysOrg.setOrgNo(oadept.getId().toString());
                                sysOrg.setOrgName(oadept.getDepartmentname());
                                sysOrg.setParentNo(oadept.getSupdepid().toString());
                                sysOrg.setStatus(1);
                                sysOrg.setSort((oadept.getShoworder() == null || "".equals(oadept.getShoworder().toString())) ? 0 : oadept.getShoworder());
                                sysOrg.setCreateTime(new Date());
                                sysOrgService.update(sysOrg);
                                System.out.println("部门：" + oadept.getDepartmentname() + "已修改");
                            }
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag == true && !(oadept.getCanceled() != null && oadept.getCanceled().equals("1"))) {
                    SysOrgEntity sysOrg = new SysOrgEntity();
                    sysOrg.setOrgNo(oadept.getId().toString());
                    sysOrg.setOrgName(oadept.getDepartmentname());
                    sysOrg.setParentNo(oadept.getSupdepid().toString());
                    sysOrg.setStatus(1);
                    sysOrg.setSort((oadept.getShoworder() == null || "".equals(oadept.getShoworder().toString())) ? 0 : oadept.getShoworder());
                    sysOrg.setCreateTime(new Date());
                    sysOrgService.quartzAdd(sysOrg);
                    System.out.println("部门：" + oadept.getDepartmentname() + "已添加");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
