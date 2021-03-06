package me.josephzhu.appinfocenter.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.josephzhu.appinfocenter.common.Exception;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by joseph on 15/7/9.
 */
@Service
public class ExceptionSubmitter extends AbstractSubmitter
{
    @Autowired
    private DbMapper dbMapper;

    private Logger logger = Logger.getLogger(ExceptionSubmitter.class);

    @Override
    protected String getChannelName()
    {
        return "appinfocenter/exception";
    }

    @Override
    protected void submit(String message)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Exception exception = mapper.readValue(message, Exception.class);
            if (dbMapper.createException(
                    exception.getType(),
                    exception.getMessage(),
                    exception.getStackTrace(),
                    exception.getTime(),
                    exception.getContextId(),
                    exception.getServerName(),
                    exception.getServerIp(),
                    exception.getAppName(),
                    exception.getAppVersion(), exception.getExtraInfo()) == 1)
                logger.warn("保存" + message + "失败");
        }
        catch (java.lang.Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
