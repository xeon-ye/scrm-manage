/*
 * 项目名称:platform-plus
 * 类名称:MemberDao.java
 * 包名称:com.platform.modules.qkjvip.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/8/7 16:48            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.modules.qkjvip.entity.MemberBasicEntity;
import com.platform.modules.qkjvip.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * MemberBasicDao
 *
 * @author liuqianru
 * @date 2020/8/7 16:48
 */
@Mapper
public interface MemberBasicDao extends BaseMapper<MemberBasicEntity> {
    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberBasicEntity> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 查询所有列表
     * @return List
     */
    List<MemberBasicEntity> queryList();
}
