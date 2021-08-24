package com.course.testng.multiThread;

import org.testng.annotations.Test;

public class MultiThreadOnAnnotion {

    @Test(invocationCount = 10,threadPoolSize =2 )
    public void test(){
        System.out.println("哈哈哈哈");
        System.out.println("Thread"+Thread.currentThread().getId());
    }

}
