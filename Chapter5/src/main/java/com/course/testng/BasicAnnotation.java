package com.course.testng;

import org.testng.annotations.*;

public class BasicAnnotation {

    @Test
    public void testCase1(){
        System.out.println("这是测试用例1");
    }

    @Test
    public void testCase2(){
        System.out.println("这是测试用例2");
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("beforeMethod在测试方法之前运行");
    }

    @AfterMethod
    public void afterMethod(){
        System.out.println("afterMethod在测试方法之后运行");
    }

    @BeforeClass
    public void beforeClass(){
        System.out.println("beforeClass在类运行之前运行");
    }

    @AfterClass
    public void afterClass(){
        System.out.println("afterClass在类运行之后运行");
    }

    @BeforeSuite
    public  void beforeSuite(){
        System.out.println("beforeSuite测试套件");
    }

    @AfterSuite
    public  void AfterSuite(){
        System.out.println("afterSuite测试套件");
    }
}
