/*
 * 项目名称:platform-plus
 * 类名称:MemberVisitEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/13 10:14            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.validator.group.AddGroup;
import com.platform.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * MemberVisitEntity
 *
 * @author liuqianru
 * @date 2020/3/13 10:14
 */
@Data
@TableName("qkjvip_member_visit")
public class MemberVisitEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String uuid;
    /**
     * 会员id
     */
    private String memberId;
    /*
    * 联系方式
     */
    @NotBlank(message = "联系方式不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String mobile;
    /*
    * 拜访时间
     */
    @NotBlank(message = "拜访时间不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String visitTime;
    /**
     * 拜访方式
     */
    @NotBlank(message = "拜访方式不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String visitType;
    /**
     * 拜访人
     */
    @NotBlank(message = "拜访人不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String person;
    /**
     * 结果反馈
     */
    private String content;
    /**
     * 是否生成订单
     */
    private Integer isOrder;
    /**
     * 待处理问题
     */
    private String question;
    /**
     * 拜访周期
     */
    private String visitCycle;
    /*
     * 添加人
     */
    private String addUser;
    /*
     * 添加部门
     */
    private String addDept;
    /*
     * 添加时间
     */
    private Date addTime;
}
