package me.josephzhu.appinfocenter.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by joseph on 15/7/13.
 */
public class ExceptionResolver implements HandlerExceptionResolver
{

    @Autowired
    AppInfoCenter appInfoCenter;
    private Logger logger = Logger.getLogger(ExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
    {
        if (appInfoCenter != null)
            appInfoCenter.exception(e);
        logger.error(e);
        try
        {
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        return null;
    }
}
