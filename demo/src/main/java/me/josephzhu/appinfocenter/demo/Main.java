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
    static AppInfoCenter appInfoCenter;
    static ApplicationContext context;

    public static void main(String[] args) throws Exception
    {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        appInfoCenter = (AppInfoCenter) context.getBean("appInfoCenter");
        test(appInfoCenter);

        Scanner scan = new Scanner(System.in);
        scan.nextLine();

        System.out.println("exit");
    }

    public static void test(AppInfoCenter appInfoCenter)
    {
        for (int i = 0; i < 2; i++)
        {
            appInfoCenter.debug("11debug消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息一条比较长的消息" + i);
            appInfoCenter.info("11info消息" + i);
            appInfoCenter.warning("11warning消息" + i);
            appInfoCenter.error("11error消息" + i);
            appInfoCenter.exception(new NullPointerException("11异常信息" + i));
            gotexception();
        }

        throw new RuntimeException("11这是一个未处理异常");
    }

    private static void gotexception()
    {
        try
        {
            context.getBean("asdasdas");

        }
        catch (Exception ex)
        {
            appInfoCenter.exception(ex);
        }
    }

    @Override
    public boolean getStatus()
    {
        return true;
    }
}
