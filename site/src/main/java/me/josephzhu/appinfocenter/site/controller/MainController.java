package me.josephzhu.appinfocenter.site.controller;

import me.josephzhu.appinfocenter.client.AppInfoCenter;
import me.josephzhu.appinfocenter.common.*;
import me.josephzhu.appinfocenter.site.entity.App;
import me.josephzhu.appinfocenter.site.mapper.MainMapper;
import me.josephzhu.appinfocenter.site.util.PagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.lang.Exception;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by joseph on 15/7/11.
 */
@Controller
public class MainController
{
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
        List<App> current = apps.stream()
                .filter(item -> item.getId()
                        .equals(appId.toString()))
                .collect(Collectors.toList());
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
        if(page < 1) {
            page = 1;
        } else if(page > pageCount) {
            page = pageCount;
        }
        List<Integer> pageList = PagerUtil.generatePageList(page, pageCount);
        modelAndView.addObject("pageList",pageList);
        modelAndView.addObject("pageCount",pageCount);
        modelAndView.addObject("p", page);
        modelAndView.addObject("serverIds", serverIds);
        modelAndView.addObject("levels", levels);
        modelAndView.addObject("contextId", contextId);
        List<Log> logs = mainMapper.getLogs(begin, end, contextId, levels, appId, serverIds, (page - 1) * PAGESIZE, PAGESIZE);
        modelAndView.addObject("logs",logs);
        modelAndView.addObject("servers",mainMapper.getLogServers(appId));
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
}
