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

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 会员检索条件实体类
 * @author liuqianru
 * @date 2020/10/29 14:21
 */

@Data
public class MemberQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberid;
    private String membername;
    private String realname;
    private String mobile;
    private Integer startage;
    private Integer endage;
    private Integer sex;
    private String startregtime;
    private String endregtime;
    private String orgno;
    private String orguserid;
    private Double startamount1;
    private Double endamount1;
    private Double startamount3;
    private Double endamount3;
    private Double startamount6;
    private Double endamount6;
    private Double startamount12;
    private Double endamount12;
    private Integer startbuynum1;
    private Integer endbuynum1;
    private Integer startbuynum3;
    private Integer endbuynum3;
    private Integer startbuynum6;
    private Integer endbuynum6;
    private Integer startbuynum12;
    private Integer endbuynum12;
    private Double scanamountfrom1;
    private Double scanamountto1;
    private Double scanamountfrom3;
    private Double scanamountto3;
    private Double scanamountfrom6;
    private Double scanamountto6;
    private Double scanamountfrom12;
    private Double scanamountto12;
    private Double scanamountfromall;
    private Double scanamounttoall;
    private Integer scannumfrom1;
    private Integer scannumto1;
    private Integer scannumfrom3;
    private Integer scannumto3;
    private Integer scannumfrom6;
    private Integer scannumto6;
    private Integer scannumfrom12;
    private Integer scannumto12;
    private Integer scannumfromall;
    private Integer scannumtoall;
    private Double startunitprice;
    private Double endunitprice;
    private String startbirthday;
    private String endbirthday;
    private String areatype;
    private String membertype;
    private String activitytype;
    private String startlastactivitydate;
    private String endlastactivitydate;
    private String city;
    private String listorgno;  // 用户的权限部门
    private String currentmemberid;  // 当前登录用户id
    private String listmemberchannel;  // 用户的渠道权限
    private String searchmemberchannel; // 检索条件的渠道
    private List<MemberTagsQueryEntity> membertags;
    private Integer pageindex;
    private Integer pagesize;
    private Integer queryall;  // 0:分页 1：不分页
}
