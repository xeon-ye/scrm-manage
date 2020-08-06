/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderlogiController.java
 * 包名称:com.platform.modules.qkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-09-10 11:18:28        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.RestResponse;
import com.platform.modules.accesstoken.entity.AccesstokenEntity;
import com.platform.modules.accesstoken.service.AccesstokenService;
import com.platform.modules.qkj.entity.QkjROrderEntity;
import com.platform.modules.qkj.entity.QkjROrderlogidetailEntity;
import com.platform.modules.qkj.entity.QkjRSonorderEntity;
import com.platform.modules.qkj.service.QkjROrderService;
import com.platform.modules.qkj.service.QkjROrderlogidetailService;
import com.platform.modules.qkj.service.QkjRSonorderService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkj.entity.QkjROrderlogiEntity;
import com.platform.modules.qkj.service.QkjROrderlogiService;
import com.platform.modules.util.DingMsg;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2019-09-10 11:18:28
 */
@RestController
@RequestMapping("qkj/rorderlogi")
public class QkjROrderlogiController extends AbstractController {
    @Autowired
    private QkjROrderlogiService qkjROrderlogiService;
    @Autowired
    private QkjROrderService orderservice;
    @Autowired
    private QkjRSonorderService qkjsonservice;
    @Autowired
    private QkjROrderlogidetailService logidetailService;

    @Autowired
    private AccesstokenService accesstokenService;


    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkj:rorderlogi:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjROrderlogiEntity> list = qkjROrderlogiService.queryAll(params);

        return RestResponse.success().put("list", list);
    }


    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryOrder")
    @RequiresPermissions("qkj:rorderlogi:list")
    public RestResponse queryOrder(@RequestParam Map<String, Object> params) {
        QkjROrderlogiEntity qkjROrderlogi = new QkjROrderlogiEntity();
        //查询子表
        List<QkjRSonorderEntity> sonlist=qkjsonservice.queryAll(params);
        qkjROrderlogi.setDataListson(sonlist);

        return RestResponse.success().put("rorderlogi", qkjROrderlogi);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkj:rorderlogi:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjROrderlogiService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkj:rorderlogi:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjROrderlogiEntity qkjROrderlogi = qkjROrderlogiService.getById(id);
        return RestResponse.success().put("rorderlogi", qkjROrderlogi);
    }

    /**
     * 根据主键查询打印详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/printinfo/{id}")
    public RestResponse printinfo(@PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id",id);
        QkjROrderlogiEntity qkjROrderlogi = qkjROrderlogiService.queryAll(map).get(0);
        //子表
        map.clear();
        map.put("logiid",id);
        qkjROrderlogi.setLogidetails(logidetailService.queryPrintAll(map));
        return RestResponse.success().put("rorderlogi", qkjROrderlogi);
    }

    /**
     * 新增
     *
     * @param qkjROrderlogi qkjROrderlogi
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkj:rorderlogi:save")
    public RestResponse save(@RequestBody QkjROrderlogiEntity qkjROrderlogi) {
        List<QkjRSonorderEntity> sons=new ArrayList<>();
        sons=qkjROrderlogi.getDataListson();
        String date=qkjROrderlogi.getLogidate();
        String shdate=qkjROrderlogi.getShdate();
        qkjROrderlogi.setLogidate(date.substring(0,10));
        qkjROrderlogi.setShdate(shdate.substring(0,10));
        qkjROrderlogiService.add(qkjROrderlogi);

        //修改发货数量queryOrder并添加发货子表
        QkjRSonorderEntity soe=new QkjRSonorderEntity();
        for(int i=0;i<sons.size();i++){
            QkjRSonorderEntity e=new QkjRSonorderEntity();
            e=sons.get(i);

            QkjROrderlogidetailEntity de=new QkjROrderlogidetailEntity();
            de.setLogiid(qkjROrderlogi.getId());//发货单id
            de.setMaterid(e.getId());//子表
            de.setNum(e.getSendnum());
            if(e.getSendnum()!=null){
                de.setBancle(e.getBnum()-e.getSendnum());
                logidetailService.add(de);//添加发货子表

                qkjsonservice.updateSendnumById(e);//修改累计发货数量
            }


        }

        //修改状态发货状态只要发货数量小于需求数量就是部分发货
        QkjROrderEntity order=new QkjROrderEntity();
        order.setId(qkjROrderlogi.getOrderid());
        orderservice.updateStateBySend(order);

        //发钉钉消息
        String meg2="";
        meg2="您好订单编号"+qkjROrderlogi.getOrderid()+"已发货请查收。";
        AccesstokenEntity ak=accesstokenService.queryAll(null).get(0);
        String[] dinglist = new String[]{"684322505018", "01133208032727","0119166528323","280423350920050354"};
        for(int i=0;i<4;i++){
            sendDing(meg2,dinglist[i],"",ak.getAccessToken());
        }
        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjROrderlogi qkjROrderlogi
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkj:rorderlogi:update")
    public RestResponse update(@RequestBody QkjROrderlogiEntity qkjROrderlogi) {
        qkjROrderlogi.setDataListson(null);
        qkjROrderlogiService.update(qkjROrderlogi);

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
    @RequiresPermissions("qkj:rorderlogi:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjROrderlogiService.deleteBatch(ids);

        return RestResponse.success();
    }

    private void sendDing(String msg,String ding,String singleid,String token) {
        DingMsg demo=new DingMsg();
        try {
            demo.sendLinkMessage(msg, ding,singleid,token);
        } catch (Exception e) {
            // TODO Auto-generated catch block

        }
    }
}
