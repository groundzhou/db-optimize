DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(20)  NOT NULL,
    address    VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS account;
CREATE TABLE account
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    user_id        INT            NOT NULL,
    account_number VARCHAR(20)    NOT NULL,
    balance        DECIMAL(10, 2) NOT NULL,
    created_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS transaction_record;
CREATE TABLE transaction_record
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    account_id       INT            NOT NULL,
    transaction_type VARCHAR(50)    NOT NULL,
    amount           DECIMAL(10, 2) NOT NULL,
    currency         VARCHAR(10)    NOT NULL DEFAULT 'CNY',
    transaction_date DATETIME       NOT NULL,
    created_at       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE products
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100),
    description  TEXT,
    price        DECIMAL(10, 2),
    weight       DECIMAL(5, 2),
    dimensions   VARCHAR(50),
    color        VARCHAR(30),
    material     VARCHAR(30),
    manufacturer VARCHAR(50),
    sku          VARCHAR(20) UNIQUE,
    stock        INT,
    created_at   DATETIME,
    updated_at   DATETIME
);

CREATE TABLE orders
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE order_item
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    order_id   INT,
    product_id INT,
    quantity   INT,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

DROP TABLE IF EXISTS log;
CREATE TABLE log
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id   INT,
    action    VARCHAR(100),
    timestamp DATETIME,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE data_types
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    small_int    SMALLINT,
    big_int      BIGINT,
    decimal_val  DECIMAL(10, 5),
    single_char  CHAR,
    long_text    LONGTEXT,
    binary_data  VARBINARY(100),
    date_val     DATE,
    time_val     TIME,
    datetime_val DATETIME
);