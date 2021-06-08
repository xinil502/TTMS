package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.mapper.IUserMapper;
import cn.xinill.ttms.po.User;
import cn.xinill.ttms.service.IUserService;
import cn.xinill.ttms.utils.MyException;
import cn.xinill.ttms.utils.OSSClientUtil;
import cn.xinill.ttms.utils.RedisUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements IUserService {
    private static final Object LOCK = new Object();
    private IUserMapper userMapper;
    private OSSClientUtil ossClientUtil;

    @Autowired(required = false)
    public void setUserMapper(IUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setOssClientUtil(OSSClientUtil ossClientUtil) {
        this.ossClientUtil = ossClientUtil;
    }

    @Override
    public boolean phoneExist(String phone) {
        if(userMapper.findUserByPhone(phone)!=null){ //手机号已经被注册了
            return true;
        }
        return false;
    }

    @Override
    public boolean logOn(String phone) {
        try {
            User user = new User();
            user.setPhone(phone);
            return 1 == userMapper.insertUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int logIn(String phone) {
        //未注册用户进行注册
        if (!phoneExist(phone)){
            logOn(phone);
        }
        User findUser = userMapper.findUserByPhone(phone);
        return findUser.getUserId();
    }

    @Override
    public User findUserByUid(int id) {
        User user =  userMapper.findUserByUid(id);
        user.setPortrait("https://xinil.oss-cn-shanghai.aliyuncs.com/TTMS/"+user.getPortrait());
        return user;
    }

    @Override
    public boolean updateUserInform(User user) {
        return 1 == userMapper.updateUserInform(user);
    }

    @Override
    public Boolean updatePortrait(int id, MultipartFile file) throws MyException {
        String fileName = ossClientUtil.uploadImg2Oss(file);
        User user = new User();
        user.setUserId(id);
        user.setPortrait(fileName);
        return 1 == userMapper.updateUserInform(user);
    }


    @Override
    public boolean updateUserMoney(int userId, double change) throws MyException {
        synchronized (UserServiceImpl.LOCK){
            //收款
            User user = findUserByUid(userId);
            if(user.getBalance() + change < 0){
                throw new MyException("余额不足，请前往前台充值");
            }else{
                user.setBalance(user.getBalance() + change);
                user.setPortrait(null);
                updateUserInform(user);
                return true;
            }
        }
    }
}
