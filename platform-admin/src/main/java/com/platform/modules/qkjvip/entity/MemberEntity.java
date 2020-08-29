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

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "会员昵称", orderNum = "0", width = 15)
    @NotBlank(message = "会员名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String memberName;
    /*
    * 会员真实姓名
     */
    @Excel(name = "真实姓名", orderNum = "1", width = 15)
    private String realName;
    /*
     * 会员手机
     */
    @Excel(name = "电话", orderNum = "2", width = 15)
    @NotBlank(message = "会员手机号不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String mobile;
    /*
     * 会员邮件
     */
    @Excel(name = "邮件", orderNum = "3", width = 15)
    private String email;
    /*
     * 会员性别
     */
    @Excel(name = "性别", orderNum = "4", width = 15,replace={"男_1","女_2","未知_3"})
    private Integer sex;
    /*
     * 年龄
     */
    @Excel(name = "年龄", orderNum = "5", width = 15)
    private Integer age;
    /*
     * 生日
     */
    @Excel(name = "生日", orderNum = "6", width = 15)
    private String birthday;
    /*
     * 会员类型
     */
    @Excel(name = "会员类型", orderNum = "7", width = 15,replace={"门店会员_0","团购会员_1","大店会员_2","意见领袖_3","普通消费者_4","公众粉丝_5","其他_6"})
    private String memberType;
    /*
     * 会员性质
     */
    @Excel(name = "会员性质", orderNum = "8", width = 15,replace={"企业单位_0","事业单位_1","政府机关_2","个人_3"})
    private String memberNature;
    /*
     * 会员等级
     */
    @Excel(name = "会员等级", orderNum = "9", width = 15,replace={"普通会员_0","三星会员_1","四星会员_2","五星会员_3","六星会员_4"})
    private String memberLevel;
    /*
     * 会员来源
     */
    @Excel(name = "会员来源", orderNum = "10", width = 15,replace={"OMS门店_0","线下活动_1","线上活动_2","线上交易_3","线下交易_4","会员推荐_5","旅游景区_6","其他来源_7"})
    private String memberSource;
    /*
     * 行业类别
     */
    @Excel(name = "行业类别", orderNum = "11", width = 15)
    private String industryType;
    /*
     * 单位性质
     */
    @Excel(name = "单位性质", orderNum = "12", width = 15)
    private String unitProperty;
    /*
     * 公司名称
     */
    @Excel(name = "公司名称", orderNum = "13", width = 15)
    private String companyName;
    /*
     * 职位
     */
    @Excel(name = "职位", orderNum = "14", width = 15)
    private String jobTitle;
    /*
     * 加入时间/注册时间
     */
    @Excel(name = "注册时间", orderNum = "15", width = 15)
    private String regTime;
    /*
     * 推荐人部门
     */
    @Excel(name = "推荐人部门", orderNum = "16", width = 15)
    private String referrerDept;
    /*
     * 推荐人
     */
    @Excel(name = "推荐人", orderNum = "17", width = 15)
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
    @Excel(name = "是否潜在客户", orderNum = "18", width = 15,replace={"否_0","是_1"})
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
    /*
    * 积分
     */
    @Excel(name = "积分", orderNum = "19", width = 15)
    private Integer integral;
    /*
    * 余额
     */
    @Excel(name = "余额", orderNum = "20", width = 15)
    private Double realtotalprice;
    /*
    * 所属店铺
     */
    @Excel(name = "所属店铺", orderNum = "21", width = 15)
    private String shopname;
    /*
    * 微信id
     */
    @Excel(name = "微信id", orderNum = "22", width = 15)
    private String openid;
    /*
    * 微信头像
     */
    @Excel(name = "微信头像", orderNum = "23", width = 15)
    private String headImgUrl;
    /*
    * 会员标签
     */
    private String memberLabel;
    /*
    * 近一个月购买金额
     */
    private Double amount1;
    /*
     * 近三个月购买金额
     */
    private Double amount3;
    /*
     * 近半年购买金额
     */
    private Double amount6;
    /*
     * 近一年购买金额
     */
    private Double amount12;
    /*
    * 近一个月购买次数
     */
    private Integer buynum1;
    /*
     * 近三个月购买次数
     */
    private Integer buynum3;
    /*
     * 近半年购买次数
     */
    private Integer buynum6;
    /*
     * 近一年购买次数
     */
    private Integer buynum12;
    /*
     * 近一年客单价
     */
    private Double unitprice;

    /*
     * 最后购买日期
     */
    private String lastbuydate;

    @TableField(exist = false)
    private String orgUsername;
    /**
     * 会员标签列表
     */
    @TableField(exist = false)
    private List<String> labelIdList;

    @TableField(exist = false)
    private Integer ageFrom;

    @TableField(exist = false)
    private Integer ageTo;

    @TableField(exist = false)
    private String regTimeFrom;

    @TableField(exist = false)
    private String regTimeTo;

    @TableField(exist = false)
    private String page;

    @TableField(exist = false)
    private String limit;

    @TableField(exist = false)
    private String buyPeriod;

    @TableField(exist = false)
    private Double amountfrom;
    @TableField(exist = false)
    private Double amountto;

    @TableField(exist = false)
    private Integer buynumfrom;
    @TableField(exist = false)
    private Integer buynumto;

    @TableField(exist = false)
    private Double unitpricefrom;
    @TableField(exist = false)
    private Double unitpriceto;

    @TableField(exist = false)
    private String lastbuydatefrom;

    @TableField(exist = false)
    private String lastbuydateto;


}
