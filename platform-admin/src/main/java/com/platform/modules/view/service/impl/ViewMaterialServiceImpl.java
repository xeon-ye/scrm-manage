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
import com.platform.modules.view.dao.ViewMaterialDao;
import com.platform.modules.view.entity.ViewMaterialEntity;
import com.platform.modules.view.service.ViewMaterialService;
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
@Service("viewMaterialService")
public class ViewMaterialServiceImpl extends ServiceImpl<ViewMaterialDao, ViewMaterialEntity> implements ViewMaterialService {

    @Override
    @DataSource(name="second")
    public List<ViewMaterialEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

}
