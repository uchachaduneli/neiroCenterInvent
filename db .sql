-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.23 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             11.2.0.6230
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for invent
CREATE DATABASE IF NOT EXISTS `invent` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `invent`;

-- Dumping structure for table invent.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table invent.departments
CREATE TABLE IF NOT EXISTS `departments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table invent.invent_database
CREATE TABLE IF NOT EXISTS `invent_database` (
  `invent_id` int NOT NULL AUTO_INCREMENT,
  `invent_code` varchar(50) DEFAULT '/--/',
  `invent_person` varchar(255) DEFAULT NULL,
  `invent_thing` varchar(255) DEFAULT NULL,
  `invent_description` varchar(255) DEFAULT NULL,
  `invent_one_cost` double DEFAULT NULL,
  `invent_amount` int DEFAULT NULL,
  `invent_sum_cost` double DEFAULT NULL,
  `invent_status_old` varchar(255) DEFAULT NULL,
  `invent_status_new` varchar(255) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL COMMENT 'ამათთვის სართულად ვიყენებთ',
  `stelaji` varchar(255) DEFAULT NULL COMMENT 'ამათთვის ოთახის ნომრად',
  `invent_remark` varchar(255) DEFAULT NULL,
  `structure` varchar(200) DEFAULT NULL,
  `zednadebiDate` date DEFAULT NULL,
  `dasawyobeba` date DEFAULT NULL,
  `auditory` varchar(50) DEFAULT NULL,
  `category` varchar(200) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `stocked_at` timestamp NULL DEFAULT NULL COMMENT 'სეზონის ცვლიილებისას საწყობში ჩავარდნის დრო',
  PRIMARY KEY (`invent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9830 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table invent.log
