/*
 * 项目名称:platform-plus
 * 类名称:QkjvipMemberActivityController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-09-03 09:49:29        孙珊珊     初版做成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.WriterException;
import com.platform.common.annotation.SysLog;
import com.platform.common.exception.BusinessException;
import com.platform.common.utils.RestResponse;
import com.platform.common.utils.StringUtils;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.qkjvip.service.*;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.sys.entity.SysSmsLogEntity;
import com.platform.modules.sys.entity.SysUserChannelEntity;
import com.platform.modules.sys.service.SysSmsLogService;
import com.platform.modules.sys.service.SysUserChannelService;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.QRCodeUtil;
import com.platform.modules.util.Vars;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import com.platform.modules.util.ExportExcelUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.afterturn.easypoi.excel.entity.enmus.CellValueType.Date;

/**
 * Controller
 *
 * @author 孙珊珊
 * @date 2020-09-03 09:49:29
 */
@RestController
@RequestMapping("qkjvip/memberactivity")
public class QkjvipMemberActivityController extends AbstractController {
    @Autowired
    private QkjvipMemberActivityService qkjvipMemberActivityService;
    @Autowired
    private QkjvipMemberActivitymbsService qkjvipMemberActivitymbsService;
    @Autowired
    private QkjvipMemberImportService qkjvipMemberImportService;
    @Autowired
    private QkjvipMemberSignupaddressService qkjvipMemberSignupaddressService;
    @Autowired
    private SysSmsLogService sysSmsLogService;
    @Autowired
    private QkjvipMemberSignupService qkjvipMemberSignupService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private QkjvipMemberSignupmemberService qkjvipMemberSignupmemberService;
    @Autowired
    private SysUserChannelService sysUserChannelService;

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAll")
    @RequiresPermissions("qkjvip:memberactivity:list")
    public RestResponse queryAll(@RequestParam Map<String, Object> params) {
        List<QkjvipMemberActivityEntity> list = qkjvipMemberActivityService.queryAll(params);

        return RestResponse.success().put("list", list);
    }

    /**
     * 查看所有列表
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @RequestMapping("/queryAllhtml")
    public RestResponse queryAllhtml(@RequestParam Map<String, Object> params) {
        //已参加的活动
        List<QkjvipMemberActivityEntity> list=new ArrayList<>();
        //附近的未参加的活动(公开的及邀约的)
        HttpServletRequest request = getHttpServletRequest();
        String ip = getIp(request);
        // 百度地图申请的ak
        String ak = "Ed5GYHTqRm1E5VZgHB6jm7Qz2vckMfQg";
        // 这里调用百度的ip定位api服务 详见 http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
//        try{
//            JSONObject json = readJsonFromUrl("http://api.map.baidu.com/location/ip?ip=" + ip + "&ak=" + ak);
//            //这里只取出了两个参数，根据自己需求去获取
//            if(json.get("content")!=null){
//                JSONObject obj = (JSONObject) ((JSONObject) json.get("content")).get("address_detail");
//                String province = obj.getString("province");
//                String city = obj.getString("city");
//                System.out.println(province);
//                params.put("memberIdSignAddress",city);
//            }
//            list = qkjvipMemberActivityService.queryAllSignAddress(params);
//        }catch (IOException e){
//
//        }
        params.put("ispri",0);
        String topClassShow = params.get("topClassShow")+"";
        if(topClassShow!=null&&topClassShow.equals("1")){
            Page page  = qkjvipMemberActivityService.queryAllSignAddress(params);
            return RestResponse.success().put("page", page);
        } else {
            //与我相关活动
            Page mypage = qkjvipMemberActivityService.queryAllSignAddressmain(params);
            return RestResponse.success().put("page", mypage);
        }
    }

    @RequestMapping("/queryAllcount")
    public RestResponse queryAllcount(@RequestParam Map<String, Object> params) {
        //已参加的活动
        List<QkjvipMemberActivityEntity> list=new ArrayList<>();
        params.put("ispri",0);
        String topClassShow = params.get("topClassShow")+"";
        Page page  = qkjvipMemberActivityService.queryAllSignAddress(params);
        Page mypage = qkjvipMemberActivityService.queryAllSignAddressmain(params);
        return RestResponse.success().put("page", page).put("pagetwo",mypage);
    }

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return RestResponse
     */
    @GetMapping("/list")
    @RequiresPermissions("qkjvip:memberactivity:list")
    public RestResponse list(@RequestParam Map<String, Object> params) {
        params.put("dataScope", getDataScopeContex("qkjvip:memberactivity:list", "t.adduser", "t.adddept"));
        Page page = qkjvipMemberActivityService.queryPage(params);

        return RestResponse.success().put("page", page);
    }
    @GetMapping("/listsum")
    public RestResponse listsum (@RequestParam Map<String, Object> params) {
        params.put("dataScope", getDataScopeContex("qkjvip:memberactivity:list", "t.adduser", "t.adddept"));
        Page page = qkjvipMemberActivityService.queryPageCount(params);

        return RestResponse.success().put("page", page);
    }

