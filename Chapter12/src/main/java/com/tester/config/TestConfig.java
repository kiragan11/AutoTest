package com.tester.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.ResourceBundle;


public class TestConfig {

    //用来存储cookies信息的变量
    public static CookieStore cookieStore;

    //cases.properties文件存放查询case时，sql中的条件参数值
    public static int getCaseParam(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("cases");
        return Integer.parseInt(resourceBundle.getString(key));
    }

}
