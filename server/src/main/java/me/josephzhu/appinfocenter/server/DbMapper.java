package me.josephzhu.appinfocenter.server;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * Created by joseph on 15/7/9.
 */
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
}