CREATE TABLE IF NOT EXISTS `log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invent_id` int DEFAULT NULL,
  `invent_code` int DEFAULT NULL,
  `old_person` varchar(255) DEFAULT NULL,
  `new_person` varchar(255) DEFAULT NULL,
  `invent_thing` varchar(255) DEFAULT NULL,
  `invent_description` varchar(255) DEFAULT NULL,
  `invent_one_cost` double DEFAULT NULL,
  `invent_amount` int DEFAULT NULL,
  `invent_sum_cost` double DEFAULT NULL,
  `invent_status_old` varchar(255) DEFAULT NULL,
  `invent_status_new` varchar(255) NOT NULL,
  `department` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `stelaji` varchar(255) DEFAULT NULL,
  `invent_remark` varchar(255) DEFAULT NULL,
  `insert_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table invent.names
CREATE TABLE IF NOT EXISTS `names` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for procedure invent.prc_get_user
DELIMITER //
CREATE PROCEDURE `prc_get_user`(IN `p_user_name` varchar(45), IN `p_user_pass` varchar(200), IN `p_user_id` INT)
BEGIN
if p_user_id=-1 then
select * from vi_users u where u.user_name=p_user_name and u.user_password=p_user_pass order by u.user_id asc;
elseif p_user_id=0 then
select * from vi_users u order by u.user_id asc;
elseif p_user_id>0 then
select * from vi_users u where u.user_id=p_user_id order by u.user_id asc;
end if;
END//
DELIMITER ;

-- Dumping structure for procedure invent.prc_get_user_types
DELIMITER //
CREATE PROCEDURE `prc_get_user_types`(IN `p_type_id` INT)
BEGIN
	if p_type_id = 0
		then 
			select * from user_types;
	else
		select * from user_types where user_type_id=p_type_id;
	end if;
END//
DELIMITER ;

-- Dumping structure for procedure invent.prc_start_new_season
DELIMITER //
CREATE PROCEDURE `prc_start_new_season`(OUT `p_result_id` INT)
BEGIN
	set p_result_id = 0;
	update invent_database set invent_status_old=invent_status_new;
	if ROW_COUNT()>0 then
		update invent_database set invent_status_new='-', stocked_at=CURRENT_TIMESTAMP where invent_status_new='+';
			if ROW_COUNT()>0 then
				commit;
	   		set p_result_id=1;
	   	end if;
	end if;
END//
DELIMITER ;

-- Dumping structure for procedure invent.prc_users
DELIMITER //
CREATE PROCEDURE `prc_users`(OUT `p_result_id` INT, IN `p_user_id` INT, IN `p_username` VARCHAR(500), IN `p_description` VARCHAR(500), IN `p_password` VARCHAR(500), IN `p_type_id` INT, IN `p_pass_edit` INT)
BEGIN
set p_result_id = 0;
if p_user_id=0 then
 insert into users
 (user_description,user_name,user_password,type_id) values(p_description,p_username,p_password,p_type_id);
 if ROW_COUNT()>0 then
		commit;
	   set p_result_id=1;
	   end if;
	   
elseif p_user_id>0 then

	if p_pass_edit >0 then
 	update users
 	set user_description=p_description,user_name=p_username,user_password=p_password,type_id=p_type_id where user_id=p_user_id;
 		if ROW_COUNT()>0 then
			commit;
	   	set p_result_id=1;
	   end if;
	else
	update users set user_description=p_description,user_name=p_username,type_id=p_type_id where user_id=p_user_id;
 		if ROW_COUNT()>0 then
			commit;
	   	set p_result_id=1;
	   end if;
	end if;
elseif p_user_id<0 then
 
 delete from users 
      where user_id=abs(p_user_id);
 
 if ROW_COUNT()>0 then
		commit;
	   set p_result_id=1;
	   end if;
end if;

END//
DELIMITER ;

-- Dumping structure for table invent.structures
CREATE TABLE IF NOT EXISTS `structures` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table invent.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_description` varchar(45) DEFAULT NULL,
  `user_name` varchar(45) NOT NULL,
  `user_password` varchar(200) NOT NULL,
  `type_id` int DEFAULT '0',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  KEY `FK_users_user_types` (`type_id`),
  CONSTRAINT `FK_users_user_types` FOREIGN KEY (`type_id`) REFERENCES `user_types` (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for table invent.user_types
CREATE TABLE IF NOT EXISTS `user_types` (
  `user_type_id` int NOT NULL AUTO_INCREMENT,
  `user_type_name` varchar(200) NOT NULL,
  PRIMARY KEY (`user_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- Data exporting was unselected.

-- Dumping structure for view invent.vi_users
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `vi_users` (
	`user_id` INT(10) NOT NULL,
	`user_description` VARCHAR(45) NULL COLLATE 'utf8_general_ci',
	`user_name` VARCHAR(45) NOT NULL COLLATE 'utf8_general_ci',
	`user_password` VARCHAR(200) NOT NULL COLLATE 'utf8_general_ci',
	`type_id` INT(10) NULL,
	`create_date` TIMESTAMP NULL,
	`user_type_name` VARCHAR(200) NULL COLLATE 'utf8_general_ci'
) ENGINE=MyISAM;

-- Dumping structure for trigger invent.invent_database_after_update
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
DELIMITER //
CREATE TRIGGER `invent_database_after_update` AFTER UPDATE ON `invent_database` FOR EACH ROW BEGIN
	if old.invent_person != new.invent_person then
		insert into log (invent_id, invent_code, old_person, new_person,invent_thing, invent_description, invent_one_cost,
		invent_status_old, invent_amount, invent_sum_cost, invent_status_new, department, city, stelaji, invent_remark)
		values(old.invent_id, old.invent_code, old.invent_person, new.invent_person,old.invent_thing, old.invent_description,
		old.invent_one_cost, old.invent_status_old, old.invent_amount, old.invent_sum_cost, old.invent_status_new, old.department,
		old.city, old.stelaji, old.invent_remark);
	end if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumping structure for view invent.vi_users
-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `vi_users`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vi_users` AS select `u`.`user_id` AS `user_id`,`u`.`user_description` AS `user_description`,`u`.`user_name` AS `user_name`,`u`.`user_password` AS `user_password`,`u`.`type_id` AS `type_id`,`u`.`create_date` AS `create_date`,`t`.`user_type_name` AS `user_type_name` from (`users` `u` left join `user_types` `t` on((`u`.`type_id` = `t`.`user_type_id`)));

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
