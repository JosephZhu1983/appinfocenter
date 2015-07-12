package me.josephzhu.appinfocenter.common;

/**
 * Created by joseph on 15/7/10.
 */
public class Log extends Entry
{
    private int level;
    private String message;

    public Log()
    {
        channelName = "appinfocenter/log";
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}