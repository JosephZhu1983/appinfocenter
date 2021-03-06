package me.josephzhu.appinfocenter.site.mapper;

import me.josephzhu.appinfocenter.common.Log;
import me.josephzhu.appinfocenter.site.entity.App;
import me.josephzhu.appinfocenter.site.entity.AppDetail;
import me.josephzhu.appinfocenter.site.entity.LoginUser;
import me.josephzhu.appinfocenter.site.entity.Server;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by joseph on 15/7/11.
 */
@Component
public interface MainMapper
{
    @Select("select id,email from accounts where email=#{email} and password=#{password} ")
    LoginUser login(@Param("email") String email, @Param("password") String password);

    @Select("select a.id, a.name," +
            "a.version," +
            "SUM(CASE WHEN TIMESTAMPDIFF(MINUTE, b.last_active_time, NOW()) < 1 then 1 else 0 end) as activeServers," +
            "max(b.last_active_time) as lastActiveTime " +
            "from apps a " +
            "left join app_status b " +
            "on a.id = b.app_id " +
            "group by a.id order by activeServers desc, a.`name` asc")
    List<App> getApps();

    @Select("select a.id as statusId,b.name as serverName,b.ip as serverIp,a.last_active_time as lastActiveTime,TIMESTAMPDIFF(MINUTE, a.last_active_time, NOW()) as idleMins from app_status a " +
            "inner join servers b on a.server_id = b.id where a.app_id = #{id} order by a.last_active_time desc")
    List<AppDetail> getAppDetail(@Param("id") int id);

    @Select("select id,name,ip from servers where id in (select distinct server_id from logs where app_id = #{id})")
    List<Server> getLogServers(@Param("id") int id);

    int getLogsCount(@Param("begin") Date begin, @Param("end") Date end, @Param("contextId") String contextId, @Param("levels") int[] levels, @Param("appId") Integer appId, @Param("serverIds") int[] serverIds);

    List<Log> getLogs(@Param("begin") Date begin, @Param("end") Date end, @Param("contextId") String contextId, @Param("levels") int[] levels, @Param("appId") Integer appId, @Param("serverIds") int[] serverIds, @Param("start") int start, @Param("count") int count);


    @Select("select id,name,ip from servers where id in (select distinct server_id from exceptions where app_id = #{id})")
    List<Server> getExceptionServers(@Param("id") int id);

    @Select("select distinct type from exceptions where app_id = #{id}")
    List<String> getExceptionTypes(@Param("id") int id);

    int getExceptionsCount(@Param("begin") Date begin, @Param("end") Date end, @Param("contextId") String contextId, @Param("types") String[] types, @Param("appId") Integer appId, @Param("serverIds") int[] serverIds);

    List<me.josephzhu.appinfocenter.common.Exception> getExceptions(@Param("begin") Date begin, @Param("end") Date end, @Param("contextId") String contextId, @Param("types") String[] types, @Param("appId") Integer appId, @Param("serverIds") int[] serverIds, @Param("start") int start, @Param("count") int count);


    int getHttpLogsCount(@Param("begin") Date begin, @Param("end") Date end,@Param("surl") String url, @Param("userId") String userId, @Param("appId") Integer appId, @Param("serverIds") int[] serverIds);

    List<me.josephzhu.appinfocenter.common.HttpLog> getHttpLogs(@Param("begin") Date begin, @Param("end") Date end, @Param("surl") String url, @Param("userId") String userId, @Param("appId") Integer appId, @Param("serverIds") int[] serverIds, @Param("start") int start, @Param("count") int count);

    @Select("select id,name,ip from servers where id in (select distinct server_id from httplogs where app_id = #{id})")
    List<Server> getHttpLogServers(@Param("id") int id);

    @Select("select count(0) from logs where create_time>=#{begin} and create_time<=#{end} and app_id=${app_id} and level=${level}")
    int getLogChartData(@Param("level") int level, @Param("app_id") int appId, @Param("begin") Date begin, @Param("end") Date end);

    @Select("select count(0) from exceptions where create_time>=#{begin} and create_time<=#{end} and app_id=${app_id}")
    int getExceptionChartCount(@Param("app_id") int appId, @Param("begin") Date begin, @Param("end") Date end);

    @Select("select count(0) from httplogs where create_time>=#{begin} and create_time<=#{end} and app_id=${app_id}")
    int getHttpLogsChartCount(@Param("app_id") int appId, @Param("begin") Date begin, @Param("end") Date end);

    @Delete("delete from app_status where id=#{id}")
    int deleteStatus(@Param("id") int statusId);

    @Delete("delete from apps where id=#{id}")
    int deleteApp(@Param("id") int appId);
}
