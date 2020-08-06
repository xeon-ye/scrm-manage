/*
 * 项目名称:platform-plus
 * 类名称:QkjRMaterialDao.java
 * 包名称:com.platform.modules.qkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-20 11:15:58        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.qkj.entity.QkjRMaterialEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author 孙珊珊
 * @date 2019-09-20 11:15:58
 */
@Mapper
public interface QkjRMaterialDao extends BaseMapper<QkjRMaterialEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjRMaterialEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<QkjRMaterialEntity> selectQkjRMaterialPage(IPage page, @Param("params") Map<String, Object> params);

    int truncateTable();
}
