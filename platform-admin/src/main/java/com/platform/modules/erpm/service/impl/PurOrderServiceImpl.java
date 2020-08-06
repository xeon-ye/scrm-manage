/*
 * 项目名称:platform-plus
 * 类名称:PurOrderServiceImpl.java
 * 包名称:com.platform.modules.qkjm.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-24 17:54:49        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.erpm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.datasources.annotation.DataSource;
import com.platform.modules.erpm.dao.PurOrderDao;
import com.platform.modules.erpm.entity.PurOrderEntity;
import com.platform.modules.erpm.service.PurOrderService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 记录Service实现类
 *
 * @author 孙珊珊
 * @date 2019-10-24 17:54:49
 */
@Service("purOrderService")
public class PurOrderServiceImpl extends ServiceImpl<PurOrderDao, PurOrderEntity> implements PurOrderService {

    @Override
    @DataSource(name="second")
    public List<PurOrderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public boolean saveBatch(Collection<PurOrderEntity> entityList) {
        return false;
    }
}
