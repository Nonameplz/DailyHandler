package com.nonameplz.dailyhandler.mapper;

import com.nonameplz.dailyhandler.pojo.user;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface userMapper {

    //通过用户名查询用户UUID
    @Select("select userUUID from userinfo where userName = #{userName}")
    String findUserUUIDByUserName(String userName);
    //根据用户UUID查询
    @Select("select ID, userUUID, userName, password, userAvatar, userNickName, email, phoneNumber, createTime, modifyTime from userinfo where userUUID = #{userUUID}")
    user getUser(String userUUID);
    //将用户注册信息写入数据库
    @Insert("insert into userinfo(userUUID, userName, password, email) " +
            "value (#{userUUID},#{userName},#{password},#{email})")
    void register(user user);

    //更新用户信息
    void updateUserInfo(user user);

    //更新用户密码
    @Update("update userinfo set password = #{password} where userUUID = #{userUUID} ")
    void updateUserPassword(String userUUID, String password);
}
