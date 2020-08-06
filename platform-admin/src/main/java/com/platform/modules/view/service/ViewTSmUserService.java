/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmUserService.java
 * 包名称:com.platform.modules.view.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-11-01 11:06:39        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.view.entity.ViewTSmUserEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author 孙珊珊
 * @date 2019-11-01 11:06:39
 */
public interface ViewTSmUserService extends IService<ViewTSmUserEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ViewTSmUserEntity> queryAll(Map<String, Object> params);

}
