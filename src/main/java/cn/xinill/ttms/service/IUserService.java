package cn.xinill.ttms.service;

import cn.xinill.ttms.po.User;

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
     * 账号密码登陆，返回用户的 uid
     * @param phone
     * @param passpword
     * @return
     */
    int logIn(String phone, String passpword);

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
    boolean updateUserInform(int uid, String username, String portrait, String gender, Integer age, String introduce);

    /**
     * 修改用户密码
     * @param uid 根据 uid 确定用户。
     * @param password
     * @return
     */
    boolean updateUserPwd(int uid, String password);

}
