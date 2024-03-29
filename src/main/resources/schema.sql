CREATE database `cloudnote` DEFAULT CHARACTER SET utf8;

use cloudnote;

create table cn_article (id varchar(50) not null,
content LONGTEXT not null, createTime datetime not null, hitNum integer not null,
title varchar(50) not null, nId varchar(50), uId varchar(50), primary key (id)) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;

CREATE INDEX cn_article__index_title ON cn_article (title);

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



CREATE TABLE `cloudnote`.`cn_lexicon` (
  `id` VARCHAR(36) NOT NULL COMMENT '主键',
  `name` VARCHAR(200) NULL COMMENT '分词名称',
  `uId` VARCHAR(36) NULL COMMENT '用户主键',
  `discriminator` VARCHAR(36) NULL COMMENT '词库类别',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '中文分词器之词库';

CREATE TABLE `cloudnote`.`cn_log` (
  `id` VARCHAR(36) NOT NULL COMMENT '日志表主键',
  `level` VARCHAR(10) NULL COMMENT '日志级别',
  `className` VARCHAR(100) NULL COMMENT '方法类名',
  `methodName` VARCHAR(45) NULL COMMENT '方法名',
  `message` VARCHAR(200) NULL COMMENT '信息',
  `errorMsg` VARCHAR(200) NULL COMMENT '异常信息',
  `date` DATE NULL COMMENT '日期',
  `costTime` INT NULL COMMENT '耗时',
  `prettyTime` VARCHAR(45) NULL COMMENT '耗时，友好格式',
  `ip` VARCHAR(15) NULL COMMENT '操作ip',
  `userId` VARCHAR(36) NULL COMMENT '用户主键',
  `userName` VARCHAR(45) NULL COMMENT '用户名',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '日志记录表';


ALTER TABLE `cloudnote`.`cn_image` 
ADD INDEX `IDX_NAME` (`name` ASC);

ALTER TABLE `cloudnote`.`cn_article` 
ADD COLUMN `is_shared` TINYINT(1) NULL COMMENT '是否分享到博客' AFTER `uId`;

CREATE TABLE `cloudnote`.`cn_account_book` (
  `id` VARCHAR(36) NOT NULL COMMENT '主键',
  `name` VARCHAR(45) NULL COMMENT '账本名称',
  `mark` VARCHAR(100) NULL COMMENT '账本备注',
  `create_date` DATE NULL COMMENT '创建日期',
  `user_id` VARCHAR(36) NULL COMMENT '创建人',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '记账系统-账本表';

CREATE TABLE `cloudnote`.`cn_category` (
  `id` VARCHAR(36) NOT NULL COMMENT '主键',
  `name` VARCHAR(45) NULL COMMENT '名称',
  `type` VARCHAR(10) NULL COMMENT '收支类型',
  `account_book_id` VARCHAR(36) NULL COMMENT '账本主键',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '记账系统-收支类别';

CREATE TABLE `cloudnote`.`cn_tally` (
  `id` VARCHAR(36) NOT NULL COMMENT '主键',
  `money` FLOAT NULL COMMENT '金额',
  `mark` VARCHAR(100) NULL COMMENT '备注',
  `category_id` VARCHAR(36) NULL COMMENT '收支类别主键',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '记账系统-记账条目';

ALTER TABLE `cloudnote`.`cn_category`
ADD COLUMN `parent_id` VARCHAR(36) NULL AFTER `account_book_id`;

ALTER TABLE `cloudnote`.`cn_tally` 
ADD COLUMN `create_date` DATETIME NULL COMMENT '记账日期' AFTER `category_id`;

ALTER TABLE `cloudnote`.`cn_tally` 
ADD COLUMN `account_book_id` VARCHAR(36) NULL AFTER `create_date`,
ADD COLUMN `type` VARCHAR(10) NULL AFTER `account_book_id`;

ALTER TABLE `cloudnote`.`cn_tally` 
ADD COLUMN `user_id` VARCHAR(36) NULL COMMENT '记账人' AFTER `type`;

ALTER TABLE `cloudnote`.`cn_article` 
ADD INDEX `article_user_id` USING BTREE (`uId` ASC);

ALTER TABLE `cloudnote`.`cn_article`
ADD INDEX `article_create_time_index` (`createTime` ASC);

CREATE TABLE `cloudnote`.`cn_same_article` (
  `id` VARCHAR(36) NOT NULL COMMENT '主键',
  `same_ids` VARCHAR(400) NOT NULL COMMENT '相似文章主键',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT = '相似文章对应表';

-- auto-generated definition
create table cn_quicktext
(
  id             varchar(36) not null
  comment '主键'
    primary key,
  title          varchar(40) not null
  comment '标题',
  content        text        null
  comment '内容',
  createDateTime datetime    not null
  comment '创建时间',
  updateDateTime datetime    not null
  comment '更新时间',
  userId         varchar(36) not null
  comment '用户主键'
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
  comment 'QuickText';

CREATE TABLE cn_current_todo
(
    id varchar(36) PRIMARY KEY COMMENT '主键',
    content varchar(300) NOT NULL COMMENT '待办内容',
    beginDateTime datetime NOT NULL COMMENT '开始时间',
    endDateTime datetime NOT NULL COMMENT '结束时间',
    status varchar(2) DEFAULT 0 NOT NULL COMMENT '完成状态',
    createDateTime datetime NOT NULL COMMENT '创建时间',
    updateDateTime datetime NOT NULL COMMENT '更新时间',
    fId varchar(36) COMMENT '父ID',
    userId varchar(36) NOT NULL COMMENT '用户主键'
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
  comment '待办事项';

ALTER TABLE cn_current_todo ADD INDEX cn_current_todo_createDateTime_index (createDateTime);
ALTER TABLE cn_current_todo ADD INDEX cn_current_todo_updateDateTime_index (updateDateTime);
ALTER TABLE cn_current_todo ADD INDEX cn_current_todo_beginDateTime_index (beginDateTime);
ALTER TABLE cn_current_todo ADD INDEX cn_current_todo_endDateTime_index (endDateTime);
ALTER TABLE cn_current_todo ADD INDEX cn_current_todo_status_index (status);