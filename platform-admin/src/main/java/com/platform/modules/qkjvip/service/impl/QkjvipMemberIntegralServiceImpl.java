/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberIntegralServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-12-21 15:18:24        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkjvip.dao.QkjvipMemberIntegralDao;
import com.platform.modules.qkjvip.entity.QkjvipMemberIntegralEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberIntegralService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author liuqianru
 * @date 2020-12-21 15:18:24
 */
@Service("qkjvipMemberIntegralService")
public class QkjvipMemberIntegralServiceImpl extends ServiceImpl<QkjvipMemberIntegralDao, QkjvipMemberIntegralEntity> implements QkjvipMemberIntegralService {

    @Override
    public List<QkjvipMemberIntegralEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjvipMemberIntegralEntity> page = new Query<QkjvipMemberIntegralEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjvipMemberIntegralPage(page, params));
    }

    @Override
    public boolean add(QkjvipMemberIntegralEntity qkjvipMemberIntegral) {
        return this.save(qkjvipMemberIntegral);
    }

    @Override
    public boolean update(QkjvipMemberIntegralEntity qkjvipMemberIntegral) {
        return this.updateById(qkjvipMemberIntegral);
    }

    @Override
    public void updateStatus(QkjvipMemberIntegralEntity qkjvipMemberIntegral) {
        baseMapper.updateStatus(qkjvipMemberIntegral);
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
