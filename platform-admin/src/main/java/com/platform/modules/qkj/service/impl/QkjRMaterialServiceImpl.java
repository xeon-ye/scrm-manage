/*
 * 项目名称:platform-plus
 * 类名称:QkjRMaterialServiceImpl.java
 * 包名称:com.platform.modules.qkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-20 11:15:58        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkj.dao.QkjRMaterialDao;
import com.platform.modules.qkj.entity.QkjRMaterialEntity;
import com.platform.modules.qkj.service.QkjRMaterialService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊
 * @date 2019-09-20 11:15:58
 */
@Service("qkjRMaterialService")
public class QkjRMaterialServiceImpl extends ServiceImpl<QkjRMaterialDao, QkjRMaterialEntity> implements QkjRMaterialService {

    @Override
    public List<QkjRMaterialEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.fid");
        params.put("asc", false);
        Page<QkjRMaterialEntity> page = new Query<QkjRMaterialEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjRMaterialPage(page, params));
    }

    @Override
    public boolean add(QkjRMaterialEntity qkjRMaterial) {
        return this.save(qkjRMaterial);
    }

    @Override
    public boolean update(QkjRMaterialEntity qkjRMaterial) {
        return this.updateById(qkjRMaterial);
    }

    @Override
    public boolean delete(String fid) {
        return this.removeById(fid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(String[] fids) {
        return this.removeByIds(Arrays.asList(fids));
    }

    @Override
    public void deleteAll() {
        baseMapper.truncateTable();
    }

}
