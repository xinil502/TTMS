package cn.xinill.ttms.service;

/**
 * @Author: Xinil
 * @Date: 2021/5/9 16:43
 */
public interface ITokenService {
    /**
     * 由id生成token
     * @param userId
     * @param role
     * @param seconds token过期时间，秒（token不限制过期时间，过期时间通过 redis限制）
     * @return
     */
    String creatUserToken(int userId, int role, int seconds);

    /**
     * 由token转为id
     * @param token
     * @return
     */
    int[] verifyUserToken(String token);
}
