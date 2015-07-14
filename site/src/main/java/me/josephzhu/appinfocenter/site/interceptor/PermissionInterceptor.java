package me.josephzhu.appinfocenter.site.interceptor;

import me.josephzhu.appinfocenter.site.controller.MainController;
import me.josephzhu.appinfocenter.site.entity.LoginUser;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PermissionInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		LoginUser loginUser = (LoginUser) request.getSession(true).getAttribute(MainController.IDKEY);
		if(loginUser != null) {
			return true;
		} else {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
	}

}
