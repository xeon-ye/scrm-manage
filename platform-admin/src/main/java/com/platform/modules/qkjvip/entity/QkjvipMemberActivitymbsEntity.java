/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberActivitymbsEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-18 13:35:24        孙珊珊     初版做成
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
 * @author 孙珊珊
 * @date 2020-09-18 13:35:24
 */
@Data
@TableName("QKJVIP_MEMBER_ACTIVITYMBS")
public class QkjvipMemberActivitymbsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private String id;
    /**
     * member_id
     */
    private String memberId;
    /**
     * activity_id
     */
    private String activityId;

    private Integer status;

    /**
     * 会员名称
     */
    @TableField(exist = false)
    private String memberName;
    /*
     * 会员真实姓名
     */
    @TableField(exist = false)
    private String realName;
    /*
     * 会员手机
     */
    @TableField(exist = false)
    private String mobile;

    @TableField(exist = false)
    private Integer bmstatus;
    @TableField(exist = false)
    private Integer qdstatus;
    @TableField(exist = false)
    private String bmid;
    @TableField(exist = false)
    private String headImgUrl;
    @TableField(exist = false)
    private String servicename;
    @TableField(exist = false)
    private String singtype;
    @TableField(exist = false)
    private Integer smemtype;
    @TableField(exist = false)
    private String openid;
}
