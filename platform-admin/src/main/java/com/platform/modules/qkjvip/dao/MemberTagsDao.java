/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberLabelDao.java
 * 包名称:com.platform.modules.qkjvip.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-08-26 09:00:34        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.modules.qkjvip.entity.MemberTagsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author liuqianru
 * @date 2020-08-26 09:00:34
 */
@Mapper
public interface MemberTagsDao extends BaseMapper<MemberTagsEntity> {

    /**
     * 根据用户ID，获取标签ID列表
     *
     * @param params 用户ID
     * @return List
     */
    List<MemberTagsEntity> queryTagsList(@Param("params") Map<String, Object> params);


    /**
     * 根据角色ID数组，批量删除
     *
     * @param tagIds 角色ids
     * @return int
     */
    int deleteBatch(String[] tagIds);
}
