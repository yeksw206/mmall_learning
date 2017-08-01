package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by bu_dong on 2017/7/20.
 */
public class Const {

    public static final String CURRENT_USER = "current_user";

    public interface Role{
        int ROLE_CUSTOME = 0;
        int ROLE_ADMIN = 1;//管理员
    }

    public interface Cart{
        int CHECKED = 1; //购物车选中模式
        int UN_CHECKED = 0;

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface CHECK_TYPE{
        String EMAIL = "email";
        String USERNAME = "username";
    }

    public interface PRODUCT_ORDER_TYPE{
        Set<String> PRICE_ORDER = Sets.newHashSet("price_asc", "price_desc");
    }

    public enum SALE_STATUS{
        ON_SALE(1, "在线销售");
        private int code;
        private String value;


        SALE_STATUS(int code, String value){
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
