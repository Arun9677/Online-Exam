-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: project3
-- ------------------------------------------------------
-- Server version	8.0.28

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
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Quest` varchar(300) DEFAULT NULL,
  `optiona` varchar(100) DEFAULT NULL,
  `optionb` varchar(100) DEFAULT NULL,
  `optionc` varchar(100) DEFAULT NULL,
  `optiond` varchar(100) DEFAULT NULL,
  `Answer` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'Which of the following is not a Java features?','Dynamic','Architecture Neutral','Use of pointers','Object-oriented','C'),(2,'_____ is used to find and fix bugs in the Java programs.','JVM','JRE','JDK','JDB','D'),(3,'What does the expression float a = 35 / 0 return?','0','Not a number','Infinity','Run Time Exception','C'),(4,'Evaluate the following Java expression, if x=3, y=5, and z=10: ++z + y - y + z + x++','20','24','25','26','C'),(5,'Which of the following is true about the anonymous inner class?','It has only methods','Objects can\'t be created','It has a fixed class name','It has no class name','D'),(6,'Which keyword is used for accessing the features of a package?','package','import','extends','export','B'),(7,'In java, jar stands for_____.','Java Archive Runner','Java Application Resource','Java Application Runner','None of the above','D'),(8,'What is meant by the classes and objects that dependents on each other?','Tight Coupling','Cohesion','Loose Coupling','None of the above','A'),(9,'How many threads can be executed at a time?','Only one thread','Multiple threads','Only main (main() method) thread','Two threads','B'),(10,'If three threads trying to share a single object at the same time, which condition will arise in this scenario?','Time-Lapse','Critical situation','Race condition','Recursion','C'),(11,'Which statement is true about Java?','Java is a sequence-dependent programming language','Java is a code dependent programming language','Java is a platform-dependent programming language','Java is a platform-independent programming language','D'),(12,'Which component is used to compile, debug and execute the java programs?','JRE','JIT','JDK','JVM','C'),(13,'Which of these cannot be used for a variable name in Java?','identifier & keyword','identifier','keyword','None of the above','C'),(14,'What is the extension of java code files?','.js','.txt','.class','.java','D'),(15,'Which of the following is not an OOPS concept in Java?','Compilation','Polymorphism','Inheritance','Encapsulation','A'),(16,'Which of these are selection statements in Java?','break','if()','for()','continue','B'),(17,'Which of the following is a superclass of every class in Java?','Object class','ArrayList','Abstract class','String','A'),(18,'Which of these keywords are used for the block to be examined for exceptions?','check','throw','catch','try','D'),(19,'Which one of the following is not an access modifier?','Protected','Void','Public','Private','B'),(20,'When is the object created with new keyword?','At run time','At compile time','Depends on the code','None','A');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-21 14:49:32
