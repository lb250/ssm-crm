package com.bjpowernode.crm.commons.domain;

public class ReturnObject {
    private String code;/** 处理是否成功的标志 1|0*/
    private String message; /** 错误返回信息*/
    private Object retData;/**其他数据*/
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
