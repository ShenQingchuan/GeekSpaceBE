package com.rpzjava.sqbe.services;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;

public interface NewPostService {

    Boolean newPost(JSONObject reqBody, Long uid) throws PostDataNotCompleteException;

}
