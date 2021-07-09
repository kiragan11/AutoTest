package com.course.code;

import org.testng.annotations.Test;

public class ExpectedException {

    @Test(expectedExceptions = RuntimeException.class)
    public void test(){
        System.out.println("异常测试");
        throw new RuntimeException();
    }
}
