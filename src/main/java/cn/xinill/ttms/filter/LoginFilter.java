package cn.xinill.ttms.filter;

import cn.xinill.ttms.service.impl.TokenServiceImpl;
import cn.xinill.ttms.utils.MyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: Xinil
 * @Date: 2021/1/29 23:29
 */
public class LoginFilter implements Filter {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String servletPath = ((HttpServletRequest) request).getServletPath();
            logger.info("___________________"+((HttpServletRequest) request).getMethod() + ":"+servletPath+"________________");
            //登陆放行
            if(((HttpServletRequest) request).getMethod().equals("OPTIONS")
                    || servletPath.startsWith("/user/login")
                    || servletPath.startsWith("/staff/login")
                    || ((HttpServletRequest) request).getMethod().equals("GET") && servletPath.startsWith("/movie")
                    || ((HttpServletRequest) request).getMethod().equals("GET") && servletPath.startsWith("/schedule")
                    || ((HttpServletRequest) request).getMethod().equals("GET") && servletPath.startsWith("/ticket")){
                filterChain.doFilter(request,response);
                return;
            }
            //
            String token = ((HttpServletRequest)request).getHeader("token");
            if(token == null){
                throw new MyException("token为null，需要登陆");
            }else{
                int[] verifyToken = new TokenServiceImpl().verifyUserToken(token);
                if(verifyToken == null){
                    throw new MyException("token已经过期，需要登陆");
                }else {
                    logger.info("______________token已解析 id = " + verifyToken[0] +"  role = " + verifyToken[1]+"_______________");
                    if(verifyToken[0] == 1){
                    }else if(verifyToken[1] != 6 && servletPath.startsWith("/staff/updatePwd")){
                    }else if(verifyToken[1] == 6 && (servletPath.startsWith("/user"))){
                    }else if(verifyToken[1] == 1 && (servletPath.startsWith("/ticket")
                                                    || servletPath.startsWith("/order"))){
                    }else if(verifyToken[1] == 2 && (servletPath.startsWith("/statistics"))){
                    }else if(verifyToken[1] == 3 && (servletPath.startsWith("/staff")
                                                    || servletPath.startsWith("/movie")
                                                    || servletPath.startsWith("/studio")
                                                    || servletPath.startsWith("/schedule"))){
                    }else{
                        throw new MyException("权限不足，不允许访问");
                    }

                    request.setAttribute("id", verifyToken[0]);
                    request.setAttribute("role", verifyToken[1]);
                    filterChain.doFilter(request,response);
                }
            }
        } catch (MyException e) {//需要登陆
            logger.warn(e.getMessage());
            //返回403
            ((HttpServletResponse)response).setStatus(HttpServletResponse.SC_FORBIDDEN); //设置status 403
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"status\":1," +
                    "\"msg\":\""+e.getMessage()+"\"," +
                    "\"data\":false" +
                    "}");
            writer.flush();
            writer.close();
            return;
        }catch (Exception e) {
            e.printStackTrace();
            //返回404
        }
    }

    @Override
    public void destroy() {

    }
}
