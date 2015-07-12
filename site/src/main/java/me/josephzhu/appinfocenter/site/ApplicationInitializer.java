package me.josephzhu.appinfocenter.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

@Component
public class ApplicationInitializer implements ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() != null) {
			WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
	        ServletContext servletContext = webApplicationContext.getServletContext();
	        servletContext.setAttribute("appcfg", applicationProperties);
		}
	}
}