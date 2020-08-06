/*
 * 项目名称:platform-plus
 * 类名称:QkjOaLeaveEntity.java
 * 包名称:com.platform.modules.qkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-04-22 17:25:47        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 注册实体
 *
 * @author liuqianru
 * @date 2020-04-22 17:25:47
 */
@Data
@TableName("QKJ_OA_LEAVE")
public class QkjOaLeaveEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 请假类型
     */
    private Integer leaveType;
    /**
     * 标题
     */
    private String leaveTitle;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 请假事由
     */
    private String leaveCause;
    /**
     * 提交时间
     */
    private Date addTime;
    /**
     * 提交人
     */
    private String addUser;
    /**
     * 提交部门
     */
    private String addDept;
}
