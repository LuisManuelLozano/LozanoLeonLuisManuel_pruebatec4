-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: agencia2
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `flight`
--

DROP TABLE IF EXISTS `flight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flight` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `business_seat_price` double NOT NULL,
  `business_seatsq` int NOT NULL,
  `date_from` date NOT NULL,
  `date_to` date NOT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `economy_seat_price` double NOT NULL,
  `economy_seatsq` int NOT NULL,
  `flight_number` varchar(255) NOT NULL,
  `is_activa` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `origin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaucisqx7arn3fi6eyjmsvqdb3` (`flight_number`),
  UNIQUE KEY `UKjbh38pc40bb3xpe1kxbpqo9e` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight`
--

LOCK TABLES `flight` WRITE;
/*!40000 ALTER TABLE `flight` DISABLE KEYS */;
INSERT INTO `flight` VALUES (1,250,18,'2024-03-15','2024-03-22','Barcelona',100,92,'IBE123',_binary '','Vuelo Madrid-Barcelona','Madrid'),(2,300,16,'2024-04-01','2024-04-08','París',120,79,'BA456',_binary '','Vuelo Londres-París','Londres'),(3,400,25,'2024-05-10','2024-05-17','Los Ángeles',150,120,'AA789',_binary '','Vuelo Nueva York-Los Ángeles','Nueva York'),(4,350,10,'2024-06-05','2024-06-12','Seúl',180,59,'JL012',_binary '','Vuelo Tokio-Seúl','Tokio'),(5,200,5,'2024-07-20','2024-07-27','Abu Dabi',80,40,'EK345',_binary '','Vuelo Dubái-Abu Dabi','Dubái'),(6,280,12,'2024-08-15','2024-08-22','Melbourne',110,66,'QF678',_binary '','Vuelo Sídney-Melbourne','Sídney'),(7,320,18,'2024-10-05','2024-10-12','Kuala Lumpur',130,90,'SQ234',_binary '','Vuelo Singapur-Kuala Lumpur','Singapur'),(8,270,14,'2024-11-20','2024-11-27','Nueva Delhi',105,75,'AI567',_binary '','Vuelo Mumbai-Nueva Delhi','Mumbai'),(9,300,16,'2024-12-15','2024-12-22','Montreal',125,85,'AC890',_binary '','Vuelo Toronto-Montreal','Toronto');
/*!40000 ALTER TABLE `flight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flight_booking`
--

DROP TABLE IF EXISTS `flight_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flight_booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `peopleq` int NOT NULL,
  `flight_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3uiklmjy1d7ba6rrjp6iq08kq` (`flight_id`),
  CONSTRAINT `FK3uiklmjy1d7ba6rrjp6iq08kq` FOREIGN KEY (`flight_id`) REFERENCES `flight` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flight_booking`
--

LOCK TABLES `flight_booking` WRITE;
/*!40000 ALTER TABLE `flight_booking` DISABLE KEYS */;
INSERT INTO `flight_booking` VALUES (1,'2024-03-15',3,1),(2,'2024-03-15',3,1),(3,'2024-03-15',1,1),(4,'2024-04-01',1,2),(6,'2024-03-15',1,1),(7,'2024-03-22',1,1),(8,'2024-06-12',1,4),(12,'2024-08-15',1,6);
/*!40000 ALTER TABLE `flight_booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `double_room_price` double NOT NULL,
  `double_roomsq` int NOT NULL,
  `hotel_code` varchar(255) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `simple_room_price` double NOT NULL,
  `single_roomsq` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsy3ax7w4f8ay5pb0p1csavjra` (`hotel_code`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES (1,160,80,'HTL001',_binary '','Hotel Sol','Madrid',110,55),(2,160,80,'HTL002',_binary '','Hotel Luna','Barcelona',110,60),(3,140,65,'HTL003',_binary '','Hotel Estrella','Valencia',90,40),(4,170,85,'HTL004',_binary '','Hotel Mar','Sevilla',120,70),(5,155,70,'HTL005',_binary '','Hotel Cielo','Bilbao',105,55),(6,150,75,'HTL030',_binary '\0','Hotel Sol2','Madrid',100,55);
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passenger`
--

