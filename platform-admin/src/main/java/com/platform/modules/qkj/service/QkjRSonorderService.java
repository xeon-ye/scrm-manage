/*
 * 项目名称:platform-plus
 * 类名称:QkjRSonorderService.java
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
import com.platform.modules.qkj.entity.QkjRSonorderEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service接口
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
public interface QkjRSonorderService extends IService<QkjRSonorderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjRSonorderEntity> queryAll(Map<String, Object> params);

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
     * @param qkjRSonorder 
     * @return 新增结果
     */
    boolean add(QkjRSonorderEntity qkjRSonorder);

    /**
     * 根据主键更新
     *
     * @param qkjRSonorder 
     * @return 更新结果
     */
    boolean update(QkjRSonorderEntity qkjRSonorder);

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

    void updateSendnumById(QkjRSonorderEntity qkjRSonorder);
    void updatePornumById(QkjRSonorderEntity qkjRSonorder);
    void updatePornumByIdnew(QkjRSonorderEntity qkjRSonorder);

    void updateRateById(QkjRSonorderEntity qkjRSonorder);
    void deletebyOrderAndId(String orderid, List<String> ids);
    void updateSonbyOAndM(QkjRSonorderEntity qkjRSonorder);
}
