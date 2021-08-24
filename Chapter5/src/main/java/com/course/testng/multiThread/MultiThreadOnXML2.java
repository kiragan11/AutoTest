package com.course.testng.multiThread;

import org.testng.annotations.Test;

public class MultiThreadOnXML2 {


    @Test
    public void testCase4(){
        System.out.println("4Thread:"+Thread.currentThread().getId());
    }


    @Test
    public void testCase5(){
        System.out.println("5Thread:"+Thread.currentThread().getId());
    }

    @Test
    public void testCase6(){
        System.out.println("6Thread:"+Thread.currentThread().getId());
    }
}
