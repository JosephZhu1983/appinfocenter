package me.josephzhu.appinfocenter.server;

/**
 * Created by joseph on 15/7/9.
 */
public class Config
{
    private int checkInterval;
    private String triggerLogAlarmLevel;
    private int triggerLogAlarmPerMin;
    private int triggerExceptionAlarmPerMin;
    private int sampleDataCount;
    private String redisHost;
    private int redisPort;
    private String redisPassword;

    public String getEnv()
    {
        return env;
    }

    public void setEnv(String env)
    {
        this.env = env;
    }

    private String env;

    public int getSampleDataCount()
    {
        return sampleDataCount;
    }

    public void setSampleDataCount(int sampleDataCount)
    {
        this.sampleDataCount = sampleDataCount;
    }

    public int getCheckInterval()
    {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval)
    {
        if (checkInterval >= 1000)
            this.checkInterval = checkInterval;
        else
            this.checkInterval = 1000;
    }

    public String getTriggerLogAlarmLevel()
    {
        return triggerLogAlarmLevel;
    }

    public void setTriggerLogAlarmLevel(String triggerLogAlarmLevel)
    {
        this.triggerLogAlarmLevel = triggerLogAlarmLevel;
    }

    public int getTriggerLogAlarmPerMin()
    {
        return triggerLogAlarmPerMin;
    }

    public void setTriggerLogAlarmPerMin(int triggerLogAlarmPerMin)
    {
        this.triggerLogAlarmPerMin = triggerLogAlarmPerMin;
    }

    public int getTriggerExceptionAlarmPerMin()
    {
        return triggerExceptionAlarmPerMin;
    }

    public void setTriggerExceptionAlarmPerMin(int triggerExceptionAlarmPerMin)
    {
        this.triggerExceptionAlarmPerMin = triggerExceptionAlarmPerMin;
    }

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

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }
}
