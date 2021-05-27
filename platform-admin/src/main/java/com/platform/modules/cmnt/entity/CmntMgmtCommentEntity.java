/*
 * 项目名称:platform-plus
 * 类名称:CmntMgmtCommentEntity.java
 * 包名称:com.platform.modules.cmnt.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-05-25 17:16:11        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.cmnt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 *
 * @author liuqianru
 * @date 2021-05-25 17:16:11
 */
@Data
@TableName("CMNT_MGMT_COMMENT")
public class CmntMgmtCommentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 类型id
     */
    private String mainid;
    /**
     * 评论类型
     */
    private Integer maintype;
    /**
     * 评论
     */
    private String comment;
    /**
     * 回复评论的id
     */
    private String replyid;
    /**
     * 主评论id
     */
    private String cmntid;
    /**
     * 评论人
     */
    private String memberid;
    /**
     * 评论时间
     */
    private Date createdate;
    /**
     * 评论人姓名
     */
    private String membername;
}
