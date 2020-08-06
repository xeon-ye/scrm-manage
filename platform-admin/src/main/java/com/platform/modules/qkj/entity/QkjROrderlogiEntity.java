/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderlogiEntity.java
 * 包名称:com.platform.modules.qkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-10 11:18:28        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.entity;

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
 * @date 2019-09-10 11:18:28
 */
@Data
@TableName("QKJ_R_ORDERLOGI")
public class QkjROrderlogiEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String id;
    /**
     * 订单编号
     */
    private String orderid;
    /**
     * 物流名称
     */
    private String loginame;
    /**
     * 运单号
     */
    private String loginum;
    /**
     * 物流电话
     */
    private String logiphone;
    /**
     * 发货日期
     */
    private String logidate;

    private String username;
    private String userphone;
    private String shdate;
    private String driver;
    private String driverphone;
    private String car_number;
    private String creatuser;
    private String cgphone;
    private String cgorder;

    @TableField(exist = false)
    private List<QkjRSonorderEntity> dataListson;
    @TableField(exist = false)
    private List<QkjROrderlogidetailEntity> logidetails;
    @TableField(exist = false)
    private String addormdy;
    @TableField(exist = false)
    private String number;
    @TableField(exist = false)
    private String maniname;
}
