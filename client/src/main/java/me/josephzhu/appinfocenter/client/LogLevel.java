package me.josephzhu.appinfocenter.client;

/**
 * Created by joseph on 15/7/10.
 */
public enum LogLevel
{
    Debug(1), Info(2), Warning(3), Error(4);

    private int value;

    LogLevel(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
