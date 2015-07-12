package me.josephzhu.appinfocenter.site.entity;

import java.util.Date;

/**
 * Created by joseph on 15/7/11.
 */
public class AppDetail
{
    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public Date getLastActiveTime()
    {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime)
    {
        this.lastActiveTime = lastActiveTime;
    }

    private String serverName;
    private String serverIp;
    private Date lastActiveTime;

    public int getIdleMins()
    {
        return idleMins;
    }

    public void setIdleMins(int idleMins)
    {
        this.idleMins = idleMins;
    }

    private int idleMins;
}
