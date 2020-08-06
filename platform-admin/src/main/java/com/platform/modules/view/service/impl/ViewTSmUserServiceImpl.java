/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmUserServiceImpl.java
 * 包名称:com.platform.modules.view.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-11-01 11:06:39        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.datasources.annotation.DataSource;
import com.platform.modules.view.dao.ViewTSmUserDao;
import com.platform.modules.view.entity.ViewTSmUserEntity;
import com.platform.modules.view.service.ViewTSmUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊
 * @date 2019-11-01 11:06:39
 */
@Service("viewTSmUserService")
public class ViewTSmUserServiceImpl extends ServiceImpl<ViewTSmUserDao, ViewTSmUserEntity> implements ViewTSmUserService {

    @Override
    @DataSource(name="second")
    public List<ViewTSmUserEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

}
