/*
 * 项目名称:platform-plus
 * 类名称:MemberEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/3/9 14:21            liuqianru    初版做成
 *
 */
package com.platform.modules.qkjvip.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.validator.group.AddGroup;
import com.platform.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author liuqianru
 * @date 2020/3/9 14:21
 */

@Data
@TableName("qkjvip_member")
public class MemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String memberId;
    /**
     * 会员名称
     */
    @NotBlank(message = "会员名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String memberName;
    /*
    * 会员真实姓名
     */
    private String realName;
    /*
     * 会员手机
     */
    @NotBlank(message = "会员手机号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String mobile;
    /*
     * 会员邮件
     */
    private String email;
    /*
     * 会员性别
     */
    private Integer sex;
    /*
     * 年龄区间
     */
    private String ageSection;
    /*
     * 生日
     */
    private String birthday;
    /*
     * 会员类型
     */
    private String memberType;
    /*
     * 会员性质
     */
    private String memberNature;
    /*
     * 会员等级
     */
    private String memberLevel;
    /*
     * 会员来源
     */
    private String memberSource;
    /*
     * 行业类别
     */
    private String industryType;
    /*
     * 单位性质
     */
    private String unitProperty;
    /*
     * 公司名称
     */
    private String companyName;
    /*
     * 职位
     */
    private String jobTitle;
    /*
     * 加入时间/注册时间
     */
    private String regTime;
    /*
     * 推荐人部门
     */
    private String referrerDept;
    /*
     * 推荐人
     */
    private String referrer;
    /*
     * 所属办事处
     */
    private String orgNo;
    /*
     * 所属业务人员
     */
    private String orgUserid;
    /*
     * 是否潜在客户(0否1是)
     */
    private Integer isCustomers;
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

    @TableField(exist = false)
    private String orgUsername;
}
