package com.rpzjava.sqbe.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostDataNotCompleteException extends Throwable {

    @Override
    public String getMessage() {
        return "请求提交的数据字段不完整！";
    }

    @Override
    public void printStackTrace() {
        log.error(this.getMessage());
        super.printStackTrace();
    }
}
