-- Estructura y datos actuales del proyecto
SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS policia_ciudad CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE policia_ciudad;
SET FOREIGN_KEY_CHECKS=0;
CREATE TABLE IF NOT EXISTS `personas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre_completo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `apellido` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dni` varchar(8) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `mail` varchar(254) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telefono` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_personas_dni` (`dni`),
  CONSTRAINT `chk_personas_nombre` CHECK ((char_length(trim(`nombre_completo`)) > 0))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x31 USING utf8mb4), CONVERT(0x566967696c616e746520646520707275656261 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x32 USING utf8mb4), CONVERT(0x41646d696e6973747261646f7220696e696369616c USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x33 USING utf8mb4), CONVERT(0x496e76657374696761646f7220646520707275656261 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x34 USING utf8mb4), CONVERT(0x536567756e646f20766967696c616e746520646520707275656261 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x35 USING utf8mb4), CONVERT(0x566967696c616e7465206465207072756562612061637475616c697a61646f USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x36 USING utf8mb4), CONVERT(0x4a75657a206465207072756562612061637475616c697a61646f USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x37 USING utf8mb4), CONVERT(0x506572736f6e6120646574656e696461206465207072756562612061637475616c697a616461 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x38 USING utf8mb4), CONVERT(0x536567756e646120706572736f6e6120646574656e69646120646520707275656261 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x39 USING utf8mb4), CONVERT(0x506572736f6e6120646574656e696461206465206c612062616e646120313030 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x3132 USING utf8mb4), CONVERT(0x566967696c616e74652063726561646f206465736465206c6120776562 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x3134 USING utf8mb4), CONVERT(0x506572736f6e6120646520707275656261207765622061637475616c697a616461 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x3137 USING utf8mb4), CONVERT(0x7072727565626120656e206d617175696e6120756e69 USING utf8mb4), NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `personas` (`id`, `nombre_completo`, `nombre`, `apellido`, `dni`, `fecha_nacimiento`, `mail`, `telefono`) VALUES (CONVERT(0x3138 USING utf8mb4), CONVERT(0x4c6175726120507275656261 USING utf8mb4), CONVERT(0x4c61757261 USING utf8mb4), CONVERT(0x507275656261 USING utf8mb4), CONVERT(0x3939303030303031 USING utf8mb4), CONVERT(0x323030302d31312d3034 USING utf8mb4), CONVERT(0x6c61757261406578616d706c652e636f6d USING utf8mb4), CONVERT(0x31313030303030303030 USING utf8mb4));
