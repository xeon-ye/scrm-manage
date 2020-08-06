/*
 * 项目名称:platform-plus
 * 类名称:MemberService.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/9 14:25            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.qkjvip.entity.MemberEntity;

import java.util.List;
import java.util.Map;

/**
 * @author liuqianru
 * @date 2020/3/9 14:25
 */
public interface MemberService extends IService<MemberEntity> {

    /**
     * 查询所有
     *
     * @param params 查询参数
     * @return List
     */
    List<MemberEntity> queryAll(Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return Page
     */
    Page queryPage(Map<String, Object> params);

    /**
     * 保存会员
     *
     * @param member 会员
     * @param params 查询参数
     */
    void add(MemberEntity member, Map<String, Object> params);
    void add(MemberEntity member);
    /**
     * 修改用户
     *
     * @param member 用户
     * @param params 查询参数
     */
    void update(MemberEntity member, Map<String, Object> params);

    /**
     * 删除会员
     *
     * @param userIds 会员Ids
     */
    void deleteBatch(String[] userIds);
}
