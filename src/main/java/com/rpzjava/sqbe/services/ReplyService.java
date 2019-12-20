package com.rpzjava.sqbe.services;

import com.alibaba.fastjson.JSONObject;

public interface ReplyService {

    Object newReply(Long commentId, JSONObject reqBody, String targetType);

}
