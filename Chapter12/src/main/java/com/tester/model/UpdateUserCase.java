package com.tester.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserCase {

    private int id;
    @Expose
    @SerializedName("id")
    private int userId;
    @Expose private String username;
    @Expose private String sex;
    @Expose private String age;
    @Expose private String permission;
    @Expose private String isDelete;
    private String expected;

    public String getExpected() {
        return expected;
    }
}
