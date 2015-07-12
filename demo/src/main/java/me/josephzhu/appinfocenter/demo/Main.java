package me.josephzhu.appinfocenter.demo;

import me.josephzhu.appinfocenter.client.AppInfoCenter;
import me.josephzhu.appinfocenter.client.StatusCallback;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * Created by joseph on 15/7/10.
 */
public class Main implements StatusCallback
{
    public static void main(String[] args) throws Exception
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        AppInfoCenter appInfoCenter = (AppInfoCenter) context.getBean("appInfoCenter");
        test(appInfoCenter);

        Scanner scan = new Scanner(System.in);
        scan.nextLine();

        System.out.println("exit");
    }

    public static void test(AppInfoCenter appInfoCenter)
    {
        for (int i = 0; i < 10; i++)
        {
            appInfoCenter.debug("debug消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息" + i);
            appInfoCenter.info("info消息" + i);
            appInfoCenter.warning("warning消息" + i);
            appInfoCenter.error("error消息" + i);
            appInfoCenter.exception(new NullPointerException("异常信息" + i));
        }
    }

    @Override
    public boolean getStatus()
    {
        return true;
    }
}
