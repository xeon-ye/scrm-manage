/*
 * 项目名称:platform-plus
 * 类名称:QkjOaLeaveService.java
 * 包名称:com.platform.modules.qkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-04-22 17:25:47        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.qkj.entity.QkjOaLeaveEntity;

import java.util.List;
import java.util.Map;

/**
 * 注册Service接口
 *
 * @author liuqianru
 * @date 2020-04-22 17:25:47
 */
public interface QkjOaLeaveService extends IService<QkjOaLeaveEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjOaLeaveEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询注册
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 新增注册
     *
     * @param qkjOaLeave 注册
     * @return 新增结果
     */
    boolean add(QkjOaLeaveEntity qkjOaLeave);

    /**
     * 根据主键更新注册
     *
     * @param qkjOaLeave 注册
     * @return 更新结果
     */
    boolean update(QkjOaLeaveEntity qkjOaLeave);

    /**
     * 根据主键删除注册
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
}
