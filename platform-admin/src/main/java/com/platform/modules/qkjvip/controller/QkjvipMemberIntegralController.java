/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberIntegralController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-12-21 15:18:24        liuqianru     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.entity.QkjvipMemberIntegralEntity;
import com.platform.modules.qkjvip.entity.QkjvipMemberIntegraluserEntity;
import com.platform.modules.qkjvip.service.QkjvipMemberIntegralService;
import com.platform.modules.qkjvip.service.QkjvipMemberIntegraluserService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2020-12-21 15:18:24
 */
@RestController
@RequestMapping("qkjvip/memberintegral")
public class QkjvipMemberIntegralController extends AbstractController {
    @Autowired
    private QkjvipMemberIntegralService qkjvipMemberIntegralService;
    @Autowired
    private QkjvipMemberIntegraluserService qkjvipMemberIntegraluserService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:memberintegral:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberIntegralEntity> list = qkjvipMemberIntegralService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:memberintegral:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjvipMemberIntegralService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:memberintegral:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberIntegralEntity qkjvipMemberIntegral = qkjvipMemberIntegralService.getById(id);

        Map<String, Object> map=new HashMap<String,Object>();
        map.put("integralId",id);
        qkjvipMemberIntegral.setMemberlist(qkjvipMemberIntegraluserService.queryAll(map));

        return RestResponse.success().put("memberintegral", qkjvipMemberIntegral);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberIntegral qkjvipMemberIntegral
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:memberintegral:save")
    public RestResponse save(@RequestBody QkjvipMemberIntegralEntity qkjvipMemberIntegral) {
        qkjvipMemberIntegral.setAddUser(getUserId());
        qkjvipMemberIntegral.setAddDept(getOrgNo());
        qkjvipMemberIntegral.setAddTime(new Date());
        qkjvipMemberIntegral.setSendStatus(0);  //默认0
        qkjvipMemberIntegralService.add(qkjvipMemberIntegral);

        //批量保存会员
        this.saveIntegralUser(qkjvipMemberIntegral.getId(), qkjvipMemberIntegral.getMemberlist());

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberIntegral qkjvipMemberIntegral
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:memberintegral:update")
    public RestResponse update(@RequestBody QkjvipMemberIntegralEntity qkjvipMemberIntegral) {
        qkjvipMemberIntegral.setLmUser(getUserId());
        qkjvipMemberIntegral.setLmDept(getOrgNo());
        qkjvipMemberIntegral.setLmTime(new Date());
        qkjvipMemberIntegralService.update(qkjvipMemberIntegral);
        // 会员列表删除
        qkjvipMemberIntegraluserService.deleteByIntegralId(qkjvipMemberIntegral.getId());
        // 会员列表重新插入
        this.saveIntegralUser(qkjvipMemberIntegral.getId(), qkjvipMemberIntegral.getMemberlist());

        return RestResponse.success();
    }

    /**
     * 根据主键删除
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("删除")
    @RequestMapping("/delete")
    @RequiresPermissions("qkjvip:memberintegral:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberIntegralService.deleteBatch(ids);

        // 同时批量删除会员列表数据
        qkjvipMemberIntegraluserService.deleteBatchByIntegralId(ids);
        return RestResponse.success();
    }

    /**
     * 积分发放
     *
     * @param qkjvipMemberIntegral qkjvipMemberIntegral
     * @return RestResponse
     */
    @SysLog("发放积分")
    @RequestMapping("/sendIntegral")
    @RequiresPermissions("qkjvip:memberintegral:send")
    public RestResponse sendIntegral(@RequestBody QkjvipMemberIntegralEntity qkjvipMemberIntegral) throws IOException {
        Map map = new HashMap();
        String[] memberids = new String[qkjvipMemberIntegral.getMemberlist().size()];
        int cnt = 0;
        for (QkjvipMemberIntegraluserEntity integraluser : qkjvipMemberIntegral.getMemberlist()) {
            memberids[cnt] = integraluser.getMemberId();
            cnt++;
        }
        qkjvipMemberIntegral.setSendStatus(1);  //改为积分已发状态
        qkjvipMemberIntegralService.updateStatus(qkjvipMemberIntegral);

        map.put("integral", qkjvipMemberIntegral.getIntegral());
        map.put("listmemberid", memberids);
        map.put("remark", "群发积分");
        Object obj = JSONArray.toJSON(map);

        String resultPost = HttpClient.sendPost(Vars.MEMBER_INTEGRAL_SEND_URL, JsonHelper.toJsonString(obj));
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //调用成功
            return RestResponse.success();
        }
        qkjvipMemberIntegral.setSendStatus(0);  //恢复积分发放状态
        qkjvipMemberIntegralService.updateStatus(qkjvipMemberIntegral);
        return RestResponse.error(resultObject.get("descr").toString());
    }

    /**
     * 批量保存会员
     *
     * @param integralId integralId
     * @param memberlist memberlist
     * @return RestResponse
     */
    public void saveIntegralUser(String integralId, List<QkjvipMemberIntegraluserEntity> memberlist) {
        if (memberlist != null && memberlist.size() > 0) {
            List<QkjvipMemberIntegraluserEntity> integralusers = new ArrayList<>();
            for (QkjvipMemberIntegraluserEntity integraluser : memberlist) {
                integraluser.setIntegralId(integralId);
                integralusers.add(integraluser);
            }
            qkjvipMemberIntegraluserService.addBatch(integralusers);
        }
    }
}
