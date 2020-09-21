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
package com.platform.modules.quartz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqianru
 * @date 2020/8/7 14:21
 */

@Data
@TableName("qkjvip_member_basic")
public class QrtzMemberBasicEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private String memberId;
    /*
     * 会员手机
     */
    private String cellphone;
    /*
    * 会员真实姓名
     */
    private String realname;
    /*
     * 类型
     */
    private String typename;
    /*
     * 等级
     */
    private String gradename;
    /*
     * 积分
     */
    private Integer integral;
    /*
     * 余额
     */
    private String realtotalprice;
    /*
     * 注册时间
     */
    private String createdate;
    /*
     * 会员性别
     */
    private Integer sex;
    /*
     * 所属专卖店
     */
    private String shopname;

    @Override
    public boolean equals(Object o) {
        return o instanceof QrtzMemberBasicEntity
                && this.cellphone != null && this.cellphone.equals(((QrtzMemberBasicEntity)o).cellphone)
                && this.shopname != null && this.shopname.equals(((QrtzMemberBasicEntity)o).shopname);
    }

}
