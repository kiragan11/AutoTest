package com.tester.model;

import com.google.gson.annotations.Expose;

public class AddUserCase {

    private int id;
    @Expose private String username;
    @Expose private String password;
    @Expose private String age;
    @Expose private String sex;
    @Expose private String permission;
    @Expose private String isDelete;
    private String expected;

    public String getExpected() {
        return expected;
    }
}
