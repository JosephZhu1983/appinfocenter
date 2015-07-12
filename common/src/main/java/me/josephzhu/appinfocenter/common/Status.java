package me.josephzhu.appinfocenter.common;

/**
 * Created by joseph on 15/7/10.
 */
public class Status extends Entry
{
    private boolean healthy;

    public Status()
    {
        channelName = "appinfocenter/status";
    }

    public boolean isHealthy()
    {
        return healthy;
    }

    public void setHealthy(boolean healthy)
    {
        this.healthy = healthy;
    }
}
