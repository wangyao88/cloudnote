CREATE database `cloudnote` DEFAULT CHARACTER SET utf8 ;

use cloudnote;

create table cn_article (id varchar(50) not null,
content LONGTEXT not null, createTime datetime not null, hitNum integer not null,
title varchar(50) not null, nId varchar(50), uId varchar(50), primary key (id)) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

create table cn_flag (id varchar(50) not null, name varchar(50) not null, fId varchar(50),
uId varchar(50), primary key (id))  ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


create table cn_flag_artile (flag_id varchar(50) not null, article_id varchar(50) not null) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

create table cn_note (id varchar(50) not null, name varchar(50) not null, uId varchar(50), primary key (id)) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

create table cn_user (id varchar(50) not null, email varchar(50) not null, name varchar(50) not null,
password varchar(50) not null, primary key (id)) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE TABLE `cloudnote`.`cn_image` (
  `id` VARCHAR(50) NOT NULL,
  `alt` VARCHAR(45) NULL,
  `aId` VARCHAR(50) NULL,
  `content` MEDIUMBLOB NULL,
`name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;


CREATE TABLE `cloudnote`.`cn_message` (
  `id` VARCHAR(50) NOT NULL,
  `fromId` VARCHAR(50) NOT NULL,
  `fromName` VARCHAR(50) NOT NULL,
  `toId` VARCHAR(50) NOT NULL,
  `text` VARCHAR(500) NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

ALTER TABLE `cloudnote`.`cn_user`
  ADD COLUMN `mailpass` VARCHAR(50) NOT NULL AFTER `password`;
  
  CREATE TABLE `cloudnote`.`cn_waitingtask` (
  `id` VARCHAR(50) NOT NULL COMMENT '待办事项主键',
  `name` VARCHAR(200) NULL COMMENT '待办任务名称',
  `createDate` DATETIME NULL COMMENT '创建时间',
  `expire` DATETIME NULL COMMENT '过期时间',
  `taskType` VARCHAR(10) NULL COMMENT '任务类型',
  `uId` VARCHAR(45) NULL COMMENT '用户主键',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
 
ALTER TABLE `cloudnote`.`cn_waitingtask` 
ADD COLUMN `process` DECIMAL(3,2) NULL COMMENT '任务进度' AFTER `uId`;

ALTER TABLE `cloudnote`.`cn_waitingtask` 
ADD COLUMN `content` VARCHAR(500) NULL COMMENT '任务内容' AFTER `process`;

ALTER TABLE `cloudnote`.`cn_waitingtask` 
ADD COLUMN `beginDate` DATETIME NULL COMMENT '开始日期' AFTER `content`;

ALTER TABLE `cloudnote`.`cn_waitingtask` 
CHANGE COLUMN `expire` `expireDate` DATETIME NULL DEFAULT NULL COMMENT '过期时间' ;

ALTER TABLE `cloudnote`.`cn_article` 
CHANGE COLUMN `title` `title` VARCHAR(100) NOT NULL ;


CREATE TABLE `cloudnote`.`cn_disk` (
  `id` VARCHAR(36) NOT NULL COMMENT '云盘主键',
  `totalSize` DOUBLE NULL COMMENT '总空间',
  `usedSize` DOUBLE NULL COMMENT '已使用空间',
  `uId` VARCHAR(36) NULL COMMENT '用户主键',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin
COMMENT = '云盘信息表';


CREATE TABLE `cloudnote`.`cn_fileInfo` (
  `id` VARCHAR(36) NOT NULL COMMENT '文件主键',
  `name` VARCHAR(100) NULL COMMENT '文件名称',
  `size` DOUBLE NULL COMMENT '文件大小',
  `type` VARCHAR(10) NULL COMMENT '文件类型',
  `icon` VARCHAR(10) NULL COMMENT '文件图标',
  `path` VARCHAR(200) NULL COMMENT '文件路径',
  `createDate` DATE NULL COMMENT '上传时间',
  `isshare` TINYINT NULL COMMENT '是否分享',
  `password` VARCHAR(45) NULL COMMENT '分享密码',
  `shareurl` VARCHAR(200) NULL COMMENT '分享链接',
  `description` VARCHAR(45) NULL COMMENT '文件描述',
  `md5` VARCHAR(45) NULL COMMENT 'md5值',
  `fId` VARCHAR(36) NULL COMMENT '父文件主键',
  `dId` VARCHAR(36) NULL COMMENT '所属云盘主键',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin
COMMENT = '云盘文件信息表';

ALTER TABLE `cloudnote`.`cn_image` 
CHANGE COLUMN `alt` `alt` VARCHAR(300) NULL DEFAULT NULL ;
