package me.josephzhu.appinfocenter.server;

import me.josephzhu.appinfocenter.common.Entry;
import me.josephzhu.appinfocenter.common.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by joseph on 15/7/15.
 */
@Service
public class ExceptionAlarmer extends AbstractAlarmer
{
    @Autowired
    private Config config;

    @Autowired
    private DbMapper dbMapper;

    @Override
    protected String matchCondition(List<Entry> data, App app)
    {
        Date end = new Date();
        Calendar begin = Calendar.getInstance();
        begin.setTime(new Date());
        begin.add(Calendar.MINUTE, -1);

        int count = dbMapper.getExceptionsCountForAlarm(app.getId(), begin.getTime(), end);

        if (count >= config.getTriggerLogAlarmPerMin())
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            data.addAll(dbMapper.getExceptionsForAlarm(app.getId(), begin.getTime(), end, config.getSampleDataCount()));
            return String.format("【%s (%s)】从 %s - %s 总共产生了 %d 条异常，超过了阀值 %d", app.getName(), app.getVersion(), format.format(begin.getTime()), format.format(end), count, config.getTriggerExceptionAlarmPerMin());
        }
        return "";
    }
}
