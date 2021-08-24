package com.course.testng;

import org.testng.annotations.Test;

public class TimeOutTest {

    @Test(timeOut = 3000)
    public void test() throws InterruptedException {
        Thread.sleep(2000);
    }
}
