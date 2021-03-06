package cn.xinill.ttms.service.impl;

import cn.xinill.ttms.service.ITokenService;
import cn.xinill.ttms.utils.JWTTokenUtil;
import cn.xinill.ttms.utils.RedisUtil;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

/**
 * @Author: Xinil
 * @Date: 2021/5/9 16:43
 */
@Service
public class TokenServiceImpl implements ITokenService {


    private static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        TokenServiceImpl.redisUtil = redisUtil;
    }

    @Override
    public String creatUserToken(int userId, int role, int seconds) {
        String token = JWTTokenUtil.createToken(userId, role);
        token = token + System.currentTimeMillis() + (new Random().nextInt(899999)+100000);
        if(redisUtil.set(token, (userId+"@"+role), seconds)){
            return token;
        }else{
            return null;
        }
    }

    @Override
    public int[] verifyUserToken(String token) {
        try {
//            //token解密userId
//            Map<String, Claim> map = JWTTokenUtil.verifyToken(token);
//            if(map == null){
//                return -1;
//            }
//            int userId = map.get("userId").asInt();

            //查找token，查看登陆是否过期。
            Object value = redisUtil.get(token);
            if(value != null){
                String[] split = ((String) value).split("@");
                return new int[]{Integer.valueOf(split[0]),
                        Integer.valueOf(split[1])};
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
