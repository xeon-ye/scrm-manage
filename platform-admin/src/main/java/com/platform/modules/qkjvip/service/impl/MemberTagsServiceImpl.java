/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberLabelServiceImpl.java
 * 包名称:com.platform.modules.qkjvip.service.impl
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-08-26 09:00:34        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.modules.qkjvip.dao.MemberTagsDao;
import com.platform.modules.qkjvip.entity.MemberTagsEntity;
import com.platform.modules.qkjvip.service.MemberTagsService;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service实现类
 *
 * @author liuqianru
 * @date 2020-08-26 09:00:34
 */
@Service("memberTagsService")
public class MemberTagsServiceImpl extends ServiceImpl<MemberTagsDao, MemberTagsEntity> implements MemberTagsService {

    @Override
    public void saveOrUpdate(String memberId, List<String> tagList) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("member_id", memberId);
        //先删除会员与标签关系
        this.removeByMap(map);

        if (tagList == null || tagList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        List<MemberTagsEntity> list = new ArrayList<>(tagList.size());
        for (String tagId : tagList) {
            MemberTagsEntity memberTagsEntity = new MemberTagsEntity();
            memberTagsEntity.setTagId(tagId);
            memberTagsEntity.setMemberId(memberId);

            list.add(memberTagsEntity);
        }
        this.saveBatch(list);
    }

    @Override
    public List<String> queryTagsList(String memberId) {
        return baseMapper.queryTagsList(memberId);
    }

    @Override
    public int deleteBatch(String[] tagIds) {
        return baseMapper.deleteBatch(tagIds);
    }

}
