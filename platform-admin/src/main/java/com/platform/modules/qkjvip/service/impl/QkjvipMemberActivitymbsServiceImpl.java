/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberActivitymbsServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-18 13:35:24        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkjvip.dao.QkjvipMemberActivitymbsDao;
import com.platform.modules.qkjvip.entity.QkjvipMemberActivitymbsEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberActivitymbsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊
 * @date 2020-09-18 13:35:24
 */
@Service("qkjvipMemberActivitymbsService")
public class QkjvipMemberActivitymbsServiceImpl extends ServiceImpl<QkjvipMemberActivitymbsDao, QkjvipMemberActivitymbsEntity> implements QkjvipMemberActivitymbsService {

    @Override
    public List<QkjvipMemberActivitymbsEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjvipMemberActivitymbsEntity> page = new Query<QkjvipMemberActivitymbsEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjvipMemberActivitymbsPage(page, params));
    }

    @Override
    public boolean add(QkjvipMemberActivitymbsEntity qkjvipMemberActivitymbs) {
        return this.save(qkjvipMemberActivitymbs);
    }

    @Override
    public boolean update(QkjvipMemberActivitymbsEntity qkjvipMemberActivitymbs) {
        return this.updateById(qkjvipMemberActivitymbs);
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