CREATE TABLE IF NOT EXISTS `bandas` (
  `numero` int NOT NULL,
  `cantidad_miembros` int NOT NULL,
  PRIMARY KEY (`numero`),
  CONSTRAINT `chk_bandas_cantidad_miembros` CHECK ((`cantidad_miembros` >= 0)),
  CONSTRAINT `chk_bandas_numero` CHECK ((`numero` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `bandas` (`numero`, `cantidad_miembros`) VALUES (CONVERT(0x313030 USING utf8mb4), CONVERT(0x36 USING utf8mb4));
CREATE TABLE IF NOT EXISTS `entidades_bancarias` (
  `codigo` int NOT NULL,
  `domicilio_central` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`codigo`),
  CONSTRAINT `chk_entidades_codigo` CHECK ((`codigo` > 0)),
  CONSTRAINT `chk_entidades_domicilio` CHECK ((char_length(trim(`domicilio_central`)) > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `entidades_bancarias` (`codigo`, `domicilio_central`) VALUES (CONVERT(0x31303031 USING utf8mb4), CONVERT(0x446f6d6963696c696f2063656e7472616c20646520707275656261 USING utf8mb4));
INSERT INTO `entidades_bancarias` (`codigo`, `domicilio_central`) VALUES (CONVERT(0x31303032 USING utf8mb4), CONVERT(0x4176656e6964612043656e7472616c20343536 USING utf8mb4));
CREATE TABLE IF NOT EXISTS `vigilantes` (
  `id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_vigilantes_personas` FOREIGN KEY (`id`) REFERENCES `personas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `vigilantes` (`id`) VALUES (CONVERT(0x31 USING utf8mb4));
INSERT INTO `vigilantes` (`id`) VALUES (CONVERT(0x34 USING utf8mb4));
INSERT INTO `vigilantes` (`id`) VALUES (CONVERT(0x35 USING utf8mb4));
INSERT INTO `vigilantes` (`id`) VALUES (CONVERT(0x3132 USING utf8mb4));
INSERT INTO `vigilantes` (`id`) VALUES (CONVERT(0x3138 USING utf8mb4));
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
INSERT INTO `jueces` (`id`, `clave_interna`, `anios_servicio`) VALUES (CONVERT(0x36 USING utf8mb4), CONVERT(0x35303031 USING utf8mb4), CONVERT(0x3131 USING utf8mb4));
CREATE TABLE IF NOT EXISTS `personas_detenidas` (
  `id` int NOT NULL,
  `numero_banda` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_detenidas_bandas` (`numero_banda`),
  CONSTRAINT `fk_detenidas_bandas` FOREIGN KEY (`numero_banda`) REFERENCES `bandas` (`numero`),
  CONSTRAINT `fk_detenidas_personas` FOREIGN KEY (`id`) REFERENCES `personas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `personas_detenidas` (`id`, `numero_banda`) VALUES (CONVERT(0x37 USING utf8mb4), NULL);
INSERT INTO `personas_detenidas` (`id`, `numero_banda`) VALUES (CONVERT(0x38 USING utf8mb4), NULL);
INSERT INTO `personas_detenidas` (`id`, `numero_banda`) VALUES (CONVERT(0x3137 USING utf8mb4), NULL);
INSERT INTO `personas_detenidas` (`id`, `numero_banda`) VALUES (CONVERT(0x39 USING utf8mb4), CONVERT(0x313030 USING utf8mb4));
INSERT INTO `personas_detenidas` (`id`, `numero_banda`) VALUES (CONVERT(0x3134 USING utf8mb4), CONVERT(0x313030 USING utf8mb4));
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
INSERT INTO `sucursales` (`codigo`, `codigo_entidad_bancaria`, `domicilio`, `cantidad_empleados`) VALUES (CONVERT(0x32303031 USING utf8mb4), CONVERT(0x31303031 USING utf8mb4), CONVERT(0x446f6d6963696c696f20737563757273616c206465207072756562612031 USING utf8mb4), CONVERT(0x3130 USING utf8mb4));
INSERT INTO `sucursales` (`codigo`, `codigo_entidad_bancaria`, `domicilio`, `cantidad_empleados`) VALUES (CONVERT(0x32303032 USING utf8mb4), CONVERT(0x31303031 USING utf8mb4), CONVERT(0x446f6d6963696c696f20737563757273616c206465207072756562612032 USING utf8mb4), CONVERT(0x3135 USING utf8mb4));
INSERT INTO `sucursales` (`codigo`, `codigo_entidad_bancaria`, `domicilio`, `cantidad_empleados`) VALUES (CONVERT(0x32303033 USING utf8mb4), CONVERT(0x31303032 USING utf8mb4), CONVERT(0x446f6d6963696c696f20737563757273616c20323030332061637475616c697a61646f USING utf8mb4), CONVERT(0x3132 USING utf8mb4));
INSERT INTO `sucursales` (`codigo`, `codigo_entidad_bancaria`, `domicilio`, `cantidad_empleados`) VALUES (CONVERT(0x32303034 USING utf8mb4), CONVERT(0x31303032 USING utf8mb4), CONVERT(0x446f6d6963696c696f20737563757273616c2032303034 USING utf8mb4), CONVERT(0x3135 USING utf8mb4));
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
INSERT INTO `contrataciones` (`id`, `codigo_sucursal`, `id_vigilante`, `fecha`, `con_arma`) VALUES (CONVERT(0x31 USING utf8mb4), CONVERT(0x32303031 USING utf8mb4), CONVERT(0x31 USING utf8mb4), CONVERT(0x323032362d31302d3133 USING utf8mb4), 1);
INSERT INTO `contrataciones` (`id`, `codigo_sucursal`, `id_vigilante`, `fecha`, `con_arma`) VALUES (CONVERT(0x32 USING utf8mb4), CONVERT(0x32303031 USING utf8mb4), CONVERT(0x31 USING utf8mb4), CONVERT(0x323032362d31302d3230 USING utf8mb4), 0);
INSERT INTO `contrataciones` (`id`, `codigo_sucursal`, `id_vigilante`, `fecha`, `con_arma`) VALUES (CONVERT(0x33 USING utf8mb4), CONVERT(0x32303032 USING utf8mb4), CONVERT(0x34 USING utf8mb4), CONVERT(0x323032362d31302d3139 USING utf8mb4), 0);
INSERT INTO `contrataciones` (`id`, `codigo_sucursal`, `id_vigilante`, `fecha`, `con_arma`) VALUES (CONVERT(0x35 USING utf8mb4), CONVERT(0x32303033 USING utf8mb4), CONVERT(0x3132 USING utf8mb4), CONVERT(0x323032362d31302d3238 USING utf8mb4), 1);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO `asaltos` (`id`, `codigo_sucursal`, `id_persona_detenida`, `fecha`) VALUES (CONVERT(0x31 USING utf8mb4), CONVERT(0x32303031 USING utf8mb4), CONVERT(0x37 USING utf8mb4), CONVERT(0x323032362d31302d3138 USING utf8mb4));
INSERT INTO `asaltos` (`id`, `codigo_sucursal`, `id_persona_detenida`, `fecha`) VALUES (CONVERT(0x32 USING utf8mb4), CONVERT(0x32303033 USING utf8mb4), CONVERT(0x3134 USING utf8mb4), CONVERT(0x323032362d31302d3236 USING utf8mb4));
INSERT INTO `asaltos` (`id`, `codigo_sucursal`, `id_persona_detenida`, `fecha`) VALUES (CONVERT(0x34 USING utf8mb4), CONVERT(0x32303031 USING utf8mb4), CONVERT(0x37 USING utf8mb4), CONVERT(0x323032362d31312d3032 USING utf8mb4));
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
INSERT INTO `juicios` (`id`, `id_asalto`, `id_persona_detenida`, `id_juez`, `condenado`, `anios_carcel`) VALUES (CONVERT(0x31 USING utf8mb4), CONVERT(0x31 USING utf8mb4), CONVERT(0x37 USING utf8mb4), CONVERT(0x36 USING utf8mb4), 1, CONVERT(0x35 USING utf8mb4));
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
INSERT INTO `usuarios` (`id`, `id_persona`, `nombre_usuario`, `password_hash`, `rol`) VALUES (CONVERT(0x31 USING utf8mb4), CONVERT(0x32 USING utf8mb4), CONVERT(0x61646d696e707275656261 USING utf8mb4), CONVERT(0x2432612431302474486a4657686d6e72594a513561675179676b46734f6a47774c376669696e334473537848644e686939697a74342e326e412e582e USING utf8mb4), CONVERT(0x41444d494e4953545241444f52 USING utf8mb4));
INSERT INTO `usuarios` (`id`, `id_persona`, `nombre_usuario`, `password_hash`, `rol`) VALUES (CONVERT(0x32 USING utf8mb4), CONVERT(0x33 USING utf8mb4), CONVERT(0x696e76657374696761646f72707275656261 USING utf8mb4), CONVERT(0x243261243130246a4e426f7641463942785a574961414c676a3830304f31437a676d624f6739776d654e693274492e7348494761696f566536357536 USING utf8mb4), CONVERT(0x494e56455354494741444f52 USING utf8mb4));
INSERT INTO `usuarios` (`id`, `id_persona`, `nombre_usuario`, `password_hash`, `rol`) VALUES (CONVERT(0x33 USING utf8mb4), CONVERT(0x31 USING utf8mb4), CONVERT(0x766967696c616e7465707275656261 USING utf8mb4), CONVERT(0x243261243130247535576e5848696a71743768426b797a32314d50492e69794964786b31347a4b486c75434b526a6b776a6f752e68677037664e4e36 USING utf8mb4), CONVERT(0x564947494c414e5445 USING utf8mb4));
SET FOREIGN_KEY_CHECKS=1;
