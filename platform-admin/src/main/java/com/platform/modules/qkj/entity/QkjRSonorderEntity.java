/*
 * 项目名称:platform-plus
 * 类名称:QkjRSonorderEntity.java
 * 包名称:com.platform.modules.qkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-28 14:44:59        孙珊珊sc     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
@Data
@TableName("QKJ_R_SONORDER")
public class QkjRSonorderEntity implements Serializable {
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
    private String materielid;
    /**
     * 
     */
    private Integer num;
    private Integer sendnum;
    private Double rate;
    private Integer sendstate;
    private Integer rgstate;
    private String rgid;
    private Integer pronum;

    private Integer rgnum;

    @TableField(exist = false)
    private String loginame;
    @TableField(exist = false)
    private String loginum;
    @TableField(exist = false)
    private String logiphone;
    @TableField(exist = false)
    private String logidate;
    @TableField(exist = false)
    private String fnameL2;
    @TableField(exist = false)
    private String fmodel;
    @TableField(exist = false)
    private Integer bnum;


}
