package com.course.code.groups;

import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class GroupsOnMethod {

    @BeforeGroups("server")
    public  void beforeGroup(){
        System.out.println("服务端组运行之前的方法！");
    }

    @Test(groups = "server")
    public void test1(){
        System.out.println("服务端组test1");
    }
    @Test(groups = "server")
    public void test2(){
        System.out.println("服务端组test2");
    }
    @Test(groups = "client")
    public void test3(){
        System.out.println("客户端组test3");
    }
    @Test(groups = "client")
    public void test4(){
        System.out.println("客户端组test4");
    }

    @AfterGroups("server")
    public static void afterGroup(){
        System.out.println("服务端组运行之后的方法！！！");
    }
}
