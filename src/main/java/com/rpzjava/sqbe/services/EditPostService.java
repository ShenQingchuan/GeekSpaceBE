package com.rpzjava.sqbe.services;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.beans.EditPostType;
import com.rpzjava.sqbe.exceptions.PostDataNotCompleteException;

public interface EditPostService {
    boolean newEdit(JSONObject reqBody, EditPostType type) throws PostDataNotCompleteException;
}
