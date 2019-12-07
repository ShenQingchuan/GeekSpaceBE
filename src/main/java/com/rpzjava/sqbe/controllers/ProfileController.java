package com.rpzjava.sqbe.controllers;

import com.alibaba.fastjson.JSONObject;
import com.rpzjava.sqbe.daos.IProfileDao;
import com.rpzjava.sqbe.daos.IUserDAO;
import com.rpzjava.sqbe.entities.UserEntity;
import com.rpzjava.sqbe.entities.UserProfile;
import com.rpzjava.sqbe.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/update")
@Slf4j
public class ProfileController {
    private final IProfileDao IProfileDao;
    private final IUserDAO IUserDAO;
    public ProfileController(IProfileDao IProfileDao, IUserDAO iUserDAO) {
        this.IProfileDao = IProfileDao;
        IUserDAO = iUserDAO;
    }

    @PostMapping("/static_data")//前提条件是前端要提交完整的资料信息（也就是不能单独修改一个，或几个，必须全部）
    public Object updateStatic(@RequestBody JSONObject jsonObject,@RequestParam(name="photo",required=false) MultipartFile photo) throws IOException {
        String sicnuid = jsonObject.get("sicnuid").toString();
        String bio = jsonObject.get("bio").toString();
        String nick_name = jsonObject.get("nick_name").toString();
        String sex = jsonObject.get("sex").toString();
        //上传头像
        if (photo != null) {
            byte[] avatar = photo.getBytes();//转成二进制流
            if (IProfileDao.updateData(sicnuid,avatar,bio,nick_name,sex) > 0) {//判断是否修改成功
                return ResultUtils.success("修改成功！");
            }
        }
        //不上传头像
//        if (IProfileDao.updateDataNotAvatar(sicnuid,bio,nick_name,sex) > 0) {//判断是否修改成功
//            return ResultUtils.success("修改成功！");
//        }
        return ResultUtils.error("修改失败！");
    }

    @GetMapping("/dynamic_data") //修改点赞数
    public int updateDynamic(@RequestBody JSONObject jsonObject){
        String sicnuid = jsonObject.get("sicnuid").toString();
        boolean pointStatus = (boolean) jsonObject.get("status");//知晓修改操作是增加还是删除

        int currentPoints = IUserDAO.findBySicnuid(sicnuid).get().getUserProfile().getTeaPoint();//获取当前改用户的teaPoint值

        if (!pointStatus) {
            if (currentPoints != 0){//当point数为0时不会修改值
                IProfileDao.reduceTeaPoint(sicnuid);
            }
        }else {
            IProfileDao.addTeaPoint(sicnuid);
        }
        return IUserDAO.findBySicnuid(sicnuid).get().getUserProfile().getTeaPoint();//返回修改后的teaPoint值
    }
}