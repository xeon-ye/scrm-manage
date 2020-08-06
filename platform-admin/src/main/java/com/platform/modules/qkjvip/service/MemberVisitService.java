/*
 * 项目名称:platform-plus
 * 类名称:MemberVisitService.java
 * 包名称:com.platform.modules.qkjvip.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/13 10:13            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.qkjvip.entity.MemberVisitEntity;

import java.util.List;
import java.util.Map;

/**
 * MemberVisitService
 *
 * @author liuqianru
 * @date 2020/3/13 10:13
 */
public interface MemberVisitService extends IService<MemberVisitEntity> {
    /**
     * 查询所有
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberVisitEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 保存拜访记录
     *
     * @param member 会员
     * @param params 查询参数
     */
    void add(MemberVisitEntity member, Map<String, Object> params);
    void add(MemberVisitEntity member);
    /**
     * 修改拜访记录
     *
     * @param member 用户
     * @param params 查询参数
     */
    void update(MemberVisitEntity member, Map<String, Object> params);

    /**
     * 删除会员
     *
     * @param uuids 会员Ids
     */
    void deleteBatch(String[] uuids);
}
