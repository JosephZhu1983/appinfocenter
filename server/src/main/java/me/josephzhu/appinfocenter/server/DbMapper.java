package me.josephzhu.appinfocenter.server;

import me.josephzhu.appinfocenter.common.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by joseph on 15/7/9.
 */
@Component
public interface DbMapper
{
    @Select("call sp_create_log (#{p_level},#{p_message},#{p_create_time},#{p_context_id},#{p_server_name},#{p_server_ip},#{p_app_name},#{p_app_version})")
    int createLog(@Param("p_level") int level,
                  @Param("p_message") String message,
                  @Param("p_create_time") Date createTime,
                  @Param("p_context_id") String contextId,
                  @Param("p_server_name") String serverName,
                  @Param("p_server_ip") String serverIp,
                  @Param("p_app_name") String appName,
                  @Param("p_app_version") String appVersion);

    @Select("call sp_create_exception (#{p_type},#{p_message},#{p_stacktrace},#{p_create_time},#{p_context_id},#{p_server_name},#{p_server_ip},#{p_app_name},#{p_app_version})")
    int createException(@Param("p_type") String type,
                        @Param("p_message") String message,
                        @Param("p_stacktrace") String stacktrace,
                        @Param("p_create_time") Date createTime,
                        @Param("p_context_id") String contextId,
                        @Param("p_server_name") String serverName,
                        @Param("p_server_ip") String serverIp,
                        @Param("p_app_name") String appName,
                        @Param("p_app_version") String appVersion);

    @Select("call sp_update_app_status(#{p_create_time},#{p_server_name},#{p_server_ip},#{p_app_name},#{p_app_version})")
    int updateStatus(@Param("p_create_time") Date createTime,
                     @Param("p_server_name") String serverName,
                     @Param("p_server_ip") String serverIp,
                     @Param("p_app_name") String appName,
                     @Param("p_app_version") String appVersion);

    @Insert("insert into alarmmails (mailsubject,mailbody,sendto,sendtime,error) values (#{mailsubject}, #{mailbody}, #{sendto}, #{sendtime}, #{error})")
    int sendAlamMail(@Param("sendtime") Date sendtime, @Param("mailsubject") String mailsubject,@Param("mailbody") String mailbody,@Param("sendto") String sendto,@Param("error") String error);

    @Select("select email from accounts where receivealarm=1")
    List<String> getAlarmMailReceivers();

    @Select("select l.level, l.message,l.create_time as time, l.context_id as contextId,a.name as appName, a.version as appVersion, s.name as serverName, s.ip as serverIp from logs l inner join apps a on l.app_id = a.id inner join servers s on s.id = l.app_id where l.app_id=#{app_id} and l.create_time >= #{begin} and l.create_time <= #{end} and l.level>=#{level} order by l.create_time desc limit #{count}")
    List<Log> getLogsForAlarm(@Param("app_id") int appId, @Param("begin") Date begin, @Param("end") Date end, @Param("level") int level, @Param("count") int count);

    @Select("select l.type, l.stacktrace as stackTrace, l.message,l.create_time as time, l.context_id as contextId,a.name as appName, a.version as appVersion, s.name as serverName, s.ip as serverIp from exceptions l inner join apps a on l.app_id = a.id inner join servers s on s.id = l.app_id where l.app_id=#{app_id} and l.create_time >= #{begin} and l.create_time <= #{end} order by l.create_time desc limit #{count}")
    List<me.josephzhu.appinfocenter.common.Exception> getExceptionsForAlarm(@Param("app_id") int appId, @Param("begin") Date begin, @Param("end") Date end,@Param("count") int count);

    @Select("select count(0) from logs where app_id=#{app_id} and create_time >= #{begin} and create_time <= #{end} and level>=#{level}")
    int getLogsCountForAlarm(@Param("app_id") int appId, @Param("begin") Date begin, @Param("end") Date end, @Param("level") int level);

    @Select("select count(0) from exceptions where app_id=#{app_id} and create_time >= #{begin} and create_time <= #{end}")
    int getExceptionsCountForAlarm(@Param("app_id") int appId, @Param("begin") Date begin, @Param("end") Date end);

    @Select("select id,name,version from apps")
    List<App> getApps();
}
