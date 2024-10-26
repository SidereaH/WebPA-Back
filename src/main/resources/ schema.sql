CREATE TABLE IF NOT EXISTS Category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS Seller (
    seller_id INT PRIMARY KEY AUTO_INCREMENT,
    seller_name VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS ProductCard (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    price INT NOT NULL,
    category_id INT,
    isAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    originCountry VARCHAR(50),
    warranty DATE DEFAULT NULL,
    seller_id INT DEFAULT NULL,
    color VARCHAR(20) DEFAULT NULL,
    sku VARCHAR(20) NOT NULL UNIQUE,
    img_url VARCHAR(255) DEFAULT NULL,

    CONSTRAINT fk_category
    FOREIGN KEY (category_id) REFERENCES Category(category_id),
    CONSTRAINT fk_seller
    FOREIGN KEY (seller_id) REFERENCES Seller(seller_id)
    );