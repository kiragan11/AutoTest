package com.tester.cases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tester.config.TestConfig;
import com.tester.model.UpdateUserCase;
import com.tester.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateUserTest {

    @Test(dependsOnGroups = "loginTrue",description = "更新用户信息")
    public void updateUser() throws IOException {
        //创建一个数据连接
        String resource = "mybatis-config.xml";
        Reader reader= Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //查询case，用case请求接口，返回结果
        UpdateUserCase updateUserCase = sqlSession
                .selectOne("updateUserCase", TestConfig.getCaseParam("updateUser"));
        String result = getResult(updateUserCase);

        //查预期结果
        User user = sqlSession
                .selectOne(updateUserCase.getExpected(),updateUserCase);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String expected = gson.toJson(user);


        System.out.println(result);
        System.out.println(expected);

        Assert.assertNotNull(expected);
    }

    @Test(dependsOnGroups = "loginTrue",description = "删除用户")
    public void deleteUser(){

    }

    private String getResult(UpdateUserCase updateUserCase) throws IOException {
        //定义一个post请求
        ResourceBundle resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
        String url = resourceBundle.getString("test.url")+resourceBundle.getString("updateUser.uri");
        HttpPost httpPost = new HttpPost(url);

        //设置请求头
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");

        //设置请求体
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String case2Json = gson.toJson(updateUserCase);
        StringEntity reqEntity = new StringEntity(case2Json,"utf-8");
        httpPost.setEntity(reqEntity);

        //设置cookie，执行post
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //返回结果
        String result="";
        try {
            HttpEntity resEntity = response.getEntity();
            result= EntityUtils.toString(resEntity,"utf-8");
        }finally {
            response.close();
        }
        return result;
    }
}
