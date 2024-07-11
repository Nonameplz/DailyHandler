package com.nonameplz.dailyhandler.service.impl;

import com.nonameplz.dailyhandler.mapper.userMapper;
import com.nonameplz.dailyhandler.pojo.user;
import com.nonameplz.dailyhandler.service.userService;
import com.nonameplz.dailyhandler.utils.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {
    private static final Logger log = LoggerFactory.getLogger(userServiceImpl.class);
    @Autowired
    private userMapper usermapper;

    @Override
    public String findUserByUserName(String username) {
        return usermapper.findUserUUIDByUserName(username);
    }

    @Override
    public user getUser(String userUUID) {
        return usermapper.getUser(userUUID);
    }

    @Override
    public user getUser(String userName, String password) {

        user user = usermapper.getUser(usermapper.findUserUUIDByUserName(userName));

        if (user == null) {
            return null;
        }
        if (!Md5Util.checkPassword(password, user.getPassword())) {
            return null;
        }

        return user;
    }

    @Override
    public void updateUserInfo(user user) {
        usermapper.updateUserInfo(user);
    }

    @Override
    public void updateUserPassword(String userUUID, String password) {

        usermapper.updateUserPassword(userUUID, Md5Util.getMD5String(password));

    }


}
