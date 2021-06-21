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

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "会员名称", orderNum = "3", width = 15, groupName = "扩展信息",  fixedIndex = 3)
    private String memberName;
    /*
     * 会员真实姓名
     */
    @TableField(exist = false)
    @Excel(name = "会员真实姓名", orderNum = "4", width = 15, groupName = "扩展信息",  fixedIndex = 4)
    private String realName;
    /*
     * 会员手机
     */
    @TableField(exist = false)
    @Excel(name = "会员手机号", orderNum = "5", width = 15, groupName = "扩展信息",  fixedIndex = 5)
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
    private String memberidto;


    @TableField(exist = false)
    @Excel(name = "活动标题", orderNum = "0", width = 15, groupName = "扩展信息",  fixedIndex = 0)
    private String title;
    @TableField(exist = false)
    @Excel(name = "活动开始时间", orderNum = "1", width = 15, groupName = "扩展信息",  fixedIndex = 1)
    private String star_date;
    @TableField(exist = false)
    @Excel(name = "活动结束时间", orderNum = "2", width = 15, groupName = "扩展信息",  fixedIndex = 2)
    private String end_date;
    @Excel(name = "邀约方式", orderNum = "6", width = 15, groupName = "扩展信息",  fixedIndex = 6)
    private String statusstr;
    @Excel(name = "报名状态", orderNum = "6", width = 15, groupName = "扩展信息",  fixedIndex = 6)
    private String bmstatusstr;
    @Excel(name = "签到状态", orderNum = "6", width = 15, groupName = "扩展信息",  fixedIndex = 6)
    private String qdstatusstr;
}
