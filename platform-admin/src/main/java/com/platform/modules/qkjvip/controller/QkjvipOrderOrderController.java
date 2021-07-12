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
import com.platform.datascope.ContextHelper;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.qkjvip.service.*;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.service.SysUserChannelService;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

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
    @Autowired
    private QkjvipOrderOrderdetailService qkjvipOrderOrderdetailService;
    @Autowired
    private QkjvipProductStockService qkjvipProductStockService;
    @Autowired
    private QkjvipOrderDeliverlogService qkjvipOrderDeliverlogService;
    @Autowired
    private SysUserChannelService sysUserChannelService;
    @Autowired
    private QkjvipOrderErporderService qkjvipOrderErporderService;

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
     * 查看最近收货地址
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryorderbyMember")
    public RestResponse queryorderbyMember(@RequestParam Map<String, Object> params) {
        List<QkjvipOrderOrderEntity> list = qkjvipOrderOrderService.queryorderbyMember(params);
        QkjvipOrderOrderEntity qkjvipo=new QkjvipOrderOrderEntity();
        if(list!=null&&list.size()>0){
            qkjvipo=list.get(0);
        }
        return RestResponse.success().put("qkjvipOrderAddress", qkjvipo);
    }

    /**
     * 分页查询
     *
     * @param
     * @return RestResponse
     */
    @PostMapping("/list")
    public RestResponse list(@RequestBody QkjvipOrderOrderQuaryEntity order) throws IOException {
        Set<String> list = new HashSet<>();
        if (order!=null&&order.getDatetype()!=null) {
            Date date=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date2=sdf.format(date);
            Calendar c = Calendar.getInstance();
            if (order.getDatetype()==1) {//一个月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -1);
                Date m = c.getTime();
                String mon = sdf.format(m);
                order.setStartorderdate(mon + " 00:00:00");
            } else if (order.getDatetype()==2) {//三个月
                //过去三个月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -3);
                Date m3 = c.getTime();
                String mon3 = sdf.format(m3);
                order.setStartorderdate(mon3 + " 00:00:00");
            }else if (order.getDatetype()==3) { //半年
                c.setTime(new Date());
                c.add(Calendar.MONTH, -6);
                Date m3 = c.getTime();
                String mon3 = sdf.format(m3);
                order.setStartorderdate(mon3 + " 00:00:00");
            }else if (order.getDatetype()==4) {//一年
                c.setTime(new Date());
                c.add(Calendar.YEAR, -1);
                Date y = c.getTime();
                String year = sdf.format(y);
                order.setStartorderdate(year + " 00:00:00");
            }
            order.setEndorderdate(date2 + " 23:59:59");
        }
        list=ContextHelper.setOrdertypesm("qkjvip:memberactivity:list",getUserId());
        if (list.size()>0) {
            order.setListordertype(StringUtils.join(list.toArray(), ","));
        } else { //无任何类别查询权限
            order.setListordertype("0");
        }
        if (!getUser().getUserName().contains("admin")) {  // 空默认是全部所有权限
            order.setCurrentmemberid(getUserId());
//            memberQuery.setListorgno(sysRoleOrgService.queryOrgNoListByUserIdAndPerm(getUserId(), "qkjvip:member:list"));
            order.setListmemberchannel("0".equals(sysUserChannelService.queryChannelIdByUserId(getUserId())) ? "-1" : sysUserChannelService.queryChannelIdByUserId(getUserId())); // 0代表选择的是全部渠道传-1
        } else {
            order.setListmemberchannel("-1");
        }
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
    public RestResponse info(@PathVariable("id") String id) throws IOException {
        //QkjvipOrderOrderEntity qkjvipOrderOrder = qkjvipOrderOrderService.getById(id);
        QkjvipOrderOrderQuaryEntity order=new QkjvipOrderOrderQuaryEntity();
        order.setMorderid(id);
        Object obj = JSONArray.toJSON(order);
        String queryJsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        String resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_LISTDETILE, queryJsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        QkjvipOrderOrderEntity qkjvipOrderOrder = JSON.toJavaObject(resultObject,QkjvipOrderOrderEntity.class);
        qkjvipOrderOrder.setId(qkjvipOrderOrder.getMorderid());
        qkjvipOrderOrder.setOrderid(qkjvipOrderOrder.getMorderid());
        //查询库存分配
        if(qkjvipOrderOrder.getOrdertype()==null){
            qkjvipOrderOrder.setOrdertype(5);
        }
        if(qkjvipOrderOrder.getOrdertype()==5){
            List<QkjvipProductStockEntity> liststocks=new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            params.put("orderid",qkjvipOrderOrder.getMorderid());
            liststocks=qkjvipProductStockService.queryAll(params);
            List<QkjvipOrderDeliverlogEntity> listout=new ArrayList<>();
            listout=qkjvipOrderDeliverlogService.queryAll(params);

            List<QkjvipProductProductEntity> newps=new ArrayList<>();
            if(qkjvipOrderOrder!=null&&qkjvipOrderOrder.getListproduct()!=null){
                for(QkjvipProductProductEntity es:qkjvipOrderOrder.getListproduct()){
                    Double outsum=0.00;
                    for(QkjvipProductStockEntity e:liststocks){
                        if(es.getProductid().equals(e.getProductid())){
                            if(e.getOuttotalcount()!=null){
                                outsum+=e.getOuttotalcount().doubleValue();
                            }
                        }
                    }
                    es.setOutcount(outsum);
                    newps.add(es);
                }
            }
            qkjvipOrderOrder.setListproduct(newps);
            qkjvipOrderOrder.setListstock(liststocks);
            qkjvipOrderOrder.setListout(listout);
        }
        //查询封坛
        List<QkjvipOrderErporderEntity> list = new ArrayList<>();
        if (qkjvipOrderOrder!=null&&qkjvipOrderOrder.getErpordercode()!=null&&!qkjvipOrderOrder.getErpordercode().equals("")){
            Map<String, Object> params= new HashMap<>();
            params.put("ordercode",qkjvipOrderOrder.getErpordercode());
            list = qkjvipOrderErporderService.queryAllDetail(params);
        }
        return RestResponse.success().put("qkjvipOrderOrder", qkjvipOrderOrder).put("erplist",list);
    }

    /**
     * 新增
     *
     * @param qkjvipOrderOrder qkjvipOrderOrder
     * @return RestResponse
     */
    @SysLog("新增&修改")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody QkjvipOrderOrderEntity qkjvipOrderOrder) throws IOException {
        qkjvipOrderOrder.setListuserfile(null);
        qkjvipOrderOrder.setListfinalfile(null);
        qkjvipOrderOrder.setCreatoradminid(getUserId());
        qkjvipOrderOrder.setCreatoradmin(getUser().getUserName());
        if(qkjvipOrderOrder.getCrmMemberid()!=null){
            qkjvipOrderOrder.setMemberid(qkjvipOrderOrder.getCrmMemberid());
        }
        Object obj = JSONArray.toJSON(qkjvipOrderOrder);
        String JsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        String resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_ADD, JsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if (!"200".equals(resultObject.get("resultcode").toString())) {  //清洗失败
            return RestResponse.error(resultObject.get("descr").toString());
        }
        //qkjvipOrderOrderService.add(qkjvipOrderOrder);
        return RestResponse.success();
    }

    /**
     * 新增
     *
     * @param qkjvipOrderOrder qkjvipOrderOrder
     * @return RestResponse
     */
    @SysLog("推送商城")
    @RequestMapping("/savetocom")
    public RestResponse savetocom(@RequestBody QkjvipOrderOrderEntity qkjvipOrderOrder) throws IOException {
        // 修改信息
        qkjvipOrderOrder.setListuserfile(null);
        qkjvipOrderOrder.setListfinalfile(null);
        qkjvipOrderOrder.setCreatoradminid(getUserId());
        qkjvipOrderOrder.setCreatoradmin(getUser().getUserName());
        if(qkjvipOrderOrder.getCrmMemberid()!=null){
            qkjvipOrderOrder.setMemberid(qkjvipOrderOrder.getCrmMemberid());
        }
        Object obj = JSONArray.toJSON(qkjvipOrderOrder);
        String JsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        String resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_ADD, JsonStr);

        // 推送
        QkjvipOrderOrderEntity newo =new QkjvipOrderOrderEntity();
        newo.setOrderstatus(20);
        newo.setMorderid(qkjvipOrderOrder.getMorderid());
        newo.setToqkh(1);
        obj = JSONArray.toJSON(newo);
        JsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_MDYSTATUS, JsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if (!"200".equals(resultObject.get("resultcode").toString())) {  //清洗失败
            return RestResponse.error(resultObject.get("descr").toString());
        }
        //qkjvipOrderOrderService.add(qkjvipOrderOrder);
        return RestResponse.success();
    }

    /**
     * 新增
     *
     * @param
     * @return RestResponse
     */
    @SysLog("转换正式订单")
    @RequestMapping("/savestatus")
    public RestResponse savestatus(@RequestBody QkjvipOrderOrderEntity qkjvipOrderOrder) throws IOException {
        // 推送
        QkjvipOrderOrderEntity newo =new QkjvipOrderOrderEntity();
        newo.setOrderstatus(70);
        newo.setMorderid(qkjvipOrderOrder.getId());
        newo.setToqkh(0);
        Object obj = JSONArray.toJSON(newo);
        String JsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        String resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_MDYSTATUS, JsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if (!"200".equals(resultObject.get("resultcode").toString())) {  //清洗失败
            return RestResponse.error(resultObject.get("descr").toString());
        }
        //qkjvipOrderOrderService.add(qkjvipOrderOrder);
        return RestResponse.success();
    }

    /**
     * 新增
     *
     * @param qkjvipOrderOrder qkjvipOrderOrder
     * @return RestResponse
     */
    @SysLog("新增&修改封坛")
    @RequestMapping("/saveft")
    public RestResponse saveft(@RequestBody QkjvipOrderOrderEntity qkjvipOrderOrder) throws IOException {
        qkjvipOrderOrder.setCreatoradminid(getUserId());
        qkjvipOrderOrder.setCreatoradmin(getUser().getUserName());
        if(qkjvipOrderOrder.getCrmMemberid()!=null){
            qkjvipOrderOrder.setMemberid(qkjvipOrderOrder.getCrmMemberid());
        }
        Object obj = JSONArray.toJSON(qkjvipOrderOrder);
        String JsonStr = JsonHelper.toJsonString(obj, "yyyy-MM-dd HH:mm:ss");
        String resultPost = HttpClient.sendPost(Vars.MEMBER_ORDER_ORDER_ADD, JsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //修改成功
            String orderid=resultObject.get("morderid").toString();
            //添加入库分配
            qkjvipProductStockService.deleteBatchByOrder(orderid);
            if(qkjvipOrderOrder.getListstock()!=null&&qkjvipOrderOrder.getListstock().size()>0){
                List<QkjvipProductStockEntity> liststock=new ArrayList<>();
                for(QkjvipProductStockEntity st:qkjvipOrderOrder.getListstock()){
                    if(st.getOuttotalcount()==null){
                        st.setProductcount(st.getIntotalcount());
                    }else{
                        st.setProductcount(st.getIntotalcount().subtract(st.getOuttotalcount()));
                    }
                    st.setCreator(getUserId());
                    st.setCreateon(new Date());
                    st.setOrderid(orderid);
                    liststock.add(st);
                }
                qkjvipProductStockService.batchAdd(liststock);
            }
            qkjvipOrderDeliverlogService.deleteBatchByOrder(orderid);
            //添加出库记录
            if(qkjvipOrderOrder.getListout()!=null&&qkjvipOrderOrder.getListout().size()>0){
                List<QkjvipOrderDeliverlogEntity> liststock=new ArrayList<>();
                for(QkjvipOrderDeliverlogEntity st:qkjvipOrderOrder.getListout()){
                    Date date = new Date();//获取当前的日期
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String str = df.format(date);//获取String类型的时间
                    st.setSaleunit(5);
                    st.setCreator(getUserId());
                    st.setCreateon(str);
                    st.setOrderid(orderid);
                    liststock.add(st);
                }
                qkjvipOrderDeliverlogService.batchAdd(liststock);
            }
        }else {
            return RestResponse.error(resultObject.get("descr").toString());
        }

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
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipOrderOrderService.deleteBatch(ids);

        return RestResponse.success();
    }
}
