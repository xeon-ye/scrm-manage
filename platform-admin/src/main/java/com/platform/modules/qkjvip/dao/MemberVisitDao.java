/*
 * 项目名称:platform-plus
 * 类名称:MemberVisitDao.java
 * 包名称:com.platform.modules.qkjvip.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/13 10:15            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.modules.qkjvip.entity.MemberVisitEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * MemberVisitDao
 *
 * @author liuqianru
 * @date 2020/3/13 10:15
 */
@Mapper
public interface MemberVisitDao extends BaseMapper<MemberVisitEntity> {
    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberVisitEntity> queryAll(@Param("params") Map<String, Object> params);
    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<MemberVisitEntity> selectMemberVisitList(IPage page, @Param("params") Map<String, Object> params);
}
