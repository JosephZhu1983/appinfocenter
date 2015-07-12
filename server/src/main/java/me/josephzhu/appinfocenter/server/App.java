package me.josephzhu.appinfocenter.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by joseph on 15/7/9.
 */
public class App
{
    public static void main(String[] args) throws Exception
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.getBeansOfType(AbstractSubmitter.class).values().forEach(submitter ->
        {
            System.out.println(submitter.getClass().getName());
            submitter.start();
        });
    }
}
