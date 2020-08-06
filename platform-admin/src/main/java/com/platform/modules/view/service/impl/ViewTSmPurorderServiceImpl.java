/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmPurorderServiceImpl.java
 * 包名称:com.platform.modules.view.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-29 14:00:54        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.datasources.annotation.DataSource;
import com.platform.modules.view.dao.ViewTSmPurorderDao;
import com.platform.modules.view.entity.ViewTSmPurorderEntity;
import com.platform.modules.view.service.ViewTSmPurorderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊
 * @date 2019-10-29 14:00:54
 */
@Service("viewTSmPurorderService")
public class ViewTSmPurorderServiceImpl extends ServiceImpl<ViewTSmPurorderDao, ViewTSmPurorderEntity> implements ViewTSmPurorderService {

    @Override
    @DataSource(name="second")
    public List<ViewTSmPurorderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }
    @Override
    @DataSource(name="second")
    public List<ViewTSmPurorderEntity> queryAllMain(Map<String, Object> params) {
        return baseMapper.queryAllMain(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.caigouorg");
        params.put("asc", false);
        Page<ViewTSmPurorderEntity> page = new Query<ViewTSmPurorderEntity>(params).getPage();
        return page.setRecords(baseMapper.selectViewTSmPurorderPage(page, params));
    }

    @Override
    public boolean add(ViewTSmPurorderEntity viewTSmPurorder) {
        return this.save(viewTSmPurorder);
    }

    @Override
    public boolean update(ViewTSmPurorderEntity viewTSmPurorder) {
        return this.updateById(viewTSmPurorder);
    }

    @Override
    public boolean delete(String caigouorg) {
        return this.removeById(caigouorg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] caigouorgs) {
        return this.removeByIds(Arrays.asList(caigouorgs));
    }
}
