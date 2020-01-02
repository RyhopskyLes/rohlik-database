USE rohlik_database;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `parentId` int(11) NOT NULL,
  `active` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2713 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `child` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `active` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7006 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `category_child` (
  `id_category` int(11) NOT NULL,
  `id_child` int(11) NOT NULL,
  KEY `id_category` (`id_category`),
  KEY `id_child` (`id_child`),
  CONSTRAINT `id_category` FOREIGN KEY (`id_category`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_child` FOREIGN KEY (`id_child`) REFERENCES `child` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `categorykosik` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `equiId` int(11) DEFAULT NULL,
  `equiCategoryName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `parentName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `parentUri` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `equiParentId` int(11) DEFAULT NULL,
  `active` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=303 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `childkosik` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `equiId` int(11) DEFAULT NULL,
  `categoryName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `equiCategoryName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `parentUri` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `active` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=305 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `categorykosik_childkosik` (
  `id_categorykosik` int(11) NOT NULL,
  `id_childkosik` int(11) NOT NULL,
  KEY `id_categorykosik` (`id_categorykosik`),
  KEY `id_childkosik` (`id_childkosik`),
  CONSTRAINT `id_categorykosik` FOREIGN KEY (`id_categorykosik`) REFERENCES `categorykosik` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_childkosik` FOREIGN KEY (`id_childkosik`) REFERENCES `childkosik` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `kosik_rohlik_category` (
  `kosik_id` int(11) NOT NULL,
  `rohlik_id` int(11) NOT NULL,
  KEY `kosik_id` (`kosik_id`),
  KEY `rohlik_id` (`rohlik_id`),
  CONSTRAINT `kosik_id` FOREIGN KEY (`kosik_id`) REFERENCES `categorykosik` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rohlik_id` FOREIGN KEY (`rohlik_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL,
  `productName` varchar(200) CHARACTER SET utf8mb4 NOT NULL,
  `producer` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  `originalPrice` double DEFAULT NULL,
  `price` double NOT NULL,
  `textualAmount` varchar(45) CHARACTER SET utf8mb4 NOT NULL,
  `unit` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  `baseLink` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `imgPath` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `inStock` tinyint(4) DEFAULT NULL,
  `active` tinyint(4) DEFAULT '0',
  `hasSales` tinyint(4) DEFAULT '0',
  `link` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `pricePerUnit` double DEFAULT NULL,
  `mainCategoryId` int(11) DEFAULT NULL,
  `mainCategoryName` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `isFromRohlik` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20515 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `sales` (
  `discountPercentage` double DEFAULT NULL,
  `idSales` int(11) NOT NULL AUTO_INCREMENT,
  `discountPrice` double DEFAULT NULL,
  `id` int(11) DEFAULT NULL,
  `originalPrice` double DEFAULT NULL,
  `price` double DEFAULT NULL,
  `pricePerUnit` double DEFAULT NULL,
  `type` varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL,
  `promoted` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`idSales`)
) ENGINE=InnoDB AUTO_INCREMENT=16155 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;


CREATE TABLE `product_sales` (
  `id_product` int(11) NOT NULL,
  `id_sales` int(11) NOT NULL,
  `product_sales_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`product_sales_id`),
  KEY `id_product_idx` (`id_product`),
  KEY `id_sales_idx` (`id_sales`),
  CONSTRAINT `id_product` FOREIGN KEY (`id_product`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_sales` FOREIGN KEY (`id_sales`) REFERENCES `sales` (`idSales`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16155 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `product_category` (
  `product_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  KEY `product_id` (`product_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_id` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `product_kosik` (
  `id_kosik` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `producer_kosik` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `idProduct_kosik` int(11) DEFAULT NULL,
  `orig_price` double DEFAULT NULL,
  `actual_price` double DEFAULT NULL,
  `category` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `product_Path` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `image_Src` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `amount_product` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
  `unit_Price` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  `in_Stock` tinyint(4) DEFAULT '0',
  `active` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id_kosik`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `kosik_rohlik_product` (
  `kosik` int(11) NOT NULL,
  `rohlik` int(11) NOT NULL,
  KEY `kosik` (`kosik`),
  KEY `rohlik` (`rohlik`),
  CONSTRAINT `kosik` FOREIGN KEY (`kosik`) REFERENCES `product_kosik` (`id_kosik`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rohlik` FOREIGN KEY (`rohlik`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

CREATE TABLE `product_kosik_category_kosik` (
  `product_kosik_id` int(11) NOT NULL,
  `categorykosik_id` int(11) NOT NULL,
  KEY `product_kosik_id` (`product_kosik_id`),
  KEY `categorykosik_id` (`categorykosik_id`),
  CONSTRAINT `categorykosik_id` FOREIGN KEY (`categorykosik_id`) REFERENCES `categorykosik` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product_kosik_id` FOREIGN KEY (`product_kosik_id`) REFERENCES `product_kosik` (`id_kosik`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

