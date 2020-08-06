/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderService.java
 * 包名称:com.platform.modules.qkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-28 14:44:59        孙珊珊sc     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.qkj.entity.QkjROrderEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
public interface QkjROrderService extends IService<QkjROrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjROrderEntity> queryAll(Map<String, Object> params);

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
     * @param qkjROrder 
     * @return 新增结果
     */
    boolean add(QkjROrderEntity qkjROrder);

    /**
     * 根据主键更新
     *
     * @param qkjROrder 
     * @return 更新结果
     */
    boolean update(QkjROrderEntity qkjROrder);

    /**
     * 根据主键删除
     *
     * @param id id
     * @return 删除结果
     */
    boolean delete(String id);

    /**
     * 根据主键批量删除
     *
     * @param ids ids
     * @return 删除结果
     */
    boolean deleteBatch(String[] ids);

    /**
     * 修改状态
     *
     * @param user 用户
     * @param params 查询参数
     */
    void updateState(QkjROrderEntity order);
    void updateState2(QkjROrderEntity order);
    void updateStateBySend(QkjROrderEntity order);
    void sureBatchById(String[] ids);
}
