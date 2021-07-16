/*
 * 项目名称:platform-plus
 * 类名称:QkjvipQuestionnaireController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-07-16 10:27:08        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipQuestionnaireEntity;
import com.platform.modules.qkjvip.service.QkjvipQuestionnaireService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2021-07-16 10:27:08
 */
@RestController
@RequestMapping("qkjvip/questionnaire")
public class QkjvipQuestionnaireController extends AbstractController {
    @Autowired
    private QkjvipQuestionnaireService qkjvipQuestionnaireService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:questionnaire:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipQuestionnaireEntity> list = qkjvipQuestionnaireService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:questionnaire:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipQuestionnaireService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:questionnaire:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipQuestionnaireEntity qkjvipQuestionnaire = qkjvipQuestionnaireService.getById(id);

        return RestResponse.success().put("questionnaire", qkjvipQuestionnaire);
    }

    /**
     * 新增
     *
     * @param qkjvipQuestionnaire qkjvipQuestionnaire
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:questionnaire:save")
    public RestResponse save(@RequestBody QkjvipQuestionnaireEntity qkjvipQuestionnaire) {

        qkjvipQuestionnaireService.add(qkjvipQuestionnaire);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipQuestionnaire qkjvipQuestionnaire
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:questionnaire:update")
    public RestResponse update(@RequestBody QkjvipQuestionnaireEntity qkjvipQuestionnaire) {

        qkjvipQuestionnaireService.update(qkjvipQuestionnaire);

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
    @RequiresPermissions("qkjvip:questionnaire:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipQuestionnaireService.deleteBatch(ids);

        return RestResponse.success();
    }
}
