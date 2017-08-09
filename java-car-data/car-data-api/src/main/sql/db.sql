CREATE DATABASE car_data;

CREATE TABLE `car` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `year` smallint,
  `make` text,
  `model` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `trim_level` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` text,
  `car_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `trim_level_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE USER 'dbuser'@'localhost' IDENTIFIED by 'dbpassword';
GRANT SELECT, INSERT, DELETE, UPDATE ON car_data.* TO 'dbuser'@'localhost';
