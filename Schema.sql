-- MySQL dump 10.13  Distrib 5.6.21, for Win64 (x86_64)
--
-- Host: cis-linux2.temple.edu    Database: Fa14_4340_Museum
-- ------------------------------------------------------
-- Server version	5.5.38-0ubuntu0.14.04.1

--
-- Table structure for table `Items`
--

DROP TABLE IF EXISTS `Items`;
CREATE TABLE `Items` (
  `Item_id` int(11) NOT NULL AUTO_INCREMENT,
  `Item_name` varchar(45) NOT NULL,
  `Item_URL` varchar(200) NOT NULL,
  `Item_additionalInfo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Museum`
--

DROP TABLE IF EXISTS `Museum`;
CREATE TABLE `Museum` (
  `Museum_id` int(11) NOT NULL AUTO_INCREMENT,
  `Museum_Name` varchar(200) NOT NULL,
  `Museum_Start_hours` datetime NOT NULL,
  `Museum_End_hours` datetime NOT NULL,
  `Museum_interesting_exhibit` varchar(200) DEFAULT NULL,
  `Museum_website` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`Museum_id`),
  UNIQUE KEY `Museum_id_UNIQUE` (`Museum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RelationTable`
--

DROP TABLE IF EXISTS `RelationTable`;
CREATE TABLE `RelationTable` (
  `Relation_id` int(11) NOT NULL,
  `Item_id` int(11) DEFAULT NULL,
  `Museum_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Relation_id`),
  KEY `Museum_id_idx` (`Museum_id`),
  KEY `Item_id_idx` (`Item_id`),
  CONSTRAINT `Item_id` FOREIGN KEY (`Item_id`) REFERENCES `Items` (`Item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `Museum_id` FOREIGN KEY (`Museum_id`) REFERENCES `Museum` (`Museum_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


-- Dump completed on 2014-12-02 15:16:30
