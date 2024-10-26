CREATE TABLE IF NOT EXISTS ProductCard (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price INT NOT NULL,
    img_url VARCHAR(255) DEFAULT NULL,
    main_info TEXT,
    main_characteristics TEXT,
    additional_information TEXT,
    description TEXT
    );
