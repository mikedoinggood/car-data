-- MySQL dump 10.13  Distrib 5.7.25, for Linux (x86_64)
--
-- Host: localhost    Database: homestead
-- ------------------------------------------------------
-- Server version	5.7.25-0ubuntu0.18.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cars`
--

DROP TABLE IF EXISTS `cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cars` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `make` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `model` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cars`
--

LOCK TABLES `cars` WRITE;
/*!40000 ALTER TABLE `cars` DISABLE KEYS */;
INSERT INTO `cars` VALUES (186,2017,'Honda','Civic','2019-03-07 09:59:45','2019-03-07 09:59:45'),(187,2016,'Toyota','Corolla','2019-03-07 09:59:49','2019-03-07 09:59:49'),(188,2013,'Ford','Explorer','2019-03-07 09:59:54','2019-03-07 09:59:54'),(189,2017,'Honda','Civic','2019-03-07 10:00:05','2019-03-07 10:00:05'),(191,2017,'Honda','Civic','2019-03-07 10:00:32','2019-03-07 10:00:32'),(192,2017,'Honda','Civic','2019-03-07 10:00:54','2019-03-07 10:00:54'),(193,2012,'Ford','Focus','2019-03-07 10:01:09','2019-03-07 10:01:18'),(194,2017,'Honda','Accord','2019-03-07 10:01:53','2019-03-07 10:01:53'),(195,2017,'Honda','Accord','2019-03-07 10:02:36','2019-03-07 10:02:36'),(196,2017,'Honda','Civic','2019-03-07 20:24:50','2019-03-07 20:24:50'),(197,2016,'Toyota','Corolla','2019-03-07 20:24:53','2019-03-07 20:24:53'),(198,2013,'Ford','Explorer','2019-03-07 20:24:56','2019-03-07 20:24:56'),(199,2017,'Honda','Civic','2019-03-07 20:25:05','2019-03-07 20:25:05'),(201,2017,'Honda','Civic','2019-03-07 20:25:28','2019-03-07 20:25:28'),(202,2017,'Honda','Civic','2019-03-07 20:25:49','2019-03-07 20:25:49'),(203,1991,'Toyota','Corolla','2019-03-07 20:26:09','2019-03-07 20:26:16'),(204,2017,'Honda','Civic','2019-03-07 20:26:59','2019-03-07 20:26:59'),(205,2017,'Honda','Civic','2019-03-07 20:27:24','2019-03-07 20:27:24'),(206,2017,'Honda','Civic','2019-03-07 20:31:00','2019-03-07 20:31:00'),(207,2016,'Toyota','Corolla','2019-03-07 20:31:03','2019-03-07 20:31:03'),(208,2013,'Ford','Explorer','2019-03-07 20:31:06','2019-03-07 20:31:06'),(209,2017,'Honda','Civic','2019-03-07 20:31:15','2019-03-07 20:31:15'),(211,2017,'Honda','Civic','2019-03-07 20:31:38','2019-03-07 20:31:38'),(212,2017,'Honda','Civic','2019-03-07 20:32:02','2019-03-07 20:32:02'),(213,1991,'Toyota','Corolla','2019-03-07 20:32:17','2019-03-07 20:32:23'),(214,2017,'Honda','Civic','2019-03-07 20:33:04','2019-03-07 20:33:04'),(215,2017,'Honda','Civic','2019-03-07 20:33:28','2019-03-07 20:33:28'),(216,2017,'Honda','Civic','2019-03-07 20:37:10','2019-03-07 20:37:10'),(217,2016,'Toyota','Corolla','2019-03-07 20:37:13','2019-03-07 20:37:13'),(218,2013,'Ford','Explorer','2019-03-07 20:37:16','2019-03-07 20:37:16'),(219,2017,'Honda','Civic','2019-03-07 20:37:27','2019-03-07 20:37:27'),(221,2017,'Honda','Civic','2019-03-07 20:37:56','2019-03-07 20:37:56'),(222,2017,'Honda','Civic','2019-03-07 20:38:13','2019-03-07 20:38:13'),(223,2012,'Ford','Focus','2019-03-07 20:38:30','2019-03-07 20:38:37'),(224,2017,'Honda','Accord','2019-03-07 20:38:59','2019-03-07 20:38:59'),(225,2017,'Honda','Accord','2019-03-07 20:39:30','2019-03-07 20:39:30'),(226,1999,'honda','civic','2019-03-08 00:08:12','2019-03-08 00:08:12'),(229,2017,'Honda','Civic','2019-03-08 04:09:26','2019-03-08 04:09:26'),(230,2016,'Toyota','Corolla','2019-03-08 04:09:28','2019-03-08 04:09:28'),(231,2013,'Ford','Explorer','2019-03-08 04:09:30','2019-03-08 04:09:30'),(232,2017,'Honda','Civic','2019-03-08 04:09:41','2019-03-08 04:09:41'),(234,2017,'Honda','Civic','2019-03-08 04:10:08','2019-03-08 04:10:08'),(235,2017,'Honda','Civic','2019-03-08 04:10:24','2019-03-08 04:10:24'),(236,2012,'Ford','Focus','2019-03-08 04:10:39','2019-03-08 04:10:44'),(237,2017,'Honda','Accord','2019-03-08 04:11:05','2019-03-08 04:11:05'),(238,2017,'Honda','Accord','2019-03-08 04:11:33','2019-03-08 04:11:33');
/*!40000 ALTER TABLE `cars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `migrations`
--

DROP TABLE IF EXISTS `migrations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `migrations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `migration` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `migrations`
--

LOCK TABLES `migrations` WRITE;
/*!40000 ALTER TABLE `migrations` DISABLE KEYS */;
INSERT INTO `migrations` VALUES (4,'2014_10_12_000000_create_users_table',1),(5,'2014_10_12_100000_create_password_resets_table',1),(6,'2019_03_04_065455_create_cars_table',1),(7,'2019_03_04_065610_create_trim_levels_table',1);
/*!40000 ALTER TABLE `migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_resets`
--

DROP TABLE IF EXISTS `password_resets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_resets` (
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  KEY `password_resets_email_index` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_resets`
--

LOCK TABLES `password_resets` WRITE;
/*!40000 ALTER TABLE `password_resets` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_resets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trim_levels`
--

DROP TABLE IF EXISTS `trim_levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trim_levels` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `car_id` bigint(20) unsigned NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `trim_levels_car_id_foreign` (`car_id`),
  CONSTRAINT `trim_levels_car_id_foreign` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=313 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trim_levels`
--

LOCK TABLES `trim_levels` WRITE;
/*!40000 ALTER TABLE `trim_levels` DISABLE KEYS */;
INSERT INTO `trim_levels` VALUES (186,'J3G5Q',186,'2019-03-07 09:59:45','2019-03-07 09:59:45'),(187,'wAjVi',186,'2019-03-07 09:59:45','2019-03-07 09:59:45'),(188,'OnjNS',186,'2019-03-07 09:59:45','2019-03-07 09:59:45'),(189,'C8ZXf',187,'2019-03-07 09:59:49','2019-03-07 09:59:49'),(190,'pyV63',187,'2019-03-07 09:59:49','2019-03-07 09:59:49'),(191,'THvKy',187,'2019-03-07 09:59:49','2019-03-07 09:59:49'),(192,'6Od6H',188,'2019-03-07 09:59:54','2019-03-07 09:59:54'),(193,'2egWv',188,'2019-03-07 09:59:54','2019-03-07 09:59:54'),(194,'1NETE',188,'2019-03-07 09:59:54','2019-03-07 09:59:54'),(195,'F6PAB',189,'2019-03-07 10:00:05','2019-03-07 10:00:05'),(196,'x7ndY',189,'2019-03-07 10:00:05','2019-03-07 10:00:05'),(197,'G7bbo',189,'2019-03-07 10:00:05','2019-03-07 10:00:05'),(201,'DudXV',191,'2019-03-07 10:00:32','2019-03-07 10:00:42'),(202,'oVber',191,'2019-03-07 10:00:32','2019-03-07 10:00:42'),(203,'pvyZB',191,'2019-03-07 10:00:32','2019-03-07 10:00:42'),(204,'ktNiV',191,'2019-03-07 10:00:42','2019-03-07 10:00:42'),(205,'05Vmy',192,'2019-03-07 10:00:54','2019-03-07 10:00:59'),(206,'pNdOK',192,'2019-03-07 10:00:54','2019-03-07 10:00:59'),(208,'GkheZ',193,'2019-03-07 10:01:09','2019-03-07 10:01:18'),(209,'MG9YK',193,'2019-03-07 10:01:09','2019-03-07 10:01:18'),(210,'o2FvU',193,'2019-03-07 10:01:09','2019-03-07 10:01:18'),(211,'9Z9gR',196,'2019-03-07 20:24:50','2019-03-07 20:24:50'),(212,'NYyo4',196,'2019-03-07 20:24:50','2019-03-07 20:24:50'),(213,'aYpVF',196,'2019-03-07 20:24:50','2019-03-07 20:24:50'),(214,'Yk1SB',197,'2019-03-07 20:24:53','2019-03-07 20:24:53'),(215,'cHEbe',197,'2019-03-07 20:24:53','2019-03-07 20:24:53'),(216,'dv51B',197,'2019-03-07 20:24:53','2019-03-07 20:24:53'),(217,'04O17',198,'2019-03-07 20:24:56','2019-03-07 20:24:56'),(218,'gDZhE',198,'2019-03-07 20:24:56','2019-03-07 20:24:56'),(219,'oCGs9',198,'2019-03-07 20:24:56','2019-03-07 20:24:56'),(220,'C92IK',199,'2019-03-07 20:25:05','2019-03-07 20:25:05'),(221,'R5PYc',199,'2019-03-07 20:25:05','2019-03-07 20:25:05'),(222,'UnYb4',199,'2019-03-07 20:25:05','2019-03-07 20:25:05'),(226,'LNpxt',201,'2019-03-07 20:25:28','2019-03-07 20:25:33'),(227,'cpN4h',201,'2019-03-07 20:25:28','2019-03-07 20:25:33'),(228,'vm8Za',201,'2019-03-07 20:25:28','2019-03-07 20:25:33'),(229,'ualOy',201,'2019-03-07 20:25:33','2019-03-07 20:25:33'),(231,'OX8il',202,'2019-03-07 20:25:49','2019-03-07 20:25:54'),(232,'TtRD0',202,'2019-03-07 20:25:49','2019-03-07 20:25:54'),(233,'kfFxH',203,'2019-03-07 20:26:09','2019-03-07 20:26:16'),(234,'3dq9v',203,'2019-03-07 20:26:09','2019-03-07 20:26:16'),(235,'9tR9z',203,'2019-03-07 20:26:09','2019-03-07 20:26:16'),(236,'RhuNv',206,'2019-03-07 20:31:00','2019-03-07 20:31:00'),(237,'uwDrJ',206,'2019-03-07 20:31:00','2019-03-07 20:31:00'),(238,'vF3JH',206,'2019-03-07 20:31:00','2019-03-07 20:31:00'),(239,'2kEYB',207,'2019-03-07 20:31:03','2019-03-07 20:31:03'),(240,'wVSu4',207,'2019-03-07 20:31:03','2019-03-07 20:31:03'),(241,'zqer3',207,'2019-03-07 20:31:03','2019-03-07 20:31:03'),(242,'9zSqW',208,'2019-03-07 20:31:06','2019-03-07 20:31:06'),(243,'ZBj6x',208,'2019-03-07 20:31:06','2019-03-07 20:31:06'),(244,'h8RX8',208,'2019-03-07 20:31:06','2019-03-07 20:31:06'),(245,'3MzbT',209,'2019-03-07 20:31:15','2019-03-07 20:31:15'),(246,'9tvBP',209,'2019-03-07 20:31:15','2019-03-07 20:31:15'),(247,'XsyBx',209,'2019-03-07 20:31:15','2019-03-07 20:31:15'),(251,'ARXN2',211,'2019-03-07 20:31:38','2019-03-07 20:31:45'),(252,'IMWTC',211,'2019-03-07 20:31:38','2019-03-07 20:31:45'),(253,'PIgaX',211,'2019-03-07 20:31:38','2019-03-07 20:31:45'),(254,'O6865',211,'2019-03-07 20:31:45','2019-03-07 20:31:45'),(256,'5t4TS',212,'2019-03-07 20:32:02','2019-03-07 20:32:08'),(257,'wV1dL',212,'2019-03-07 20:32:02','2019-03-07 20:32:08'),(258,'1k6An',213,'2019-03-07 20:32:17','2019-03-07 20:32:23'),(259,'Dy7Wt',213,'2019-03-07 20:32:17','2019-03-07 20:32:23'),(260,'FBr4M',213,'2019-03-07 20:32:17','2019-03-07 20:32:23'),(261,'doyWh',216,'2019-03-07 20:37:10','2019-03-07 20:37:10'),(262,'eU6Es',216,'2019-03-07 20:37:10','2019-03-07 20:37:10'),(263,'LbTBF',216,'2019-03-07 20:37:10','2019-03-07 20:37:10'),(264,'6vdnU',217,'2019-03-07 20:37:13','2019-03-07 20:37:13'),(265,'SzNSw',217,'2019-03-07 20:37:13','2019-03-07 20:37:13'),(266,'z73Id',217,'2019-03-07 20:37:13','2019-03-07 20:37:13'),(267,'OuRoj',218,'2019-03-07 20:37:16','2019-03-07 20:37:16'),(268,'EgiOf',218,'2019-03-07 20:37:16','2019-03-07 20:37:16'),(269,'5dAAW',218,'2019-03-07 20:37:16','2019-03-07 20:37:16'),(270,'K2Ytt',219,'2019-03-07 20:37:27','2019-03-07 20:37:27'),(271,'8sLq5',219,'2019-03-07 20:37:27','2019-03-07 20:37:27'),(272,'KL1ys',219,'2019-03-07 20:37:27','2019-03-07 20:37:27'),(276,'rdadd',221,'2019-03-07 20:37:56','2019-03-07 20:38:02'),(277,'KF2hi',221,'2019-03-07 20:37:56','2019-03-07 20:38:02'),(278,'mRk1c',221,'2019-03-07 20:37:56','2019-03-07 20:38:02'),(279,'u1MVq',221,'2019-03-07 20:38:02','2019-03-07 20:38:02'),(280,'3wxew',222,'2019-03-07 20:38:13','2019-03-07 20:38:19'),(281,'TcJ0X',222,'2019-03-07 20:38:13','2019-03-07 20:38:19'),(283,'FYtRf',223,'2019-03-07 20:38:30','2019-03-07 20:38:37'),(284,'Z4pRR',223,'2019-03-07 20:38:30','2019-03-07 20:38:37'),(285,'AMJJ3',223,'2019-03-07 20:38:30','2019-03-07 20:38:37'),(286,'a',226,'2019-03-08 00:08:12','2019-03-08 00:08:12'),(287,'b',226,'2019-03-08 00:08:12','2019-03-08 00:08:12'),(288,'y2HlG',229,'2019-03-08 04:09:26','2019-03-08 04:09:26'),(289,'c3nEY',229,'2019-03-08 04:09:26','2019-03-08 04:09:26'),(290,'f0lX2',229,'2019-03-08 04:09:26','2019-03-08 04:09:26'),(291,'nxUmh',230,'2019-03-08 04:09:28','2019-03-08 04:09:28'),(292,'NOVpw',230,'2019-03-08 04:09:28','2019-03-08 04:09:28'),(293,'gfnQF',230,'2019-03-08 04:09:28','2019-03-08 04:09:28'),(294,'M0bff',231,'2019-03-08 04:09:30','2019-03-08 04:09:30'),(295,'juhUU',231,'2019-03-08 04:09:30','2019-03-08 04:09:30'),(296,'UPoV5',231,'2019-03-08 04:09:30','2019-03-08 04:09:30'),(297,'lF7IW',232,'2019-03-08 04:09:41','2019-03-08 04:09:41'),(298,'kwJk3',232,'2019-03-08 04:09:41','2019-03-08 04:09:41'),(299,'uWC8T',232,'2019-03-08 04:09:41','2019-03-08 04:09:41'),(303,'BmIhq',234,'2019-03-08 04:10:08','2019-03-08 04:10:13'),(304,'WOLW9',234,'2019-03-08 04:10:08','2019-03-08 04:10:13'),(305,'O9Z2k',234,'2019-03-08 04:10:08','2019-03-08 04:10:13'),(306,'ckyUb',234,'2019-03-08 04:10:13','2019-03-08 04:10:13'),(307,'bOYkb',235,'2019-03-08 04:10:24','2019-03-08 04:10:29'),(308,'Gu94T',235,'2019-03-08 04:10:24','2019-03-08 04:10:29'),(310,'kTgtf',236,'2019-03-08 04:10:39','2019-03-08 04:10:44'),(311,'74psz',236,'2019-03-08 04:10:39','2019-03-08 04:10:44'),(312,'1VBZX',236,'2019-03-08 04:10:39','2019-03-08 04:10:44');
/*!40000 ALTER TABLE `trim_levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'user','user@example.com',NULL,'$2y$10$UmRihBgli5NQZGMyzaj2h.wATGDXOVMSFjMBTAJGVW8AsnI4teRW2',NULL,'2019-03-07 07:29:05','2019-03-07 07:29:05');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-08 20:33:02