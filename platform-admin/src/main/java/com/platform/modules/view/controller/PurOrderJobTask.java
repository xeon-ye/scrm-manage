/*
 * 项目名称:platform-plus
 * 类名称:ViewTSmPurorderController.java
 * 包名称:com.platform.modules.view.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-10-29 14:00:54        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.view.controller;

import com.platform.modules.qkj.entity.QkjRMaterialEntity;
import com.platform.modules.qkj.entity.QkjROrderEntity;
import com.platform.modules.qkj.entity.QkjRSonorderEntity;
import com.platform.modules.qkj.service.QkjRMaterialService;
import com.platform.modules.qkj.service.QkjROrderService;
import com.platform.modules.qkj.service.QkjRSonorderService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysUserEntity;
import com.platform.modules.sys.service.SysUserRoleService;
import com.platform.modules.sys.service.SysUserService;
import com.platform.modules.view.entity.ViewMaterialEntity;
import com.platform.modules.view.entity.ViewTSmPurorderEntity;
import com.platform.modules.view.entity.ViewTSmUserEntity;
import com.platform.modules.view.service.ViewMaterialService;
import com.platform.modules.view.service.ViewTSmPurorderService;
import com.platform.modules.view.service.ViewTSmUserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2019-10-29 14:00:54
 */

@Component("purordertask")
@Slf4j
public class PurOrderJobTask extends AbstractController {
    @Autowired
    private ViewTSmPurorderService viewTSmPurorderService;
    @Autowired
    private QkjROrderService qkjROrderService;
    @Autowired
    private QkjRSonorderService qkjsonservice;
    @Autowired
    private ViewTSmUserService tsuser;
    @Autowired
    private SysUserService userc;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private ViewMaterialService erpvms;
    @Autowired
    private QkjRMaterialService qkjms;

    public void consoleLog(){
        System.out.println("通过测试接口 来控制定时任务");
    }

    public void getPurOrder(String pur){
        //获得erpd订单
        //起止时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        String startDate=df.format(date);
        System.out.println("系统当前时间      ："+df.format(date));
        c.setTime(date);//设置参数时间
        c.add(Calendar.MINUTE,-30);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date=c.getTime(); //这个时间就是日期往后推一天的结果
        String endDate = df.format(date);
        System.out.println("系统前30分时间："+endDate);
        Map<String, Object> params=new HashMap<String, Object>();
        if(pur!=null&&pur.equals("1")){
            params.put("startDate",startDate);
        }else{
            params.put("startDate","2020-01-01");
        }
        params.put("endDate",endDate);
        System.out.println(params.get("startDate"));
        List<ViewTSmPurorderEntity> vtses=viewTSmPurorderService.queryAll(params);
        List<ViewTSmPurorderEntity> mainvtses=viewTSmPurorderService.queryAllMain(params);
        if(mainvtses.size()>0){//主表
            List<QkjROrderEntity> addqoe=new ArrayList<>();
            List<QkjRSonorderEntity> sonaddqoe=new ArrayList<>();
            List<String> orderidList=new ArrayList<>();//存放插入到数据库中的主表id用来判断是否插入子表
            for(ViewTSmPurorderEntity v:mainvtses){
                //判断id是否在数据库中
                QkjROrderEntity aros=new QkjROrderEntity();
                aros=qkjROrderService.getById(v.getId());
                if(aros==null) {//未在数据库中
                    QkjROrderEntity oe=new QkjROrderEntity();
                    oe.setId(v.getId());
                    oe.setUserid(v.getUserid());
                    if(v.getFBaseStatus()!=null&&v.getFBaseStatus()==7){//已关闭的单据
                        oe.setState(8);
                    }
                    oe.setStsdate(v.getStsdate());
                    oe.setNumber(v.getNumber());
                    oe.setSource(1);
                    oe.setAddtime(v.getFCreateTime());
                    oe.setAdduser("1");
                    oe.setCreatuser(v.getCreatuser());
                    addqoe.add(oe);//主表只添加一次
                    orderidList.add(v.getId());
                }
            }
            //子表
            for(ViewTSmPurorderEntity v:vtses){
                if(orderidList.size()>0){
                    Boolean flag=false;
                    for(int i=0;i<orderidList.size();i++){
                        String newid=orderidList.get(i);
                        if(newid.equals(v.getId())){
                            flag=true;
                            break;
                        }
                    }

                    if(flag==true){
                        QkjRSonorderEntity son=new QkjRSonorderEntity();
                        son.setOrderid(v.getOrderid());
                        son.setMaterielid(v.getMaterielid());
                        son.setNum(v.getNum());
                        if(v.getFBaseStatus()!=null&&v.getFBaseStatus()==7){//已关闭的单据
                            son.setRgnum(v.getRknum());
                            son.setRgstate(1);
                        }
                        sonaddqoe.add(son);
                    }
                }
            }

            //批量添加
            for(QkjROrderEntity main:addqoe){
                String ii=main.getId();
                if(ii.equals("KJQAAAAo6Pcxcb+t")){
                    System.out.println(11);
                }
                qkjROrderService.add(main);
            }
            for(QkjRSonorderEntity son:sonaddqoe){
                qkjsonservice.add(son);
            }
        }
    }


