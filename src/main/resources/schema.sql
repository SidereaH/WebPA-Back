CREATE TABLE IF NOT EXISTS ProductCard (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,  -- Changed from INT to DOUBLE
    img_url VARCHAR(255) DEFAULT NULL,
    main_info TEXT,
    main_characteristics TEXT,
    additional_information TEXT,
    description TEXT,
    marketplace VARCHAR(50),
    url TEXT
);