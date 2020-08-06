/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderlogiServiceImpl.java
 * 包名称:com.platform.modules.qkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-10 11:18:28        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkj.dao.QkjROrderlogiDao;
import com.platform.modules.qkj.entity.QkjROrderlogiEntity;
import com.platform.modules.qkj.service.QkjROrderlogiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊
 * @date 2019-09-10 11:18:28
 */
@Service("qkjROrderlogiService")
public class QkjROrderlogiServiceImpl extends ServiceImpl<QkjROrderlogiDao, QkjROrderlogiEntity> implements QkjROrderlogiService {

    @Override
    public List<QkjROrderlogiEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjROrderlogiEntity> page = new Query<QkjROrderlogiEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjROrderlogiPage(page, params));
    }

    @Override
    public boolean add(QkjROrderlogiEntity qkjROrderlogi) {
        return this.save(qkjROrderlogi);
    }

    @Override
    public boolean update(QkjROrderlogiEntity qkjROrderlogi) {
        return this.updateById(qkjROrderlogi);
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
