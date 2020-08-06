/*
 * 项目名称:platform-plus
 * 类名称:QkjROrderController.java
 * 包名称:com.platform.modules.qkj.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-08-28 14:44:59        孙珊珊sc     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkj.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.word.WordExportUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.DateUtils;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkj.entity.*;
import com.platform.modules.qkj.service.QkjROrderlogiService;
import com.platform.modules.qkj.service.QkjROrderlogidetailService;
import com.platform.modules.qkj.service.QkjRSonorderService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkj.service.QkjROrderService;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysSmsLogService;
import com.platform.modules.sys.service.SysUserService;
import com.platform.modules.util.ExportWordUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * Controller
 *
 * @author 孙珊珊sc
 * @date 2019-08-28 14:44:59
 */
@RestController
@RequestMapping("qkj/rorder")
public class QkjROrderController extends AbstractController {
    @Autowired
    private QkjROrderService qkjROrderService;

    @Autowired
    private QkjRSonorderService qkjsonservice;

    @Autowired
    private QkjROrderlogiService logiservice;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysSmsLogService sysSmsLogService;

    @Autowired
    private QkjROrderlogidetailService logidetailService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkj:rorder:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjROrderEntity> list = qkjROrderService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkj:rorder:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        params.put("dataScope", getDataScope("T.userid","u.ORG_NO"));
        Page page = qkjROrderService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info")
    @RequiresPermissions("qkj:rorder:info")
    public RestResponse info(@RequestParam Map<String, Object> mapid) {
        //查询主表
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("id",mapid.get("id"));
        List<QkjROrderEntity> list = qkjROrderService.queryAll(params);
        //查询子表
        params.clear();
        params.put("orderid",mapid.get("id"));
        List<QkjRSonorderEntity> sonlist=qkjsonservice.queryAll(params);
        List<QkjROrderlogidetailEntity> logis=logidetailService.queryAll(params);
        QkjROrderEntity qkjROrder=new QkjROrderEntity();
        if(list.size()>0){
            qkjROrder=list.get(0);
            if(sonlist.size()>0){
                qkjROrder.setSonorders(sonlist);
            }
        }
        //查询物流信息
        if(logis.size()>0)qkjROrder.setLogis(logis);
        return RestResponse.success().put("rorder", qkjROrder);
    }

    /**
     * 新增
     *
     * @param qkjROrder qkjROrder
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkj:rorder:save")
    public RestResponse save(@RequestBody QkjROrderEntity qkjROrder) {
        List<QkjRSonorderEntity> sons=new ArrayList<>();
        sons=qkjROrder.getSonorders();
        qkjROrder.setSonorders(null);
        qkjROrder.setAddtime(new Date());
        qkjROrder.setAdduser(getUserId());

        String date=qkjROrder.getEnddate();
        qkjROrder.setEnddate(date.substring(0,10));
        qkjROrderService.save(qkjROrder);
        String id=qkjROrder.getId();
        QkjRSonorderEntity soe=new QkjRSonorderEntity();
        for(int i=0;i<sons.size();i++){
           QkjRSonorderEntity e=new QkjRSonorderEntity();
           e=sons.get(i);
           e.setOrderid(id);
           qkjsonservice.save(e);
        }
        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjROrder qkjROrder
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkj:rorder:update")
    public RestResponse update(@RequestBody QkjROrderEntity qkjROrder) {
        List<QkjRSonorderEntity> sons=new ArrayList<>();
        sons=qkjROrder.getSonorders();
        qkjROrder.setSonorders(null);
        qkjROrderService.update(qkjROrder);

        String id=qkjROrder.getId();
        QkjRSonorderEntity soe=new QkjRSonorderEntity();
        for(int i=0;i<sons.size();i++){
            QkjRSonorderEntity e=new QkjRSonorderEntity();
            e=sons.get(i);
            if(e.getId()!=null){//修改
                qkjsonservice.update(e);
            }else{//添加
                e.setOrderid(id);
                qkjsonservice.save(e);
            }

        }

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
    @RequiresPermissions("qkj:rorder:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjROrderService.deleteBatch(ids);

        return RestResponse.success();
    }

    /**
     * 审核
     */
    @SysLog("下单")
    @RequestMapping("/checkstate")
    @RequiresPermissions("qkj:rorder:checkstate")
    public RestResponse checkstate(@RequestBody QkjROrderEntity qkjROrder) {
        //修改状态及附件
        String usid=qkjROrder.getUserid();
        String number=qkjROrder.getNumber();
        qkjROrder.setApprover(getUserId());
        qkjROrderService.updateState(qkjROrder);

        //发短信
        SysUserEntity user=new SysUserEntity();
        user = sysUserService.getById(usid);
        if(user.getMobile()!=null){
            SysSmsLogEntity smsLog=new SysSmsLogEntity();
            smsLog.setContent("您好，您编号为"+number+"的订单已下单请及时确认。账户为："+user.getUserName()+"初始密码为：888888");
            smsLog.setMobile(user.getMobile());
            SysSmsLogEntity sysSmsLogEntity = sysSmsLogService.sendSms(smsLog);
        }
        return RestResponse.success();
    }

