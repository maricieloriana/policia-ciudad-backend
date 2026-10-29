-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         26.7.0 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.21.0.7344
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para policia_ciudad
CREATE DATABASE IF NOT EXISTS `policia_ciudad` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `policia_ciudad`;

-- Volcando estructura para tabla policia_ciudad.asaltos
CREATE TABLE IF NOT EXISTS `asaltos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo_sucursal` int NOT NULL,
  `id_persona_detenida` int NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asaltos_id_detenida` (`id`,`id_persona_detenida`),
  KEY `fk_asaltos_sucursales` (`codigo_sucursal`),
  KEY `fk_asaltos_detenidas` (`id_persona_detenida`),
  CONSTRAINT `fk_asaltos_detenidas` FOREIGN KEY (`id_persona_detenida`) REFERENCES `personas_detenidas` (`id`),
  CONSTRAINT `fk_asaltos_sucursales` FOREIGN KEY (`codigo_sucursal`) REFERENCES `sucursales` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.asaltos: ~2 rows (aproximadamente)
INSERT INTO `asaltos` (`id`, `codigo_sucursal`, `id_persona_detenida`, `fecha`) VALUES
	(1, 2001, 7, '2026-10-18'),
	(2, 2003, 14, '2026-10-26');

-- Volcando estructura para tabla policia_ciudad.bandas
CREATE TABLE IF NOT EXISTS `bandas` (
  `numero` int NOT NULL,
  `cantidad_miembros` int NOT NULL,
  PRIMARY KEY (`numero`),
  CONSTRAINT `chk_bandas_cantidad_miembros` CHECK ((`cantidad_miembros` >= 0)),
  CONSTRAINT `chk_bandas_numero` CHECK ((`numero` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.bandas: ~1 rows (aproximadamente)
INSERT INTO `bandas` (`numero`, `cantidad_miembros`) VALUES
	(100, 6);

-- Volcando estructura para tabla policia_ciudad.contrataciones
CREATE TABLE IF NOT EXISTS `contrataciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo_sucursal` int NOT NULL,
  `id_vigilante` int NOT NULL,
  `fecha` date NOT NULL,
  `con_arma` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contratacion_sucursal_vigilante_fecha` (`codigo_sucursal`,`id_vigilante`,`fecha`),
  KEY `fk_contrataciones_vigilantes` (`id_vigilante`),
  CONSTRAINT `fk_contrataciones_sucursales` FOREIGN KEY (`codigo_sucursal`) REFERENCES `sucursales` (`codigo`),
  CONSTRAINT `fk_contrataciones_vigilantes` FOREIGN KEY (`id_vigilante`) REFERENCES `vigilantes` (`id`),
  CONSTRAINT `chk_contrataciones_con_arma` CHECK ((`con_arma` in (0,1)))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.contrataciones: ~4 rows (aproximadamente)
INSERT INTO `contrataciones` (`id`, `codigo_sucursal`, `id_vigilante`, `fecha`, `con_arma`) VALUES
	(1, 2001, 1, '2026-10-13', 1),
	(2, 2001, 1, '2026-10-20', 0),
	(3, 2002, 4, '2026-10-19', 0),
	(5, 2003, 12, '2026-10-28', 1);

