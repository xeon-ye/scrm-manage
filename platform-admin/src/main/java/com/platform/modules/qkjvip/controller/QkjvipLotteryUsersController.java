/*
 * 项目名称:platform-plus
 * 类名称:QkjvipLotteryUsersController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-07-05 10:59:58        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.oss.entity.UploadData;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.service.QkjvipLotteryUsersService;
import com.platform.modules.sys.entity.SysDictEntity;
import com.platform.modules.sys.entity.SysUserChannelEntity;
import com.platform.modules.util.*;
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
 * @date 2021-07-05 10:59:58
 */
@RestController
@RequestMapping("qkjvip/lotteryusers")
public class QkjvipLotteryUsersController extends AbstractController {
    @Autowired
    private QkjvipLotteryUsersService qkjvipLotteryUsersService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:lotteryusers:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipLotteryUsersEntity> list = qkjvipLotteryUsersService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:lotteryusers:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipLotteryUsersService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:lotteryusers:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipLotteryUsersEntity qkjvipLotteryUsers = qkjvipLotteryUsersService.getById(id);

        return RestResponse.success().put("lotteryusers", qkjvipLotteryUsers);
    }

    /**
     * 新增
     *
     * @param qkjvipLotteryUsers qkjvipLotteryUsers
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:lotteryusers:save")
    public RestResponse save(@RequestBody QkjvipLotteryUsersEntity qkjvipLotteryUsers) {

        qkjvipLotteryUsersService.add(qkjvipLotteryUsers);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipLotteryUsers qkjvipLotteryUsers
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:lotteryusers:update")
    public RestResponse update(@RequestBody QkjvipLotteryUsersEntity qkjvipLotteryUsers) {

        qkjvipLotteryUsersService.update(qkjvipLotteryUsers);

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
    @RequiresPermissions("qkjvip:lotteryusers:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipLotteryUsersService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 导出模板
     */
    @SysLog("导出模板")
    @RequestMapping("/exportTpl")
    public void exportTplExcel(HttpServletRequest request, HttpServletResponse response) {
        List<QkjvipLotteryUsersEntity> list = new ArrayList<>();
        try {
            ExportExcelUtils.exportExcel(list,"人员信息表","人员信息", QkjvipLotteryUsersEntity.class,"人员信息",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入人员
     */
    @SysLog("导入人员")
    @RequestMapping("/import")
    public RestResponse importExcel(MultipartFile file) {
        List<QkjvipLotteryUsersEntity> list = new ArrayList<>();
        try {
            list = ExportExcelUtils.importExcel(file, 1, 1,QkjvipLotteryUsersEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResponse.error("导入接口异常！");
        }
        return RestResponse.success().put("msg", "导入成功！").put("userList", list);
    }
}
