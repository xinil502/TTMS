package cn.xinill.ttms.service;

/**
 * @Author: Xinil
 * @Date: 2021/3/31 15:49
 */
public interface ICodeService {

    /**
     * 发送注册验证码
     */
    boolean sendLOGON(String phone);


    /**
     * 发送登陆验证码
     */
    boolean sendLOGIN(String phone);

    /**
     * 发送重要信息修改验证码
     */
    boolean sendUPDATE(String phone);


    /**
     * 验证注册验证码
     */
    boolean judgeLOGON(String phone, String code);

    /**
     * 验证登陆验证码
     */
    boolean judgeLOGIN(String phone, String code);

    /**
     * 验证重要信息修改验证码
     */
    boolean judgeUPDATE(String phone, String code);
}
