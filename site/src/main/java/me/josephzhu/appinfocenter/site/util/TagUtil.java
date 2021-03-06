package me.josephzhu.appinfocenter.site.util;

/**
 * Created by joseph on 15/7/12.
 */
public class TagUtil
{
    public static boolean intcontains(int[] list, int i)
    {
        boolean result = false;
        if (list != null)
        {
            for (int item : list)
            {
                if (item == i)
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean stringcontains(String[] list, String i)
    {
        boolean result = false;
        if (list != null)
        {
            for (String item : list)
            {
                if (item.equalsIgnoreCase(i))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public static String shortname(String s)
    {
        int i = s.lastIndexOf('.');
        if (i > 0)
            return s.substring(i + 1);
        return s;
    }
}
