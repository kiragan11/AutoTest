package com.tester.model;

import com.google.gson.annotations.Expose;

public class GetUserListCase {

    @Expose private String username;
    @Expose private String age;
    @Expose private String sex;
    private String expected;

    public String getExpected() {
        return expected;
    }

}