    public void getUser(String pur){
        //获得erp供应商
        //起止时间前一天
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        String startDate=df.format(date).substring(0,10)+" 00:00:00";
        System.out.println("系统当前天凌晨      ："+df.format(date));
        c.setTime(date);//设置参数时间
        c.add(Calendar.DATE,-1);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date=c.getTime(); //这个时间就是日期往后推一天的结果
        String endDate = df.format(date).substring(0,10)+" 00:00:00";;
        System.out.println("系统前1天凌晨："+endDate);
        Map<String, Object> params=new HashMap<String, Object>();
        if(pur!=null&&pur.equals("1")){
            params.put("startDate",startDate);
        }
        params.put("endDate",endDate);
        System.out.println(params.get("startDate"));
        List<ViewTSmUserEntity> vtses=tsuser.queryAll(params);
        if(vtses.size()>0){//主表
            for(ViewTSmUserEntity v:vtses){
                //判断id是否在数据库中
                SysUserEntity user =new SysUserEntity();
                user.setUserId(v.getUserid());
                user.setSex(2);
                user.setRealName(v.getRealName());
                user.setUserName(v.getUserName());
                user.setOrgNo("01");
                user.setStatus(v.getStatus());
                user.setCreateState(1);
                user.setCreateTime(v.getCreateTime());

                List<String> roleIdList = new ArrayList<>();
                roleIdList.add(v.getRoleidlist());
                user.setRoleIdList(roleIdList);

                userc.add(user);
            }



        }
    }


    public void getTackGood(String pur){
        //查询所有已发货产品订单号
        List<QkjROrderEntity> prolist=new ArrayList<>();
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("state",6);
        params.put("source",1);
        prolist=qkjROrderService.queryAll(params);
        if(prolist.size()>0){
            Set<String> bfsh = new HashSet<>();//部分收货集合
            Set<String> qbsh = new HashSet<>();//全部收货集合
            for(QkjROrderEntity p:prolist){
                //查询是否已入库
                if(p.getId()!=null){
                    params.clear();
                    params.put("id",p.getId());
                    List<ViewTSmPurorderEntity> vtses=viewTSmPurorderService.queryAll(params);
                    if(vtses.size()>0){
                        for(ViewTSmPurorderEntity r:vtses){
                            if(r.getFBaseStatus()!=null&&r.getFBaseStatus()==7){//关闭
                                qbsh.add(p.getId());
                            }else{
                                bfsh.add(p.getId());
                            }
                            //修改此单此物料数量及状态
                            QkjRSonorderEntity son=new QkjRSonorderEntity();
                            son.setOrderid(p.getId());
                            son.setMaterielid(r.getMaterielid());
                            son.setRgnum(r.getRknum());//合格数量
                            son.setRgstate(1);
                            if(r.getRknum()>0){//有入库数量
                                qkjsonservice.updateSonbyOAndM(son);
                            }

                        }
                    }

                    if(qbsh.size()>0){
                        for (String qb : qbsh) {
                            QkjROrderEntity ore=new QkjROrderEntity();
                            ore.setState(8);
                            ore.setId(qb);
                            qkjROrderService.updateState2(ore);
                        }
                    }
                    if(bfsh.size()>0){
                        for (String qb : bfsh) {
                            QkjROrderEntity ore=new QkjROrderEntity();
                            ore.setState(7);
                            ore.setId(qb);
                            qkjROrderService.updateState2(ore);
                        }
                    }
                }
            }
        }
    }

    //获取物料
    public void getMaterie(String pur){
        //查询所有物料
        List<ViewMaterialEntity> erpvmes=new ArrayList<>();
        erpvmes=erpvms.queryAll(null);
        if(erpvmes.size()>0){
            //删除之前物料
            qkjms.deleteAll();
            for(ViewMaterialEntity v:erpvmes){
                QkjRMaterialEntity qkje=new QkjRMaterialEntity();
                qkje.setFid(v.getFid());
                qkje.setFnameL2(v.getFnameL2());
                qkje.setFnumber(v.getFnumber());
                qkje.setFmodel(v.getFmodel());
                qkje.setFbaseunit(v.getFbaseunit());
                qkje.setGroupid(v.getGroupid());
                qkjms.add(qkje);
            }
        }
    }

}
