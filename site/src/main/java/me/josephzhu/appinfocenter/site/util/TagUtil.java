package me.josephzhu.appinfocenter.site.util;

/**
 * Created by joseph on 15/7/12.
 */
public class TagUtil
{
    public static boolean contains(int[] list, int i) {
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
}
