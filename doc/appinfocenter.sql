/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50624
 Source Host           : localhost
 Source Database       : appinfocenter

 Target Server Type    : MySQL
 Target Server Version : 50624
 File Encoding         : utf-8

 Date: 07/24/2015 13:03:50 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `accounts`
-- ----------------------------
DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `receivealarm` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `alarmmails`
-- ----------------------------
DROP TABLE IF EXISTS `alarmmails`;
CREATE TABLE `alarmmails` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mailsubject` varchar(200) DEFAULT NULL,
  `mailbody` mediumtext,
  `sendtime` datetime DEFAULT NULL,
  `sendto` varchar(255) DEFAULT NULL,
  `error` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `app_status`
-- ----------------------------
DROP TABLE IF EXISTS `app_status`;
CREATE TABLE `app_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(11) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `last_active_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `apps`
-- ----------------------------
DROP TABLE IF EXISTS `apps`;
CREATE TABLE `apps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`,`version`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `exceptions`
-- ----------------------------
DROP TABLE IF EXISTS `exceptions`;
CREATE TABLE `exceptions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `stacktrace` varchar(20000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `app_id` int(11) DEFAULT NULL,
  `context_id` varchar(50) DEFAULT NULL,
  `extrainfo` text,
  PRIMARY KEY (`id`),
  KEY `type` (`type`),
  KEY `server_id` (`server_id`),
  KEY `app_id` (`app_id`),
  KEY `context_id` (`context_id`) USING HASH,
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3152 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `httplogs`
-- ----------------------------
DROP TABLE IF EXISTS `httplogs`;
CREATE TABLE `httplogs` (
  `id` int(11) NOT NULL,
  `request` text,
  `response` text,
  `create_time` time DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `app_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `logs`
-- ----------------------------
DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `level` tinyint(4) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `app_id` int(11) DEFAULT NULL,
  `context_id` varchar(50) DEFAULT NULL,
  `extrainfo` text,
  PRIMARY KEY (`id`),
  KEY `server_id` (`server_id`),
  KEY `app_id` (`app_id`),
  KEY `level` (`level`),
  KEY `context_id` (`context_id`) USING BTREE,
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=11466 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `servers`
-- ----------------------------
DROP TABLE IF EXISTS `servers`;
CREATE TABLE `servers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ip` (`ip`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Procedure structure for `sp_create_exception`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_create_exception`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_create_exception`(
IN p_type varchar(255),
IN p_message varchar(500),
IN p_stacktrace varchar(20000),
IN p_create_time datetime,
IN p_context_id varchar(50),
IN p_server_name varchar(255),
IN p_server_ip varchar(255),
IN p_app_name varchar(255),
IN p_app_version varchar(255),
IN p_extrainfo text
)
BEGIN  
DECLARE v_server_id INT DEFAULT 0;
DECLARE v_app_id INT DEFAULT 0;
DECLARE v_error INTEGER DEFAULT 0;  

DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET v_error=1;  

START TRANSACTION;  

SELECT id INTO v_app_id FROM apps WHERE `name` = p_app_name and `version` = p_app_version;
IF v_app_id=0 THEN
	INSERT INTO apps (`name`, `version`, `create_time`) VALUES (p_app_name, p_app_version, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_app_id;
END IF;

SELECT id INTO v_server_id FROM servers WHERE `name` = p_server_name and `ip` = p_server_ip;
IF v_server_id=0 THEN
	INSERT INTO servers (`name`, `ip`, `create_time`) VALUES (p_server_name, p_server_ip, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_server_id;
END IF;

INSERT INTO exceptions (`server_id`, `app_id`, `type`, `message`, `stacktrace`, `context_id`, `create_time`, `extrainfo`) 
VALUES (v_server_id, v_app_id, p_type, p_message, p_stacktrace, p_context_id, p_create_time, p_extrainfo);

IF v_error = 1 THEN
	ROLLBACK;
ELSE
	COMMIT;
END IF;

SELECT LAST_INSERT_ID();

END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `sp_create_httplog`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_create_httplog`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_create_httplog`(
IN p_create_time datetime,
IN p_server_name varchar(255),
IN p_server_ip varchar(255),
IN p_app_name varchar(255),
IN p_app_version varchar(255),
IN p_request text,
IN p_resposne text
)
BEGIN  
DECLARE v_server_id INT DEFAULT 0;
DECLARE v_app_id INT DEFAULT 0;
DECLARE v_error INTEGER DEFAULT 0;  

DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET v_error=1;  

START TRANSACTION;  

SELECT id INTO v_app_id FROM apps WHERE `name` = p_app_name and `version` = p_app_version;
IF v_app_id=0 THEN
	INSERT INTO apps (`name`, `version`, `create_time`) VALUES (p_app_name, p_app_version, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_app_id;
END IF;

SELECT id INTO v_server_id FROM servers WHERE `name` = p_server_name and `ip` = p_server_ip;
IF v_server_id=0 THEN
	INSERT INTO servers (`name`, `ip`, `create_time`) VALUES (p_server_name, p_server_ip, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_server_id;
END IF;

INSERT INTO logs (`server_id`, `app_id`, `request`, `response`, `create_time`) 
VALUES (v_server_id, v_app_id, p_request, p_response, p_create_time);

IF v_error = 1 THEN
	ROLLBACK;
ELSE
	COMMIT;
END IF;

SELECT LAST_INSERT_ID();

END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `sp_create_log`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_create_log`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_create_log`(
IN p_level tinyint,
IN p_message varchar(500),
IN p_create_time datetime,
IN p_context_id varchar(50),
IN p_server_name varchar(255),
IN p_server_ip varchar(255),
IN p_app_name varchar(255),
IN p_app_version varchar(255),
IN p_extrainfo text
)
BEGIN  
DECLARE v_server_id INT DEFAULT 0;
DECLARE v_app_id INT DEFAULT 0;
DECLARE v_error INTEGER DEFAULT 0;  

DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET v_error=1;  

START TRANSACTION;  

SELECT id INTO v_app_id FROM apps WHERE `name` = p_app_name and `version` = p_app_version;
IF v_app_id=0 THEN
	INSERT INTO apps (`name`, `version`, `create_time`) VALUES (p_app_name, p_app_version, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_app_id;
END IF;

SELECT id INTO v_server_id FROM servers WHERE `name` = p_server_name and `ip` = p_server_ip;
IF v_server_id=0 THEN
	INSERT INTO servers (`name`, `ip`, `create_time`) VALUES (p_server_name, p_server_ip, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_server_id;
END IF;

INSERT INTO logs (`server_id`, `app_id`, `level`, `message`, `context_id`, `create_time`, `extrainfo`) 
VALUES (v_server_id, v_app_id, p_level, p_message, p_context_id, p_create_time, p_extrainfo);

IF v_error = 1 THEN
	ROLLBACK;
ELSE
	COMMIT;
END IF;

SELECT LAST_INSERT_ID();

END
 ;;
delimiter ;

-- ----------------------------
--  Procedure structure for `sp_update_app_status`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_update_app_status`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_update_app_status`(
IN p_create_time datetime,
IN p_server_name varchar(255),
IN p_server_ip varchar(255),
IN p_app_name varchar(255),
IN p_app_version varchar(255)
)
BEGIN  
DECLARE v_server_id INT DEFAULT 0;
DECLARE v_app_id INT DEFAULT 0;
DECLARE v_app_status_id INT DEFAULT 0;
DECLARE v_error INTEGER DEFAULT 0;  

-- DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET v_error=1;  

START TRANSACTION;  

SELECT id INTO v_app_id FROM apps WHERE `name` = p_app_name and `version` = p_app_version;
IF v_app_id=0 THEN
	INSERT INTO apps (`name`, `version`, `create_time`) VALUES (p_app_name, p_app_version, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_app_id;
END IF;

SELECT id INTO v_server_id FROM servers WHERE `name` = p_server_name and `ip` = p_server_ip;
IF v_server_id=0 THEN
	INSERT INTO servers (`name`, `ip`, `create_time`) VALUES (p_server_name, p_server_ip, p_create_time);
	SELECT LAST_INSERT_ID() INTO v_server_id;
END IF;

SELECT id INTO v_app_status_id FROM app_status WHERE `app_id` = v_app_id and `server_id` = v_server_id;
IF v_app_status_id=0 THEN
	INSERT INTO app_status (`app_id`, `server_id`, `last_active_time`) VALUES (v_app_id, v_server_id, p_create_time);
ELSE
	UPDATE app_status SET last_active_time = p_create_time WHERE `id` = v_app_status_id;
END IF;

IF v_error = 1 THEN
	ROLLBACK;
ELSE
	COMMIT;
END IF;
SELECT v_error;

END
 ;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
