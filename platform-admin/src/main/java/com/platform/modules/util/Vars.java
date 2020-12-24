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
	//数据清洗url end
	//会员检索url start
	public static String MEMBER_GETLIST_URL = "http://api.scrm.qkj.com.cn/api/Member/GetMemberList";
	public static String MEMBER_GETINFO_URL = "http://api.scrm.qkj.com.cn/api/Member/GetMemberEntity";
	//会员检索url end
	//积分发放url start
	public static String MEMBER_INTEGRAL_SEND_URL = "http://api.scrm.qkj.com.cn/api/Integral/SetBatchMemberIntegral";
	//积分发放url end
}
