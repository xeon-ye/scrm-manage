/*
 * 项目名称:platform-plus
 * 类名称:PurOrderDao.java
 * 包名称:com.platform.modules.qkjm.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-24 17:54:49        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.erpm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.erpm.entity.PurOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 记录Dao
 *
 * @author 孙珊珊
 * @date 2019-10-28 17:54:49
 */
@Mapper
public interface PurOrderDao extends BaseMapper<PurOrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<PurOrderEntity> queryAll(@Param("params") Map<String, Object> params);
}
