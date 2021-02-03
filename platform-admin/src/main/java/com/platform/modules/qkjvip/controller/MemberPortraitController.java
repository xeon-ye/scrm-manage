/*
 * 项目名称:platform-plus
 * 类名称:SysUserController.java
 * 包名称:com.platform.modules.sys.controller
 *
 * 修改履历:
 *      日期                修正者      主要内容
 *      2021-02-02 10:33    liuqianru      初版完成
 *
 * Copyright (c) 2019-2019 微同软件
 */
package com.platform.modules.qkjvip.controller;

import cn.emay.util.JsonHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.common.utils.RestResponse;
import com.platform.modules.qkjvip.entity.*;
import com.platform.modules.sys.controller.AbstractController;
import com.platform.modules.util.HttpClient;
import com.platform.modules.util.Vars;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller
 *
 * @author liuqianru
 * @date 2021-02-02 10:33:22
 */
@RestController
@RequestMapping("/qkjvip/memberportrait")
public class MemberPortraitController extends AbstractController {

    /**
     * 性别统计
     *
     * @param memberPortraitSexEntity 查询参数
     * @return RestResponse
     */
    @PostMapping("/getMemberSexReport")
    public RestResponse getMemberSexReport(@RequestBody MemberPortraitSexEntity memberPortraitSexEntity) throws IOException {
        List<MemberPortraitSexEntity> list = new ArrayList<>();
        Object obj = JSONArray.toJSON(memberPortraitSexEntity);
        String queryJsonStr = JsonHelper.toJsonString(obj);

        String resultPost = HttpClient.sendPost(Vars.MEMBER_PORTRAIT_SEX_URl, queryJsonStr);
        System.out.println("性别统计检索条件：" + queryJsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //调用成功
            list = JSON.parseArray(resultObject.getString("list"),MemberPortraitSexEntity.class);
        }
        return RestResponse.success().put("list", list);
    }

    /**
     * 年龄统计
     *
     * @param memberPortraitAgeEntity 查询参数
     * @return RestResponse
     */
    @PostMapping("/getMemberAgeReport")
    public RestResponse getMemberAgeReport(@RequestBody MemberPortraitAgeEntity memberPortraitAgeEntity) throws IOException {
        List<MemberPortraitAgeEntity> list = new ArrayList<>();
        Object obj = JSONArray.toJSON(memberPortraitAgeEntity);
        String queryJsonStr = JsonHelper.toJsonString(obj);

        String resultPost = HttpClient.sendPost(Vars.MEMBER_PORTRAIT_AGE_URl, queryJsonStr);
        System.out.println("年龄统计检索条件：" + queryJsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //调用成功
            list = JSON.parseArray(resultObject.getString("list"),MemberPortraitAgeEntity.class);
        }
        return RestResponse.success().put("list", list);
    }

    /**
     * 地区统计
     *
     * @param memberPortraitAreaEntity 查询参数
     * @return RestResponse
     */
    @PostMapping("/getMemberCityReport")
    public RestResponse getMemberCityReport(@RequestBody MemberPortraitAreaEntity memberPortraitAreaEntity) throws IOException {
        List<MemberPortraitAreaEntity> list = new ArrayList<>();
        Object obj = JSONArray.toJSON(memberPortraitAreaEntity);
        String queryJsonStr = JsonHelper.toJsonString(obj);

        String resultPost = HttpClient.sendPost(Vars.MEMBER_PORTRAIT_AREA_URl, queryJsonStr);
        System.out.println("地区统计检索条件：" + queryJsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //调用成功
            list = JSON.parseArray(resultObject.getString("list"),MemberPortraitAreaEntity.class);
            if (!memberPortraitAreaEntity.getIsall()) {  //true:分页查询
                Page page = new Page();
                page.setRecords(list);
                page.setTotal(Long.parseLong(resultObject.get("totalcount").toString()));
                page.setSize(memberPortraitAreaEntity.getPagesize() == null? 0 : memberPortraitAreaEntity.getPagesize());
                page.setCurrent(memberPortraitAreaEntity.getPageindex() == null? 0 : memberPortraitAreaEntity.getPageindex());
                return RestResponse.success().put("page", page);
            }
        }
        return RestResponse.success().put("list", list);
    }

}
