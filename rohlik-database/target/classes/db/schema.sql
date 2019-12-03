
CREATE TABLE IF NOT EXISTS `product` (
  `id` INT NOT NULL IDENTITY,
  `productId` INT not NULL,
  `productName` VARCHAR(200) NULL,
  `originalPrice` DOUBLE NULL,
  `price` DOUBLE NOT NULL,
  `textualAmount` VARCHAR(45) NULL,
  `unit` VARCHAR(45) NULL,
  `baseLink` VARCHAR(200) NULL,
  `imgPath` VARCHAR(200) NULL,
  `inStock` TINYINT NULL,
  `link` VARCHAR(200) NULL,
  `pricePerUnit` DOUBLE NULL,
  `mainCategoryId` INT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `sales` (
  `discountPercentage` INT NULL,
  `idSales` INT NOT NULL AUTO_INCREMENT,
  `discountPrice` INT NULL,
  `id` INT NULL,
  `originalPrice` DOUBLE NULL,
  `price` DOUBLE NULL,
  `pricePerUnit` DOUBLE NULL,
  `type` VARCHAR(10) NULL,
  `promoted` TINYINT NULL,
  PRIMARY KEY (`idSales`))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `product_sales` (
  `id_product` INT NOT NULL,
  `id_sales` INT NOT NULL,
    CONSTRAINT `id_product`
    FOREIGN KEY (`id_product`)
    REFERENCES `product` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `id_sales`
    FOREIGN KEY (`id_sales`)
    REFERENCES `sales` (`idSales`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

