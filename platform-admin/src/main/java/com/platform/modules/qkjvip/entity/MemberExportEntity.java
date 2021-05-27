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
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.emay.util.DateUtil;
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
 * 会员导出实体类
 * @author liuqianru
 * @date 2020/3/9 14:21
 */

@Data
public class MemberExportEntity implements Serializable {

    /**
     * 隐藏列，必须有，否则groupName会报错
     */
    @Excel(name = "隐藏列", orderNum = "0", isColumnHidden = true, fixedIndex = -1)
    @TableField(exist = false)
    private String memberHidden;

    @Excel(name = "会员昵称", orderNum = "1", width = 15, groupName = "必填信息", fixedIndex = 0)
    private String memberName;
    /*
     * 会员真实姓名
     */
    @Excel(name = "真实姓名", orderNum = "2", width = 15, groupName = "必填信息", fixedIndex = 1)
    private String realName;
    /*
     * 会员手机
     */
    @Excel(name = "电话", orderNum = "3", width = 15, groupName = "必填信息", fixedIndex = 2)
    private String mobile;
    /*
     * 会员性别
     */
    @Excel(name = "性别", orderNum = "4", width = 15,replace={"男_1","女_2","未知_3"}, groupName = "必填信息", fixedIndex = 3)
    private Integer sex;
    /*
     * 是否潜在客户(0否1是)
     */
    @Excel(name = "是否潜在客户", orderNum = "5", width = 15,replace={"否_0","是_1"}, groupName = "必填信息", fixedIndex = 4)
    private Integer isCustomers;
    /**
     * 服务号名称，会员渠道
     */
    @Excel(name = "会员渠道", orderNum = "6", width = 15, groupName = "必填信息", fixedIndex = 5)
    private String servicename;
    /**
     * 新增或导入时备注
     */
    @Excel(name = "备注", orderNum = "7", width = 15, groupName = "扩展信息", fixedIndex = 6)
    private String remark2;
    /*
     * 推荐人
     */
    @Excel(name = "推荐人", orderNum = "8", width = 15, groupName = "扩展信息", fixedIndex = 7)
    private String referrer;
    /*
     * 年龄
     */
    @Excel(name = "年龄", orderNum = "9", width = 15, groupName = "扩展信息", fixedIndex = 8)
    private Integer age;
    /*
     * 生日
     */
    @Excel(name = "生日", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "10", width = 15, groupName = "扩展信息", fixedIndex = 9)
    private Date birthday;
    /*
     * 会员邮件
     */
    @Excel(name = "邮件", orderNum = "11", width = 15, groupName = "扩展信息", fixedIndex = 10)
    private String email;
    /*
     * 加入时间/注册时间
     */
    @Excel(name = "注册时间", exportFormat = "yyyy-MM-dd HH:mm:ss", orderNum = "12", width = 15, groupName = "扩展信息", fixedIndex = 11)
    private Date regTime;
    /*
     * 会员类型
     */
    @Excel(name = "会员类型", orderNum = "13", width = 15, groupName = "扩展信息", fixedIndex = 12, replace={"门店会员_0","团购会员_1","大店会员_2","意见领袖_3","普通消费者_4","公众粉丝_5","工业游_6","零售_7","批发_8","员工_9","旅游特通_10","其他_20"})
    private String memberType;
    /*
     * 会员性质
     */
    @Excel(name = "会员性质", orderNum = "14", width = 15, groupName = "扩展信息",  fixedIndex = 13, replace={"企业单位_0","事业单位_1","政府机关_2","个人_3"})
    private String memberNature;
    /*
     * 会员来源
     */
    @Excel(name = "会员来源", orderNum = "15", width = 15, groupName = "扩展信息",  fixedIndex = 14, replace={"OMS门店_0","线下活动_1","线上活动_2","线上交易_3","线下交易_4","会员推荐_5","旅游景区_6","其他来源_20"})
    private String memberSource;
    /*
     * 行业类别
     */
    @Excel(name = "行业类别", orderNum = "16", width = 15, groupName = "扩展信息",  fixedIndex = 15)
    private String industryType;
    /*
     * 单位性质
     */
    @Excel(name = "单位性质", orderNum = "17", width = 15, groupName = "扩展信息",  fixedIndex = 16)
    private String unitProperty;
    /*
     * 公司名称
     */
    @Excel(name = "公司名称", orderNum = "18", width = 15, groupName = "扩展信息",  fixedIndex = 17)
    private String companyName;
    /*
     * 职位
     */
    @Excel(name = "职位", orderNum = "19", width = 15, groupName = "扩展信息",  fixedIndex = 18)
    private String jobTitle;
    /*
     * 推荐人部门
     */
    @Excel(name = "推荐人部门", orderNum = "20", width = 15, groupName = "扩展信息",  fixedIndex = 19)
    private String referrerDept;
    /*
     * 身份证号
     */
    @Excel(name = "身份证", orderNum = "21", width = 15, groupName = "扩展信息",  fixedIndex = 20)
    private String idcard;
}
