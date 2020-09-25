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

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.modules.qkjvip.dao.MemberTagsDao;
import com.platform.modules.qkjvip.entity.MemberEntity;
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
    public void saveOrUpdate(MemberEntity member) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("member_id", member.getMemberId());
        //先删除会员与标签关系
        this.removeByMap(map);

        if (member.getMemberLabel() != null && !"".equals(member.getMemberLabel())) {
            JSONArray jsonArray = JSONArray.parseArray(member.getMemberLabel());
            List<MemberTagsEntity> memberTagsEntityList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray tagIdList = (JSONArray) jsonArray.getJSONObject(i).get("tagIdList");
                String tagGroupId = jsonArray.getJSONObject(i).get("tagGroupId").toString();
                for (int j = 0; j < tagIdList.size(); j++) {
                    MemberTagsEntity memberTagsEntity = new MemberTagsEntity();
                    memberTagsEntity.setMemberId(member.getMemberId());
                    memberTagsEntity.setTagGroupId(tagGroupId);
                    memberTagsEntity.setTagId(tagIdList.get(j).toString());
                    memberTagsEntityList.add(memberTagsEntity);
                }
            }
            //保存用户与标签关系
            if (memberTagsEntityList.size() > 0) {
                this.saveBatch(memberTagsEntityList);
            }
        }
    }

    @Override
    public List<MemberTagsEntity> queryTagsList(Map<String, Object> params) {
        return baseMapper.queryTagsList(params);
    }

    @Override
    public int deleteBatch(String[] memberIds) {
        return baseMapper.deleteBatch(memberIds);
    }

}
