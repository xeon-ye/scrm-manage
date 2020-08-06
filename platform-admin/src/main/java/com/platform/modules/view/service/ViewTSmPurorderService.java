/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmPurorderService.java
 * 包名称:com.platform.modules.view.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-29 14:00:54        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.view.entity.ViewTSmPurorderEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author 孙珊珊
 * @date 2019-10-29 14:00:54
 */
public interface ViewTSmPurorderService extends IService<ViewTSmPurorderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ViewTSmPurorderEntity> queryAll(Map<String, Object> params);
    List<ViewTSmPurorderEntity> queryAllMain(Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增
     *
     * @param viewTSmPurorder 
     * @return 新增结果
     */
    boolean add(ViewTSmPurorderEntity viewTSmPurorder);

    /**
     * 根据主键更新
     *
     * @param viewTSmPurorder 
     * @return 更新结果
     */
    boolean update(ViewTSmPurorderEntity viewTSmPurorder);

    /**
     * 根据主键删除
     *
     * @param caigouorg caigouorg
     * @return 删除结果
     */
    boolean delete(String caigouorg);

    /**
     * 根据主键批量删除
     *
     * @param caigouorgs caigouorgs
     * @return 删除结果
     */
    boolean deleteBatch(String[] caigouorgs);
}
