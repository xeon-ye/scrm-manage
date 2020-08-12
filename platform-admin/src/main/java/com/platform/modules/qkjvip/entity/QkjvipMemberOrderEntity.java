/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberOrderEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-08-11 14:15:23        孙珊珊     初版做成
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
 * @author 孙珊珊
 * @date 2020-08-11 14:15:23
 */
@Data
@TableName("QKJVIP_MEMBER_ORDER")
public class QkjvipMemberOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String id;
    /**
     * 
     */
    private String orderid;
    /**
     * 
     */
    private String productname;
    /**
     * 
     */
    private String saleprice;
    /**
     * 
     */
    private String quantity;
    /**
     * 
     */
    private String realtotalprice;
    /**
     * 
     */
    private String orderdate;
    /**
     * 
     */
    private String cellphone;
    /**
     * 
     */
    private String buyertype;
    /**
     * 
     */
    private String shopname;
    /**
     * 
     */
    private String shopprovincename;
    /**
     * 
     */
    private String shopcityname;
    /**
     * 
     */
    private String orderstatus;
    /**
     * 
     */
    private Integer giveaway;
}
