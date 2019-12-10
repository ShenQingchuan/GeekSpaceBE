package com.rpzjava.sqbe.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostDataNotCompleteException extends Throwable {

    @Override
    public void printStackTrace() {
        log.error("请求提交的数据字段不完整！");

        super.printStackTrace();
    }
}
