package com.course.testng.parameter;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class DataProviderTest {

    @Test(dataProvider = "test1")
    public void test1(String name, int age) {
        System.out.println("name=" + name + "，age=" + age);
    }

    @DataProvider(name = "test1")
    public Object[][] providerData() {
        return new Object[][]{
                {"zhangsan", 10},
                {"lisi", 20}
        };
    }

    @Test(dataProvider = "methoddata")
    public void test2(String name, int age) {
        System.out.println("name=" + name + "，age=" + age);
    }

    @Test(dataProvider = "methoddata")
    public void test3(String name, int age) {
        System.out.println("name=" + name + "，age=" + age);
    }

    @DataProvider(name = "methoddata")
    public Object[][] methodData(Method method) {
        Object[][] o = null;
        if (method.getName().equals("test2")) {
            o = new Object[][]{
                    {"zhangsan", 10},
                    {"lisi", 20},
            };
        } else if (method.getName().equals("test3")) {
            o = new Object[][]{
                    {"wangwu", 30},
                    {"sunli", 20},
            };
        }
        return o;
    }
}
