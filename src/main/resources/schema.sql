CREATE TABLE IF NOT EXISTS PRODUCT_CARD (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    img_url VARCHAR(255),
    main_info CLOB,
    main_characteristics CLOB,
    additional_information CLOB,
    description CLOB,
    marketplace VARCHAR(50),
    url CLOB,
    excel_file BLOB,
    excel_filename VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);