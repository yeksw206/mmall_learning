package com.mmall.common;

/**
 * Created by bu_dong on 2017/7/20.
 */
public class Const {

    public static final String CURRENT_USER = "current_user";

    public interface Role{
        int ROLE_CUSTOME = 0;
        int ROLE_ADMIN = 1;
    }

    public interface CHECK_TYPE{
        String EMAIL = "email";
        String USERNAME = "username";
    }
}
