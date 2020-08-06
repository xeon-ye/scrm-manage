/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmPurorderController.java
 * 包名称:com.platform.modules.view.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-29 14:00:54        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.view.entity.ViewTSmPurorderEntity;
import com.platform.modules.view.service.ViewTSmPurorderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2019-10-29 14:00:54
 */
@RestController
@RequestMapping("view/tsmpurorder")
public class ViewTSmPurorderController extends AbstractController {
    @Autowired
    private ViewTSmPurorderService viewTSmPurorderService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("view:tsmpurorder:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<ViewTSmPurorderEntity> list = viewTSmPurorderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }


}
