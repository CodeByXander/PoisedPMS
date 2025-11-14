-- MySQL dump 10.13  Distrib 9.4.0, for macos15 (arm64)
--
-- Host: localhost    Database: PoisedPMS
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Person`
--

DROP TABLE IF EXISTS `Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Person` (
  `person_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Person`
--

LOCK TABLES `Person` WRITE;
/*!40000 ALTER TABLE `Person` DISABLE KEYS */;
INSERT INTO `Person` VALUES (1,'Sarah','Jacobs','0823456789','sarah@designhub.co.za','5 Long St, Cape Town'),(2,'Lorna','Smith','0847789911','lorna@blueprint.co.za','21 Design Ave, Johannesburg'),(3,'Peter','Adams','0846677788','peter@archiworks.co.za','15 Design Rd, Pretoria'),(4,'Tom','Baker','0835551212','tom@buildit.co.za','19 Stone Rd, Cape Town'),(5,'Mark','Daniels','0823339090','mark@construct.co.za','88 Main Rd, Johannesburg'),(6,'Neil','Carter','0835566778','neil@buildsmart.co.za','9 Industry St, Pretoria'),(7,'Mike','Tyson','0728899933','mike.tyson@gmail.com','12 Maple Rd, Cape Town'),(8,'Jared','Goldman','0712278899','jgoldman@gmail.com','8 Hillcrest Ave, Johannesburg'),(9,'Rohan','Patel','0799988776','rohan.patel@gmail.com','101 Market St, Pretoria'),(10,'Mark','Roberts','0812396563','mrob@gmail.com','13 River Rd, Durban'),(11,'Edward','Johnson','0796365859','edward337@yahoo.com','13 Parfit Ave, Bloemfontein');
/*!40000 ALTER TABLE `Person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Project` (
  `project_id` int NOT NULL AUTO_INCREMENT,
  `project_name` varchar(100) DEFAULT NULL,
  `building_type` varchar(100) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `erf_number` varchar(50) DEFAULT NULL,
  `total_fee` decimal(10,2) DEFAULT NULL,
  `amount_paid` decimal(10,2) DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `completion_date` date DEFAULT NULL,
  `status` enum('In Progress','Finalised') DEFAULT 'In Progress',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
INSERT INTO `Project` VALUES (1,'House Tyson','House','12 Maple Rd, Cape Town','1054',850000.00,400000.00,'2025-12-23',NULL,'In Progress'),(2,'Apartment Goldman','Apartment','8 Hillcrest Ave, Johannesburg','2089',3200000.00,1500000.00,'2026-02-23',NULL,'In Progress'),(3,'Store Roberts','Store','45 Queen St, Durban','3021',12000000.00,12000000.00,'2025-09-05','2025-08-23','Finalised'),(4,'Office Patel','Office','101 Market St, Pretoria','4567',2700000.00,1200000.00,'2024-11-15','2024-11-22','Finalised'),(5,'House Johnson','House','64 Olive Rd, Bloemfontein','5123',980000.00,800000.00,'2025-12-10',NULL,'In Progress');
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project_Person`
--

DROP TABLE IF EXISTS `Project_Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Project_Person` (
  `project_id` int NOT NULL,
  `person_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`project_id`,`person_id`,`role_id`),
  KEY `person_id` (`person_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `project_person_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `Project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `project_person_ibfk_2` FOREIGN KEY (`person_id`) REFERENCES `Person` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `project_person_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `Role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project_Person`
--

LOCK TABLES `Project_Person` WRITE;
/*!40000 ALTER TABLE `Project_Person` DISABLE KEYS */;
INSERT INTO `Project_Person` VALUES (5,1,1),(2,2,1),(4,2,1),(1,3,1),(3,3,1),(3,4,2),(2,5,2),(4,5,2),(1,6,2),(5,6,2),(1,7,3),(2,8,3),(4,9,3),(3,10,3),(5,11,3);
/*!40000 ALTER TABLE `Project_Person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES (1,'Architect'),(2,'Contractor'),(3,'Customer');
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-14 14:29:23
