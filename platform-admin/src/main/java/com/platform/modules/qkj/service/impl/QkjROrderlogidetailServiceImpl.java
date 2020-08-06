/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderlogidetailServiceImpl.java
 * 包名称:com.platform.modules.qkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-11-28 09:16:35        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkj.dao.QkjROrderlogidetailDao;
import com.platform.modules.qkj.entity.QkjROrderlogidetailEntity;
import com.platform.modules.qkj.service.QkjROrderlogidetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊
 * @date 2019-11-28 09:16:35
 */
@Service("qkjROrderlogidetailService")
public class QkjROrderlogidetailServiceImpl extends ServiceImpl<QkjROrderlogidetailDao, QkjROrderlogidetailEntity> implements QkjROrderlogidetailService {

    @Override
    public List<QkjROrderlogidetailEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }
    @Override
    public List<QkjROrderlogidetailEntity> queryPrintAll(Map<String, Object> params) {
        return baseMapper.queryPrintAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjROrderlogidetailEntity> page = new Query<QkjROrderlogidetailEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjROrderlogidetailPage(page, params));
    }

    @Override
    public boolean add(QkjROrderlogidetailEntity qkjROrderlogidetail) {
        return this.save(qkjROrderlogidetail);
    }

    @Override
    public boolean update(QkjROrderlogidetailEntity qkjROrderlogidetail) {
        return this.updateById(qkjROrderlogidetail);
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
