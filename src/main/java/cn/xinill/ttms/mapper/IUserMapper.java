package cn.xinill.ttms.mapper;

import cn.xinill.ttms.po.User;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserMapper {

    int insertUser(User user);

    User findUserByPhone(String phone);

    User findUserByUid(int userId);

    int updateUserInform(User user);
}
