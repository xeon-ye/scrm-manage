/*
 * 项目名称:platform-plus
 * 类名称:MyTest.java
 * 包名称:com.platform
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/8/17 17:40            liuqianru    初版做成
 *
 */
package com.platform;

import com.platform.common.utils.JwtUtils;
import com.platform.modules.util.HttpClient;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * MyTest
 *
 * @author liuqianru
 * @date 2020/8/17 17:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
    @Autowired
    private JwtUtils jwtUtils;
    private WxMpService wxService;

    @Test
    public void main() throws WxErrorException {
        /*获取access_token start*/
//        String appid = "wxc64e4c7b44af0595";
//        String appsecret = "3fd8281d244ecdc38b77bd4ad7cdea28";
//        List<NameValuePair> list = new ArrayList<>();
//        list.add(new BasicNameValuePair("grant_type","client_credential"));
//        list.add(new BasicNameValuePair("appid","wxc64e4c7b44af0595"));
//        list.add(new BasicNameValuePair("secret","3fd8281d244ecdc38b77bd4ad7cdea28"));
//        String url = "https://api.weixin.qq.com/cgi-bin/token";
//        String strResult = HttpClient.doHttpGet(url, list);
        /*获取access_token end*/

        /*获取粉丝列表 start*/
        String url = "https://api.weixin.qq.com/cgi-bin/user/get";
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("access_token","36_enDka3cZ_eXqCovbs8wL-bdaya1VWMpJs9hrqUMrDOcq3DTZlat3UTALNrQidUaxTdsuNUWsMQ_9262Rr8f3Ie1hG3MU4XAX7sXdMmuPE7Ie2Y-voxo9EEHlLuelqL4wyVUG4VeOFphzsvY9OYOjACAEOE"));
        String strResult = HttpClient.doGet(url, list);
        System.out.print(strResult);
        /*获取粉丝列表 end*/
    }
}
