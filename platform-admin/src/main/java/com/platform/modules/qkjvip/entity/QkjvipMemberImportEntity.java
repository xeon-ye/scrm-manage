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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 实体
 *
 * @author liuqianru
 * @date 2020-09-21 15:46:52
 */
@Data
@TableName("QKJVIP_MEMBER_IMPORT")
public class QkjvipMemberImportEntity implements Serializable {
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
     * 会员名称(昵称)
     */
    @Excel(name = "会员昵称", orderNum = "1", width = 15, groupName = "必填信息", fixedIndex = 1)
    private String memberName;
    /**
     * 会员真实姓名
     */
    @Excel(name = "真实姓名", orderNum = "2", width = 15, groupName = "必填信息", fixedIndex = 2)
    private String realName;
    /**
     * 会员手机
     */
    @Excel(name = "电话", orderNum = "3", width = 15, groupName = "必填信息", fixedIndex = 3)
    private String mobile;
    /**
     * 会员邮件
     */
    @Excel(name = "邮件", orderNum = "12", width = 15, groupName = "扩展信息", fixedIndex = 12)
    private String email;
    /**
     * 性别(0男1女）
     */
    @Excel(name = "性别", orderNum = "4", width = 15, groupName = "必填信息", fixedIndex = 4, replace={"男_1","女_2","未知_3"})
    private Integer sex;
    /**
     * 年龄区间
     */
    @Excel(name = "年龄", orderNum = "10", width = 15, groupName = "扩展信息", fixedIndex = 10)
    private Integer age;
    /**
     * 生日
     */
    @Excel(name = "生日", orderNum = "11", width = 15, groupName = "扩展信息", fixedIndex = 11)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date birthday;
    /**
     * 会员类型
     */
    @Excel(name = "会员类型", orderNum = "14", width = 15, groupName = "扩展信息", fixedIndex = 14, replace={"门店会员_0","团购会员_1","大店会员_2","意见领袖_3","普通消费者_4","公众粉丝_5","工业游_6","零售_7","批发_8","员工_9","旅游特通_10","其他_20"})
    private String memberType;
    /**
     * 会员性质
     */
    @Excel(name = "会员性质", orderNum = "15", width = 15, groupName = "扩展信息", fixedIndex = 15, replace={"企业单位_0","事业单位_1","政府机关_2","个人_3"})
    private String memberNature;
    /**
     * 会员等级
     */
    @Excel(name = "会员等级", orderNum = "16", width = 15, groupName = "扩展信息", fixedIndex = 16, replace={"普通会员_10","人之德级_20","家之德级_30","国之德级_40","天之德级_50","德天下级_60"})
    private String memberLevel;
    /**
     * 会员来源
     */
    @Excel(name = "会员来源", orderNum = "17", width = 15, groupName = "扩展信息", fixedIndex = 17, replace={"OMS门店_0","线下活动_1","线上活动_2","线上交易_3","线下交易_4","会员推荐_5","旅游景区_6","其他来源_20"})
    private String memberSource;
    /**
     * 行业类别
     */
    @Excel(name = "行业类别", orderNum = "18", width = 15, groupName = "扩展信息", fixedIndex = 18)
    private String industryType;
    /**
     * 单位性质
     */
    @Excel(name = "单位性质", orderNum = "19", width = 15, groupName = "扩展信息", fixedIndex = 19)
    private String unitProperty;
    /**
     * 公司名称
     */
    @Excel(name = "公司名称", orderNum = "20", width = 15, groupName = "扩展信息", fixedIndex = 20)
    private String companyName;
    /**
     * 职位
     */
    @Excel(name = "职位", orderNum = "21", width = 15, groupName = "扩展信息", fixedIndex = 21)
    private String jobTitle;
    /**
     * 加入时间/注册时间
     */
    @Excel(name = "注册时间", orderNum = "13", width = 15, groupName = "扩展信息", fixedIndex = 13)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date regTime;
    /**
     * 推荐人部门
     */
    @Excel(name = "推荐人部门", orderNum = "22", width = 15, groupName = "扩展信息", fixedIndex = 22)
    private String referrerDept;
    /**
     * 推荐人
     */
    @Excel(name = "推荐人", orderNum = "9", width = 15, groupName = "扩展信息", fixedIndex = 9)
    private String referrer;
    /**
     * 所属办事处
     */
    private String orgNo;
    /**
     * 所属业务员
     */
    private String orgUserid;
    /**
     * 是否潜在客户(0否1是)
     */
    @Excel(name = "是否潜在客户", orderNum = "5", width = 15,replace={"否_0","是_1"}, groupName = "必填信息", fixedIndex = 5)
    private Integer isCustomers;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 余额
     */
    private BigDecimal realtotalprice;
    /**
     * 所属店铺
     */
    @Excel(name = "所属店铺", orderNum = "7", width = 15, groupName = "必填信息", fixedIndex = 7)
    private String shopname;
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
     * 身份证
     */
    @Excel(name = "身份证", orderNum = "23", width = 15, groupName = "扩展信息", fixedIndex = 23)
    private String idcard;
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
     * 服务号名称，会员渠道
     */
    @Excel(name = "会员渠道", orderNum = "6", width = 15, groupName = "必填信息", fixedIndex = 6)
    private String servicename;
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
     * 新增或导入时备注
     */
    @Excel(name = "备注", orderNum = "8", width = 15, groupName = "扩展信息", fixedIndex = 8)
    private String remark2;
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
     * 标签数组
     */
    @TableField(exist = false)
    private List<MemberTagsQueryEntity> membertags;
}
