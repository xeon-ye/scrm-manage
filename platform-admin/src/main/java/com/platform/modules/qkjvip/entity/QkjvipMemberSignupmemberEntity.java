/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberSignupmemberEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-10-26 13:18:34        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 实体
 *
 * @author 孙珊珊
 * @date 2020-10-26 13:18:34
 */
@Data
@TableName("QKJVIP_MEMBER_SIGNUPMEMBER")
public class QkjvipMemberSignupmemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private String id;
    /**
     * 活动
     */
    private String activityId;
    /**
     * 签到人
     */
    private String signupId;
    /**
     * 签到时间
     */
    private String time;

    private String memberId;

    @TableField(exist = false)
    private List<QkjvipMemberActivitymbsEntity> memlist;
}
