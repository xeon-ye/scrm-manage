/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderEntity.java
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
import com.platform.modules.qkj.controller.QkjRSonorderController;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
@Data
@TableName("QKJ_R_ORDER")
public class QkjROrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String id;
    /**
     * 供应商
     */
    private String userid;
    /**
     * 制单依据
     */
    private String makesys;
    /**
     * 批准人
     */
    private String approver;
    /**
     * 经办人
     */
    private String agent;
    /**
     * 负责人
     */
    private String percharge;
    /**
     * 
     */
    private String stsdate;
    /**
     * 
     */
    private Date addtime;
    /**
     * 
     */
    private String adduser;

    private Integer state;

    private String number;

    private String enddate;

    private String remark;

    private String uploadad;

    private Integer source;
    private String creatuser;

    @TableField(exist = false)
    private List<QkjRSonorderEntity> sonorders;
    @TableField(exist = false)
    private List<QkjROrderlogidetailEntity> logis;
    @TableField(exist = false)
    private String realname;
    @TableField(exist = false)
    private String agentname;
    @TableField(exist = false)
    private  String approvername;
    @TableField(exist = false)
    private String stsdatel;
    @TableField(exist = false)
    private String prostate;
    @TableField(exist = false)
    private Integer countnum;
    @TableField(exist = false)
    private String logintitle;


}
