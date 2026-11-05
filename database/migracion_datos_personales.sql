-- Migración para bases existentes. Completar previamente un respaldo.
USE policia_ciudad;

SET @cambio_personas = IF((SELECT COUNT(*) = 0 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='personas' AND COLUMN_NAME='nombre'), 'ALTER TABLE personas ADD COLUMN nombre VARCHAR(100) NULL', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) = 0 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='personas' AND COLUMN_NAME='apellido'), 'ALTER TABLE personas ADD COLUMN apellido VARCHAR(100) NULL', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) = 0 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='personas' AND COLUMN_NAME='dni'), 'ALTER TABLE personas ADD COLUMN dni VARCHAR(8) NULL', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) = 0 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='personas' AND COLUMN_NAME='fecha_nacimiento'), 'ALTER TABLE personas ADD COLUMN fecha_nacimiento DATE NULL', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) = 0 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='personas' AND COLUMN_NAME='mail'), 'ALTER TABLE personas ADD COLUMN mail VARCHAR(254) NULL', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) = 0 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='personas' AND COLUMN_NAME='telefono'), 'ALTER TABLE personas ADD COLUMN telefono VARCHAR(30) NULL', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) = 0 FROM information_schema.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='personas' AND COLUMN_NAME='dni' AND NON_UNIQUE=0), 'ALTER TABLE personas ADD CONSTRAINT uk_personas_dni UNIQUE (dni)', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) > 0 FROM information_schema.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='vigilantes' AND CONSTRAINT_NAME='chk_vigilantes_edad'), 'ALTER TABLE vigilantes DROP CHECK chk_vigilantes_edad', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

SET @cambio_personas = IF((SELECT COUNT(*) > 0 FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='vigilantes' AND COLUMN_NAME='edad'), 'ALTER TABLE vigilantes DROP COLUMN edad', 'SELECT 1');
PREPARE cambio_personas FROM @cambio_personas;
EXECUTE cambio_personas;
DEALLOCATE PREPARE cambio_personas;

-- Los datos anteriores permanecen como NULL hasta completarlos desde la edición.
