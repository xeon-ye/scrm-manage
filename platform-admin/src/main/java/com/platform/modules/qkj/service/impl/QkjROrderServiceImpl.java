/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderServiceImpl.java
 * 包名称:com.platform.modules.qkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-28 14:44:59        孙珊珊sc     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkj.dao.QkjROrderDao;
import com.platform.modules.qkj.entity.QkjROrderEntity;
import com.platform.modules.qkj.service.QkjROrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
@Service("qkjROrderService")
public class QkjROrderServiceImpl extends ServiceImpl<QkjROrderDao, QkjROrderEntity> implements QkjROrderService {
    @Override
    public List<QkjROrderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.addtime");
        params.put("asc", false);
        Page<QkjROrderEntity> page = new Query<QkjROrderEntity>(params).getPage();
        List<QkjROrderEntity> listCount=new ArrayList<>();
        listCount=baseMapper.pageCount(params);
        page.setTotal(listCount.get(0).getCountnum());
        return page.setRecords(baseMapper.selectQkjROrderPage(page, params));
    }

    @Override
    public boolean add(QkjROrderEntity qkjROrder) {
        return this.save(qkjROrder);
    }

    @Override
    public boolean update(QkjROrderEntity qkjROrder) {
        return this.updateById(qkjROrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(QkjROrderEntity order) {
        baseMapper.updateStateById(order);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStateBySend(QkjROrderEntity order) {
        baseMapper.updateStateBySend(order);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState2(QkjROrderEntity order) {
        baseMapper.updateStateById2(order);

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sureBatchById(String[] ids) {
         baseMapper.sureBatchById(ids);
    }
}
