/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmPurorderEntity.java
 * 包名称:com.platform.modules.view.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-29 14:00:54        孙珊珊     初版做成
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
 * @date 2019-10-29 14:00:54
 */
@Data
@TableName("VIEW_T_SM_PURORDER")
public class ViewTSmPurorderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;
    private String stsdate;
    private String number;
    private String userid;

    private String orderid;
    private String materielid;
    private Integer num;

    private Date FCreateTime;
    private Integer FBaseStatus;
    private Integer rknum;

    private String creatuser;


}
