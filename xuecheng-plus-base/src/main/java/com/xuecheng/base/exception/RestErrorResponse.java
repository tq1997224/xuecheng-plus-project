package com.xuecheng.base.exception;

import java.io.Serializable;

/**
 * @author: Tq
 * @Desc:  和前端约定返回的异常信息
 * @create: 2023-08-01 14:37
 **/
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}

