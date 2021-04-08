/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberActivityEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-03 09:49:29        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author 孙珊珊
 * @date 2020-09-03 09:49:29
 */
@Data
@TableName("QKJVIP_MEMBER_ACTIVITY")
public class QkjvipMemberActivityEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;
    /**
     * 网页内容
     */
    private String content;
    /**
     * 是否签到
     */
    private Integer issign;
    /**
     * id
     */
    @TableId
    private String id;

    private String regStarDate;
    private String regEndDate;
    private String starDate;
    private String endDate;
    private Integer ispri;
    private String priPerson;
    private String leading;
    private String remark;
    private Integer isaud;

    private String adduser;
    private String adddept;
    private String address;
    private String bgimg;
    private String issignimg;
    private String atype;
    private Integer status;
    private String triplog;
    private String activilog;
    private Date addtime;
    private Integer showtype;//活动详情展示形式

    @TableField(exist = false)
    private String htmlurl;
    @TableField(exist = false)
    private List<QkjvipMemberActivitymbsEntity> mbs;
    @TableField(exist = false)
    private List<QkjvipMemberSignupaddressEntity> addresses;
    @TableField(exist = false)
    private Integer mbsnum;
    @TableField(exist = false)
    private Integer signupnum;
    @TableField(exist = false)
    private Integer smnum;
    @TableField(exist = false)
    private String url;
    @TableField(exist = false)
    private String msgcontent;
    @TableField(exist = false)
    private Integer istake;
    @TableField(exist = false)
    private String addressstr;
    @TableField(exist = false)
    private String realname;
    @TableField(exist = false)
    private Integer activeper;
}
