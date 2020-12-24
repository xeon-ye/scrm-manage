/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberCponEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-12-17 14:46:26        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

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
 * @date 2020-12-17 14:46:26
 */
@Data
@TableName("QKJVIP_MEMBER_CPON")
public class QkjvipMemberCponEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private String id;
    /**
     * title
     */
    private String title;
    /**
     * add_user
     */
    private String addUser;
    /**
     * add_dept
     */
    private String addDept;
    /**
     * add_time
     */
    private String addTime;

    @TableField(exist = false)
    private List<QkjvipMemberCponsonEntity> sonlists;
}
