package me.josephzhu.appinfocenter.site.entity;

import java.util.Date;

/**
 * Created by joseph on 15/7/11.
 */
public class App
{
    private String id;
    private String name;
    private String version;
    private Date lastActiveTime;
    private int activeServers;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getLastActiveTime()
    {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime)
    {
        this.lastActiveTime = lastActiveTime;
    }

    public int getActiveServers()
    {
        return activeServers;
    }

    public void setActiveServers(int activeServers)
    {
        this.activeServers = activeServers;
    }
}
