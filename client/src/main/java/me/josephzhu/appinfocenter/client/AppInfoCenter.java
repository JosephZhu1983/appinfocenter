package me.josephzhu.appinfocenter.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.josephzhu.appinfocenter.common.*;
import me.josephzhu.appinfocenter.common.Exception;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by joseph on 15/7/10.
 */
public class AppInfoCenter
{
    private ThreadLocal<UUID> contextId = new ThreadLocal<>();
    private StatusCallback callback;
    private JedisPool jedisPool;
    private String hostIp;
    private String hostName;
    private final String appName;
    private final String appVersion;
    private final String redisHost;
    private final String logLevel;
    private final int redisPort;
    private int queueSize;
    private LimitQueue<Entry> data = new LimitQueue<>(queueSize);
    private Thread backgroundDataSubmitter;
    private Thread backgroundStatusSubmitter;
    private Logger logger = Logger.getLogger(AppInfoCenter.class);

    public AppInfoCenter(StatusCallback callback, int queueSize, String appName, String appVersion, String redisHost, int redisPort, String logLevel)
    {
        this.callback = callback;
        this.queueSize = queueSize;
        this.appName = appName;
        this.appVersion = appVersion;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.logLevel = logLevel;

        jedisPool = new JedisPool(redisHost, redisPort);

        try
        {
            hostIp = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        try
        {
            hostName = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        logger.debug(String.format("hostip:%s,hostname:%s,appname:%s,appversion:%s,queuesize:%s,loglevel:%s", hostIp, hostName, appName, appVersion, queueSize, logLevel));

        backgroundDataSubmitter = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        submitData();
                    }
                    catch (java.lang.Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally
                    {
                        try
                        {
                            Thread.sleep(10);
                        }
                        catch (InterruptedException e)
                        {

                        }
                    }
                }
            }
        });

        backgroundDataSubmitter.start();

        backgroundStatusSubmitter = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        updateStatus();
                    }
                    catch (java.lang.Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    finally
                    {
                        try
                        {
                            Thread.sleep(10 * 1000);
                        }
                        catch (InterruptedException e)
                        {

                        }
                    }
                }
            }
        });
        backgroundStatusSubmitter.start();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException(Thread t, Throwable e)
            {
                exception(e);
            }
        });
    }

    private void updateStatus()
    {
        boolean healthy = true;
        if (callback != null)
            healthy = callback.getStatus();
        if (healthy)
        {
            Status status = new Status();
            initEntry(status, null);
            status.setHealthy(healthy);

            publish(status);
        }
    }

    private void submitData()
    {
        for (int i = 0; i < 10; i++)
        {
            Entry entry = data.poll();
            if (entry != null)
            {
                publish(entry);
            }
        }
    }

    private void publish(Entry data)
    {
        ObjectMapper mapper = new ObjectMapper();
        String message = "";
        try
        {
            message = mapper.writeValueAsString(data);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        if (!message.equalsIgnoreCase(""))
        {
            Jedis jedis = jedisPool.getResource();
            try
            {
                jedis.publish(data.getChannelName(), message);
            }
            catch (java.lang.Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                jedisPool.returnResourceObject(jedis);
            }

            logger.debug(String.format("Send %s:%s", data.getChannelName(), message));
        }
    }

    private void initEntry(Entry entry, Map<String, Object> extraInfo)
    {
        if (contextId.get() == null)
            contextId.set(UUID.randomUUID());

        entry.setTime(new Date());
        entry.setContextId(contextId.get().toString());
        entry.setServerIp(hostIp);
        entry.setServerName(hostName);
        entry.setAppName(appName);
        entry.setAppVersion(appVersion);
        if (extraInfo == null)
        {
            entry.setExtraInfo("");
        }
        else
        {
            ObjectMapper mapper = new ObjectMapper();
            try
            {

                entry.setExtraInfo(mapper.writeValueAsString(extraInfo));
            }
            catch (java.lang.Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void log(LogLevel level, String message)
    {
        log(level, message, null);
    }

    public void log(LogLevel level, String message, Map<String, Object> extraInfo)
    {
        try
        {
            LogLevel submitLevel = Enum.valueOf(LogLevel.class, logLevel);
            if (submitLevel.getValue() <= level.getValue())
            {
                Log log = new Log();
                initEntry(log, extraInfo);

                log.setLevel(level.getValue());
                log.setMessage(message);

                data.offer(log);
            } else
            {
                logger.info(String.format("配置的记录级别为 %s，舍弃日志 %s - %s", submitLevel, level, message));
            }
        }
        catch (java.lang.Exception ex)
        {
            logger.warn("appinfocenter:" + ex.toString());
        }
    }

    public void exception(Throwable ex)
    {
        exception(ex, null);
    }

    public void exception(Throwable ex, Map<String, Object> extraInfo)
    {
        try
        {
            Exception exception = new Exception();
            initEntry(exception, extraInfo);

            exception.setMessage(ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            exception.setStackTrace(sw.toString());
            exception.setType(ex.getClass().getName());

            data.offer(exception);

            if (ex.getCause() != null)
            {
                Throwable innerexception = ex.getCause();
                Exception exception2 = new Exception();
                initEntry(exception2, extraInfo);

                exception2.setMessage(innerexception.getMessage());
                StringWriter sw2 = new StringWriter();
                PrintWriter pw2 = new PrintWriter(sw2);
                innerexception.printStackTrace(pw2);
                exception2.setStackTrace(sw2.toString());
                exception2.setType(innerexception.getClass().getName());

                data.offer(exception2);
            }
        }
        catch (java.lang.Exception e)
        {
            logger.warn("appinfocenter:" + e.toString());
        }
    }

    public void httplog(String request, String response)
    {
        try
        {
            HttpLog log = new HttpLog();
            initEntry(log,null);
            log.setRequest(request);
            log.setResponse(response);
            data.offer(log);
        }
        catch (java.lang.Exception e)
        {
            logger.warn("appinfocenter:" + e.toString());
        }

    }

    public void debug(String message)
    {
        log(LogLevel.Debug, message);
    }

    public void info(String message)
    {
        log(LogLevel.Info, message);
    }

    public void warning(String message)
    {
        log(LogLevel.Warning, message);
    }

    public void error(String message)
    {
        log(LogLevel.Error, message);
    }

    public void debug(String message, Map<String, Object> extraInfo)
    {
        log(LogLevel.Debug, message, extraInfo);
    }

    public void info(String message, Map<String, Object> extraInfo)
    {
        log(LogLevel.Info, message, extraInfo);
    }

    public void warning(String message, Map<String, Object> extraInfo)
    {
        log(LogLevel.Warning, message, extraInfo);
    }

    public void error(String message, Map<String, Object> extraInfo)
    {
        log(LogLevel.Error, message, extraInfo);
    }


}
