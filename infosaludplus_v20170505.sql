-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: localhost    Database: infosaludplus
-- ------------------------------------------------------
-- Server version	5.7.16-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `infosaludplus`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `infosaludplus` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish_ci */;

USE `infosaludplus`;

--
-- Table structure for table `centros`
--

DROP TABLE IF EXISTS `centros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `centros` (
  `idCentro` int(11) NOT NULL AUTO_INCREMENT,
  `imagen` longblob,
  `nombre` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idCentro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `centros`
--

LOCK TABLES `centros` WRITE;
/*!40000 ALTER TABLE `centros` DISABLE KEYS */;
/*!40000 ALTER TABLE `centros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `citas` (
  `idCita` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `tipo` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `idCentro` int(11) DEFAULT NULL,
  `idMedico` int(11) DEFAULT NULL,
  `idPaciente` int(11) DEFAULT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`idCita`),
  KEY `FK6cr2x1m2auvwjgvi0usodvw23` (`idCentro`),
  KEY `FK42qguedunchpc1ycmuehssuwv` (`idMedico`),
  KEY `FKdk4e5qfjrf46yfk2po6yjp4ji` (`idPaciente`),
  KEY `FK2r8us55vcipr4ectjtpfm4j24` (`idUsuario`),
  CONSTRAINT `FK2r8us55vcipr4ectjtpfm4j24` FOREIGN KEY (`idUsuario`) REFERENCES `pacientes` (`idUsuario`),
  CONSTRAINT `FK42qguedunchpc1ycmuehssuwv` FOREIGN KEY (`idMedico`) REFERENCES `medicos` (`idUsuario`),
  CONSTRAINT `FK6cr2x1m2auvwjgvi0usodvw23` FOREIGN KEY (`idCentro`) REFERENCES `centros` (`idCentro`),
  CONSTRAINT `FKdk4e5qfjrf46yfk2po6yjp4ji` FOREIGN KEY (`idPaciente`) REFERENCES `pacientes` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citas`
--

LOCK TABLES `citas` WRITE;
/*!40000 ALTER TABLE `citas` DISABLE KEYS */;
/*!40000 ALTER TABLE `citas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `direcciones`
--

