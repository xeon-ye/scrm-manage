/*
 * 项目名称:platform-plus
 * 类名称:QkjRSonorderDao.java
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
import com.platform.modules.qkj.entity.QkjRSonorderEntity;
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
public interface QkjRSonorderDao extends BaseMapper<QkjRSonorderEntity> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<QkjRSonorderEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<QkjRSonorderEntity> selectQkjRSonorderPage(IPage page, @Param("params") Map<String, Object> params);

    int updateSendnumById(QkjRSonorderEntity order);
    int updatePornumById(QkjRSonorderEntity order);
    int updatePornumByIdnew(QkjRSonorderEntity order);
    int updateRateById(QkjRSonorderEntity order);
    int deletebyOrderAndId(String orderid,List<String> ids);
    int updateSonbyOAndM(QkjRSonorderEntity order);
}
