package me.josephzhu.appinfocenter.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.josephzhu.appinfocenter.common.Status;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by joseph on 15/7/9.
 */
@Service
public class AppStatusSubmitter extends AbstractSubmitter
{
    @Autowired
    private DbMapper dbMapper;
    private Logger logger = Logger.getLogger(AppStatusSubmitter.class);

    @Override
    protected String getChannelName()
    {
        return "appinfocenter/status";
    }

    @Override
    protected void submit(String message)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            Status status = mapper.readValue(message, Status.class);
            if (dbMapper.updateStatus(status.getTime(),
                    status.getServerName(), status.getServerIp(), status.getAppName(), status.getAppVersion()) == 1)
                logger.warn("保存" + message + "失败");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
