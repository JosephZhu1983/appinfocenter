package me.josephzhu.appinfocenter.common;

/**
 * Created by joseph on 15/7/10.
 */
public class Exception extends Entry
{
    private String type;
    private String message;
    private String stackTrace;

    public Exception()
    {
        channelName = "appinfocenter/exception";
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace)
    {
        this.stackTrace = stackTrace;
    }

}
