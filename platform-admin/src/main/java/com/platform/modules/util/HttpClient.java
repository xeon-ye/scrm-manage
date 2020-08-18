/*
 * 项目名称:platform-plus
 * 类名称:HttpClient.java
 * 包名称:com.platform.modules.util
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020/8/6 10:14            liuqianru    初版做成
 *
 */
package com.platform.modules.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * HttpClient 使用httpclient调用外部接口
 *
 * @author liuqianru
 * @date 2020/8/6 10:14
 */
public class HttpClient {
    /**
     * get 请求
     * @return
     */
    public static String doGet(String url, List<NameValuePair> params){
        String result = null;
        //1.获取httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //接口返回结果
        CloseableHttpResponse response = null;
        String paramStr = null;
        try {
            paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params));
            //拼接参数
            StringBuffer sb = new StringBuffer();
            sb.append(url);
            sb.append("?");
            sb.append(paramStr);
            //2.创建get请求
            HttpGet httpGet = new HttpGet(sb.toString());
            //3.设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            /*此处可以添加一些请求头信息，例如：
            httpGet.addHeader("content-type","text/xml");*/
            //4.提交参数
            response = httpClient.execute(httpGet);
            //5.得到响应信息
            int statusCode = response.getStatusLine().getStatusCode();
            //6.判断响应信息是否正确
            if(HttpStatus.SC_OK != statusCode){
                //终止并抛出异常
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            //7.转换成实体类
            HttpEntity entity = response.getEntity();
            if(null != entity){
                result = EntityUtils.toString(entity);
            }
            EntityUtils.consume(entity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //8.关闭所有资源连接
            if(null != response){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != httpClient){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * http post 请求
     */
    public static String doPost(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        URI uri = null;
        try {
            uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpPost httpRequest = new HttpPost(uri);
        HttpResponse response = new DefaultHttpClient().execute(httpRequest);
        if (response.getStatusLine().getStatusCode() == 200) {// 获取调用api返回数据
            // doing something
            InputStream content = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(content, "UTF-8");
            BufferedReader bufr = new BufferedReader(reader);// 缓冲
            String str;
            while ((str = bufr.readLine()) != null) {
                return str;
            }
        }
        return null;
    }
}
