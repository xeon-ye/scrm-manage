/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberFansEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-09 14:02:22        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author liuqianru
 * @date 2020-09-09 14:02:22
 */
@Data
@TableName("qkjvip_lastupdatetime")
public class QrtzLastUpdateTimeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 粉丝最后更新时间
     */
    private Date fansLastDatetime;
    /**
     * 会员最后更新时间
     */
    private Date memberLastDatetime;
    /**
     * 订单最后更新时间
     */
    private Date orderLastDatetime;
}
