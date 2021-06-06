package cn.xinill.ttms.config;

import cn.xinill.ttms.filter.CrossFilter;
//import cn.xinill.ttms.filter.LoginFilter;
import cn.xinill.ttms.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Author: Xinil
 * @Date: 2021/1/31 22:27
 *
 * 过滤器配置类
 */
@Configuration
public class FilterConfig {
    /**
     * 用户 token过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoginFilter());
        registration.addUrlPatterns("/*");
        registration.setName("tokenFilter");
        registration.setOrder(2); //值越小越先执行。
        return registration;
    }

    /**
     * 配置跨域访问的过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean crossFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        registration.setFilter(new CrossFilter());
        registration.setName("crossFilter");
        registration.setOrder(1); //值越小越先执行。
        return registration;
    }

}