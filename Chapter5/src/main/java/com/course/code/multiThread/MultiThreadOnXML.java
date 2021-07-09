package com.course.code.multiThread;

import org.testng.annotations.Test;

public class MultiThreadOnXML {


    @Test
    public void testCase1(){
        System.out.println("1Thread:"+Thread.currentThread().getId());
    }


    @Test
    public void testCase2(){
        System.out.println("2Thread:"+Thread.currentThread().getId());
    }

    @Test
    public void testCase3(){
        System.out.println("3Thread:"+Thread.currentThread().getId());
    }
}
