CREATE DATABASE  IF NOT EXISTS `hotelappdatabase` ;
USE `hotelappdatabase`;



DROP TABLE IF EXISTS `guest`;
CREATE TABLE `guest` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `age` int(3) DEFAULT NULL,
  `gender` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

UNLOCK TABLES;

