package me.josephzhu.appinfocenter.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.josephzhu.appinfocenter.common.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by joseph on 15/7/9.
 */
@Service
public class LogSubmitter extends AbstractSubmitter
{
    @Autowired
    private DbMapper dbMapper;
    private Logger logger = Logger.getLogger(LogSubmitter.class);

    @Override
    protected String getChannelName()
    {
        return "appinfocenter/log";
    }

    @Override
    protected void submit(String message)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Log log = mapper.readValue(message, Log.class);
            if (dbMapper.createLog(
                    log.getLevel(),
                    log.getMessage(),
                    log.getTime(),
                    log.getContextId(),
                    log.getServerName(),
                    log.getServerIp(),
                    log.getAppName(),
                    log.getAppVersion()) == 1)
                logger.warn("保存" + message + "失败");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
