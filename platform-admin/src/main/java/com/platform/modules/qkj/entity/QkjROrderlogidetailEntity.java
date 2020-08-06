/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderlogidetailEntity.java
 * 包名称:com.platform.modules.qkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-11-28 09:16:35        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 实体
 *
 * @author 孙珊珊
 * @date 2019-11-28 09:16:35
 */
@Data
@TableName("QKJ_R_ORDERLOGIDETAIL")
public class QkjROrderlogidetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String id;
    /**
     * 发货单id
     */
    private String logiid;
    /**
     * 物料id
     */
    private String materid;
    /**
     * 发货数量
     */
    private Integer num;
    private Integer bancle;



    /**
     * 订单编号
     */
    @TableField(exist = false)
    private String orderid;
    /**
     * 物流名称
     */
    @TableField(exist = false)
    private String loginame;
    /**
     * 运单号
     */
    @TableField(exist = false)
    private String loginum;
    /**
     * 物流电话
     */
    @TableField(exist = false)
    private String logiphone;
    /**
     * 发货日期
     */
    @TableField(exist = false)
    private String logidate;
    @TableField(exist = false)
    private String matername;
    @TableField(exist = false)
    private String parentId;
    @TableField(exist = false)
    private Integer totle;
    @TableField(exist = false)
    private String fnum;
    @TableField(exist = false)
    private String fbaseunit;
}
