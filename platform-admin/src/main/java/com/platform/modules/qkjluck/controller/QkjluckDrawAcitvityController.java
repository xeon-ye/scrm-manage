/*
 * 项目名称:platform-plus
 * 类名称:QkjluckDrawAcitvityController.java
 * 包名称:com.platform.modules.qkjluck.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021-07-05 17:26:24        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjluck.controller;

import cn.emay.util.DateUtil;
import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.annotation.SysLog;
import com.platform.common.utils.JedisUtil;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjluck.entity.QkjluckDrawAcitiityitemEntity;
import com.platform.modules.qkjluck.entity.QkjluckDrawResultEntity;
import com.platform.modules.qkjluck.service.QkjluckDrawAcitiityitemService;
import com.platform.modules.qkjluck.service.QkjluckDrawResultService;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.qkjluck.entity.QkjluckDrawAcitvityEntity;
import com.platform.modules.qkjluck.service.QkjluckDrawAcitvityService;
import com.platform.modules.sys.entity.SysCacheEntity;
import com.platform.modules.sys.service.SysCacheService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2021-07-05 17:26:24
 */
@RestController
@RequestMapping("qkjluck/drawacitvity")
public class QkjluckDrawAcitvityController extends AbstractController {
    @Autowired
    private QkjluckDrawAcitvityService qkjluckDrawAcitvityService;
    @Autowired
    private QkjluckDrawAcitiityitemService qkjluckDrawAcitiityitemService;
    @Autowired
    private QkjluckDrawResultService qkjluckDrawResultService;
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    SysCacheService sysCacheService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjluckDrawAcitvityEntity> list = qkjluckDrawAcitvityService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        Page page = qkjluckDrawAcitvityService.queryPage(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    public RestResponse info(@PathVariable("id") String id) {
        QkjluckDrawAcitvityEntity qkjluckDrawAcitvity = qkjluckDrawAcitvityService.getById(id);
        // 查询奖品
        Map<String, Object> params = new HashMap<>();
        params.put("activityId",id);
        Page page = qkjluckDrawAcitiityitemService.queryPage(params);
        List<QkjluckDrawAcitiityitemEntity> items = page.getRecords();
        qkjluckDrawAcitvity.setItemlist(items);
        return RestResponse.success().put("drawacitvity", qkjluckDrawAcitvity);
    }

    /**
     * 根据主键查询详情抽奖
     *
     * @param paramsa 主键
     * @return RestResponse
     */
    @GetMapping("/luckinfo")
    public RestResponse luckinfo(@RequestParam Map<String, Object> paramsa) {
        String openid = paramsa.get("openid")+"";//参与人
        String unionid = paramsa.get("unionid")+"";//参与人
        String id=paramsa.get("id")+"";//活动id
        QkjluckDrawAcitvityEntity qkjluckDrawAcitvity = qdklucbBean(id,paramsa);//查询活动
        List itemlist = new ArrayList<>();
        itemlist=getitemlist(id);
        qkjluckDrawAcitvity.setItemlist(itemlist);//查询奖项

        //是否在抽奖时间段内
        Date nowDate = new Date();
        Date star_date= DateUtil.parseDate(qkjluckDrawAcitvity.getStrDate(),"yyyy-MM-dd");
        Date end_date=DateUtil.parseDate(qkjluckDrawAcitvity.getEndDate(),"yyyy-MM-dd");
        String nowday = DateUtil.toString(nowDate,"yyyy-MM-dd");
        Boolean endflag = nowDate.before(end_date);
        Boolean starflag = nowDate.after(star_date);


        // 抽奖
        int luckresultindex = -1;
        String luckresultid = "";//抽中奖品id
        int lucknum = 0;//剩余抽奖次数
        Boolean issureluck = true;
        if ((endflag == true || qkjluckDrawAcitvity.getEndDate().equals(nowday)) && (starflag == true || qkjluckDrawAcitvity.getStrDate().equals(nowday))) { //在有效时间内
            if (qkjluckDrawAcitvity!=null) {
                List<QkjluckDrawResultEntity> itemlistresult = new ArrayList<>();
                String itemlsresult = jedisUtil.get("MTM_CACHE:LUCKACTIVITY:RESULT_" + id +":" + unionid);
                itemlistresult = JSON.parseArray(itemlsresult, QkjluckDrawResultEntity.class);

                String lucnum = luckDraw(qkjluckDrawAcitvity,itemlistresult,id,openid,unionid);
                String[] res = lucnum.split(",");
                if(!lucnum.equals("-1")&&!lucnum.equals("-2")&&res.length>1){ //有抽奖机会
                    luckresultindex = Integer.parseInt(res[0]);
                    luckresultid = res[1];
                    lucknum = Integer.parseInt(res[2]);
                }else if (lucnum.equals("-2")){
                    luckresultindex = -2;
                }
            }
        } else {
            issureluck = false;
        }
        return RestResponse.success().put("drawacitvity", qkjluckDrawAcitvity).put("luckresultindex",luckresultindex).put("luckresultid",luckresultid).put("lucknum",lucknum).put("issureluck",issureluck);
    }

    /**
     * 修改抽奖状态已抽奖
     *
     * @param qkjluckDrawResult qkjluckDrawAcitvity
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/updateluckstatus")
    public RestResponse updateluckstatus(@RequestBody QkjluckDrawResultEntity qkjluckDrawResult) {
        List<QkjluckDrawResultEntity> itemlistresult = new ArrayList<>();
        String itemlsresult = jedisUtil.get("MTM_CACHE:LUCKACTIVITY:RESULT_" + qkjluckDrawResult.getActivityId() +":"+  qkjluckDrawResult.getUnionid());
        itemlistresult = JSON.parseArray(itemlsresult, QkjluckDrawResultEntity.class);
        int luckresultindex = -1;
        String luckresultid = "";//抽中奖品id
        int lucknum =0;
        Map<String, Object> paramsa = new HashMap<>();
        paramsa.put("id",qkjluckDrawResult.getActivityId());
        QkjluckDrawAcitvityEntity qkjluckDrawAcitvity = qdklucbBean(qkjluckDrawResult.getActivityId(),paramsa);//查询活动
        saveluckresult(qkjluckDrawResult);
        if(itemlistresult!=null){
            List<QkjluckDrawResultEntity> newitemlist = new ArrayList<>();
            for(QkjluckDrawResultEntity qd:itemlistresult){
                if (qd!=null&&qd.getActivityId().equals(qkjluckDrawResult.getActivityId())&&qd.getUnionid().equals(qkjluckDrawResult.getUnionid())&&qd.getItemId().equals(qkjluckDrawResult.getItemId())) {
                    qd.setNum(1);
                }
                newitemlist.add(qd);
            }
            String queryJsonStr = JsonHelper.toJsonString(newitemlist, "yyyy-MM-dd HH:mm:ss");
            jedisUtil.set("MTM_CACHE:LUCKACTIVITY:RESULT_" + qkjluckDrawResult.getActivityId() +":"+  qkjluckDrawResult.getUnionid(),queryJsonStr,0);
            // 查询是否还有抽奖机会，有返回下一次抽奖结果
            List itemlist = new ArrayList<>();
            itemlist=getitemlist(qkjluckDrawResult.getActivityId());
            qkjluckDrawAcitvity.setItemlist(itemlist);//查询奖项orderorder
            String lucnum = luckDraw(qkjluckDrawAcitvity,itemlistresult,qkjluckDrawResult.getActivityId(),qkjluckDrawResult.getOpenid(),qkjluckDrawResult.getUnionid());
            String[] res = lucnum.split(",");
            if(!lucnum.equals("-1")&&!lucnum.equals("-2")&&res.length>1) { //有抽奖机会
                luckresultindex = Integer.parseInt(res[0]);
                luckresultid = res[1];
                lucknum = Integer.parseInt(res[2]);
            }else if (lucnum.equals("-2")){
                luckresultindex = -2;
            }
        }

        return RestResponse.success().put("drawacitvity", qkjluckDrawAcitvity).put("luckresultindex",luckresultindex).put("luckresultid",luckresultid).put("lucknum",lucknum);
    }

    //查询活动
    public QkjluckDrawAcitvityEntity qdklucbBean(String id,Map<String, Object> paramsa){
        QkjluckDrawAcitvityEntity qkjluckDrawAcitvity = new QkjluckDrawAcitvityEntity();
        QkjluckDrawAcitvityEntity redisactivity = (QkjluckDrawAcitvityEntity) jedisUtil.getObject("MTM_CACHE:LUCKACTIVITY:INFO_" + id);
        if (redisactivity!=null&&redisactivity.getId()!=null) {
            qkjluckDrawAcitvity = redisactivity;
        } else {
            List<QkjluckDrawAcitvityEntity> list = qkjluckDrawAcitvityService.queryAll(paramsa);
            if(list!=null&&list.size()>0){
                qkjluckDrawAcitvity = list.get(0);
                jedisUtil.setObject("MTM_CACHE:LUCKACTIVITY:INFO_" + qkjluckDrawAcitvity.getId(),qkjluckDrawAcitvity,0);
            }
        }
        return qkjluckDrawAcitvity;
    }
    //查询奖品
    public List getitemlist(String id){
        List itemlist = new ArrayList<>();
        String itemls = jedisUtil.get("MTM_CACHE:LUCKACTIVITY:ITEM_" + id);
        itemlist = JSON.parseArray(itemls,QkjluckDrawAcitiityitemEntity.class);
        if (itemlist==null||itemlist.size()<=0) {
            // 查询奖品
            Map<String, Object> params = new HashMap<>();
            params.put("activityId",id);
            Page page = qkjluckDrawAcitiityitemService.queryPage(params);
            List items = page.getRecords();
            itemlist=new ArrayList<>();
            itemlist=items;
            String queryJsonStr = JsonHelper.toJsonString(items, "yyyy-MM-dd HH:mm:ss");
            jedisUtil.set("MTM_CACHE:LUCKACTIVITY:ITEM_" + id,queryJsonStr,0);
        }
        return itemlist;
    }
    //抽奖
    public String luckDraw (QkjluckDrawAcitvityEntity qkjluckDrawAcitvity,List<QkjluckDrawResultEntity> itemlist,String id,String openid,String unionid) {
        int num = qkjluckDrawAcitvity.getPeonum()!=null ? qkjluckDrawAcitvity.getPeonum() : 1;
        String index= "-1";//默认没有抽奖机会
        int lucknum = itemlist!=null ? itemlist.size():0;//已抽奖次数
        String checkid = "";//有未实际抽的奖品
        if (itemlist!=null&&itemlist.size()>0) {
            lucknum = 0;
            for (QkjluckDrawResultEntity qdre:itemlist) {
                if(qdre!=null&&(qdre.getNum()==null||(qdre.getNum()!= null&&qdre.getNum()==0))){//有未抽的奖品
                    checkid = qdre.getItemId();
                } else {
                    lucknum += 1;
                }
            }
            if (!checkid.equals("")) {
                int len =qkjluckDrawAcitvity.getItemlist().size();
                String [] array = new String[len];
                for (int i = 0; i < len; i++) {
                    array[i]=qkjluckDrawAcitvity.getItemlist().get(i).getId();
                    if (array[i].equals(checkid)) {
                        index= i+","+array[i]+"" + "," + (num - lucknum);
                        break;
                    }
                }
            }

        }
        if (checkid.equals("")){
            if ((num - lucknum)>0) {//开始
                int n;
                Random random = new Random();
                //String [] array = new String[100];
                List<String> arraylist = new ArrayList<String>();
                int cindex = 0;
                for (int i=0;i<qkjluckDrawAcitvity.getItemlist().size();i++) {
                    Integer number = qkjluckDrawAcitvity.getItemlist().get(i).getNumber();// 库存总数
                    String itemid = qkjluckDrawAcitvity.getItemlist().get(i).getId();
                    int totelnum=0;
                    if (number!=null) {
                        totelnum = number;
                    }
                    Boolean ischeck = false;
                    if (totelnum == 0) { //数量不限制
                        ischeck = true;
                    } else {
                        //其它人这个奖项的抽奖个数
                        Map<String, String> params= new HashMap<>();
                        params.put("pattern", "MTM_CACHE:LUCKACTIVITY:RESULT_" + id  +":"+ "*");
                        List<SysCacheEntity> list = sysCacheService.queryAll(params);
                        int othernum = 0;
                        for (SysCacheEntity sce:list) {
                            List<QkjluckDrawResultEntity> oterresult = new ArrayList<>();
                            oterresult = JSON.parseArray(sce.getValue(), QkjluckDrawResultEntity.class);
                            for (QkjluckDrawResultEntity other:oterresult) {
                                if (other.getItemId().equals(itemid)) {//是本奖项
                                    othernum +=1;
                                }
                            }
                        }
                        if ((totelnum-othernum)<=0) { //其它人抽奖结果与总数相等则删除此奖品
                            ischeck = false;
                        } else {
                            ischeck = true;
                        }
                    }
                    if (ischeck == true) {
                        int itemnum = qkjluckDrawAcitvity.getItemlist().get(i).getWeight();
                        for (int j =0;j<itemnum;j++) {
                            //array[cindex] = qkjluckDrawAcitvity.getItemlist().get(i).getId()+","+i;
                            arraylist.add(qkjluckDrawAcitvity.getItemlist().get(i).getId()+","+i);
                            cindex += 1;
                        }
                    }
                }

                if (arraylist.size()>0) { //有奖品
                    int length=arraylist.size();
                    System.out.println("奖品总比例"+ length);
                    int resultnum = (int) (Math.random()*length);
                    String[] str = arraylist.get(resultnum).split(","); //array[resultnum].split(",");
                    index= str[1]+","+str[0]+"," + (num - lucknum);;

                    // 保存抽奖结果
                    List<QkjluckDrawResultEntity> qdes = new ArrayList<>();
                    QkjluckDrawResultEntity qkjluckDrawResult = new QkjluckDrawResultEntity();
                    qkjluckDrawResult.setActivityId(id);
                    qkjluckDrawResult.setOpenid(openid);
                    qkjluckDrawResult.setUnionid(unionid);
                    qkjluckDrawResult.setItemId(str[0]);
                    qkjluckDrawResult.setNum(0);
                    qkjluckDrawResult.setAddtime(new Date());
                    qdes.add(qkjluckDrawResult);
                    String queryJsonStr = JsonHelper.toJsonString(qdes, "yyyy-MM-dd HH:mm:ss");
                    if (itemlist != null && itemlist.size() > 0) {
                        itemlist.add(qkjluckDrawResult);
                        queryJsonStr = JsonHelper.toJsonString(itemlist, "yyyy-MM-dd HH:mm:ss");
                    }
                    //更新redis
                    jedisUtil.set("MTM_CACHE:LUCKACTIVITY:RESULT_" + id +":"+ unionid, queryJsonStr,0);
                    //添加数据库（异步）
                    addluckresult(qkjluckDrawResult);
                } else { //所有奖品都无库存
                    index = "-2";
                }

            }

        }
        return index;
    }

    @Async
    public void addluckresult (QkjluckDrawResultEntity qkjluckDrawResult) {
        qkjluckDrawResultService.add(qkjluckDrawResult);
    }
    @Async
    public void saveluckresult (QkjluckDrawResultEntity qkjluckDrawResult) {
        qkjluckDrawResultService.updateByPama(qkjluckDrawResult);
    }

    /**
     * 新增
     *
     * @param qkjluckDrawAcitvity qkjluckDrawAcitvity
     * @return RestResponse
     */
    @SysLog("新增&修改")
    @RequestMapping("/save")
    public RestResponse save(@RequestBody QkjluckDrawAcitvityEntity qkjluckDrawAcitvity) {
        if (qkjluckDrawAcitvity!=null&&qkjluckDrawAcitvity.getId()!=null&&!qkjluckDrawAcitvity.getId().equals("")) {
            qkjluckDrawAcitvityService.update(qkjluckDrawAcitvity);

        } else {
            qkjluckDrawAcitvity.setAdduser(getUserId());
            qkjluckDrawAcitvity.setAddtime(new Date());
            qkjluckDrawAcitvityService.add(qkjluckDrawAcitvity);
        }
        // 删除所有项目
        qkjluckDrawAcitiityitemService.deleteBatchByMain(qkjluckDrawAcitvity.getId());
        if (qkjluckDrawAcitvity!=null&&qkjluckDrawAcitvity.getItemlist()!=null&&qkjluckDrawAcitvity.getItemlist().size()>0) {
            for (QkjluckDrawAcitiityitemEntity item:qkjluckDrawAcitvity.getItemlist()) {
                if(item.getPrizetakenpath()!=null){
                    if(item.getPrizetakentype().equals("1")){ //小程序拼接参数
                        String str ="&activity=";
                        if(!item.getPrizetakenpath().contains("?")){
                            str = "?activity=";
                        }
                        str = item.getPrizetakenpath() + str + "turntable-" + qkjluckDrawAcitvity.getId();
                        item.setPrizetakenpathresult(str);
                    }else if (item.getPrizetakentype().equals("3")){ //产品
                        String str = "/pages/product/detail" + "?from=lottery&fromid="+qkjluckDrawAcitvity.getId()+"&pid=" + item.getPrizetakenpath();
                        item.setPrizetakenpathresult(str);
                    }else {
                        item.setPrizetakenpathresult(item.getPrizetakenpath());
                    }
                }
                item.setActivityId(qkjluckDrawAcitvity.getId());
                qkjluckDrawAcitiityitemService.add(item);
            }
        }

        //更新redis
        // 查询奖品
        Map<String, Object> params = new HashMap<>();
        params.put("activityId",qkjluckDrawAcitvity.getId());
        Page page = qkjluckDrawAcitiityitemService.queryPage(params);
        List<QkjluckDrawAcitiityitemEntity> items = page.getRecords();
        qkjluckDrawAcitvity.setItemlist(items);
        jedisUtil.setObject("MTM_CACHE:LUCKACTIVITY:INFO_" + qkjluckDrawAcitvity.getId(),qkjluckDrawAcitvity,0);
        String queryJsonStr = JsonHelper.toJsonString(items, "yyyy-MM-dd HH:mm:ss");
        jedisUtil.set("MTM_CACHE:LUCKACTIVITY:ITEM_" + qkjluckDrawAcitvity.getId(),queryJsonStr,0);
        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjluckDrawAcitvity qkjluckDrawAcitvity
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    public RestResponse update(@RequestBody QkjluckDrawAcitvityEntity qkjluckDrawAcitvity) {

        qkjluckDrawAcitvityService.update(qkjluckDrawAcitvity);

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
        qkjluckDrawAcitvityService.deleteBatch(ids);

        return RestResponse.success();
    }


}
