package com.tester.cases;

import com.google.gson.JsonObject;
import com.tester.config.TestConfig;
import com.tester.model.LoginCase;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginTest {

    @Test(groups = "loginTrue",description = "登录成功" )
    public void loginTrue() throws IOException {
        //创建一个sql连接
        //1、创建一个SqlSessionFactory
        String resource =  "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //2、创建一个SqlSession，用来建立数据库连接
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //从表里查询登录成功的case
        LoginCase loginCase = sqlSession
                .selectOne("loginCase",TestConfig.getCaseParam("loginTrue"));

        //打印case
        System.out.println(loginCase.toString());

        //把case传入到接口，获取接口返回的数据
        String result = getResult(loginCase);

        //比较 接口实际返回的数据与case的预期expected
        Assert.assertEquals(result,loginCase.getExpected());
    }

    @Test(groups = "loginFalse",description = "登录失败")
    public void loginFalse() throws IOException {
        //创建一个sql连接
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(reader);

        //创建一个SqlSession用来建立数据库连接
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //从表里查询登录失败的case
        LoginCase loginCase = sqlSession
                .selectOne("loginCase",TestConfig.getCaseParam("loginFalse"));

        //打印case
        System.out.println(loginCase.toString());

        //把case传入到接口，获取接口返回的数据
        String result = getResult(loginCase);

        //比较 接口实际返回的数据与case的预期expected
        Assert.assertEquals(result,loginCase.getExpected());
    }

    //获取接口返回结果
    private String getResult(LoginCase loginCase) throws IOException {
        //获取请求url
        //1、创建一个ResourceBundle对象来获取application配置文件中的地址
        ResourceBundle resourceBundle=ResourceBundle.getBundle("application", Locale.CHINA);
        //2、获取资源文件里面的url和uri，拼接
        String url = resourceBundle.getString("test.url")+resourceBundle.getString("login.uri");

        //创建一个POST请求
        HttpPost httpPost = new HttpPost(url);

        //给post请求设置请求头
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");

        //设置请求体
        //1、创建一个json对象
        JsonObject jsonObject = new JsonObject();
        //2、给json对象添加请求参数
        jsonObject.addProperty("username",loginCase.getUsername());
        jsonObject.addProperty("password",loginCase.getPassword());
        //3、json对象转为string
        String jsonObjectStr = jsonObject.toString();
        //4、创建一个请求体对象，将json传入
        StringEntity reqEntity = new StringEntity(jsonObjectStr,"utf-8");
        //5、给post请求设置请求体
        httpPost.setEntity(reqEntity);

        //执行post请求
        //1、new一个BasicCookieStore,赋值给已经定义好的cookieStore变量
        CookieStore cookieStore = new BasicCookieStore();
        //2、创建一个带cookieStore的client用来执行post
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();
        //CloseableHttp
        //3、执行post，返回一个response
        CloseableHttpResponse response= httpClient.execute(httpPost);

        //获取response返回体结果
        //1、设置一个变量来存返回结果
        String result ="";
        try{
            //2、获取response返回体
            HttpEntity resEntity = response.getEntity();
            //3、将返回体转为string，赋值给result
            result=EntityUtils.toString(resEntity,"utf-8");
        }finally {
            //4、关闭response
            response.close();
        }

        //打印cookies
        //1、从cookieStore中获取cookies
        TestConfig.cookieStore = cookieStore;
        List<Cookie> cookies =TestConfig.cookieStore.getCookies();
        //2、遍历cookies，打印
        for (Cookie cookie :cookies){
            System.out.println(cookie.getName()+"="+cookie.getValue());
        }

        return result;
    }
}

