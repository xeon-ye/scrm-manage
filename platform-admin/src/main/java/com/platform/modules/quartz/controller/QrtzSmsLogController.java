/*
 * 项目名称:platform-plus
 * 类名称:QrtzSmsLogController.java
 * 包名称:com.platform.modules.quartz.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021/6/1 15:48            liuqianru    初版做成
 *
 */
package com.platform.modules.quartz.controller;

import cn.emay.Example;
import cn.emay.ResultModel;
import cn.emay.RetrieveReportExample;
import cn.emay.eucp.inter.http.v1.dto.response.ReportResponse;
import cn.emay.util.DateUtil;
import cn.emay.util.JsonHelper;
import cn.emay.util.Md5;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.Constant;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.StringUtils;
import com.platform.modules.quartz.entity.QrtzLastUpdateTimeEntity;
import com.platform.modules.quartz.service.QrtzLastUpdateTimeService;
import com.platform.modules.sys.entity.SmsConfig;
import com.platform.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QrtzSmsLogController
 *
 * @author liuqianru
 * @date 2021/6/1 15:48
 */
public class QrtzSmsLogController {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private QrtzLastUpdateTimeService qrtzLastUpdateTimeService;

    /**
     * 定时获取短信发送状态报告
     *
     */
    @SysLog("获取短信发送状态报告")
    @RequestMapping("/getSmsReport")
    public void getSmsReport() {
        //获取云存储配置信息
        SmsConfig config = sysConfigService.getConfigObject(Constant.SMS_CONFIG_KEY, SmsConfig.class);
        if (StringUtils.isNullOrEmpty(config)) {
            throw new BusinessException("请先配置短信平台信息");
        }
        if (StringUtils.isNullOrEmpty(config.getHost())) {
            throw new BusinessException("请先配置短信平台网址");
        }
        if (StringUtils.isNullOrEmpty(config.getSecretkey())) {
            throw new BusinessException("请先配置短信平台密钥");
        }
        if (StringUtils.isNullOrEmpty(config.getAppid())) {
            throw new BusinessException("请先配置短信平台appid");
        }
        //青稞酒
        // appId
        String appId = config.getAppid();// 请联系销售，或者在页面中 获取
        // 密钥
        String secretKey = config.getSecretkey();// 请联系销售，或者在页面中 获取
        // 接口地址
        String host = config.getHost();// 请联系销售获取
        // 编码
        String encode = "UTF-8";
        // 时间戳
        String timestamp = DateUtil.toString(new Date(), "yyyyMMddHHmmss");
        // 签名
        String sign = Md5.md5((appId + secretKey + timestamp).getBytes());
        // 开始时间
        String startTime = DateUtil.toString(new Date(), "yyyyMMddHHmmss");
        // 结束时间
        String endTime = DateUtil.toString(new Date(), "yyyyMMddHHmmss");
        String smsId = "123123123,321321321";

        Map params = new HashMap();
        params.put("type", 5);
        List<QrtzLastUpdateTimeEntity> updateTimeList = qrtzLastUpdateTimeService.queryAll(params);
        QrtzLastUpdateTimeEntity updateTimeEntity = new QrtzLastUpdateTimeEntity();
        updateTimeEntity = updateTimeList.get(0);
        if (updateTimeList.get(0).getLastDatetime() == null) {
            startTime = "2017-01-01";
//            DateUtils.addDateMinutes(endTime, -5);
        } else {
//            updateTime = sdf.format(updateTimeList.get(0).getLastDatetime());
        }

        RetrieveReportExample.getReport(appId, host, encode, timestamp, sign, startTime, endTime, smsId);

    }
}
