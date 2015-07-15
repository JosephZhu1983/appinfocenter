package me.josephzhu.appinfocenter.server;

import me.josephzhu.appinfocenter.common.Entry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 15/7/15.
 */
public abstract class AbstractAlarmer extends Thread
{
    @Autowired
    private Config config;

    @Autowired
    private DbMapper dbMapper;

    @Autowired
    private EmailService emailService;

    private Logger logger = Logger.getLogger(AbstractAlarmer.class);

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                check();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            try
            {
                Thread.sleep(config.getCheckInterval());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    protected abstract String matchCondition(List<Entry> data, App app);

    private void check()
    {

        for (App app : dbMapper.getApps())
        {
            List<Entry> data = new ArrayList<>();
            String condition = matchCondition(data, app);
            if (!condition.isEmpty())
            {
                StringBuilder sb = new StringBuilder();
                sb.append("<table style='font-size:13px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;'>");
                boolean header = false;
                for (Entry item : data)
                {
                    if (!header)
                    {
                        sb.append("<tr>");
                        for (Field field : item.getClass().getSuperclass().getDeclaredFields())
                        {
                            sb.append("<th border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;'>" + field.getName() + "</th>");
                        }
                        for (Field field : item.getClass().getDeclaredFields())
                        {
                            sb.append("<th border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;'>" + field.getName() + "</th>");
                        }
                        sb.append("</tr>");
                        header = true;
                    }
                    sb.append("<tr>");
                    for (Field field : item.getClass().getSuperclass().getDeclaredFields())
                    {
                        field.setAccessible(true);

                        try
                        {
                            sb.append("<td  border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff;'>" + field.get(item) + "</td>");
                        }

                        catch (IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    for (Field field : item.getClass().getDeclaredFields())
                    {
                        field.setAccessible(true);

                        try
                        {
                            sb.append("<td  border-width: 1px; padding: 8px; border-style: solid; border-color: #666666; background-color: #ffffff;'>" + field.get(item) + "</td>");
                        }

                        catch (IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    sb.append("</tr>");
                }
                sb.append("</table>");

                List<String> emails = dbMapper.getAlarmMailReceivers();
                for (String email : emails)
                    emailService.sendAlarmMail(email, condition, sb.toString());
            }
        }
    }
}
