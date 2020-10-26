/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberSignupServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-30 09:20:27        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkjvip.dao.QkjvipMemberSignupDao;
import com.platform.modules.qkjvip.entity.QkjvipMemberSignupEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberSignupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊
 * @date 2020-09-30 09:20:27
 */
@Service("qkjvipMemberSignupService")
public class QkjvipMemberSignupServiceImpl extends ServiceImpl<QkjvipMemberSignupDao, QkjvipMemberSignupEntity> implements QkjvipMemberSignupService {

    @Override
    public List<QkjvipMemberSignupEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjvipMemberSignupEntity> page = new Query<QkjvipMemberSignupEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjvipMemberSignupPage(page, params));
    }

    @Override
    public boolean add(QkjvipMemberSignupEntity qkjvipMemberSignup) {
        return this.save(qkjvipMemberSignup);
    }

    @Override
    public boolean update(QkjvipMemberSignupEntity qkjvipMemberSignup) {
        return this.updateById(qkjvipMemberSignup);
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
