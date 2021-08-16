/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberImportEntity.java
 * 包名称:com.platform.modules.qkjvip.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-21 15:46:52        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 会员导入实体
 *
 * @author liuqianru
 * @date 2020-09-21 15:46:52
 */
@Data
@TableName("QKJVIP_MEMBER_IMPORT")
public class QkjvipMemberImportBcEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private String memberId;
    /**
     * 隐藏列，必须有，否则groupName会报错
     */
    @Excel(name = "隐藏列", orderNum = "0", isColumnHidden = false, fixedIndex = 0)
    @TableField(exist = false)
    private String memberHidden;
    /**
     * 会员手机
     */
    @Excel(name = "电话", orderNum = "1", width = 15, groupName = "必填信息", fixedIndex = 1)
    private String mobile;
    /**
     * 服务号名称，会员渠道
     */
    @Excel(name = "会员渠道", orderNum = "2", width = 15, groupName = "必填信息", fixedIndex = 2)
    private String servicename;
    /*
     * 标签-圈层
     */
    @TableField(exist = false)
    @Excel(name = "圈层", orderNum = "3", width = 15, groupName = "必填信息", fixedIndex = 3)
    private String tag3;
    /**
     * 会员名称(昵称)
     */
    @Excel(name = "会员昵称", orderNum = "4", width = 15, groupName = "扩展信息", fixedIndex = 4)
    private String memberName;
    /**
     * 会员真实姓名
     */
    @Excel(name = "真实姓名", orderNum = "5", width = 15, groupName = "扩展信息", fixedIndex = 5)
    private String realName;
    /**
     * 性别(0男1女）
     */
    @Excel(name = "性别", orderNum = "6", width = 15, groupName = "扩展信息", fixedIndex = 6, replace={"男_1","女_2","未知_3"})
    private Integer sex;
    /**
     * 是否潜在客户(0否1是)
     */
    @Excel(name = "是否潜在客户", orderNum = "7", width = 15,replace={"否_0","是_1"}, groupName = "扩展信息", fixedIndex = 7)
    private Integer isCustomers;

    /**
     * 新增或导入时备注
     */
    @Excel(name = "备注", orderNum = "8", width = 15, groupName = "扩展信息", fixedIndex = 8)
    private String remark2;
    /**
     * 推荐人
     */
    @Excel(name = "推荐人", orderNum = "9", width = 15, groupName = "扩展信息", fixedIndex = 9)
    private String referrer;
    /**
     * 年龄区间
     */
    @Excel(name = "年龄", orderNum = "10", width = 15, groupName = "扩展信息", fixedIndex = 10)
    private Integer age;
    /**
     * 生日
     * 不可修改类型，否则导入的时间为null
     */
    @Excel(name = "生日", importFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "11", width = 15, groupName = "扩展信息", fixedIndex = 11)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String birthday;
    /**
     * 会员邮件
     */
    @Excel(name = "邮件", orderNum = "12", width = 15, groupName = "扩展信息", fixedIndex = 12)
    private String email;
    /**
     * 加入时间/注册时间
     * 不可修改类型，否则导入的时间为null
     */
    @Excel(name = "注册时间", importFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "13", width = 15, groupName = "扩展信息", fixedIndex = 13)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String regTime;
    /**
     * 会员性质
     */
    @Excel(name = "会员性质", orderNum = "14", width = 15, groupName = "扩展信息", fixedIndex = 14, replace={"企业单位_0","事业单位_1","政府机关_2","消费者_3","核心团购客户_4","团购客户_5","核心终端_6","终端_7","酒店_8","核心酒店_9","核心消费者_10","陪同人员_11", "经销商_12"})
    private String memberNature;
    /**
     * 会员来源
     */
    @Excel(name = "会员来源", orderNum = "15", width = 15, groupName = "扩展信息", fixedIndex = 15, replace={"OMS门店_0","线下活动_1","线上活动_2","线上交易_3","线下交易_4","会员推荐_5","旅游景区_6","回厂游_7", "其他来源_20"})
    private String memberSource;
    /**
     * 行业类别
     */
    @Excel(name = "行业类别", orderNum = "16", width = 15, groupName = "扩展信息", fixedIndex = 16)
    private String industryType;
    /**
     * 单位性质
     */
    @Excel(name = "单位性质", orderNum = "17", width = 15, groupName = "扩展信息", fixedIndex = 17)
    private String unitProperty;
    /**
     * 公司名称
     */
    @Excel(name = "公司名称", orderNum = "18", width = 15, groupName = "扩展信息", fixedIndex = 18)
    private String companyName;
    /**
     * 职位
     */
    @Excel(name = "职位", orderNum = "19", width = 15, groupName = "扩展信息", fixedIndex = 19)
    private String jobTitle;
    /**
     * 推荐人部门
     */
    @Excel(name = "推荐人部门", orderNum = "20", width = 15, groupName = "扩展信息", fixedIndex = 20)
    private String referrerDept;
    /**
     * 身份证
     */
    @Excel(name = "身份证", orderNum = "21", width = 15, groupName = "扩展信息", fixedIndex = 21)
    private String idcard;
    /*
     * 标签-省
     */
    @TableField(exist = false)
    @Excel(name = "省", orderNum = "22", width = 15, groupName = "会员标签", fixedIndex = 22)
    private String tag1;
    /*
     * 标签-市
     */
    @TableField(exist = false)
    @Excel(name = "市", orderNum = "23", width = 15, groupName = "会员标签", fixedIndex = 23)
    private String tag2;
    /*
     * 标签-消费者群体
     */
    @TableField(exist = false)
    @Excel(name = "消费者群体", orderNum = "24", width = 15, groupName = "会员标签", fixedIndex = 24)
    private String tag4;
    /**
     * 会员类型
     */
    private String memberType;
    /**
     * 所属办事处
     */
    private String orgNo;
    /**
     * 所属业务员
     */
    private String orgUserid;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 余额
     */
    private BigDecimal realtotalprice;
    /**
     * 微信id
     */
    private String openid;
    /**
     * 微信头像
     */
    private String headImgUrl;
    /**
     * 添加人
     */
    private String addUser;
    /**
     * add_dept
     */
    private String addDept;
    /**
     * add_time
     */
    private Date addTime;
    /**
     * 会员标签
     */
    private String memberLabel;
    /**
     * 近一个月购买金额
     */
    private Double amount1;
    /**
     * amount3
     */
    private Double amount3;
    /**
     * amount6
     */
    private Double amount6;
    /**
     * amount12
     */
    private Double amount12;
    /**
     * 近一个月购买次数
     */
    private Integer buynum1;
    /**
     * buynum3
     */
    private Integer buynum3;
    /**
     * buynum6
     */
    private Integer buynum6;
    /**
     * buynum12
     */
    private Integer buynum12;
    /**
     * 近一年客单价
     */
    private Double unitprice;
    /**
     * 最后购买日期
     */
    private Date lastbuydate;
    /**
     * 是否是手动导入的会员(1：是）
     */
    private Integer offlineflag;
    /**
     * 是否关注
     */
    private Integer isunsubscribe;
    /**
     * 取消关注时间
     */
    private Date unsubscribetime;
    /**
     * 用户的语言，简体中文为zh_CN
     */
    private String language;
    /**
     * 用户所在城市
     */
    private String city;
    /**
     * 用户所在省份
     */
    private String province;
    /**
     * 用户所在国家
     */
    private String country;
    /**
     * 用户关注时间。如果用户曾多次关注，则取最后关注时间
     */
    private Date subscribetime;
    /**
     * UnionId
     */
    private String unionid;
    /**
     * 用户更新时间
     */
    private Date updatetime;
    /**
     * AppId
     */
    private String appid;
    /**
     * 清洗备注
     */
    private String remark;
    /**
     * 清洗失败的标识，失败则为1
     */
    private Integer isfail;
    /**
     * 会员清洗id
     */
    private String crmMemberid;
    /**
     * 会员渠道号
     */
    private Integer memberchannel;
    /**
     * 是否同步到青稞荟
     */
    private Integer toqkh;
    /**
     * 会员等级有效期
     */
    private String validstarttime;
    /**
     * 会员等级有效期
     */
    private String validendtime;
    /**
     * 导入批次号
     */
    private String batchno;
    /**
     * 标签数组
     */
    @TableField(exist = false)
    private List<MemberTagsQueryEntity> membertags;
    /**
     * 所属部门
     */
    @TableField(exist = false)
    private List<QkjvipMemberDatadepEntity> deptlist;
    /**
     * 所属业务员
     */
    @TableField(exist = false)
    private List<QkjvipMemberOrguserEntity> userlist;
}
