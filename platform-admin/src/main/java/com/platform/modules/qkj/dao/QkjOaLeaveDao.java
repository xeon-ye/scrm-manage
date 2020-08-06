/*
 * 项目名称:platform-plus
 * 类名称:QkjOaLeaveDao.java
 * 包名称:com.platform.modules.qkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-04-22 17:25:47        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.qkj.entity.QkjOaLeaveEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 注册Dao
 *
 * @author liuqianru
 * @date 2020-04-22 17:25:47
 */
@Mapper
public interface QkjOaLeaveDao extends BaseMapper<QkjOaLeaveEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjOaLeaveEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<QkjOaLeaveEntity> selectQkjOaLeavePage(IPage page, @Param("params") Map<String, Object> params);
}
