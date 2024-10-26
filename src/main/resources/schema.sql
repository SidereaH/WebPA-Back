CREATE TABLE ProductCard (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price INT NOT NULL,
    img_url VARCHAR(255) DEFAULT NULL,
    main_info VARCHAR(65535),
    main_characteristics VARCHAR(65535),
    additional_information VARCHAR(65535),
    description varchar(65535)
);