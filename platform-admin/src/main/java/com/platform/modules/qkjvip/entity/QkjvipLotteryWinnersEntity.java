/*
 * 项目名称:platform-plus
 * 类名称:QkjvipLotteryWinnersEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-07-07 16:06:26        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体
 *
 * @author liuqianru
 * @date 2021-07-07 16:06:26
 */
@Data
@TableName("QKJVIP_LOTTERY_WINNERS")
public class QkjvipLotteryWinnersEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 奖品
     */
    private String prizename;
    /**
     * 奖项等级
     */
    private String prizelevelname;
    /**
     * mainId
     */
    private String mainid;
    /**
     * userId
     */
    private String userid;
}
