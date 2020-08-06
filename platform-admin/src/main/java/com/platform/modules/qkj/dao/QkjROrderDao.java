/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderDao.java
 * 包名称:com.platform.modules.qkj.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-28 14:44:59        孙珊珊sc     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.qkj.entity.QkjROrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
@Mapper
public interface QkjROrderDao extends BaseMapper<QkjROrderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjROrderEntity> queryAll(@Param("params") Map<String, Object> params);

    List<QkjROrderEntity> pageCount(@Param("params") Map<String, Object> params);
    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<QkjROrderEntity> selectQkjROrderPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 根据key，更新vsate
     *
     * @param paramKey   key
     * @param paramValue value
     * @return int
     */
    int updateStateById(QkjROrderEntity order);
    int updateStateById2(QkjROrderEntity order);
    int updateStateBySend(QkjROrderEntity order);
    //Arrays.asList(userIds)

    int sureBatchById(String[] ids);

}
