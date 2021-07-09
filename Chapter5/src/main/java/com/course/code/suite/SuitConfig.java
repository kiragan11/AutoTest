package com.course.code.suite;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class SuitConfig {

    @BeforeSuite
    public  void beforeSuite(){
        System.out.println("before suite运行啦");
    }

    @AfterSuite
    public  void AfterSuite(){
        System.out.println("after suite运行啦");
    }

    @BeforeTest
    public void beforeTest(){
        System.out.println("before Test运行啦");
    }

    @AfterTest
    public void afterTest(){
        System.out.println("after Test运行啦");
    }
}
