package com.course.code;

import org.testng.annotations.Test;

public class DependTest {

    @Test
    public void testCase1(){
        System.out.println("这是测试用例1");
        throw new RuntimeException();
    }

    @Test(dependsOnMethods = {"testCase1"})
    public void testCase2(){
        System.out.println("这是测试用例2");
    }

}
