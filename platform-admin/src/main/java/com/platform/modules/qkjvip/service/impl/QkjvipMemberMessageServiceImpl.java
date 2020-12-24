/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberMessageServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-12-22 11:05:08        李鹏军     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkjvip.dao.QkjvipMemberMessageDao;
import com.platform.modules.qkjvip.entity.QkjvipMemberMessageEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 李鹏军
 * @date 2020-12-22 11:05:08
 */
@Service("qkjvipMemberMessageService")
public class QkjvipMemberMessageServiceImpl extends ServiceImpl<QkjvipMemberMessageDao, QkjvipMemberMessageEntity> implements QkjvipMemberMessageService {

    @Override
    public List<QkjvipMemberMessageEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjvipMemberMessageEntity> page = new Query<QkjvipMemberMessageEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjvipMemberMessagePage(page, params));
    }

    @Override
    public boolean add(QkjvipMemberMessageEntity qkjvipMemberMessage) {
        return this.save(qkjvipMemberMessage);
    }

    @Override
    public boolean update(QkjvipMemberMessageEntity qkjvipMemberMessage) {
        return this.updateById(qkjvipMemberMessage);
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
}
