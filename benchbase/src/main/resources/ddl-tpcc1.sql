SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS new_order;
DROP TABLE IF EXISTS order_line;
DROP TABLE IF EXISTS oorder;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS district;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS warehouse;

CREATE TABLE warehouse
(
    w_id       int            NOT NULL,
    w_ytd      decimal(12, 2) NOT NULL,
    w_tax      decimal(4, 4)  NOT NULL,
    w_name     varchar(10)    NOT NULL,
    w_street_1 varchar(20)    NOT NULL,
    w_street_2 varchar(20)    NOT NULL,
    w_city     varchar(20)    NOT NULL,
    w_state    char(2)        NOT NULL,
    w_zip      char(9)        NOT NULL,
    PRIMARY KEY (w_id)
);

CREATE TABLE item
(
    i_id    int           NOT NULL,
    i_name  varchar(24)   NOT NULL,
    i_price decimal(5, 2) NOT NULL,
    i_data  varchar(50)   NOT NULL,
    i_im_id int           NOT NULL,
    PRIMARY KEY (i_id)
);

CREATE TABLE stock
(
    s_w_id       int           NOT NULL,
    s_i_id       int           NOT NULL,
    s_quantity   int           NOT NULL,
    s_ytd        decimal(8, 2) NOT NULL,
    s_order_cnt  int           NOT NULL,
    s_remote_cnt int           NOT NULL,
    s_data       varchar(50)   NOT NULL,
    s_dist_01    char(24)      NOT NULL,
    s_dist_02    char(24)      NOT NULL,
    s_dist_03    char(24)      NOT NULL,
    s_dist_04    char(24)      NOT NULL,
    s_dist_05    char(24)      NOT NULL,
    s_dist_06    char(24)      NOT NULL,
    s_dist_07    char(24)      NOT NULL,
    s_dist_08    char(24)      NOT NULL,
    s_dist_09    char(24)      NOT NULL,
    s_dist_10    char(24)      NOT NULL,
    FOREIGN KEY (s_w_id) REFERENCES warehouse (w_id) ON DELETE CASCADE,
    FOREIGN KEY (s_i_id) REFERENCES item (i_id) ON DELETE CASCADE,
    PRIMARY KEY (s_w_id, s_i_id)
);

CREATE TABLE district
(
    d_w_id      int            NOT NULL,
    d_id        int            NOT NULL,
    d_ytd       decimal(12, 2) NOT NULL,
    d_tax       decimal(4, 4)  NOT NULL,
    d_next_o_id int            NOT NULL,
    d_name      varchar(10)    NOT NULL,
    d_street_1  varchar(20)    NOT NULL,
    d_street_2  varchar(20)    NOT NULL,
    d_city      varchar(20)    NOT NULL,
    d_state     char(2)        NOT NULL,
    d_zip       char(9)        NOT NULL,
    FOREIGN KEY (d_w_id) REFERENCES warehouse (w_id) ON DELETE CASCADE,
    PRIMARY KEY (d_w_id, d_id)
);

CREATE TABLE customer
(
    c_w_id         int            NOT NULL,
    c_d_id         int            NOT NULL,
    c_id           int            NOT NULL,
    c_discount     decimal(4, 4)  NOT NULL,
    c_credit       char(2)        NOT NULL,
    c_last         varchar(16)    NOT NULL,
    c_first        varchar(16)    NOT NULL,
    c_credit_lim   decimal(12, 2) NOT NULL,
    c_balance      decimal(12, 2) NOT NULL,
    c_ytd_payment  float          NOT NULL,
    c_payment_cnt  int            NOT NULL,
    c_delivery_cnt int            NOT NULL,
    c_street_1     varchar(20)    NOT NULL,
    c_street_2     varchar(20)    NOT NULL,
    c_city         varchar(20)    NOT NULL,
    c_state        char(2)        NOT NULL,
    c_zip          char(9)        NOT NULL,
    c_phone        char(16)       NOT NULL,
    c_since        timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    c_middle       char(2)        NOT NULL,
    c_data         varchar(500)   NOT NULL,
    FOREIGN KEY (c_w_id, c_d_id) REFERENCES district (d_w_id, d_id) ON DELETE CASCADE,
    PRIMARY KEY (c_w_id, c_d_id, c_id)
);

