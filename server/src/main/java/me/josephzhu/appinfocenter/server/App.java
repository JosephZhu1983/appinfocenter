package me.josephzhu.appinfocenter.server;

/**
 * Created by joseph on 15/7/15.
 */
public class App
{
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    private int id;
    private String name;
    private String version;
}
