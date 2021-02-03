/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberLabelEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-08-26 14:33:22        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platform.modules.util.Double2Serializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体
 *
 * @author liuqianru
 * @date 2021-02-02 10:33:22
 */
@Data
public class MemberPortraitAreaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /*检索条件start*/
    private Integer sex;
    private Integer membergeneration;
    private Boolean isall;
    /*检索条件end*/

    /*返回值start*/
    private String city;
    private Integer count;
    @JsonSerialize(using = Double2Serializer.class)
    private Double percent;
    /*返回值end*/

    // 分页
    private Integer pageindex;
    private Integer pagesize;

}