CREATE TABLE history
(
    h_c_id   int           NOT NULL,
    h_c_d_id int           NOT NULL,
    h_c_w_id int           NOT NULL,
    h_d_id   int           NOT NULL,
    h_w_id   int           NOT NULL,
    h_date   timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    h_amount decimal(6, 2) NOT NULL,
    h_data   varchar(24)   NOT NULL,
    FOREIGN KEY (h_c_w_id, h_c_d_id, h_c_id) REFERENCES customer (c_w_id, c_d_id, c_id) ON DELETE CASCADE,
    FOREIGN KEY (h_w_id, h_d_id) REFERENCES district (d_w_id, d_id) ON DELETE CASCADE
);

CREATE TABLE oorder
(
    o_w_id       int       NOT NULL,
    o_d_id       int       NOT NULL,
    o_id         int       NOT NULL,
    o_c_id       int       NOT NULL,
    o_carrier_id int                DEFAULT NULL,
    o_ol_cnt     int       NOT NULL,
    o_all_local  int       NOT NULL,
    o_entry_d    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (o_w_id, o_d_id, o_id),
    FOREIGN KEY (o_w_id, o_d_id, o_c_id) REFERENCES customer (c_w_id, c_d_id, c_id) ON DELETE CASCADE,
    UNIQUE (o_w_id, o_d_id, o_c_id, o_id)
);

CREATE TABLE new_order
(
    no_w_id int NOT NULL,
    no_d_id int NOT NULL,
    no_o_id int NOT NULL,
    FOREIGN KEY (no_w_id, no_d_id, no_o_id) REFERENCES oorder (o_w_id, o_d_id, o_id) ON DELETE CASCADE,
    PRIMARY KEY (no_w_id, no_d_id, no_o_id)
);

CREATE TABLE order_line
(
    ol_w_id        int           NOT NULL,
    ol_d_id        int           NOT NULL,
    ol_o_id        int           NOT NULL,
    ol_number      int           NOT NULL,
    ol_i_id        int           NOT NULL,
    ol_delivery_d  timestamp     NULL DEFAULT NULL,
    ol_amount      decimal(6, 2) NOT NULL,
    ol_supply_w_id int           NOT NULL,
    ol_quantity    int           NOT NULL,
    ol_dist_info   char(24)      NOT NULL,
    FOREIGN KEY (ol_w_id, ol_d_id, ol_o_id) REFERENCES oorder (o_w_id, o_d_id, o_id) ON DELETE CASCADE,
    FOREIGN KEY (ol_supply_w_id, ol_i_id) REFERENCES stock (s_w_id, s_i_id) ON DELETE CASCADE,
    PRIMARY KEY (ol_w_id, ol_d_id, ol_o_id, ol_number)
);

CREATE INDEX idx_customer_name ON customer (c_w_id, c_d_id, c_last, c_first);


-- 银行账户系统
DROP TABLE IF EXISTS transaction_record;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS log;

CREATE TABLE account
(
    a_w_id        INT            NOT NULL,
    a_d_id        INT            NOT NULL,
    a_c_id        INT            NOT NULL,
    a_id          INT            NOT NULL AUTO_INCREMENT,
    a_type        INT            NOT NULL DEFAULT 1,
    a_number      VARCHAR(20)    NOT NULL,
    a_balance     DECIMAL(12, 2) NOT NULL,
    a_balance_lim DECIMAL(12, 2) NOT NULL,
    a_day_lim     DECIMAL(10, 2) NOT NULL,
    a_created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    a_updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (a_id),
    FOREIGN KEY (a_w_id, a_d_id, a_c_id) REFERENCES customer (c_w_id, c_d_id, c_id) ON DELETE CASCADE
);

CREATE TABLE transaction_record
(
    tr_id          INT            NOT NULL AUTO_INCREMENT,
    tr_a_id        INT            NOT NULL,
    tr_type        VARCHAR(50)    NOT NULL,
    tr_amount      DECIMAL(10, 2) NOT NULL,
    tr_date        DATETIME       NOT NULL,
    tr_description LONGTEXT       NOT NULL,
    tr_created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tr_updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (tr_id)
);

CREATE TABLE log
(
    l_id        INT          NOT NULL AUTO_INCREMENT,
    l_type      INT          NOT NULL DEFAULT 0,
    l_timestamp TIMESTAMP    NULL,
    l_action    VARCHAR(255) NULL,
    PRIMARY KEY (l_id)
);

CREATE INDEX idx_account ON account (a_w_id, a_d_id, a_c_id);
CREATE INDEX idx_tr_aid ON transaction_record(tr_a_id);

SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;