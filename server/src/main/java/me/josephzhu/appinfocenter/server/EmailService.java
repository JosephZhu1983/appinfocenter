package me.josephzhu.appinfocenter.server;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService
{
    private Logger logger = Logger.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private DbMapper dbMapper;

    @Autowired
    private VelocityEngine velocityEngine;


    public void sendAlarmMail(String env, String email, String condition, String data)
    {

        Map<String, Object> model = new HashMap<>();
        model.put("condition", condition);
        model.put("data", data);
        String text = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, "alarmmail.vm", "UTF-8", model);
        logger.debug(text);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        String subject = "*应用程序信息中心报警* " + env  + condition;
        try
        {
            helper.setFrom("appinfocenter <notification@yamichu.com>");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text, true);
        }
        catch (MessagingException ex)
        {
            ex.printStackTrace();
            return;
        }

        String error = "";
        try
        {
            mailSender.send(message);
        }
        catch (MailException ex)
        {
            error = ex.getMessage();
            ex.printStackTrace();
        }
        finally
        {
            dbMapper.sendAlamMail(new Date(), subject, text, email, error);
        }
    }
}
