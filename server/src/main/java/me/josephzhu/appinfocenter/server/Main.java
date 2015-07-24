package me.josephzhu.appinfocenter.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by joseph on 15/7/9.
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        for(AbstractSubmitter submitter : context.getBeansOfType(AbstractSubmitter.class).values())
        {
            System.out.println(submitter.getClass().getName());
            submitter.start();
        }

        for(AbstractAlarmer alarmer : context.getBeansOfType(AbstractAlarmer.class).values())
        {
            System.out.println(alarmer.getClass().getName());
            alarmer.start();
        }
    }
}
