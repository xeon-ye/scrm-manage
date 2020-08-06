/*
 * 项目名称:platform-plus
 * 类名称:QkjOaLeaveServiceImpl.java
 * 包名称:com.platform.modules.qkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-04-22 17:25:47        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkj.dao.QkjOaLeaveDao;
import com.platform.modules.qkj.entity.QkjOaLeaveEntity;
import com.platform.modules.qkj.service.QkjOaLeaveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 注册Service实现类
 *
 * @author liuqianru
 * @date 2020-04-22 17:25:47
 */
@Service("qkjOaLeaveService")
public class QkjOaLeaveServiceImpl extends ServiceImpl<QkjOaLeaveDao, QkjOaLeaveEntity> implements QkjOaLeaveService {

    @Override
    public List<QkjOaLeaveEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjOaLeaveEntity> page = new Query<QkjOaLeaveEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjOaLeavePage(page, params));
    }

    @Override
    public boolean add(QkjOaLeaveEntity qkjOaLeave) {
        return this.save(qkjOaLeave);
    }

    @Override
    public boolean update(QkjOaLeaveEntity qkjOaLeave) {
        return this.updateById(qkjOaLeave);
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
