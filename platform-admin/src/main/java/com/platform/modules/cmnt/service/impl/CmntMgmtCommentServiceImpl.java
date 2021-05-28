/*
 * 项目名称:platform-plus
 * 类名称:CmntMgmtCommentServiceImpl.java
 * 包名称:com.platform.modules.cmnt.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-05-25 17:16:11        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.cmnt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.cmnt.dao.CmntMgmtCommentDao;
import com.platform.modules.cmnt.entity.CmntMgmtCommentEntity;
import com.platform.modules.cmnt.service.CmntMgmtCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author liuqianru
 * @date 2021-05-25 17:16:11
 */
@Service("cmntMgmtCommentService")
public class CmntMgmtCommentServiceImpl extends ServiceImpl<CmntMgmtCommentDao, CmntMgmtCommentEntity> implements CmntMgmtCommentService {

    @Override
    public List<CmntMgmtCommentEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.addtime");
        params.put("asc", false);
        Page<CmntMgmtCommentEntity> page = new Query<CmntMgmtCommentEntity>(params).getPage();
        page.setRecords(baseMapper.selectCmntMgmtCommentPage(page, params));
        page.setTotal(baseMapper.queryAll(params).size());
        return page;
    }

    @Override
    public boolean add(CmntMgmtCommentEntity cmntMgmtComment) {
        return this.save(cmntMgmtComment);
    }

    @Override
    public boolean update(CmntMgmtCommentEntity cmntMgmtComment) {
        return this.updateById(cmntMgmtComment);
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
