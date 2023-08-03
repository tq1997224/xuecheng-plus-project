package com.xuecheng.base.exception;

/**
 * @author: Tq
 * @Desc: 学成在线项目自定义异常
 * @create: 2023-08-01 14:40
 **/
public class XueChengPlusException extends RuntimeException {

    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public XueChengPlusException() {

    }

    public XueChengPlusException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }



    public static void cast(String errmessage ){
        throw new XueChengPlusException(errmessage);
    }

    public static void cast(CommonError error){
        throw new XueChengPlusException(error.getErrMessage());
    }



}
