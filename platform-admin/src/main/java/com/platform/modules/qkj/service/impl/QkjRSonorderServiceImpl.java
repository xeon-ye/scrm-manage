/*
 * 项目名称:platform-plus
 * 类名称:QkjRSonorderServiceImpl.java
 * 包名称:com.platform.modules.qkj.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-28 14:44:59        孙珊珊sc     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkj.dao.QkjRSonorderDao;
import com.platform.modules.qkj.entity.QkjRSonorderEntity;
import com.platform.modules.qkj.service.QkjRSonorderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
@Service("qkjRSonorderService")
public class QkjRSonorderServiceImpl extends ServiceImpl<QkjRSonorderDao, QkjRSonorderEntity> implements QkjRSonorderService {

    @Override
    public List<QkjRSonorderEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "T.id");
        params.put("asc", false);
        Page<QkjRSonorderEntity> page = new Query<QkjRSonorderEntity>(params).getPage();
        return page.setRecords(baseMapper.selectQkjRSonorderPage(page, params));
    }

    @Override
    public boolean add(QkjRSonorderEntity qkjRSonorder) {
        return this.save(qkjRSonorder);
    }

    @Override
    public boolean update(QkjRSonorderEntity qkjRSonorder) {
        return this.updateById(qkjRSonorder);
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

    @Override
    public void updateSendnumById(QkjRSonorderEntity qkjRSonorder) {
         baseMapper.updateSendnumById(qkjRSonorder);
    }

    @Override
    public void updatePornumById(QkjRSonorderEntity qkjRSonorder) {
        baseMapper.updatePornumById(qkjRSonorder);
    }

    @Override
    public void updatePornumByIdnew(QkjRSonorderEntity qkjRSonorder) {
        baseMapper.updatePornumByIdnew(qkjRSonorder);
    }

    @Override
    public void updateRateById(QkjRSonorderEntity qkjRSonorder){
        baseMapper.updateRateById(qkjRSonorder);
    }

    @Override
    public void deletebyOrderAndId(String orderid,List<String> ids){baseMapper.deletebyOrderAndId(orderid,ids);}

    @Override
    public void updateSonbyOAndM(QkjRSonorderEntity qkjRSonorder){
        baseMapper.updateSonbyOAndM(qkjRSonorder);
    }


}
