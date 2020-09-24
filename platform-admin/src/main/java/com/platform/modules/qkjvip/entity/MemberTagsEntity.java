/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberLabelEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-08-26 14:33:22        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体
 *
 * @author liuqianru
 * @date 2020-08-26 14:33:22
 */
@Data
@TableName("QKJVIP_MEMBER_TAGS")
public class MemberTagsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * tag_id
     */
    private String tagId;
    /**
     * member_id
     */
    private String memberId;
    /**
     * 标签组id
     */
    private String tagGroupId;
    /**
     * 以逗号隔开的标签字符串
     */
    @TableField(exist = false)
    private String items;
}