    /**
     * 根据主键查询详情
     *
     * @param id 主键
     * @return RestResponse
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("qkjvip:memberactivity:info")
    public RestResponse info(@PathVariable("id") String id) {
        QkjvipMemberActivityEntity qkjvipMemberActivity = qkjvipMemberActivityService.getById(id);
        Map<String, Object> map=new HashMap<String,Object>();
        map.put("activityId",id);
        qkjvipMemberActivity.setMbs(qkjvipMemberActivitymbsService.queryAll(map));
        //查询地址
        qkjvipMemberActivity.setAddresses(qkjvipMemberSignupaddressService.queryAll(map));

        return RestResponse.success().put("memberactivity", qkjvipMemberActivity);
    }

    /**
     * 根据主键查询详情
     *
     * @param params 主键
     * @return RestResponse
     */
    @RequestMapping("/infohtml")
    public RestResponse infohtml(@RequestParam Map<String, Object> params) {
        QkjvipMemberActivityEntity qkjvipMemberActivity=new QkjvipMemberActivityEntity();
        Map<String, Object> acmap=new HashMap<>();
        acmap.put("id",params.get("id").toString());
        List<QkjvipMemberActivityEntity> acs=new ArrayList<>();
        QkjvipMemberSignupEntity qsign=new QkjvipMemberSignupEntity();//报名信息
        acs=qkjvipMemberActivityService.queryAll(acmap);
        if(acs!=null&&acs.size()==1){
            qkjvipMemberActivity=acs.get(0);
        }
        Map<String, Object> map=new HashMap<String,Object>();
        map.put("activityId",params.get("id").toString());
        List<QkjvipMemberActivitymbsEntity> mmbs = new ArrayList<>();
        if(params.get("isqiandao") == null || !params.get("isqiandao").equals("1")) { //是报名才查询详情及地址
            mmbs = qkjvipMemberActivitymbsService.queryAll(map);
            if (mmbs != null && mmbs.size() > 0) {
                qkjvipMemberActivity.setMbs(mmbs);
            }

        }
        //查询地址
        List<QkjvipMemberSignupaddressEntity> addresses = new ArrayList<>();
        addresses = qkjvipMemberSignupaddressService.queryAll(map);
        if (addresses != null && addresses.size() > 0) {
            qkjvipMemberActivity.setAddresses(addresses);
        }
        int iscanjia=0;
        int isbaoming =0;
        List<MemberEntity> list=new ArrayList<>();
        if(params.get("myopenid")!=null && !params.get("myopenid").equals("")){//查询是否参加过本活动
            logger.info("查询活动详情：openid:" + params.get("myopenid"));
            acmap.clear();
            acmap.put("openid",params.get("myopenid"));
            if(params.get("isfollow")!=null && !params.get("isfollow").equals("")){
                acmap.put("isfollow",params.get("isfollow"));
            }
            list = memberService.selectMemberByOpenid(acmap);
            if(params.get("isqiandao") == null || !params.get("isqiandao").equals("1")){ //是报名
                Map<String, Object> mapt = new HashMap<>();
                map.put("myopenid",params.get("myopenid")+"");
                map.put("acitvityId",params.get("id").toString());
                List<QkjvipMemberSignupEntity> sgs=new ArrayList<>();
                sgs=qkjvipMemberSignupService.queryAll(map);
                if(sgs.size()>0){
                    iscanjia=1;
                }
            } else { //是否签到
                Map<String, Object> mapt = new HashMap<>();
                map.put("myopenid",params.get("myopenid")+"");
                map.put("acitvityId",params.get("id").toString());
                List<QkjvipMemberSignupEntity> sgs=new ArrayList<>();
                sgs=qkjvipMemberSignupService.queryAll(map);
                if(sgs.size()>0){
                    isbaoming=1;
                    qsign=sgs.get(0);
                }

                mapt.clear();
                mapt.put("myopenid", params.get("myopenid") + "");
                mapt.put("activityId",params.get("id").toString());
                List<QkjvipMemberSignupmemberEntity> listed = qkjvipMemberSignupmemberService.queryTopOne(mapt);
                if (listed.size() > 0) {
                    iscanjia = 1;
                    logger.info("已签到");
                }
            }

        }
        if(params.get("juerumemberid")!=null && !params.get("juerumemberid").equals("")){//根据觉如memberid查询是否参加过本活动
            acmap.clear();
            acmap.put("memberId",params.get("juerumemberid"));
            if(params.get("isfollow")!=null && !params.get("isfollow").equals("")){
                acmap.put("isfollow",params.get("isfollow"));
            }
            list = memberService.selectMemberByJuruMemberid(acmap);
            acmap.clear();
            Map<String, Object> mapt = new HashMap<>();
            if(params.get("isqiandao") == null || !params.get("isqiandao").equals("1")) { //是报名
                mapt.put("memberid", params.get("juerumemberid") + "");
                mapt.put("acitvityId", params.get("id").toString());
                List<QkjvipMemberSignupEntity> sgs = new ArrayList<>();
                sgs = qkjvipMemberSignupService.queryAll(mapt);
                if (sgs.size() > 0) {
                    iscanjia = 1;
                }
            } else { //是否签到
                mapt.clear();
                mapt.put("memberid", params.get("juerumemberid") + "");
                mapt.put("acitvityId", params.get("id").toString());
                List<QkjvipMemberSignupEntity> sgs = new ArrayList<>();
                sgs = qkjvipMemberSignupService.queryAll(mapt);
                if (sgs.size() > 0) {
                    isbaoming = 1;
                    qsign=sgs.get(0);
                }
                mapt.clear();
                mapt.put("memberId", params.get("juerumemberid") + "");
                mapt.put("activityId",params.get("id").toString());
                List<QkjvipMemberSignupmemberEntity> listed = qkjvipMemberSignupmemberService.queryTopOne(mapt);
                if (listed.size() > 0) {
                    iscanjia = 1;
                }
            }
        }
        String isabove = "0"; //未超出人数限制
        String isinvite="0";//未被邀请
        if(params.get("isqiandao") == null || !params.get("isqiandao").equals("1")) { //是报名才查询详情及地址
            if (qkjvipMemberActivity != null && qkjvipMemberActivity.getIspri() != null && (qkjvipMemberActivity.getPriPerson() != null && qkjvipMemberActivity.getPriPerson() > 0)) { //的活动(人数限制)
                if (qkjvipMemberActivity.getPriPerson() != null) {
                    if (qkjvipMemberActivity.getSignper() != null) { //报名人数
                        if (qkjvipMemberActivity.getSignper() >= qkjvipMemberActivity.getPriPerson()) {//报名人数小于限制人数
                            isabove = "1";
                        }
                    }
                }
            }
        }

        if (list != null && list.size() > 0) {
            String myop = list.get(0).getMemberId();
            map.clear();
            map.put("activityId", qkjvipMemberActivity.getId());
            map.put("memberId", myop);
            List<QkjvipMemberActivitymbsEntity> mbslist = new ArrayList<>();
            mbslist = qkjvipMemberActivitymbsService.queryTopOne(map);
            if (mbslist.size() > 0) {//有邀约
                isinvite = "1";
            }
        }

        return RestResponse.success().put("memberactivity", qkjvipMemberActivity).put("istake",iscanjia).put("isabove",isabove).put("isinvite",isinvite).put("list",list).put("isbaoming",isbaoming).put("qsign",qsign);
    }