DROP TABLE IF EXISTS `direcciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `direcciones` (
  `idDireccion` int(11) NOT NULL AUTO_INCREMENT,
  `direccion` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `movil` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `telefono` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `idCentro` int(11) DEFAULT NULL,
  `idPueblo` int(11) DEFAULT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`idDireccion`),
  KEY `FKiua2p1wtmqcx0ptlsbxe8v8n1` (`idCentro`),
  KEY `FKe9klcqvgwx5tkdw6r5f0bj90s` (`idPueblo`),
  KEY `FK76qj4qt0pqhdpeqxi0c01wy4t` (`idUsuario`),
  CONSTRAINT `FK76qj4qt0pqhdpeqxi0c01wy4t` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`),
  CONSTRAINT `FKe9klcqvgwx5tkdw6r5f0bj90s` FOREIGN KEY (`idPueblo`) REFERENCES `pueblos` (`idPueblo`),
  CONSTRAINT `FKiua2p1wtmqcx0ptlsbxe8v8n1` FOREIGN KEY (`idCentro`) REFERENCES `centros` (`idCentro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `direcciones`
--

LOCK TABLES `direcciones` WRITE;
/*!40000 ALTER TABLE `direcciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `direcciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `informes`
--

DROP TABLE IF EXISTS `informes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `informes` (
  `idInforme` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL,
  `observacion` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `titulo` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `tratamiento` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `idMedico` int(11) DEFAULT NULL,
  `idPaciente` int(11) DEFAULT NULL,
  PRIMARY KEY (`idInforme`),
  KEY `FKd61dr8r5dai8tk6kp92in2cd8` (`idMedico`),
  KEY `FKgr18btd4q45xfwjat8knqkvut` (`idPaciente`),
  CONSTRAINT `FKd61dr8r5dai8tk6kp92in2cd8` FOREIGN KEY (`idMedico`) REFERENCES `medicos` (`idUsuario`),
  CONSTRAINT `FKgr18btd4q45xfwjat8knqkvut` FOREIGN KEY (`idPaciente`) REFERENCES `pacientes` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `informes`
--

LOCK TABLES `informes` WRITE;
/*!40000 ALTER TABLE `informes` DISABLE KEYS */;
/*!40000 ALTER TABLE `informes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicos`
--

DROP TABLE IF EXISTS `medicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `medicos` (
  `jefe` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `numColegiado` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `titulos` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `idServicio` int(11) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  KEY `FK30tqqntmhn6fr5thchjw5cxon` (`idServicio`),
  CONSTRAINT `FK30tqqntmhn6fr5thchjw5cxon` FOREIGN KEY (`idServicio`) REFERENCES `servicios` (`idServicio`),
  CONSTRAINT `FKslydjqco97ckwllk2et1khrcb` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicos`
--

LOCK TABLES `medicos` WRITE;
/*!40000 ALTER TABLE `medicos` DISABLE KEYS */;
/*!40000 ALTER TABLE `medicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pacientes` (
  `numSegSoc` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `idUsuario` int(11) NOT NULL,
  PRIMARY KEY (`idUsuario`),
  CONSTRAINT `FKtml88ekccr0fkmfvoonuxo7e` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pacientes`
--

LOCK TABLES `pacientes` WRITE;
/*!40000 ALTER TABLE `pacientes` DISABLE KEYS */;
INSERT INTO `pacientes` VALUES ('1235',2);
/*!40000 ALTER TABLE `pacientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provincias`
--

DROP TABLE IF EXISTS `provincias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provincias` (
  `idProvincia` int(11) NOT NULL,
  `nombre` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idProvincia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provincias`
--

LOCK TABLES `provincias` WRITE;
/*!40000 ALTER TABLE `provincias` DISABLE KEYS */;
/*!40000 ALTER TABLE `provincias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pueblos`
--

DROP TABLE IF EXISTS `pueblos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pueblos` (
  `idPueblo` int(11) NOT NULL AUTO_INCREMENT,
  `codigoPostal` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `idProvincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`idPueblo`),
  KEY `FK8gcrngv3n4vk5xxi4jeja2seg` (`idProvincia`),
  CONSTRAINT `FK8gcrngv3n4vk5xxi4jeja2seg` FOREIGN KEY (`idProvincia`) REFERENCES `provincias` (`idProvincia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pueblos`
--

LOCK TABLES `pueblos` WRITE;
/*!40000 ALTER TABLE `pueblos` DISABLE KEYS */;
/*!40000 ALTER TABLE `pueblos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicios`
--

DROP TABLE IF EXISTS `servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servicios` (
  `idServicio` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  PRIMARY KEY (`idServicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `bloqueado` set('s','n') COLLATE utf8_spanish_ci NOT NULL DEFAULT 'n',
  `clave` text COLLATE utf8_spanish_ci NOT NULL,
  `email` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `fechaAlta` datetime DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `imagen` longblob,
  `nif` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `nombre` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `tipo` set('p','m','a','e','o') COLLATE utf8_spanish_ci NOT NULL DEFAULT 'p',
  `idCentro` int(11) DEFAULT NULL,
  `idDireccion` int(11) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `UK_7siao1h1arues629dbovxn1xb` (`email`),
  KEY `FK3d9j1m0cp6a2sha22hol6ntei` (`idCentro`),
  KEY `FKscluv5dnq3j4yggcrc42k7vsr` (`idDireccion`),
  CONSTRAINT `FK3d9j1m0cp6a2sha22hol6ntei` FOREIGN KEY (`idCentro`) REFERENCES `centros` (`idCentro`),
  CONSTRAINT `FKscluv5dnq3j4yggcrc42k7vsr` FOREIGN KEY (`idDireccion`) REFERENCES `direcciones` (`idDireccion`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (2,'Diaz','n','gdyb21LQTcIANtvYMT7QVQ==','ricardo@gmail.com','2017-05-04 23:37:14','2017-05-09',NULL,'12345667A','Ricardo','p',NULL,NULL);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-05 21:43:28
