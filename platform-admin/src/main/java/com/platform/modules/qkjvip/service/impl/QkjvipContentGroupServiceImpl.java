/*
 * 项目名称:platform-plus
 * 类名称:QkjvipContentGroupServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-03-24 15:41:39        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkjvip.dao.QkjvipContentGroupDao;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.qkjvip.service.QkjvipContentGroupService;
import com.platform.modules.qkjvip.service.QkjvipContentGroupcontentService;
import com.platform.modules.qkjvip.service.QkjvipContentPushchannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author liuqianru
 * @date 2021-03-24 15:41:39
 */
@Service("qkjvipContentGroupService")
public class QkjvipContentGroupServiceImpl extends ServiceImpl<QkjvipContentGroupDao, QkjvipContentGroupEntity> implements QkjvipContentGroupService {

    @Autowired
    private QkjvipContentGroupcontentService qkjvipContentGroupcontentService;
    @Autowired
    private QkjvipContentPushchannelService qkjvipContentPushchannelService;

    @Override
    public List<QkjvipContentGroupEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.add_time");
        params.put("asc", false);
        Page<QkjvipContentGroupEntity> page = new Query<QkjvipContentGroupEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjvipContentGroupPage(page, params));
    }

    @Override
    public void add(QkjvipContentGroupEntity qkjvipContentGroup) {
        this.save(qkjvipContentGroup);
        // 批量保存分组内容
        saveGroupContent(qkjvipContentGroup);
        // 批量保存推送渠道
        savePushChannel(qkjvipContentGroup);

    }

    @Override
    public void update(QkjvipContentGroupEntity qkjvipContentGroup) {
        this.updateById(qkjvipContentGroup);
        // 批量保存分组内容
        qkjvipContentGroupcontentService.deleteByGroupId(qkjvipContentGroup.getId());
        saveGroupContent(qkjvipContentGroup);
        // 批量保存推送渠道
        qkjvipContentPushchannelService.deleteByGroupId(qkjvipContentGroup.getId());
        savePushChannel(qkjvipContentGroup);
    }

    @Override
    public boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] ids) {
        return this.removeByIds(Arrays.asList(ids));
    }

    /**
     * 批量插入推送渠道
     *
     * @param qkjvipContentGroup qkjvipContentGroup
     * @return RestResponse
     */
    private void savePushChannel(QkjvipContentGroupEntity qkjvipContentGroup) {
        if (qkjvipContentGroup.getAppids() != null && qkjvipContentGroup.getAppids().size() > 0) {
            List<QkjvipContentPushchannelEntity> pushChannelList = new ArrayList<>();
            for (int i = 0; i < qkjvipContentGroup.getAppids().size(); i++) {
                QkjvipContentPushchannelEntity pushChannel = new QkjvipContentPushchannelEntity();
                for (QkjvipOptionsEntity option : qkjvipContentGroup.getChannelList()) {
                    if (option.getAppid().equals(qkjvipContentGroup.getAppids().get(i))) {
                        pushChannel.setAppid(option.getAppid());
                        pushChannel.setGroupid(qkjvipContentGroup.getId());
                        pushChannel.setAppname(option.getName());
                        pushChannelList.add(pushChannel);
                    }
                }
            }
            qkjvipContentPushchannelService.addBatch(pushChannelList);
        }
    }

    /**
     * 批量插入分组内容
     *
     * @param qkjvipContentGroup qkjvipContentGroup
     * @return RestResponse
     */
    private void saveGroupContent(QkjvipContentGroupEntity qkjvipContentGroup) {
        if (qkjvipContentGroup.getContentList() != null && qkjvipContentGroup.getContentList().size() > 0) {
            List<QkjvipContentGroupcontentEntity> groupContentList = new ArrayList<>();
            int cnt = 0;
            for (QkjvipContentEntity contentEntity : qkjvipContentGroup.getContentList()) {
                QkjvipContentGroupcontentEntity groupContent = new QkjvipContentGroupcontentEntity();
                groupContent.setGroupid(qkjvipContentGroup.getId());
                groupContent.setContentid(contentEntity.getId());
                groupContent.setSortvalue(cnt);
                groupContentList.add(groupContent);
                cnt++;
            }
            qkjvipContentGroupcontentService.addBatch(groupContentList);
        }
    }
}
