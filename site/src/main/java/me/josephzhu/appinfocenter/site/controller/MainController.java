package me.josephzhu.appinfocenter.site.controller;

import me.josephzhu.appinfocenter.client.AppInfoCenter;
import me.josephzhu.appinfocenter.common.HttpLog;
import me.josephzhu.appinfocenter.common.Log;
import me.josephzhu.appinfocenter.site.entity.App;
import me.josephzhu.appinfocenter.site.entity.LoginUser;
import me.josephzhu.appinfocenter.site.mapper.MainMapper;
import me.josephzhu.appinfocenter.site.util.MD5;
import me.josephzhu.appinfocenter.site.util.PagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by joseph on 15/7/11.
 */
@Controller
public class MainController
{
    public static String IDKEY = "loginuser";

    private static int PAGESIZE = 10;
    @Autowired
    private AppInfoCenter appInfoCenter;
    @Autowired
    private MainMapper mainMapper;


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView test()
    {
        appInfoCenter.debug("测试debug日志");
        appInfoCenter.info("测试info日志");
        appInfoCenter.warning("测试warning日志");
        appInfoCenter.error("测试error日志");
        throw new RuntimeException("测试未处理异常");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login()
    {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String login(String email, String password, String saveUser,
                        HttpSession session, HttpServletResponse response)
    {
        password = MD5.GetMD5Code(password + "appinfocenter");
        LoginUser loginUser = mainMapper.login(email, password);
        if (loginUser != null)
        {
            session.setAttribute(IDKEY, loginUser);

            if (saveUser != null && saveUser.equals("saveUser"))
            {
                String encode = "";
                try
                {
                    encode = URLEncoder.encode(email + "|" + password, "UTF-8");
                    Cookie cookie = new Cookie(IDKEY, encode);
                    cookie.setMaxAge(60 * 60 * 24 * 365);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }

            return "redirect:/";
        } else
        {
            return "redirect:/login?error=true";
        }
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session, HttpServletResponse response)
    {
        session.removeAttribute(IDKEY);
        Cookie cookie = new Cookie(IDKEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index()
    {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("apps", mainMapper.getApps());
        return modelAndView;
    }

    @RequestMapping(value = "/status/{appId}", method = RequestMethod.GET)
    public ModelAndView status(@PathVariable Integer appId)
    {
        ModelAndView modelAndView = new ModelAndView("status");

        List<App> apps = mainMapper.getApps();
        List<App> current = new ArrayList<>();

        for(App app : apps)
        {
            if (app.getId().equals(appId.toString()))
                current.add(app);
        }

        if (current.size() > 0)
            modelAndView.addObject("appInfo", current.get(0));
        modelAndView.addObject("currentAppId", appId);
        modelAndView.addObject("appDetail", mainMapper.getAppDetail(appId));
        modelAndView.addObject("apps", apps);
        modelAndView.addObject("section", "status");
        return modelAndView;
    }

    @RequestMapping(value = "/log/{appId}", method = RequestMethod.GET)
    public ModelAndView log(@PathVariable Integer appId,
                            @RequestParam(value = "contextId", required = false) String contextId,
                            @RequestParam(value = "begin", required = false) Date begin,
                            @RequestParam(value = "end", required = false) Date end,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "serverIds", required = false) int[] serverIds,
                            @RequestParam(value = "levels", required = false) int[] levels)
    {
        ModelAndView modelAndView = new ModelAndView("log");

        int logCount = mainMapper.getLogsCount(begin, end, contextId, levels, appId, serverIds);
        int pageCount = PagerUtil.getPageCount(logCount, PAGESIZE);
        if (page < 1)
        {
            page = 1;
        } else if (page > pageCount)
        {
            page = pageCount;
        }
        List<Integer> pageList = PagerUtil.generatePageList(page, pageCount);
        modelAndView.addObject("pageList", pageList);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("p", page);
        modelAndView.addObject("serverIds", serverIds);
        modelAndView.addObject("levels", levels);
        modelAndView.addObject("contextId", contextId);
        List<Log> logs = mainMapper.getLogs(begin, end, contextId, levels, appId, serverIds, (page - 1) * PAGESIZE, PAGESIZE);
        modelAndView.addObject("logs", logs);
        modelAndView.addObject("servers", mainMapper.getLogServers(appId));
        List<App> apps = mainMapper.getApps();
        modelAndView.addObject("currentAppId", appId);
        modelAndView.addObject("apps", apps);
        modelAndView.addObject("section", "log");

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        if (begin != null)
            modelAndView.addObject("begin", format.format(begin));
        if (end != null)
            modelAndView.addObject("end", format.format(end));
        return modelAndView;
    }


    @RequestMapping(value = "/exception/{appId}", method = RequestMethod.GET)
    public ModelAndView exception(@PathVariable Integer appId,
                                  @RequestParam(value = "contextId", required = false) String contextId,
                                  @RequestParam(value = "begin", required = false) Date begin,
                                  @RequestParam(value = "end", required = false) Date end,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "serverIds", required = false) int[] serverIds,
                                  @RequestParam(value = "types", required = false) String[] types)
    {
        ModelAndView modelAndView = new ModelAndView("exception");

        int logCount = mainMapper.getExceptionsCount(begin, end, contextId, types, appId, serverIds);
        int pageCount = PagerUtil.getPageCount(logCount, PAGESIZE);
        if (page < 1)
        {
            page = 1;
        } else if (page > pageCount)
        {
            page = pageCount;
        }
        List<Integer> pageList = PagerUtil.generatePageList(page, pageCount);
        modelAndView.addObject("pageList", pageList);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("p", page);
        modelAndView.addObject("serverIds", serverIds);
        modelAndView.addObject("types", types);
        modelAndView.addObject("alltypes", mainMapper.getExceptionTypes(appId));
        modelAndView.addObject("contextId", contextId);
        List<me.josephzhu.appinfocenter.common.Exception> logs = mainMapper.getExceptions(begin, end, contextId, types, appId, serverIds, (page - 1) * PAGESIZE, PAGESIZE);
        modelAndView.addObject("logs", logs);
        modelAndView.addObject("servers", mainMapper.getExceptionServers(appId));
        List<App> apps = mainMapper.getApps();
        modelAndView.addObject("currentAppId", appId);
        modelAndView.addObject("apps", apps);
        modelAndView.addObject("section", "exception");


        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        if (begin != null)
            modelAndView.addObject("begin", format.format(begin));
        if (end != null)
            modelAndView.addObject("end", format.format(end));
        return modelAndView;
    }

    @RequestMapping(value = "/httplog/{appId}", method = RequestMethod.GET)
    public ModelAndView httplog(@PathVariable Integer appId,
                                  @RequestParam(value = "userId", required = false) String userId,
                                @RequestParam(value = "url", required = false) String url,
                                  @RequestParam(value = "begin", required = false) Date begin,
                                  @RequestParam(value = "end", required = false) Date end,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "serverIds", required = false) int[] serverIds
                             )
    {
        ModelAndView modelAndView = new ModelAndView("httplog");

        int logCount = mainMapper.getHttpLogsCount(begin, end, url, userId, appId, serverIds);
        int pageCount = PagerUtil.getPageCount(logCount, PAGESIZE);
        if (page < 1)
        {
            page = 1;
        } else if (page > pageCount)
        {
            page = pageCount;
        }
        List<Integer> pageList = PagerUtil.generatePageList(page, pageCount);
        modelAndView.addObject("pageList", pageList);
        modelAndView.addObject("pageCount", pageCount);
        modelAndView.addObject("p", page);
        modelAndView.addObject("serverIds", serverIds);

        modelAndView.addObject("url", url);
        modelAndView.addObject("userId", userId);
        List<HttpLog> logs = mainMapper.getHttpLogs(begin, end, url, userId, appId, serverIds, (page - 1) * PAGESIZE, PAGESIZE);
        modelAndView.addObject("logs", logs);
        modelAndView.addObject("servers", mainMapper.getHttpLogServers(appId));
        List<App> apps = mainMapper.getApps();
        modelAndView.addObject("currentAppId", appId);
        modelAndView.addObject("apps", apps);
        modelAndView.addObject("section", "httplog");


        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        if (begin != null)
            modelAndView.addObject("begin", format.format(begin));
        if (end != null)
            modelAndView.addObject("end", format.format(end));
        return modelAndView;
    }
}
