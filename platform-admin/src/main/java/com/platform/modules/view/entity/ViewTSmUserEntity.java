/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmUserEntity.java
 * 包名称:com.platform.modules.view.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-11-01 11:06:39        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author 孙珊珊
 * @date 2019-11-01 11:06:39
 */
@Data
@TableName("VIEW_T_SM_USER")
public class ViewTSmUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String userid;
    /**
     * 
     */
    private String userName;
    /**
     * 
     */
    private String realName;
    /**
     * 
     */
    private Integer sex;
    /**
     * 
     */
    private Integer orgNo;
    /**
     * 
     */
    private Integer status;
    /**
     * 
     */
    private String roleidlist;
    /**
     * 
     */
    private Date createTime;
    /**
     * 
     */
    private Integer createState;
}