-- Volcando estructura para tabla policia_ciudad.entidades_bancarias
CREATE TABLE IF NOT EXISTS `entidades_bancarias` (
  `codigo` int NOT NULL,
  `domicilio_central` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`codigo`),
  CONSTRAINT `chk_entidades_codigo` CHECK ((`codigo` > 0)),
  CONSTRAINT `chk_entidades_domicilio` CHECK ((char_length(trim(`domicilio_central`)) > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.entidades_bancarias: ~2 rows (aproximadamente)
INSERT INTO `entidades_bancarias` (`codigo`, `domicilio_central`) VALUES
	(1001, 'Domicilio central de prueba'),
	(1002, 'Avenida Central 456');

-- Volcando estructura para tabla policia_ciudad.jueces
CREATE TABLE IF NOT EXISTS `jueces` (
  `id` int NOT NULL,
  `clave_interna` int NOT NULL,
  `anios_servicio` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_jueces_clave_interna` (`clave_interna`),
  CONSTRAINT `fk_jueces_personas` FOREIGN KEY (`id`) REFERENCES `personas` (`id`),
  CONSTRAINT `chk_jueces_anios_servicio` CHECK ((`anios_servicio` >= 0)),
  CONSTRAINT `chk_jueces_clave_interna` CHECK ((`clave_interna` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.jueces: ~1 rows (aproximadamente)
INSERT INTO `jueces` (`id`, `clave_interna`, `anios_servicio`) VALUES
	(6, 5001, 11);

-- Volcando estructura para tabla policia_ciudad.juicios
CREATE TABLE IF NOT EXISTS `juicios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_asalto` int NOT NULL,
  `id_persona_detenida` int NOT NULL,
  `id_juez` int NOT NULL,
  `condenado` tinyint(1) NOT NULL,
  `anios_carcel` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_juicios_asalto` (`id_asalto`),
  KEY `fk_juicios_asalto_detenida` (`id_asalto`,`id_persona_detenida`),
  KEY `fk_juicios_jueces` (`id_juez`),
  CONSTRAINT `fk_juicios_asalto_detenida` FOREIGN KEY (`id_asalto`, `id_persona_detenida`) REFERENCES `asaltos` (`id`, `id_persona_detenida`),
  CONSTRAINT `fk_juicios_jueces` FOREIGN KEY (`id_juez`) REFERENCES `jueces` (`id`),
  CONSTRAINT `chk_juicios_resolucion` CHECK ((((`condenado` = 0) and (`anios_carcel` = 0)) or ((`condenado` = 1) and (`anios_carcel` > 0))))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.juicios: ~1 rows (aproximadamente)
INSERT INTO `juicios` (`id`, `id_asalto`, `id_persona_detenida`, `id_juez`, `condenado`, `anios_carcel`) VALUES
	(1, 1, 7, 6, 1, 5);

-- Volcando estructura para tabla policia_ciudad.personas
CREATE TABLE IF NOT EXISTS `personas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_completo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `chk_personas_nombre` CHECK ((char_length(trim(`nombre_completo`)) > 0))
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.personas: ~12 rows (aproximadamente)
INSERT INTO `personas` (`id`, `nombre_completo`) VALUES
	(1, 'Vigilante de prueba'),
	(2, 'Administrador inicial'),
	(3, 'Investigador de prueba'),
	(4, 'Segundo vigilante de prueba'),
	(5, 'Vigilante de prueba actualizado'),
	(6, 'Juez de prueba actualizado'),
	(7, 'Persona detenida de prueba actualizada'),
	(8, 'Segunda persona detenida de prueba'),
	(9, 'Persona detenida de la banda 100'),
	(12, 'Vigilante creado desde la web'),
	(14, 'Persona de prueba web actualizada'),
	(17, 'prrueba en maquina uni');

-- Volcando estructura para tabla policia_ciudad.personas_detenidas
CREATE TABLE IF NOT EXISTS `personas_detenidas` (
  `id` int NOT NULL,
  `numero_banda` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_detenidas_bandas` (`numero_banda`),
  CONSTRAINT `fk_detenidas_bandas` FOREIGN KEY (`numero_banda`) REFERENCES `bandas` (`numero`),
  CONSTRAINT `fk_detenidas_personas` FOREIGN KEY (`id`) REFERENCES `personas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.personas_detenidas: ~5 rows (aproximadamente)
INSERT INTO `personas_detenidas` (`id`, `numero_banda`) VALUES
	(7, NULL),
	(8, NULL),
	(9, 100),
	(14, 100),
	(17, NULL);

-- Volcando estructura para tabla policia_ciudad.sucursales
CREATE TABLE IF NOT EXISTS `sucursales` (
  `codigo` int NOT NULL,
  `codigo_entidad_bancaria` int NOT NULL,
  `domicilio` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cantidad_empleados` int NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `fk_sucursales_entidades` (`codigo_entidad_bancaria`),
  CONSTRAINT `fk_sucursales_entidades` FOREIGN KEY (`codigo_entidad_bancaria`) REFERENCES `entidades_bancarias` (`codigo`),
  CONSTRAINT `chk_sucursales_codigo` CHECK ((`codigo` > 0)),
  CONSTRAINT `chk_sucursales_domicilio` CHECK ((char_length(trim(`domicilio`)) > 0)),
  CONSTRAINT `chk_sucursales_empleados` CHECK ((`cantidad_empleados` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.sucursales: ~4 rows (aproximadamente)
INSERT INTO `sucursales` (`codigo`, `codigo_entidad_bancaria`, `domicilio`, `cantidad_empleados`) VALUES
	(2001, 1001, 'Domicilio sucursal de prueba 1', 10),
	(2002, 1001, 'Domicilio sucursal de prueba 2', 15),
	(2003, 1002, 'Domicilio sucursal 2003 actualizado', 12),
	(2004, 1002, 'Domicilio sucursal 2004', 15);

-- Volcando estructura para tabla policia_ciudad.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_persona` int NOT NULL,
  `nombre_usuario` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` enum('ADMINISTRADOR','INVESTIGADOR','VIGILANTE') COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuarios_persona` (`id_persona`),
  UNIQUE KEY `uk_usuarios_nombre` (`nombre_usuario`),
  CONSTRAINT `fk_usuarios_personas` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id`),
  CONSTRAINT `chk_usuarios_nombre` CHECK ((char_length(trim(`nombre_usuario`)) > 0)),
  CONSTRAINT `chk_usuarios_password_hash` CHECK ((char_length(trim(`password_hash`)) > 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.usuarios: ~4 rows (aproximadamente)
INSERT INTO `usuarios` (`id`, `id_persona`, `nombre_usuario`, `password_hash`, `rol`) VALUES
	(1, 2, 'adminprueba', '$2a$10$tHjFWhmnrYJQ5agQygkFsOjGwL7fiin3DsSxHdNhi9izt4.2nA.X.', 'ADMINISTRADOR'),
	(2, 3, 'investigadorprueba', '$2a$10$jNBovAF9BxZWIaALgj800O1CzgmbOg9wmeNi2tI.sHIGaioVe65u6', 'INVESTIGADOR'),
	(3, 1, 'vigilanteprueba', '$2a$10$u5WnXHijqt7hBkyz21MPI.iyIdxk14zKHluCKRjkwjou.hgp7fNN6', 'VIGILANTE');

-- Volcando estructura para tabla policia_ciudad.vigilantes
CREATE TABLE IF NOT EXISTS `vigilantes` (
  `id` int NOT NULL,
  `edad` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_vigilantes_personas` FOREIGN KEY (`id`) REFERENCES `personas` (`id`),
  CONSTRAINT `chk_vigilantes_edad` CHECK ((`edad` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla policia_ciudad.vigilantes: ~4 rows (aproximadamente)
INSERT INTO `vigilantes` (`id`, `edad`) VALUES
	(1, 30),
	(4, 35),
	(5, 29),
	(12, 28);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
