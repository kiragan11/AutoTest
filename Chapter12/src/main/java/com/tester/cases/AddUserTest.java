package com.tester.cases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tester.config.TestConfig;
import com.tester.model.AddUserCase;
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
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddUserTest {

    @Test(dependsOnGroups = "loginTrue", description = "添加用户")
    public void addUser() throws IOException, InterruptedException {
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);

        //查case
        AddUserCase addUserCase = sqlSession
                .selectOne("addUserCase", TestConfig.getCaseParam("addUser"));
        //将User表已经测过的数据置为无效
        sqlSession.update("updateAddedUser",addUserCase);


        //把case传给接口，请求
        String result = getResult(addUserCase);
        System.out.println(result);

        //判断是否插入成功，对比
        //1、给接口请求时间，确保数据插入成功
        Thread.sleep(3600);
        //2、执行addUser接口之后，查询用户是否添加成功

        User user = sqlSession.selectOne("getAddUser", addUserCase);

        //3、根据查询结果，对比预期值
        if (!Objects.isNull(user)) {
            Assert.assertEquals(result, addUserCase.getExpected());
        } else {
            Assert.fail("添加用户失败");
        }

    }

    private String getResult(AddUserCase addUserCase) throws IOException {

        ResourceBundle resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
        String url = resourceBundle.getString("test.url") + resourceBundle.getString("addUser.uri");

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonObjectStr = gson.toJson(addUserCase);
        StringEntity reqEntity = new StringEntity(jsonObjectStr, "utf-8");
        httpPost.setEntity(reqEntity);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(TestConfig.cookieStore).build();
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = "";
        try {
            HttpEntity resEntity = response.getEntity();
            result = EntityUtils.toString(resEntity, "utf-8");
        } finally {
            response.close();
        }
        return result;
    }
}
