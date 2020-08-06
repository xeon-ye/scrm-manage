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
package com.platform.modules.erpm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.modules.qkj.entity.QkjROrderlogiEntity;
import com.platform.modules.qkj.entity.QkjRSonorderEntity;
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
public class PurOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String caigouOrg;


}
