/*
 * 项目名称:platform-plus
 * 类名称:MemberVisitServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/13 10:19            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.qkjvip.dao.MemberVisitDao;
import com.platform.modules.qkjvip.entity.MemberAddressEntity;
import com.platform.modules.qkjvip.entity.MemberVisitEntity;
import com.platform.modules.qkjvip.service.MemberVisitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MemberVisitServiceImpl
 *
 * @author liuqianru
 * @date 2020/3/13 10:19
 */
@Service("memberVisitService")
public class MemberVisitServiceImpl extends ServiceImpl<MemberVisitDao, MemberVisitEntity> implements MemberVisitService {

    @Override
    public List<MemberVisitEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "mv.add_time");
        params.put("asc", false);
        Page<MemberVisitEntity> page = new Query<MemberVisitEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMemberVisitList(page, params));
    }

    @Override
    public void add(MemberVisitEntity mvisit, Map<String, Object> params) {
        this.save(mvisit);
    }

    @Override
    public void add(MemberVisitEntity mvisit) {
        this.save(mvisit);
    }

    @Override
    public void update(MemberVisitEntity mvisit, Map<String, Object> params) {
        this.updateById(mvisit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] uuids) {
        this.removeByIds(Arrays.asList(uuids));
    }
}
