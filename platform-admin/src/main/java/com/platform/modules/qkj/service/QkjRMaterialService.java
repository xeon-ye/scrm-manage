/*
 * 项目名称:platform-plus
 * 类名称:QkjRMaterialService.java
 * 包名称:com.platform.modules.qkj.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-20 11:15:58        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.qkj.entity.QkjRMaterialEntity;

import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author 孙珊珊
 * @date 2019-09-20 11:15:58
 */
public interface QkjRMaterialService extends IService<QkjRMaterialEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjRMaterialEntity> queryAll(Map<String, Object> params);

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
     * @param qkjRMaterial 
     * @return 新增结果
     */
    boolean add(QkjRMaterialEntity qkjRMaterial);

    /**
     * 根据主键更新
     *
     * @param qkjRMaterial 
     * @return 更新结果
     */
    boolean update(QkjRMaterialEntity qkjRMaterial);

    /**
     * 根据主键删除
     *
     * @param fid fid
     * @return 删除结果
     */
    boolean delete(String fid);

    /**
     * 根据主键批量删除
     *
     * @param fids fids
     * @return 删除结果
     */
    boolean deleteBatch(String[] fids);
    void deleteAll();
}
