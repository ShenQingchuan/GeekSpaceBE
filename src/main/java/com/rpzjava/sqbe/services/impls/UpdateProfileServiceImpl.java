package com.rpzjava.sqbe.services.impls;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.entities.UserProfile;
import com.rpzjava.sqbe.services.UpdateProfileService;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileServiceImpl implements UpdateProfileService {

    @Override
    public UserEntity viaRequest(UserEntity userEntity, JSONObject reqBody) {
        UserProfile profile = userEntity.getUserProfile();
        profile.setBio(reqBody.get("bio").toString());
        profile.setNickName(reqBody.get("nickName").toString());
        profile.setSex(Integer.parseInt(reqBody.get("sex").toString()));
        profile.setAvatarUrl(reqBody.get("avatarUrl").toString());
        userEntity.setUserProfile(profile);
        return userEntity;
    }

}
