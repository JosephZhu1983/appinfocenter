package me.josephzhu.appinfocenter.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.josephzhu.appinfocenter.common.HttpLog;
import me.josephzhu.appinfocenter.common.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by joseph on 15/7/9.
 */
@Service
public class HttpLogSubmitter extends AbstractSubmitter
{
    @Autowired
    private DbMapper dbMapper;
    private Logger logger = Logger.getLogger(HttpLogSubmitter.class);

    @Override
    protected String getChannelName()
    {
        return "appinfocenter/httplog";
    }

    @Override
    protected void submit(String message)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            HttpLog log = mapper.readValue(message, HttpLog.class);
            int error = dbMapper.createHttpLog(
                    log.getUserId(),
                    log.getUrl(),
                    log.getQueryString(),
                    log.getRequestHeader(),
                    log.getRequestBody(),
                    log.getResponseHeader(),
                    log.getResponseBody(),
                    log.getTime(),
                    log.getServerName(),
                    log.getServerIp(),
                    log.getAppName(),
                    log.getAppVersion());
            if (error == 1)
                logger.warn("保存" + message + "失败");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
