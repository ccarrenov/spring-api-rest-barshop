 
-- -----------------------------------------------------
-- Schema barshop_bbdd
-- -----------------------------------------------------
USE `barshop_bbdd` ;

-- -----------------------------------------------------
-- Table `country`
-- -----------------------------------------------------
-- DROP TABLE IF EXISTS `country`;
CREATE TABLE IF NOT EXISTS `country` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Country Identifier',
  `name` VARCHAR(250) NOT NULL COMMENT 'Country Name',
  `country_code` SMALLINT NOT NULL COMMENT 'Country Code',
  `two_digit_iso` VARCHAR(2) NOT NULL COMMENT 'Two Digit ISO',
  `three_digit_iso` VARCHAR(3) NOT NULL COMMENT 'Three Digit ISO',
  `country_calling_code` VARCHAR(5) NOT NULL COMMENT 'Contry Calling Code',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `country_code` (`country_code` ASC) ,
  UNIQUE INDEX `two_digit_iso` (`two_digit_iso` ASC) ,
  UNIQUE INDEX `three_digit_iso` (`three_digit_iso` ASC) ,
  UNIQUE INDEX `country_calling_code` (`country_calling_code` ASC) );

-- -----------------------------------------------------
-- Table `persona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `document_number` VARCHAR(10) NOT NULL,
  `names` VARCHAR(45) NOT NULL,
  `first_lastname` VARCHAR(45) NOT NULL,
  `second_lastname` DATE NOT NULL,
  `telefono_fijo` VARCHAR(45) NULL,
  `telefono_movil` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `rut_UNIQUE` (`rut` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre_usuario` VARCHAR(16) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `persona_id` INT NOT NULL,
  UNIQUE INDEX `nombre_usuario_UNIQUE` (`nombre_usuario` ASC) ,
  PRIMARY KEY (`id`),
  INDEX `fk_usuario_persona1_idx` (`persona_id` ASC) ,
  CONSTRAINT `fk_usuario_persona1`
    FOREIGN KEY (`persona_id`)
    REFERENCES `persona` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cliente` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_cliente_usuario1_idx` (`usuario_id` ASC) ,
  CONSTRAINT `fk_cliente_usuario1`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `empleado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `empleado` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `usuario_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_empleado_usuario1_idx` (`usuario_id` ASC) ,
  CONSTRAINT `fk_empleado_usuario1`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rol` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `permiso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `permiso` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `permiso_rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `permiso_rol` (
  `permiso_id` INT NOT NULL,
  `rol_id` INT NOT NULL,
  INDEX `fk_permiso_rol_permiso1_idx` (`permiso_id` ASC) ,
  INDEX `fk_permiso_rol_rol1_idx` (`rol_id` ASC) ,
  CONSTRAINT `fk_permiso_rol_permiso1`
    FOREIGN KEY (`permiso_id`)
    REFERENCES `permiso` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_permiso_rol_rol1`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rol` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usuario_rol`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usuario_rol` (
  `rol_id` INT NOT NULL,
  `usuario_id` INT NOT NULL,
  INDEX `fk_usuario_rol_rol1_idx` (`rol_id` ASC) ,
  INDEX `fk_usuario_rol_usuario1_idx` (`usuario_id` ASC) ,
  CONSTRAINT `fk_usuario_rol_rol1`
    FOREIGN KEY (`rol_id`)
    REFERENCES `rol` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_rol_usuario1`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `usuario` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `boleta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `boleta` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `numero` VARCHAR(45) NOT NULL,
  `fecha` DATE NOT NULL,
  `cliente_id` INT NOT NULL,
  `empleado_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_boleta_cliente1_idx` (`cliente_id` ASC) ,
  INDEX `fk_boleta_empleado1_idx` (`empleado_id` ASC) ,
  CONSTRAINT `fk_boleta_cliente1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_boleta_empleado1`
    FOREIGN KEY (`empleado_id`)
    REFERENCES `empleado` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `servicio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `servicio` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  `precio` INT NOT NULL,
  `activo` BOOLEAN NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `detalle_boleta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `detalle_boleta` (
  `cantidad` INT NOT NULL,
  `boleta_id` INT NOT NULL,
  `servicio_id` INT NOT NULL,
  `precio` DOUBLE NULL,
  INDEX `fk_Det_Boleta_boleta_idx` (`boleta_id` ASC) ,
  INDEX `fk_Det_Boleta_servicio1_idx` (`servicio_id` ASC) ,
  CONSTRAINT `fk_Det_Boleta_boleta`
    FOREIGN KEY (`boleta_id`)
    REFERENCES `boleta` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Det_Boleta_servicio1`
    FOREIGN KEY (`servicio_id`)
    REFERENCES `servicio` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `registro_economico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `registro_economico` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `afp` VARCHAR(45) NULL,
  `finiquito` VARCHAR(45) NULL,
  `liquidacion` VARCHAR(45) NULL,
  `cliente_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_registro_economico_cliente1_idx` (`cliente_id` ASC) ,
  CONSTRAINT `fk_registro_economico_cliente1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rubro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rubro` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `proveedor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rut` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(200) NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `rut_UNIQUE` (`rut` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rubro_proveedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rubro_proveedor` (
  `rubro_id` INT NOT NULL,
  `proveedor_id` INT NOT NULL,
  INDEX `fk_rubro_proveedor_rubro1_idx` (`rubro_id` ASC) ,
  INDEX `fk_rubro_proveedor_proveedor1_idx` (`proveedor_id` ASC) ,
  CONSTRAINT `fk_rubro_proveedor_rubro1`
    FOREIGN KEY (`rubro_id`)
    REFERENCES `rubro` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_rubro_proveedor_proveedor1`
    FOREIGN KEY (`proveedor_id`)
    REFERENCES `proveedor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pedido` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fecha` DATE NULL,
  `empleado_emisor` INT NOT NULL,
  `proveedor_id` INT NOT NULL,
  `empleado_receptor` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pedido_empleado1_idx` (`empleado_emisor` ASC) ,
  INDEX `fk_pedido_proveedor1_idx` (`proveedor_id` ASC) ,
  INDEX `fk_pedido_empleado2_idx` (`empleado_receptor` ASC) ,
  CONSTRAINT `fk_pedido_empleado1`
    FOREIGN KEY (`empleado_emisor`)
    REFERENCES `empleado` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedido_proveedor1`
    FOREIGN KEY (`proveedor_id`)
    REFERENCES `proveedor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedido_empleado2`
    FOREIGN KEY (`empleado_receptor`)
    REFERENCES `empleado` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `producto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  `precio` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `detalle_pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `detalle_pedido` (
  `cantidad` INT NOT NULL,
  `precio` INT NOT NULL,
  `producto_id` INT NOT NULL,
  `pedido_id` INT NOT NULL,
  `recibido` INT NULL,
  `fecha_recepcion` DATE NULL,
  INDEX `fk_detalle_pedido_producto1_idx` (`producto_id` ASC) ,
  INDEX `fk_detalle_pedido_pedido1_idx` (`pedido_id` ASC) ,
  CONSTRAINT `fk_detalle_pedido_producto1`
    FOREIGN KEY (`producto_id`)
    REFERENCES `producto` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_detalle_pedido_pedido1`
    FOREIGN KEY (`pedido_id`)
    REFERENCES `pedido` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `reserva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `reserva` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cliente_id` INT NOT NULL,
  `empleado_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_reserva_cliente1_idx` (`cliente_id` ASC) ,
  INDEX `fk_reserva_empleado1_idx` (`empleado_id` ASC) ,
  CONSTRAINT `fk_reserva_cliente1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `cliente` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reserva_empleado1`
    FOREIGN KEY (`empleado_id`)
    REFERENCES `empleado` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `detalle_reserva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `detalle_reserva` (
  `reserva_id` INT NOT NULL,
  `servicio_id` INT NOT NULL,
  INDEX `fk_detalle_reserva_reserva1_idx` (`reserva_id` ASC) ,
  INDEX `fk_detalle_reserva_servicio1_idx` (`servicio_id` ASC) ,
  CONSTRAINT `fk_detalle_reserva_reserva1`
    FOREIGN KEY (`reserva_id`)
    REFERENCES `reserva` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_detalle_reserva_servicio1`
    FOREIGN KEY (`servicio_id`)
    REFERENCES `servicio` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



