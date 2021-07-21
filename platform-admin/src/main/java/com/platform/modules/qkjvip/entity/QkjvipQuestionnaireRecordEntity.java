/*
 * 项目名称:platform-plus
 * 类名称:QkjvipQuestionnaireRecordEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-07-21 10:06:03        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author liuqianru
 * @date 2021-07-21 10:06:03
 */
@Data
@TableName("QKJVIP_QUESTIONNAIRE_RECORD")
public class QkjvipQuestionnaireRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 问卷表id
     */
    private String mainid;
    /**
     * openid
     */
    private String openid;
    /**
     * 青稞粒
     */
    private Integer integral;
    /**
     * 添加时间
     */
    private Date addtime;
}
