package cn.xinill.ttms;

import cn.xinill.ttms.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TtmsApplicationTests {

    @Autowired
    TokenServiceImpl tokenService;
    @Test
    void contextLoads() {
    }

}