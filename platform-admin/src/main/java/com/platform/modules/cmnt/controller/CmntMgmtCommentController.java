/*
 * 项目名称:platform-plus
 * 类名称:CmntMgmtCommentController.java
 * 包名称:com.platform.modules.cmnt.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-05-25 17:16:11        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.cmnt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.cmnt.entity.CmntMgmtCommentEntity;
import com.platform.modules.cmnt.service.CmntMgmtCommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2021-05-25 17:16:11
 */
@RestController
@RequestMapping("cmnt/mgmtcomment")
public class CmntMgmtCommentController extends AbstractController {
    @Autowired
    private CmntMgmtCommentService cmntMgmtCommentService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("cmnt:mgmtcomment:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<CmntMgmtCommentEntity> list = cmntMgmtCommentService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("cmnt:mgmtcomment:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = cmntMgmtCommentService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/viewList")
//    @RequiresPermissions("cmnt:mgmtcomment:list")
    public RestResponse viewList(@RequestParam Map<String, Object> params) {
        Page page = cmntMgmtCommentService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("cmnt:mgmtcomment:info")
    public RestResponse info(@PathVariable("id") String id) {
        CmntMgmtCommentEntity cmntMgmtComment = cmntMgmtCommentService.getById(id);

        return RestResponse.success().put("mgmtcomment", cmntMgmtComment);
    }

    /**
     * 新增
     *
     * @param cmntMgmtComment cmntMgmtComment
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
//    @RequiresPermissions("cmnt:mgmtcomment:save")
    public RestResponse save(@RequestBody CmntMgmtCommentEntity cmntMgmtComment) {

        cmntMgmtCommentService.add(cmntMgmtComment);

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param cmntMgmtComment cmntMgmtComment
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("cmnt:mgmtcomment:update")
    public RestResponse update(@RequestBody CmntMgmtCommentEntity cmntMgmtComment) {

        cmntMgmtCommentService.update(cmntMgmtComment);

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
    @RequiresPermissions("cmnt:mgmtcomment:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        cmntMgmtCommentService.deleteBatch(ids);

        return RestResponse.success();
    }
}