    /**
     * 读取文件流
     */
    /**
     * 预览pdf文件
     * @param fileName
     */
    @RequestMapping("/preview/{fileName}")
    public RestResponse preview(@PathVariable("fileName")  String fileName) {

        //File file = new File("D:/temp/test01/0/"+fileName);
        if (1==1){

            System.out.println(1);
        }else{
        }
        return RestResponse.success();
    }

    @RequestMapping("/getpdfview")
    public RestResponse getpdfview(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity,HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<QkjvipMemberActivityEntity> list = new ArrayList<>();
        byte[] data = null;
        String fileaddres = qkjvipMemberActivity.getActivilog();
//        File file = new File(fileaddres);
//        FileInputStream input = new FileInputStream(file);
//        data = new byte[input.available()];
//        input.read(data);
//        response.getOutputStream().write(data);
//        input.close();
        OutputStream sos = response.getOutputStream();
        try {
            URL url = new URL(fileaddres);
            HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
            // 连接指定的网络资源
            httpUrl.connect();
            BufferedInputStream input = new BufferedInputStream(httpUrl.getInputStream());
            int b;
            while ((b = input.read())!= -1){
                sos.write(b);
            }
            response.flushBuffer();
            sos.close();
            input.close();
        } catch (Exception e) {
            logger.error("pdf文件处理异常：" + e.getMessage());
        }
//        OutputStream sos;
//        try {
//            sos = response.getOutputStream();
//            URL url = new URL(file);
//            HttpURLConnection httpUrl = (HttpURLConnection)url.openConnection();
//            // 连接指定的网络资源
//            httpUrl.connect();
//            BufferedInputStream input = new BufferedInputStream(httpUrl.getInputStream());
//            int b;
//            while ((b = input.read())!= -1){
//                sos.write(b);
//            }
//            response.flushBuffer();
//            sos.close();
//            input.close();
//        } catch (Exception e) {
//            logger.error("pdf文件处理异常：" + e.getMessage());
//        }

        return RestResponse.success().put("data",sos);
    }

