package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.service.ICodeService;
import cn.xinill.ttms.utils.RedisUtil;
import cn.xinill.ttms.utils.SMSUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Xinil
 * @Date: 2021/3/31 15:49
 */
@Service
public class CodeServiceImpl implements ICodeService {
    private UserServiceImpl userService;

    private RedisUtil redisUtil;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean sendLOGON(String phone) {
        //发送验证码
        String code = SMSUtils.sendLOG_ON(phone);
        System.out.println("[phone:"+phone+"] 验证码已发送");
        return redisUtil.set(phone+"LOGON", code, 5*60);
    }

    @Override
    public boolean sendLOGIN(String phone) {
        //发送验证码
        String code = SMSUtils.sendLOG_IN(phone);
        System.out.println("[phone:"+phone+"] 验证码已发送");
        return redisUtil.set(phone+"LOGIN", code, 5*60);
    }

    @Override
    public boolean sendUPDATE(String phone) {
        //发送验证码
        String code = SMSUtils.sendUPDATE(phone);
        System.out.println("[phone:"+phone+"] 验证码已发送");
        return redisUtil.set(phone+"UPDATE", code, 5*60);
    }

    @Override
    public boolean judgeLOGON(String phone, String code) {
        //验证验证码
        System.out.println("[phone:"+phone+"] 正在比对验证码:"+code);
        Object str = redisUtil.get(phone+"LOGON");
        if(code.equals(str)){
            System.out.println("[phone:"+phone+"] 验证码正确");
            return true;
        }else{
            System.out.println("[phone:"+phone+"] 验证码错误");
            return false;
        }
    }

    @Override
    public boolean judgeLOGIN(String phone, String code) {
        //验证验证码
        System.out.println("[phone:"+phone+"] 正在比对验证码:"+code);
        Object str = redisUtil.get(phone+"LOGIN");
        if(code.equals(str)){
            System.out.println("[phone:"+phone+"] 验证码正确");
            return true;
        }else{
            System.out.println("[phone:"+phone+"] 验证码错误");
            return false;
        }
    }

    @Override
    public boolean judgeUPDATE(String phone, String code) {
        //验证验证码
        System.out.println("[phone:"+phone+"] 正在比对验证码:"+code);
        Object str = redisUtil.get(phone+"UPDATE");
        if(code.equals(str)){
            System.out.println("[phone:"+phone+"] 验证码正确");
            return true;
        }else{
            System.out.println("[phone:"+phone+"] 验证码错误");
            return false;
        }
    }
}
