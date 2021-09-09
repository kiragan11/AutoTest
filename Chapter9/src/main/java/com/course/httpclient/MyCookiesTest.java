package com.course.httpclient;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

public class MyCookiesTest {

    private String url;
    private ResourceBundle resourceBundle;
    private CookieStore cookieStore;

    @BeforeTest
    public void beforeTest(){
        resourceBundle = ResourceBundle.getBundle("app", Locale.CHINA);
        url = resourceBundle.getString("test.url");
    }

    @Test
    public void testGetCookies() throws IOException {
        String uri = resourceBundle.getString("getcookies.uri");

        cookieStore = new BasicCookieStore();
        HttpClient httpClient=HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        HttpGet httpGet = new HttpGet(url+uri);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        String result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
        System.out.println(result);

        //获取cookies
        List<Cookie> cookieList = cookieStore.getCookies();
        for (Cookie cookie:cookieList){
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println(name + "="+value);
        }
    }

    @Test(dependsOnMethods = "testGetCookies")
    public void testGetWithCookies() throws IOException {
        String uri = resourceBundle.getString("getwithcookies.uri");
        HttpGet httpGet = new HttpGet(url+uri);

        //设置cookies
        HttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

        HttpResponse httpResponse = httpClient.execute(httpGet);

       int statusCode= httpResponse.getStatusLine().getStatusCode();
        System.out.println("statusCode="+statusCode);
       if (statusCode==200){
           String result= EntityUtils.toString(httpResponse.getEntity(),"utf-8");
           System.out.println(result);
       }


    }

    @Test(dependsOnMethods = "testGetCookies")
    public void testPostWithCookies() throws IOException {
        //声明一个带CookieStore的client对象，用来执行方法
        HttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        //获取uri
        String uri = resourceBundle.getString("postwithcookies.uri");
        //声明一个方法
        HttpPost httpPost = new HttpPost(url+uri);
        //设置请求头header
        httpPost.setHeader("Content-Type","application/json; charset=utf-8");
        //设置参数
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name","kiragan");
        jsonObject.addProperty("age","33");
        //参数转为字符串
        String jsonString = jsonObject.toString();
        System.out.println(jsonString);
        //添加参数到post
        StringEntity stringEntity = new StringEntity(jsonString);
        httpPost.setEntity(stringEntity);
        //执行方法
        HttpResponse httpResponse=httpClient.execute(httpPost);
        //获取结果
        String result=EntityUtils.toString(httpResponse.getEntity(),"utf-8");
        System.out.println(result);
        //将响应结果转为json
        Gson gson = new Gson();
        JsonObject resultJsonObject = gson.fromJson(result,JsonObject.class);
        //取得结果，比较结果
        Assert.assertEquals(resultJsonObject.get("message").getAsString(),"success");
        Assert.assertEquals(resultJsonObject.get("code").getAsString(),"1");

    }
}