    /**
     * 确认
     *
     * @param ids ids
     * @return RestResponse
     */
    @SysLog("确认")
    @RequestMapping("/sure")
    @RequiresPermissions("qkj:rorder:sure")
    public RestResponse sure(@RequestBody String[] ids) {
        qkjROrderService.sureBatchById(ids);
        //生成待收获单据
        return RestResponse.success();
    }

    /**
     * 确认生产
     */
    @SysLog("确认生产")
    @RequestMapping("/progoods")
    @RequiresPermissions("qkj:rorder:progoods")
    public RestResponse progoods(@RequestBody QkjROrderEntity qkjROrder) {
        List<QkjRSonorderEntity> sons=new ArrayList<>();
        sons=qkjROrder.getSonorders();

        //修改生产数量queryOrder
        QkjRSonorderEntity soe=new QkjRSonorderEntity();
        for(int i=0;i<sons.size();i++){
            QkjRSonorderEntity e=new QkjRSonorderEntity();
            e=sons.get(i);
            e.setSendstate(1);
            qkjsonservice.updatePornumByIdnew(e);
        }

        //修改状态为3已发货(全部已发货情况下)
        //查询子表是否已全部生产(查询未生产数量)
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderid",qkjROrder.getId());
        map.put("sendstate",0);
        List<QkjRSonorderEntity> sonss=new ArrayList<>();
        sonss=qkjsonservice.queryAll(map);

        QkjROrderEntity order=new QkjROrderEntity();
        order.setId(qkjROrder.getId());
        if(sonss.size()>0){
            order.setState(3);
        }else order.setState(4);
        qkjROrderService.updateState2(order);

        return RestResponse.success();
    }
    /**
     * 确认收货
     */
    @SysLog("确认收货")
    @RequestMapping("/rgoods")
    @RequiresPermissions("qkj:rorder:rgoods")
    public RestResponse rgoods(@RequestBody QkjROrderEntity qkjROrder) {
        //查询是否为部门收货（部分收货不修改状态）
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderid",qkjROrder.getId());
        map.put("sendstate",2);
        List<QkjRSonorderEntity> sonss=new ArrayList<>();
        sonss=qkjsonservice.queryAll(map);
        if(sonss.size()<=0){
            qkjROrderService.updateState2(qkjROrder);
        }

        //修改收货达成率
        List<QkjRSonorderEntity> sons=new ArrayList<>();
        sons=qkjROrder.getSonorders();
        for(int i=0;i<sons.size();i++){
            QkjRSonorderEntity e=new QkjRSonorderEntity();
            e=sons.get(i);
            e.setRgstate(1);
            qkjsonservice.updateRateById(e);
        }
        return RestResponse.success();
    }


    /**
     * 导出 word
     */
    @RequestMapping("/exportWord")
    public RestResponse exportWord(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, Object> mapid) throws FileNotFoundException {
        String filepath="D:/wordExcept.docx";
        //String filepath=fileChooser();
        Map<String,Object> map = new HashMap<>();
        byte[] data =null;
        //查询主表
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("id",mapid.get("id"));
        List<QkjROrderEntity> list = qkjROrderService.queryAll(params);

        if(list.size()>0){
            QkjROrderEntity ro=new QkjROrderEntity();
            ro=list.get(0);
            String[] filed={"makesys","realname","stsdate","enddate","remark","approvername","agentname"};
            for (int i=0;i<filed.length;i++) {
                map.put(filed[i], getFieldValueByName(filed[i], ro));
                if(map.get(filed[i])==null){
                    map.put(filed[i], "空");
                }

            }
                //查询子表
                params.clear();
                params.put("orderid",mapid.get("id"));
                List<QkjRSonorderEntity> sonlist=qkjsonservice.queryAll(params);
                map.put("sonList",sonlist);
                //这里是我说的一行代码
                data =ExportWordUtils.exportWord("word/SimpleExcel.docx","D:/","workword.docx",map,request,response);
            }
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"AutoCode" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".docx\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        try {
            IOUtils.write(data, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static  Object getFieldValueByName(String fieldName,Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }


}
