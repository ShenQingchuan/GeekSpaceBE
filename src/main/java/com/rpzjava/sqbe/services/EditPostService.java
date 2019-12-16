package com.rpzjava.sqbe.services;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.tools.EditPostType;
import com.rpzjava.sqbe.tools.ExecuteResult;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;

public interface EditPostService {
    ExecuteResult newEdit(JSONObject reqBody, EditPostType type) throws PostDataNotCompleteException;

    ExecuteResult updateEdit(JSONObject reqBody, EditPostType type) throws PostDataNotCompleteException;
}
