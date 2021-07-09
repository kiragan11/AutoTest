package com.course.code.parameter;

import com.beust.jcommander.Parameter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParameterTest {

    @Parameters({"name","age"})
    @Test
    public  void paramTest(String name,int age){
        System.out.println("name="+ name+ "，age="+age);
    }
}
