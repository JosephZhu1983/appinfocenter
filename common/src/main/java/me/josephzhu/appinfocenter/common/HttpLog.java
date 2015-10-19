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

    private String userId;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getQueryString()
    {
        return queryString;
    }

    public void setQueryString(String queryString)
    {
        this.queryString = queryString;
    }

    public String getRequestHeader()
    {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader)
    {
        this.requestHeader = requestHeader;
    }

    public String getRequestBody()
    {
        return requestBody;
    }

    public void setRequestBody(String requestBody)
    {
        this.requestBody = requestBody;
    }

    public String getResponseHeader()
    {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader)
    {
        this.responseHeader = responseHeader;
    }

    public String getResponseBody()
    {
        return responseBody;
    }

    public void setResponseBody(String responseBody)
    {
        this.responseBody = responseBody;
    }

    private String url;
    private String queryString;
    private String requestHeader;
    private String requestBody;
    private String responseHeader;
    private String responseBody;
}
