package com.platform.modules.util;

public class Vars {

	//public static String TO_USER = "01451537149439";
	public static String TO_USER = "01672211281464";
	public static String TO_PARTY = "";
	public static String AGENT_ID = "206338972";
	public static String SENDER = "01451537149439";
	public static String CID = "";//cid需要通过jsapi获取，具体详情请查看开放平台文档--->客户端文档--->会话

	public static String APP_KEY = "open100010fb1n94ev9";

	//AES加密用常量start
	public static String ENCODE = "UTF-8";
	// 密钥
	public static String AESKEY = "2139226343519743";
	//向量
	public static String VECTOR = "4370627107694550";
	// 加密算法
	public static String ALGORITHM = "AES/CBC/NoPadding";
	//PWD
	public static String SECRETKEY = "5E2#BD40&FAE^7";
	//AES加密用常量end

	//数据清洗url start
	public static String MEMBER_ADD_URL = "http://api.scrm.qkj.com.cn/api/Member/AddMember";
	public static String MEMBER_IMPORT_URL = "http://api.scrm.qkj.com.cn/api/Member/BatchImportMember";
	public static String MEMBER_UPDATE_URL = "http://api.scrm.qkj.com.cn/api/Member/UpdateMember";
	public static String MEMBER_DELETE_URL = "http://api.scrm.qkj.com.cn/api/Member/DeleteMemberList";
	public static String MEMBER_IMPORTINFO_URL = "http://api.scrm.qkj.com.cn/api/Member/GetMemberImportInfo";

	//数据清洗url end
	//会员检索url start
	public static String MEMBER_GETLIST_URL = "http://api.scrm.qkj.com.cn/api/Member/GetMemberList";
	public static String MEMBER_GETINFO_URL = "http://api.scrm.qkj.com.cn/api/Member/GetMemberEntity";
	public static String MEMBER_GETLEVEL_URL = "http://api.scrm.qkj.com.cn/api/MemberLevel/GetMemberLevel";
	public static String MEMBER_SETLEVEL_URL = "http://api.scrm.qkj.com.cn/api/MemberLevel/SetMemberLevel";
	public static String MEMBER_GETLEVEL_FROM_SCAN = "http://api.scrm.qkj.com.cn/api/MemberLevel/ReceiveMemberSendLevel";
	public static String MEMBER_VALIDCHECK_FROM_SCAN = "http://api.scrm.qkj.com.cn/api/MemberLevel/ValidateMemberReceiveLevel";
	//会员检索url end
	//积分url
	public static String MEMBER_INTEGRAL_SEND_URL = "http://api.scrm.qkj.com.cn/api/Integral/SetBatchMemberIntegral";
	// 推送积分规则
	public static String INTEGRAL_RULE_PUSH_URL = "https://api.mall.qkj.com.cn/MobileApi/Coupon/SetIntegralRule";
	// 获取公众号
	public static String APPID_GETLIST_URL = "http://scrm-wxcb.ym.qkj.com.cn/WeiXinPublish/GetPublishList";
	//群发图文消息接口
	public static String MESSAGE_SEND = "http://scrm-wxcb.ym.qkj.com.cn/WeiXinMsg/MassMultiNewsMessage";
	// 微信url接口
	public static String APPLETS_URL_GET = "http://scrm-wxcb.ym.qkj.com.cn/MiniProgram/GetTempUrlLink";
	//优惠券查询
	public static String MEMBER_CPON_LIST_URl = "http://api.mall.qkj.com.cn/MobileAPI/Coupon/GetCouponList";
	//优惠券发送
	public static String MEMBER_CPON_SEND_URl = "http://api.scrm.qkj.com.cn/api/Coupon/SendMemberCoupon";
	/*会员画像start*/
	//性别统计
	public static String MEMBER_PORTRAIT_SEX_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberSexReport";
	//年龄统计
	public static String MEMBER_PORTRAIT_AGE_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberAgeReport";
	//地区统计
	public static String MEMBER_PORTRAIT_AREA_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberCityReport";
	// 高频统计-按渠道
	public static String MEMBER_PORTRAIT_HF_CHANNEL_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberChannelRateReport";
	// 高频统计-按地区
	public static String MEMBER_PORTRAIT_HF_CITY_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberCityRateReport";
	// 高价值统计-按渠道
	public static String MEMBER_PORTRAIT_HV_CHANNEL_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberChannelAmountReport";
	// 高价值统计-按地区
	public static String MEMBER_PORTRAIT_HV_CITY_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberCityAmountReport";
	// 价值区间统计
	public static String MEMBER_PORTRAIT_VALUERANGE_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberAmountRangeReport";
	// 新增趋势统计
	public static String MEMBER_PORTRAIT_ADDTREND_URl = "http://api.scrm.qkj.com.cn/api/Report/GetMemberTrendReport";
	/*会员画像end*/
	// 消费者首页新闻
	public static String NEWS_LIST_URl = "http://api.scrm.qkj.com.cn/api/News/GetNewsList";
	public static String NEWS_INFO_URl = "http://api.scrm.qkj.com.cn/api/News/GetNewsInfo";
	public static String PRODUCT_LIST_URL = "https://api.mall.qkj.com.cn/MobileAPI/product/GetIntegralProductList";
	// 分享得积分
	public static String CONTENT_SHARE_URL = "http://api.scrm.qkj.com.cn/api/Integral/SetCRMMemberIntegral";
	/**
	 * 订单
	 */
	//订单添加
	public static String MEMBER_ORDER_ORDER_ADD = "http://api.scrm.qkj.com.cn/api/Order/SubmitOrder";
	//订单添加
	public static String MEMBER_ORDER_ORDER_MDYSTATUS = "http://api.scrm.qkj.com.cn/api/Order/SetOrderStatus";
	//订单列表
	public static String MEMBER_ORDER_ORDER_LIST = "http://api.scrm.qkj.com.cn/api/Order/GetOrderList";
	//订单详情
	public static String MEMBER_ORDER_ORDER_LISTDETILE = "http://api.scrm.qkj.com.cn/api/Order/GetOrderDetail";
	//订单删除
	public static String MEMBER_ORDER_ORDER_DELTET = "http://api.scrm.qkj.com.cn/api/Order/AbandonedOrder";
}
