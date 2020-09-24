/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberActivityController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-03 09:49:29        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.qkjvip.entity.MemberEntity;
import com.platform.modules.qkjvip.entity.QkjvipMemberActivitymbsEntity;
import com.platform.modules.qkjvip.entity.QkjvipMemberImportEntity;
import com.platform.modules.qkjvip.service.MemberService;
import com.platform.modules.qkjvip.service.QkjvipMemberActivitymbsService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipMemberActivityEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberActivityService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.platform.modules.util.ExportExcelUtils;

import java.io.IOException;
import java.util.*;

import static cn.afterturn.easypoi.excel.entity.enmus.CellValueType.Date;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-09-03 09:49:29
 */
@RestController
@RequestMapping("qkjvip/memberactivity")
public class QkjvipMemberActivityController extends AbstractController {
    @Autowired
    private QkjvipMemberActivityService qkjvipMemberActivityService;
    @Autowired
    private QkjvipMemberActivitymbsService qkjvipMemberActivitymbsService;
    @Autowired
    private MemberService memberService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:memberactivity:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberActivityEntity> list = qkjvipMemberActivityService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:memberactivity:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberActivityService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:memberactivity:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberActivityEntity qkjvipMemberActivity = qkjvipMemberActivityService.getById(id);
        Map<String, Object> map=new HashMap<String,Object>();
        map.put("activityId",id);
        qkjvipMemberActivity.setMbs(qkjvipMemberActivitymbsService.queryAll(map));
        return RestResponse.success().put("memberactivity", qkjvipMemberActivity);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberActivity qkjvipMemberActivity
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:memberactivity:save")
    public RestResponse save(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {
        List<QkjvipMemberActivitymbsEntity> mbs=new ArrayList<>();
        mbs=qkjvipMemberActivity.getMbs();
        qkjvipMemberActivityService.add(qkjvipMemberActivity);
        if(mbs!=null&&mbs.size()>0){
            List<QkjvipMemberActivitymbsEntity> newmemList=new ArrayList<>();
            for(QkjvipMemberActivitymbsEntity m:mbs){
                m.setActivityId(qkjvipMemberActivity.getId());
                newmemList.add(m);
            }
            qkjvipMemberActivitymbsService.batchAdd(mbs);
        }

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberActivity qkjvipMemberActivity
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:memberactivity:update")
    public RestResponse update(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {
        List<QkjvipMemberActivitymbsEntity> mbs=new ArrayList<>();
        mbs=qkjvipMemberActivity.getMbs();
        qkjvipMemberActivityService.update(qkjvipMemberActivity);

        if(mbs!=null&&mbs.size()>0){
            //删除
            qkjvipMemberActivitymbsService.deleteBatchByOrder(qkjvipMemberActivity.getId());
            List<QkjvipMemberActivitymbsEntity> newmemList=new ArrayList<>();
            for(QkjvipMemberActivitymbsEntity m:mbs){
                m.setActivityId(qkjvipMemberActivity.getId());
                newmemList.add(m);
            }
            qkjvipMemberActivitymbsService.batchAdd(mbs);
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
    @RequiresPermissions("qkjvip:memberactivity:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberActivityService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 导入会员数据
     */
    @SysLog("导入会员")
    @RequestMapping("/import")
    public RestResponse importExcel(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        List<MemberEntity> mees=new ArrayList<>();
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException("请选择要导入的文件");
        } else {
            //插入impont 表
            try {
                List<QkjvipMemberImportEntity> list = ExportExcelUtils.importExcel(file, 1, 1,QkjvipMemberImportEntity.class);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setAddUser(getUserId());
                    list.get(i).setAddDept(getOrgNo());
                    list.get(i).setAddTime(new Date());
                    list.get(i).setOfflineflag(1);
                }
                //memberService.addBatch(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //调用清洗数据接口

            Map<String, Object> map=new HashMap<String,Object>();
            map.put("realName","杨贵录");
            mees=memberService.queryAll(map);
        }
        return RestResponse.success().put("memberlist", mees);
    }
}
