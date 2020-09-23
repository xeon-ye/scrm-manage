/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberLabelService.java
 * 包名称:com.platform.modules.qkjvip.service
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-08-26 09:00:34        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.modules.qkjvip.entity.MemberTagsEntity;

import java.util.List;

/**
 * Service接口
 *
 * @author liuqianru
 * @date 2020-08-26 09:00:34
 */
public interface MemberTagsService extends IService<MemberTagsEntity> {
    /**
     * saveOrUpdate
     *
     * @param memberId 用户Id
     * @param tagList tagList
     */
    void saveOrUpdate(String memberId, List<String> tagList);

    /**
     * 根据用户ID，获取标签ID列表
     *
     * @param memberId 用户Id
     * @return List
     */
    List<String> queryTagsList(String memberId);

    /**
     * 根据角色ID数组，批量删除
     *
     * @param tagIds tagIds
     * @return int
     */
    int deleteBatch(String[] tagIds);

}
