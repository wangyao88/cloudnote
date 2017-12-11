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