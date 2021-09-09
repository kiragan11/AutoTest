package com.tester.cases;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tester.config.TestConfig;
import com.tester.model.GetUserInfoCase;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GetUserInfoTest {

    @Test(dependsOnGroups = "loginTrue",description = "获取用户信息")
    public void getUserInfo() throws IOException {
        //建立sql连接
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //查询获取用户信息的case
        GetUserInfoCase getUserInfoCase = sqlSession
                .selectOne("getUserInfoCase", TestConfig.getCaseParam("getUserInfo"));

        //case传给接口，返回请求结果
        String result = getResult(getUserInfoCase);
//        JsonArray result2JsonArray = JsonParser.parseString(result).getAsJsonArray();
//        String actual =result2JsonArray.toString();

        //预期结果
        User user = sqlSession.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        String expected = new Gson().toJson(userList);

        System.out.println(result);
//        System.out.println(actual);
        System.out.println(expected);

        Assert.assertEquals(result,expected);

    }

    private String getResult(GetUserInfoCase getUserInfoCase) throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
        String url = resourceBundle.getString("test.url")+resourceBundle.getString("getUserInfo.uri");

        HttpPost httpPost = new HttpPost(url);
        //请求头
        httpPost.setHeader("Content-Type","application/json;charset=utf-8");
        //请求体
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",getUserInfoCase.getUserId());
        String jsonObjectStr = jsonObject.toString();
        StringEntity reqEntity = new StringEntity(jsonObjectStr,"utf-8");
        httpPost.setEntity(reqEntity);

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(TestConfig.cookieStore).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String result ="";
        try {
            HttpEntity resEntity =response.getEntity();
            result = EntityUtils.toString(resEntity,"utf-8");
        }finally {
            response.close();
        }
        return result;
    }
}
