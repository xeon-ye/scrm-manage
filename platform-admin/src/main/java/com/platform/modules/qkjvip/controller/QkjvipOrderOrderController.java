/*
 * 项目名称:platform-plus
 * 类名称:QkjvipOrderOrderController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-03-10 11:37:08        孙珊珊     初版做成
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
import com.platform.modules.qkjvip.entity.MemberEntity;
import com.platform.modules.qkjvip.entity.QkjvipOrderOrderQuaryEntity;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjvip.entity.QkjvipOrderOrderEntity;
import com.platform.modules.qkjvip.service.QkjvipOrderOrderService;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2021-03-10 11:37:08
 */
@RestController
@RequestMapping("qkjvip/orderorder")
public class QkjvipOrderOrderController extends AbstractController {
    @Autowired
    private QkjvipOrderOrderService qkjvipOrderOrderService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipOrderOrderEntity> list = qkjvipOrderOrderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param  查询参数
     * @return RestResponse
     */
    @PostMapping("/list")
    public RestResponse list(@RequestBody QkjvipOrderOrderQuaryEntity order) throws IOException {
        //Page page = qkjvipOrderOrderService.queryPage(params);
        Object obj = JSONArray.toJSON(order);
        String queryJsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        String resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_LIST, queryJsonStr);
        System.out.println("订单检索条件：" + queryJsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //调用成功
            List<QkjvipOrderOrderEntity> orderList = new ArrayList<>();
            orderList = JSON.parseArray(resultObject.getString("listorder"),QkjvipOrderOrderEntity.class);
            Page page = new Page();
            page.setRecords(orderList);
            page.setTotal(Long.parseLong(resultObject.get("totalcount").toString()));
            page.setSize(order.getPagesize() == null? 0 : order.getPagesize());
            page.setCurrent(order.getPageindex() == null? 0 : order.getPageindex());
            return RestResponse.success().put("page", page);
        } else {
            return RestResponse.error(resultObject.get("descr").toString());
        }
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipOrderOrderEntity qkjvipOrderOrder = qkjvipOrderOrderService.getById(id);

        return RestResponse.success().put("orderorder", qkjvipOrderOrder);
    }

    /**
     * 新增
     *
     * @param qkjvipOrderOrder qkjvipOrderOrder
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody QkjvipOrderOrderEntity qkjvipOrderOrder) throws IOException {
        qkjvipOrderOrder.setCreatoradminid(getUserId());
        qkjvipOrderOrder.setCreatoradmin(getUser().getUserName());
        Object obj = JSONArray.toJSON(qkjvipOrderOrder);
        String JsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        String resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_ADD, JsonStr);
        //qkjvipOrderOrderService.add(qkjvipOrderOrder);
        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipOrderOrder qkjvipOrderOrder
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:orderorder:update")
    public RestResponse update(@RequestBody QkjvipOrderOrderEntity qkjvipOrderOrder) {

        qkjvipOrderOrderService.update(qkjvipOrderOrder);

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
    @RequiresPermissions("qkjvip:orderorder:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipOrderOrderService.deleteBatch(ids);

        return RestResponse.success();
    }
}
