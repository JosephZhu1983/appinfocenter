package me.josephzhu.appinfocenter.site.controller;

import me.josephzhu.appinfocenter.client.AppInfoCenter;
import me.josephzhu.appinfocenter.site.entity.ChartsData;
import me.josephzhu.appinfocenter.site.entity.Series;
import me.josephzhu.appinfocenter.site.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by joseph on 15/7/13.
 */
@RestController
@RequestMapping(value = "/ajax")
public class AjaxController
{
    @Autowired
    private AppInfoCenter appInfoCenter;
    @Autowired
    private MainMapper mainMapper;

    @RequestMapping(value = "/logcharts/{appId}", method = RequestMethod.GET)
    public ChartsData logcharts(@PathVariable Integer appId)
    {
        ChartsData chartsData = new ChartsData();
        List<Date> dates = new ArrayList<>();
        List<Series> data = new ArrayList<>();
        Series debug = new Series();
        debug.setName("Debug");
        debug.setType("line");
        debug.setData(new ArrayList<>());
        data.add(debug);
        Series info = new Series();
        info.setName("Info");
        info.setType("line");
        info.setData(new ArrayList<>());
        data.add(info);
        Series warning = new Series();
        warning.setName("Warning");
        warning.setType("line");
        warning.setData(new ArrayList<>());
        data.add(warning);
        Series error = new Series();
        error.setName("Error");
        error.setType("line");
        error.setData(new ArrayList<>());
        data.add(error);
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.MINUTE, 30);

        Calendar begin = Calendar.getInstance();
        begin.setTime(new Date());
        begin.add(Calendar.DATE, -3);

        while (begin.getTime().before(now.getTime()))
        {
            Calendar tz = Calendar.getInstance();
            tz.setTime(begin.getTime());
            tz.add(Calendar.HOUR, 8);
            dates.add(tz.getTime());
            Calendar end = Calendar.getInstance();
            end.setTime(begin.getTime());
            end.add(Calendar.MINUTE, 30);

            debug.getData().add(mainMapper.getLogChartData(1, appId, begin.getTime(), end.getTime()));
            info.getData().add(mainMapper.getLogChartData(2, appId, begin.getTime(), end.getTime()));
            warning.getData().add(mainMapper.getLogChartData(3, appId, begin.getTime(), end.getTime()));
            error.getData().add(mainMapper.getLogChartData(4, appId, begin.getTime(), end.getTime()));
            begin.setTime(end.getTime());
        }

        chartsData.setX(dates);
        chartsData.setData(data);
        return chartsData;
    }

    @RequestMapping(value = "/exceptioncharts/{appId}", method = RequestMethod.GET)
    public ChartsData exceptioncharts(@PathVariable Integer appId)
    {


        ChartsData chartsData = new ChartsData();
        List<Date> dates = new ArrayList<>();
        List<Series> data = new ArrayList<>();
        Series exception = new Series();
        exception.setName("Exception");
        exception.setType("line");
        exception.setData(new ArrayList<>());
        data.add(exception);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.add(Calendar.MINUTE, 30);

        Calendar begin = Calendar.getInstance();
        begin.setTime(new Date());
        begin.add(Calendar.DATE, -3);


        while (begin.getTime().before(now.getTime()))
        {
            Calendar tz = Calendar.getInstance();
            tz.setTime(begin.getTime());
            tz.add(Calendar.HOUR, 8);
            dates.add(tz.getTime());

            Calendar end = Calendar.getInstance();
            end.setTime(begin.getTime());
            end.add(Calendar.MINUTE, 30);

            exception.getData().add(mainMapper.getExceptionChartCount(appId, begin.getTime(), end.getTime()));
            begin.setTime(end.getTime());
        }

        chartsData.setX(dates);
        chartsData.setData(data);
        return chartsData;
    }
}
