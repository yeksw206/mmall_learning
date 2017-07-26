package com.mmall.common;

/**
 * Created by bu_dong on 2017/7/20.
 */
public enum  ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(12, "ILLEGAL_ARGUMENT");

    private final int code;
    private String desc;
    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }

    public int getCode(){
        return this.code;
    }
}
