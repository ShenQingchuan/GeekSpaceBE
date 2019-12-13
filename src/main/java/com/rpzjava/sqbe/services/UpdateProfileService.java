package com.rpzjava.sqbe.services;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.entities.UserEntity;

public interface UpdateProfileService {
    UserEntity viaRequest(UserEntity userEntity, JSONObject reqBody);
}
