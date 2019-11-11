package com.rpzjava.sqbe.utils;

import lombok.Data;

public class ResultUtils {
    /**
     * 成功返回
     * @param data
     * @param msg
     * @return
     */
    public static Object success(Object data,String msg){
        Result result = new Result();
        result.setState(true);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }
    public static Object success(String msg){
        Result result = new Result();
        result.setState(true);
        result.setMsg(msg);
        return result;
    }
    public static Object success(Object data){
        Result result = new Result();
        result.setState(true);
        result.setData(data);
        return result;
    }
    public static Object success(){
        Result result = new Result();
        result.setState(true);
        return result;
    }

    /**
     * 错误返回
     * @return
     */
    public static Object error(){
        Result result = new Result();
        result.setState(false);
        return result;
    }
    public static Object error(String msg){
        Result result = new Result();
        result.setState(false);
        result.setMsg(msg);
        return result;
    }

    @Data
    private static class Result{
        private boolean state;//返回状态
        private Object data;//返回数据
        private String msg;//返回信息
    }
}
