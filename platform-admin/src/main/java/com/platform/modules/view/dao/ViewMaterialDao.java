/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmPurorderDao.java
 * 包名称:com.platform.modules.view.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-29 14:00:54        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.view.entity.ViewMaterialEntity;
import com.platform.modules.view.entity.ViewTSmPurorderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author 孙珊珊
 * @date 2019-10-29 14:00:54
 */
@Mapper
public interface ViewMaterialDao extends BaseMapper<ViewMaterialEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ViewMaterialEntity> queryAll(@Param("params") Map<String, Object> params);

}