    /**
     * 新增
     *
     * @param qkjvipMemberActivity qkjvipMemberActivity
     * @return RestResponse
     */
    @SysLog("新增")
    @RequestMapping("/save")
    @RequiresPermissions("qkjvip:memberactivity:save")
    public RestResponse save(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {
        qkjvipMemberActivity.setAdduser(getUserId());
        qkjvipMemberActivity.setAdddept(getOrgNo());
        qkjvipMemberActivity.setAddtime(new Date());
        qkjvipMemberActivity.setStatus(0);
        List<QkjvipMemberActivitymbsEntity> mbs=new ArrayList<>();
        mbs=qkjvipMemberActivity.getMbs();
        qkjvipMemberActivityService.add(qkjvipMemberActivity);
        //如果签到生成二维码
        try{
            String htmlur=qkjvipMemberActivity.getHtmlurl().substring(0,qkjvipMemberActivity.getHtmlurl().indexOf("#"));
            String url= QRCodeUtil.createQrCode(htmlur+"#/signmember?activityid="+qkjvipMemberActivity.getId(),300,".jpg");
            qkjvipMemberActivity.setIssignimg(url);
            //修改二维码
            qkjvipMemberActivityService.update(qkjvipMemberActivity);
        }catch (IOException e){

        }catch (WriterException e1){

        }

        if(mbs!=null&&mbs.size()>0){
            List<QkjvipMemberActivitymbsEntity> newmemList=new ArrayList<>();
            for(QkjvipMemberActivitymbsEntity m:mbs){
                m.setActivityId(qkjvipMemberActivity.getId());
                m.setStatus(1);//邀约
                newmemList.add(m);
            }
            qkjvipMemberActivitymbsService.batchAdd(mbs);
        }

        List<QkjvipMemberSignupaddressEntity> addresses=new ArrayList<>();
        addresses=qkjvipMemberActivity.getAddresses();
        if(addresses!=null&&addresses.size()>0){
            List<QkjvipMemberSignupaddressEntity> newList=new ArrayList<>();
            for(QkjvipMemberSignupaddressEntity m:addresses){
                m.setActivityid(qkjvipMemberActivity.getId());
                newList.add(m);
            }
            qkjvipMemberSignupaddressService.batchAdd(addresses);
        }

        return RestResponse.success();
    }

    /**
     * 修改
     *
     * @param qkjvipMemberActivity qkjvipMemberActivity
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/update")
    @RequiresPermissions("qkjvip:memberactivity:update")
    public RestResponse update(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {
        //如果签到生成二维码
        QkjvipMemberActivityEntity oldact = qkjvipMemberActivityService.getById(qkjvipMemberActivity.getId());
        if((oldact.getIssignimg()==null||oldact.getIssignimg().equals(""))&&qkjvipMemberActivity.getHtmlurl()!=null){
            try{
                String htmlur=qkjvipMemberActivity.getHtmlurl().substring(0,qkjvipMemberActivity.getHtmlurl().indexOf("#"));
//                String redirect_uri = URLEncoder.encode("http://www.baidu.com", "GBK");
//                String wxurl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7d6749694e2f73b7&redirect_uri=http://zzjx.qkj.com.cn&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
//
//                String url= QRCodeUtil.createQrCode(wxurl,90,".jpg");
                String url= QRCodeUtil.createQrCode(htmlur+"#/signmember?activityid="+qkjvipMemberActivity.getId(),300,".jpg");
                qkjvipMemberActivity.setIssignimg(url);
            }catch (IOException e){

            }catch (WriterException e1){

            }

            //System.out.println(erweima);
        }

        List<QkjvipMemberActivitymbsEntity> mbs=new ArrayList<>();
        mbs=qkjvipMemberActivity.getMbs();
        qkjvipMemberActivityService.update(qkjvipMemberActivity);

        if(mbs!=null&&mbs.size()>0){
            //删除
            qkjvipMemberActivitymbsService.deleteBatchByOrder(qkjvipMemberActivity.getId());
            List<QkjvipMemberActivitymbsEntity> newmemList=new ArrayList<>();
            for(QkjvipMemberActivitymbsEntity m:mbs){
                m.setActivityId(qkjvipMemberActivity.getId());
                m.setStatus(1);//邀约
                newmemList.add(m);
            }
            qkjvipMemberActivitymbsService.batchAdd(mbs);
        }

        List<QkjvipMemberSignupaddressEntity> addresses=new ArrayList<>();
        addresses=qkjvipMemberActivity.getAddresses();
        if(addresses!=null&&addresses.size()>0){
            //删除
            qkjvipMemberSignupaddressService.deleteBatchByOrder(qkjvipMemberActivity.getId());
            List<QkjvipMemberSignupaddressEntity> newList=new ArrayList<>();
            for(QkjvipMemberSignupaddressEntity m:addresses){
                m.setActivityid(qkjvipMemberActivity.getId());
                newList.add(m);
            }
            qkjvipMemberSignupaddressService.batchAdd(addresses);
        }
        return RestResponse.success();
    }


    /**
     * 修改
     *
     * @param qkjvipMemberActivity qkjvipMemberActivity
     * @return RestResponse
     */
    @SysLog("修改")
    @RequestMapping("/updatembs")
    public RestResponse updatembs(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {
        //QkjvipMemberActivityEntity oldact = qkjvipMemberActivityService.getById(qkjvipMemberActivity.getId());

        List<QkjvipMemberActivitymbsEntity> mbs=new ArrayList<>();
        mbs=qkjvipMemberActivity.getMbs();
        if(mbs!=null&&mbs.size()>0){
            List<QkjvipMemberActivitymbsEntity> newmemList=new ArrayList<>();
            for(QkjvipMemberActivitymbsEntity m:mbs){
                //判断是否有此会员邀约
                //是否存在记录
                Map<String, Object> map=new HashMap<String,Object>();
                map.put("activityId",qkjvipMemberActivity.getId());
                map.put("memberId",m.getMemberId());
                List<QkjvipMemberActivitymbsEntity> mbslist = new ArrayList<>();
                mbslist=qkjvipMemberActivitymbsService.queryTopOne(map);
                if(mbslist.size()<=0){//无邀约
                    m.setActivityId(qkjvipMemberActivity.getId());
                    m.setStatus(3);//现场
                    newmemList.add(m);
                    qkjvipMemberActivitymbsService.add(m);
                }
                if (qkjvipMemberActivity.getIsbackqd()!=null && qkjvipMemberActivity.getIsbackqd()==1) {
                    // 补报名
                    String bmid=qkjvipMemberSignupService.supadd(qkjvipMemberActivity.getId(),m.getMemberId(),"");
                    //签到
                    Date date=new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date2=sdf.format(date);
                    Map<String, Object> params=new HashMap<String,Object>();
                    params.put("memberId",m.getMemberId());
                    params.put("activityId",qkjvipMemberActivity.getId());
                    List<QkjvipMemberSignupmemberEntity> list = qkjvipMemberSignupmemberService.queryTopOne(params);
                    if(list.size()>0) {//已签到
                    } else {
                        QkjvipMemberSignupmemberEntity qkjvipMemberSignupmember=new QkjvipMemberSignupmemberEntity();
                        qkjvipMemberSignupmember.setMemberId(m.getMemberId());
                        qkjvipMemberSignupmember.setTime(date2);
                        qkjvipMemberSignupmember.setSignupId(bmid);
                        qkjvipMemberSignupmember.setActivityId(qkjvipMemberActivity.getId());
                        qkjvipMemberSignupmemberService.add(qkjvipMemberSignupmember);
                    }
                }

            }
            //qkjvipMemberActivitymbsService.batchAdd(mbs);

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
    @RequiresPermissions("qkjvip:memberactivity:delete")
    public RestResponse delete(@RequestBody String[] ids) {
        qkjvipMemberActivityService.deleteBatch(ids);

        return RestResponse.success();
    }

    @SysLog("发短信")
    @RequestMapping("/sendMsg")
    public RestResponse sendMsg(@RequestBody QkjvipMemberActivityEntity qkjvipMemberActivity) {
        String url=qkjvipMemberActivity.getUrl()+qkjvipMemberActivity.getId();
        //查询所有邀约人员
        List<QkjvipMemberActivitymbsEntity> mbs=new ArrayList<>();
        Map<String, Object> map=new HashMap<String,Object>();
        map.put("activityId",qkjvipMemberActivity.getId());
        mbs=qkjvipMemberActivitymbsService.queryAll(map);
        if(mbs.size()>0){
            //发短信
            SysSmsLogEntity smsLog=new SysSmsLogEntity();
            smsLog.setContent(qkjvipMemberActivity.getMsgcontent()+"戳我直达："+url);
            //smsLog.setMobile(a.getMobile());
            smsLog.setMobile("18810242427");
            SysSmsLogEntity sysSmsLogEntity = sysSmsLogService.sendSms(smsLog);
//            for(QkjvipMemberActivitymbsEntity a:mbs){
//                if(a!=null&&a.getMobile()!=null&&!a.getMobile().equals("")){
//                    //发短信
//                    SysSmsLogEntity smsLog=new SysSmsLogEntity();
//                    smsLog.setContent(qkjvipMemberActivity.getMsgcontent()+"戳我直达："+url);
//                    //smsLog.setMobile(a.getMobile());
//                    smsLog.setMobile("18810242427");
//                    SysSmsLogEntity sysSmsLogEntity = sysSmsLogService.sendSms(smsLog);
//                }
//            }
        }
        return RestResponse.success();
    }

    @GetMapping("/permChannelList")
    public RestResponse permChannelList(@RequestParam Map<String, Object> params) {
        String channelIds = "";
        channelIds = sysUserChannelService.queryChannelIdByUserId(getUserId());
        params.put("queryPermission", "all");
        if ("0".equals(channelIds) || getUser().getUserName().contains("admin")) {

        } else{
            params.put("userId", getUserId());
        }

        params.put("iscore",1);
        List<SysUserChannelEntity> permChannelList = sysUserChannelService.queryPermissionChannels(params);

        return RestResponse.success().put("list", permChannelList);
    }

    /**
     * 导入会员数据
     */
    @SysLog("导入会员")
    @RequestMapping("/import")
    public RestResponse importExcel(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        List<MemberEntity> mees=new ArrayList<>();
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException("请选择要导入的文件");
        } else {
            //插入impont 表
            try {
                List<QkjvipMemberImportEntity> list = ExportExcelUtils.importExcel(file, 1, 2,QkjvipMemberImportEntity.class);
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setAddUser(getUserId());
                    list.get(i).setAddDept(getOrgNo());
                    list.get(i).setAddTime(new Date());
                    list.get(i).setOfflineflag(1);
                }
                if (list.size() > 0) {
                    qkjvipMemberImportService.addBatch(list); //批量导入临时表
                    long start, end2;
                    start = System.currentTimeMillis();
                    //调用数据清洗接口
                    Object objList = JSONArray.toJSON(list);
                    String memberJsonStr = JsonHelper.toJsonString(objList, "yyyy-MM-dd HH:mm:ss");
                    String resultPost = HttpClient.sendPost(Vars.MEMBER_IMPORT_URL, memberJsonStr);

                    JSONObject resultObject = JSON.parseObject(resultPost);
                    if (!"200".equals(resultObject.get("resultcode").toString())) {  //清洗失败
                        return RestResponse.error(resultObject.get("descr").toString());
                    }else {
                        mees=JSON.parseArray(resultObject.getString("listmember"),MemberEntity.class);
                    }
                    end2 = System.currentTimeMillis();
                    System.out.println("批量处理n条数据，耗费了" + (end2 - start) + "ms");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return RestResponse.success().put("memberlist", mees);
    }

    /**
     * 查看活动是否对存在to韩洁
     *
     * @param
     * @return RestResponse
     */
    @RequestMapping("/actityisexist")
    public RestResponse actityisexist() {
        List<QkjvipMemberActivityEntity> list = qkjvipMemberActivityService.actityisexist();
        String oneflag = "";
        String twoflag = "";
        String threeflag = "";
        List<QkjvipIsExitEntity> islist =new ArrayList<>();
        for (QkjvipMemberActivityEntity ae : list) {
            if(ae!=null&&ae.getAtype()!=null&&ae.getAtype().equals("1")){ //品鉴会
                oneflag = ae.getId();
                break;
            }
        }

        for (QkjvipMemberActivityEntity ae : list) {
            if(ae!=null&&ae.getAtype()!=null&&ae.getAtype().equals("4")){ //回场游
                twoflag = ae.getId();
                break;
            }
        }

        for (QkjvipMemberActivityEntity ae : list) {
            if(ae!=null&&ae.getAtype()!=null&&ae.getAtype().equals("6")){ //新品试饮
                threeflag = ae.getId();
                break;
            }
        }
        QkjvipIsExitEntity isex= new QkjvipIsExitEntity();
        isex.setAtype("1");
        String surl="";
        if (oneflag!=null&&!oneflag.equals("")){//有单据
            surl = "/components/newWebview/webview?name=" + URLEncoder.encode("详情") + "&url=" + URLEncoder.encode("https://crm.qkj.com.cn/#/activity?id=" + oneflag);
            isex.setHtmlurl(surl);
        }
        islist.add(isex);

        isex= new QkjvipIsExitEntity();
        isex.setAtype("4");
        surl="";
        if (twoflag!=null&&!twoflag.equals("")){//有单据
            surl = "/components/newWebview/webview?name=" + URLEncoder.encode("详情") + "&url=" + URLEncoder.encode("https://crm.qkj.com.cn/#/activity?id=" + twoflag);
            isex.setHtmlurl(surl);
        }
        islist.add(isex);

        isex= new QkjvipIsExitEntity();
        isex.setAtype("6");
        surl="";
        if (threeflag!=null&&!threeflag.equals("")){//有单据
            surl = "/components/newWebview/webview?name=" + URLEncoder.encode("详情") + "&url=" + URLEncoder.encode("https://crm.qkj.com.cn/#/activity?id=" + threeflag);
            isex.setHtmlurl(surl);
        }
        islist.add(isex);

        return RestResponse.success().put("actitylist", islist);
    }

    public static String getIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 百度获取城市信息
     *
     * @param
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static void main(String[] args) throws JSONException, IOException {
//        HttpServletRequest request = getHttpServletRequest();
//        String ip = getIp(request);
//        // 百度地图申请的ak
//        String ak = "Ed5GYHTqRm1E5VZgHB6jm7Qz2vckMfQg";
//        // 这里调用百度的ip定位api服务 详见 http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
//        JSONObject json = readJsonFromUrl("http://api.map.baidu.com/location/ip?ip=" + ip + "&ak=" + ak);
//
//        //这里只取出了两个参数，根据自己需求去获取
//        JSONObject obj = (JSONObject) ((JSONObject) json.get("content")).get("address_detail");
//        String province = obj.getString("province");
//        System.out.println(province);
//
//        JSONObject obj2 = (JSONObject) json.get("content");
//        String address = obj2.getString("address");
//        System.out.println(address);
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    /**
     * 创建链接
     *
     * @param url
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = JSONObject.parseObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * 读取
     *
     * @param rd
     * @return
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
