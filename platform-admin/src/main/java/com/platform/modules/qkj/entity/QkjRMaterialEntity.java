/*
 * 项目名称:platform-plus
 * 类名称:QkjRMaterialEntity.java
 * 包名称:com.platform.modules.qkj.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-20 11:15:58        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体
 *
 * @author 孙珊珊
 * @date 2019-09-20 11:15:58
 */
@Data
@TableName("QKJ_R_MATERIAL")
public class QkjRMaterialEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String fid;
    /**
     * 
     */
    private String fnameL2;
    /**
     * 
     */
    private String fnumber;
    /**
     * 
     */
    private String fmodel;
    /**
     * 
     */
    private String fbaseunit;

    private String groupid;
}
