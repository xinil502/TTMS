package cn.xinill.ttms.service;

import cn.xinill.ttms.po.User;
import cn.xinill.ttms.utils.MyException;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    /**
     * 验证用户手机号是否已经被注册
     * @param phone
     */
    boolean phoneExist(String phone);

    /**
     * 用户注册
     * @param phone
     * @return
     */
    boolean logOn(String phone);


    /**
     * 手机号登陆，返回用户的 uid。
     * @param phone
     * @return
     */
    int logIn(String phone);

    /**
     * 根据 uid查找用户
     * @param id
     * @return
     */
    User findUserByUid(int id);

    /**
     * 修改用户信息
     * @return
     */
    boolean updateUserInform(User user);

    /**
     * 修改用户头像
     * @param id
     * @param file
     * @return
     */
    Boolean updatePortrait(int id, MultipartFile file) throws MyException;

    /**
     * 用户金额变动
     * @param userId
     * @param change
     * @return
     * @throws MyException
     */
    boolean updateUserMoney(int userId, double change) throws MyException;
}
