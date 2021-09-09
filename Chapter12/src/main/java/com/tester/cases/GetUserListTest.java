package com.tester.cases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tester.config.TestConfig;
import com.tester.model.GetUserListCase;
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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GetUserListTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取用户列表")
    public void getUserList() throws IOException {
        //建立数据库连接
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //查case，用来请求接口，返回数据
        GetUserListCase getUserListCase=sqlSession
                .selectOne("getUserListCase", TestConfig.getCaseParam("getUserList"));
        String result = getResult(getUserListCase);


        //查预期结果
        List<User> userList = sqlSession.selectList(getUserListCase.getExpected(),getUserListCase);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String expected = gson.toJson(userList);

        System.out.println(result);
        System.out.println(expected);

        Assert.assertEquals(result,expected);

    }

    private String getResult(GetUserListCase getUserListCase) throws IOException {
        ResourceBundle resourceBundle=ResourceBundle.getBundle("application", Locale.CHINA);
        String url = resourceBundle.getString("test.url")+resourceBundle.getString("getUserList.uri");
        HttpPost httpPost = new HttpPost(url);

        //设置请求头
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");
        //设置请求体
        Gson gson= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String case2Json = gson.toJson(getUserListCase);
        StringEntity reqEntity = new StringEntity(case2Json,"utf-8");
        httpPost.setEntity(reqEntity);

        //设置cookie，执行post
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(TestConfig.cookieStore).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String result="";
        try{
            HttpEntity resEntity = response.getEntity();
            result = EntityUtils.toString(resEntity,"utf-8");
        }finally {
            response.close();
        }

        return result;
    }
}