DROP TABLE IF EXISTS `passenger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passenger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dni` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `flight_booking_id` bigint DEFAULT NULL,
  `room_booking_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk4unw06btj21hqretcigqsyn2` (`flight_booking_id`),
  KEY `FK38up3gfy420c8o68d6n5dc1na` (`room_booking_id`),
  CONSTRAINT `FK38up3gfy420c8o68d6n5dc1na` FOREIGN KEY (`room_booking_id`) REFERENCES `room_booking` (`id`),
  CONSTRAINT `FKk4unw06btj21hqretcigqsyn2` FOREIGN KEY (`flight_booking_id`) REFERENCES `flight_booking` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passenger`
--

LOCK TABLES `passenger` WRITE;
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
INSERT INTO `passenger` VALUES (5,'99999999','Fernández','Pedro',2,11),(6,'LM202345','Martínez','Lucía',2,11),(7,'7777777X','Ramírez','Andrés',2,NULL),(8,'44444444T','Torres','Sofía',NULL,NULL),(9,'1111-2222','Herrera','Marta',4,13),(14,'12345678','Pérez','Juan',8,8),(15,'87654321','López','María',6,NULL),(18,'ABC1234567','Rodríguez','Carlos',6,NULL),(19,'DNI7777777','García','Ana',7,NULL),(21,'XYZ000111','Sánchez','Raúl',12,NULL);
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `disponibility_date_from` date NOT NULL,
  `disponibility_date_to` date NOT NULL,
  `room_type` enum('DOUBLE','SINGLE') DEFAULT NULL,
  `hotel_id` bigint NOT NULL,
  `room_booking_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdosq3ww4h9m2osim6o0lugng8` (`hotel_id`),
  KEY `FKqe49bktdl31dpucqn9c9aps5k` (`room_booking_id`),
  CONSTRAINT `FKdosq3ww4h9m2osim6o0lugng8` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`),
  CONSTRAINT `FKqe49bktdl31dpucqn9c9aps5k` FOREIGN KEY (`room_booking_id`) REFERENCES `room_booking` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (4,'2025-03-15','2025-03-20','SINGLE',2,12),(5,'2025-03-01','2025-03-10','DOUBLE',3,8),(6,'2025-03-15','2025-03-20','SINGLE',3,NULL),(7,'2025-03-01','2025-03-10','DOUBLE',4,7),(10,'2025-03-01','2025-03-10','DOUBLE',5,11),(11,'2025-03-15','2025-03-20','SINGLE',5,13),(13,'2025-03-15','2025-03-20','SINGLE',1,NULL),(15,'2025-03-15','2025-03-20','SINGLE',4,6),(16,'2025-03-15','2025-03-20','SINGLE',4,6);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_booking`
--

DROP TABLE IF EXISTS `room_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_from` date NOT NULL,
  `date_to` date NOT NULL,
  `nights` int NOT NULL,
  `peopleq` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_booking`
--

LOCK TABLES `room_booking` WRITE;
/*!40000 ALTER TABLE `room_booking` DISABLE KEYS */;
INSERT INTO `room_booking` VALUES (6,'2025-03-15','2025-03-20',5,2),(7,'2025-03-01','2025-03-08',5,2),(8,'2025-03-02','2025-03-06',4,2),(11,'2025-03-01','2025-03-05',5,2),(12,'2025-03-15','2025-03-20',2,1),(13,'2025-03-15','2025-03-20',2,1);
/*!40000 ALTER TABLE `room_booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'agencia2'
--

--
-- Dumping routines for database 'agencia2'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-18 21:26:47
