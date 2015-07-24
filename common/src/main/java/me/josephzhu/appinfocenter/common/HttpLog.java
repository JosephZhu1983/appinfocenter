package me.josephzhu.appinfocenter.common;

/**
 * Created by joseph on 15/7/24.
 */
public class HttpLog extends Entry
{
    @Override
    public String getChannelName()
    {
        return "appinfocenter/httplog";
    }

    public String getRequest()
    {
        return request;
    }

    public void setRequest(String request)
    {
        this.request = request;
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    private String request;
    private String response;
}
