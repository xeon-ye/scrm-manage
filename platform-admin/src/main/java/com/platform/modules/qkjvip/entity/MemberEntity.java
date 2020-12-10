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
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date birthday;
    /*
     * 会员类型
     */
    @Excel(name = "会员类型", orderNum = "8", width = 15,replace={"门店会员_0","团购会员_1","大店会员_2","意见领袖_3","普通消费者_4","公众粉丝_5","工业游_6","零售_7","批发_8","员工_9","旅游特通_10","其他_20"})
    private String memberType;
    /*
     * 会员性质
     */
    @Excel(name = "会员性质", orderNum = "9", width = 15,replace={"企业单位_0","事业单位_1","政府机关_2","个人_3"})
    private String memberNature;
    /*
     * 会员等级
     */
    @Excel(name = "会员等级", orderNum = "10", width = 15,replace={"普通会员_0","三星会员_1","四星会员_2","五星会员_3","六星会员_4","银牌会员_5"})
    private String memberLevel;
    /*
     * 会员来源
     */
    @Excel(name = "会员来源", orderNum = "11", width = 15,replace={"OMS门店_0","线下活动_1","线上活动_2","线上交易_3","线下交易_4","会员推荐_5","旅游景区_6","其他来源_20"})
    private String memberSource;
    /*
     * 行业类别
     */
    @Excel(name = "行业类别", orderNum = "12", width = 15)
    private String industryType;
    /*
     * 单位性质
     */
    @Excel(name = "单位性质", orderNum = "13", width = 15)
    private String unitProperty;
    /*
     * 公司名称
     */
    @Excel(name = "公司名称", orderNum = "14", width = 15)
    private String companyName;
    /*
     * 职位
     */
    @Excel(name = "职位", orderNum = "15", width = 15)
    private String jobTitle;
    /*
     * 加入时间/注册时间
     */
    @Excel(name = "注册时间", orderNum = "16", width = 15)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date regTime;
    /*
     * 推荐人部门
     */
    @Excel(name = "推荐人部门", orderNum = "17", width = 15)
    private String referrerDept;
    /*
     * 推荐人
     */
    @Excel(name = "推荐人", orderNum = "18", width = 15)
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
    @Excel(name = "是否潜在客户", orderNum = "19", width = 15,replace={"否_0","是_1"})
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
    @Excel(name = "积分", orderNum = "20", width = 15)
    private Integer integral;
    /*
    * 余额
     */
    @Excel(name = "余额", orderNum = "21", width = 15)
    private Double realtotalprice;
    /*
    * 所属店铺
     */
    @Excel(name = "所属店铺", orderNum = "22", width = 15)
    private String shopname;
    /*
    * 微信id
     */
    private String openid;
    /*
    * 微信头像
     */
    private String headImgUrl;
    /*
    * 会员标签
     */
    @TableField(fill = FieldFill.UPDATE)
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
    /*
    * 身份证号
     */
    @Excel(name = "身份证", orderNum = "23", width = 15)
    private String idcard;
    /*
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
    @Excel(name = "会员渠道", orderNum = "7", width = 15)
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
    @Excel(name = "备注", orderNum = "24", width = 15)
    private String remark2;

    /**
     * 所属人姓名
     */
    @TableField(exist = false)
    private String orgUsername;

    /**
     * 年龄
     */
    @TableField(exist = false)
    private Integer ageFrom;

    @TableField(exist = false)
    private Integer ageTo;

    /**
     * 注册时间
     */
    @TableField(exist = false)
    private String regTimeFrom;

    @TableField(exist = false)
    private String regTimeTo;

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
    private String activitydatefrom;

    @TableField(exist = false)
    private String activitydateto;

    /**
     * 最后一次参加活动日期
     */
    @TableField(exist = false)
    private String lastactivitydate;
    /**
     * 最后一次参加活动标题
     */
    @TableField(exist = false)
    private String activityTitle;
    /**
     * 活动类型
     */
    @TableField(exist = false)
    private String activeType;

    /**
     * 地区类型
     */
    @TableField(exist = false)
    private String areaType;

    /**
     * 标签数组
     */
    @TableField(exist = false)
    private List<MemberTagsQueryEntity> membertags;

}
