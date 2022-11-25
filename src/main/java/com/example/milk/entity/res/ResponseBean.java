package com.example.milk.entity.res;

import lombok.Data;

/**
 * @ClassName: ResponseBean
 * @Description: 返回接口封装
 * @date: 2021/12/19
 * @author: Jiangjunye
 * @version: v1.0.0
 */
@Data
public class ResponseBean<T> {
    private T data;
    private String returnCode = "200";
    private String message = "调用成功正常返回！";

    public ResponseBean(){}

    public ResponseBean(T data){
        this.data = data;
    }

    public ResponseBean(String returnCode){
        this.returnCode = returnCode;
        this.message = "调用失败！";
    }

    public ResponseBean(String returnCode, String message){
        this.returnCode = returnCode;
        this.message = message;
    }

    public ResponseBean(T data, String returnCode, String message){
        this.data = data;
        this.returnCode = returnCode;
        this.message = message;
    }

    public static<T> ResponseBean<T> success(T data){
        return new ResponseBean(data);
    }

    public static<T> ResponseBean<T> success(T data, String returnCode, String message){
        return new ResponseBean(data,returnCode,message);
    }

    public static<T> ResponseBean<T> fail(String returnCode){
        return new ResponseBean(returnCode);
    }

    public static<T> ResponseBean<T> fail(String returnCode, String message){
        return new ResponseBean(returnCode,message);
    }

    public boolean isSuccess(){
        if ("200".equals(this.returnCode)){
            return true;
        }
        return false;
    }
}
