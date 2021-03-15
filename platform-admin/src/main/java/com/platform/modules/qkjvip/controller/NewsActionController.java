/*
 * 项目名称:platform-plus
 * 类名称:NewsActionController.java
 * 包名称:com.platform.modules.qkjvip.controller
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2021/3/11 13:09            liuqianru    初版做成
 *
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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * NewsActionController
 *
 * @author liuqianru
 * @date 2021/3/11 13:09
 */
@RestController
@RequestMapping("/qkjvip/news")
public class NewsActionController extends AbstractController {
    /**
     * 新闻列表
     *
     * @param newsQueryEntity 查询参数
     * @return RestResponse
     */
    @PostMapping("/newslist")
    public RestResponse list(@RequestBody NewsQueryEntity newsQueryEntity) throws IOException {
        List<NewsEntity> list = new ArrayList<>();
        Object obj = JSONArray.toJSON(newsQueryEntity);
        String queryJsonStr = JsonHelper.toJsonString(obj);

        String resultPost = HttpClient.sendPost(Vars.NEWS_LIST_URl, queryJsonStr);
        System.out.println("新闻列表检索条件：" + queryJsonStr);
        JSONObject resultObject = JSON.parseObject(resultPost);
        if ("200".equals(resultObject.get("resultcode").toString())) {  //调用成功
            if (resultObject.get("newslist") != null) {
                list = JSON.parseArray(resultObject.getString("newslist"), NewsEntity.class);
            }
        }
        return RestResponse.success().put("newslist", list);
    }
}
