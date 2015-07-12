package me.josephzhu.appinfocenter.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by joseph on 15/7/9.
 */

public abstract class AbstractSubmitter extends Thread
{
    @Autowired
    private Config config;

    private Logger logger = Logger.getLogger(AbstractSubmitter.class);

    protected abstract void submit(String message);

    protected abstract String getChannelName();

    public void run()
    {
        while (true)
        {
            try
            {
                subscribe();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void subscribe()
    {
        Jedis jedis = new Jedis(config.getRedisHost(), config.getRedisPort());
        jedis.subscribe(new JedisPubSub()
        {
            @Override
            public void onMessage(String channel, String message)
            {
                logger.debug(String.format("Receive %s:%s", channel, message));
                submit(message);
            }
        }, getChannelName());
    }
}
