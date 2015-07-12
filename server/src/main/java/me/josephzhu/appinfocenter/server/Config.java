package me.josephzhu.appinfocenter.server;

/**
 * Created by joseph on 15/7/9.
 */
public class Config
{
    private String redisHost;
    private int redisPort;

    public String getRedisHost()
    {
        return redisHost;
    }

    public void setRedisHost(String redisHost)
    {
        this.redisHost = redisHost;
    }

    public int getRedisPort()
    {
        return redisPort;
    }

    public void setRedisPort(int redisPort)
    {
        this.redisPort = redisPort;
    }
}
