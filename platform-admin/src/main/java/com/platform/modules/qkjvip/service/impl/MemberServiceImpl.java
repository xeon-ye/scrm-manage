/*
 * 项目名称:platform-plus
 * 类名称:MemberServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/9 14:28            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.common.utils.Query;
import com.platform.modules.pageCont.pageCount;
import com.platform.modules.qkjvip.dao.MemberDao;
import com.platform.modules.qkjvip.entity.MemberEntity;
import com.platform.modules.qkjvip.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author liuqianru
 * @date 2020/3/9 14:28
 */

@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public List<MemberEntity> queryAll(Map<String, Object> params) {
        return baseMapper.queryAll(params);
    }

    @Override
    public pageCount selectMemberCount(Map<String, Object> params) {
        return baseMapper.selectMemberCount(params);
    }

    @Override
    public Page queryPage(Map<String, Object> params) {
        //排序
        params.put("sidx", "m.add_time");
        params.put("asc", false);
        Page<MemberEntity> page = new Query<MemberEntity>(params).getPage();
        return page.setRecords(baseMapper.selectMemberList(page, params));
    }

    @Override
    public void add(MemberEntity member, Map<String, Object> params) {
        this.save(member);
    }

    @Override
    public void add(MemberEntity member) {
        this.save(member);
    }

    @Override
    public void addBatch(List<MemberEntity> mList) {
        this.saveBatch(mList);
    }

    @Override
    public void update(MemberEntity member, Map<String, Object> params) {
        this.updateById(member);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] memberIds) {
        this.removeByIds(Arrays.asList(memberIds));
    }


}
