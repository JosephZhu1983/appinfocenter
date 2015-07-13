package me.josephzhu.appinfocenter.site.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by joseph on 15/7/13.
 */
public class ChartsData
{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "CCT")
    private List<Date> x;
    private List<Series> data;

    public List<Date> getX()
    {
        return x;
    }

    public void setX(List<Date> x)
    {
        this.x = x;
    }

    public List<Series> getData()
    {
        return data;
    }

    public void setData(List<Series> data)
    {
        this.data = data;
    }
}
