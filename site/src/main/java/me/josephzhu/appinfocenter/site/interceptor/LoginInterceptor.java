package me.josephzhu.appinfocenter.site.interceptor;

import me.josephzhu.appinfocenter.site.controller.MainController;
import me.josephzhu.appinfocenter.site.entity.LoginUser;
import me.josephzhu.appinfocenter.site.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

public class LoginInterceptor extends HandlerInterceptorAdapter
{
    @Autowired
    private MainMapper mainMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        try
        {
            LoginUser loginUser = (LoginUser) request.getSession(true).getAttribute(MainController.IDKEY);
            if (loginUser == null)
            {
                Cookie[] cookies = request.getCookies();
                if (cookies != null)
                {
                    for (Cookie cookie : cookies)
                    {
                        if (cookie.getName().equals(MainController.IDKEY))
                        {
                            String value = cookie.getValue();
                            value = URLDecoder.decode(value, "UTF-8");
                            int index = value.lastIndexOf("|");
                            String email = value.substring(0, index);
                            String password = value.substring(index + 1);
                            loginUser = mainMapper.login(email, password);
                            if (loginUser != null)
                            {
                                request.getSession(true).setAttribute(MainController.IDKEY, loginUser);
                            }

                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

}
