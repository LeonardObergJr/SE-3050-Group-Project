CREATE DATABASE IF NOT EXISTS farmers_market
    DEFAULT CHARACTER SET utf8mb4;
USE farmers_market;

-- -----------------------------------------------------------------------------
-- Drop existing tables so script can be re-run
-- -----------------------------------------------------------------------------
DROP VIEW  IF EXISTS order_totals;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS market_vendor;
DROP TABLE IF EXISTS vendor;
DROP TABLE IF EXISTS market;

-- -----------------------------------------------------------------------------
-- MARKET: one row per farmers market
-- -----------------------------------------------------------------------------
CREATE TABLE market (
    market_id       INT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(100) NOT NULL,
    address         VARCHAR(150) NOT NULL,
    city            VARCHAR(80)  NOT NULL,
    operating_days  VARCHAR(100) NOT NULL,   -- free-text schedule, e.g. 'Sat 8am-1pm'
    description     TEXT,
    PRIMARY KEY (market_id),
    CONSTRAINT uq_market_name_address UNIQUE (name, address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- VENDOR: one row per vendor business
-- -----------------------------------------------------------------------------
CREATE TABLE vendor (
    vendor_id       INT          NOT NULL AUTO_INCREMENT,
    business_name   VARCHAR(100) NOT NULL,
    contact_name    VARCHAR(100) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    phone           VARCHAR(20),
    description     TEXT,
    PRIMARY KEY (vendor_id),
    CONSTRAINT uq_vendor_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- MARKET_VENDOR: junction table (market <-> vendor, many-to-many)
-- -----------------------------------------------------------------------------
CREATE TABLE market_vendor (
    market_id       INT         NOT NULL,
    vendor_id       INT         NOT NULL,
    booth_number    VARCHAR(10),
    start_date      DATE        NOT NULL,
    end_date        DATE,
    PRIMARY KEY (market_id, vendor_id),
    CONSTRAINT fk_mv_market FOREIGN KEY (market_id)
        REFERENCES market (market_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_mv_vendor FOREIGN KEY (vendor_id)
        REFERENCES vendor (vendor_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_mv_booth UNIQUE (market_id, booth_number),
    CONSTRAINT chk_mv_dates CHECK (end_date IS NULL OR end_date >= start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- CATEGORY: lookup table, category names are stored once
-- -----------------------------------------------------------------------------
CREATE TABLE category (
    category_id     INT          NOT NULL AUTO_INCREMENT,
    name            VARCHAR(50)  NOT NULL,
    PRIMARY KEY (category_id),
    CONSTRAINT uq_category_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- PRODUCT: each product belongs to exactly one vendor and category
-- -----------------------------------------------------------------------------
CREATE TABLE product (
    product_id      INT           NOT NULL AUTO_INCREMENT,
    vendor_id       INT           NOT NULL,
    category_id     INT           NOT NULL,
    name            VARCHAR(100)  NOT NULL,
    description     TEXT,
    price           DECIMAL(8,2)  NOT NULL,
    stock_quantity  INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (product_id),
    CONSTRAINT fk_product_vendor FOREIGN KEY (vendor_id)
        REFERENCES vendor (vendor_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id)
        REFERENCES category (category_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_product_price CHECK (price >= 0),
    CONSTRAINT chk_product_stock CHECK (stock_quantity >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- CUSTOMER: person who places order
-- -----------------------------------------------------------------------------
CREATE TABLE customer (
    customer_id     INT          NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    email           VARCHAR(255) NOT NULL,
    phone           VARCHAR(20),
    PRIMARY KEY (customer_id),
    CONSTRAINT uq_customer_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- ORDERS: one row per order
-- -----------------------------------------------------------------------------
CREATE TABLE orders (
    order_id        INT           NOT NULL AUTO_INCREMENT,
    customer_id     INT           NOT NULL,
    order_date      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20)   NOT NULL DEFAULT 'pending',
    total_amount    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id)
        REFERENCES customer (customer_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_orders_status CHECK
        (status IN ('pending', 'confirmed', 'ready', 'completed', 'cancelled')),
    CONSTRAINT chk_orders_total CHECK (total_amount >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- ORDER_ITEM: junction table (orders <-> product, many-to-many)
-- -----------------------------------------------------------------------------
CREATE TABLE order_item (
    order_id        INT           NOT NULL,
    product_id      INT           NOT NULL,
    quantity        INT           NOT NULL,
    unit_price      DECIMAL(8,2)  NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_oi_order FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_oi_product FOREIGN KEY (product_id)
        REFERENCES product (product_id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_oi_quantity CHECK (quantity > 0),
    CONSTRAINT chk_oi_price CHECK (unit_price >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- Indexes on foreign key columns
-- -----------------------------------------------------------------------------
CREATE INDEX idx_mv_vendor        ON market_vendor (vendor_id);
CREATE INDEX idx_product_vendor   ON product (vendor_id);
CREATE INDEX idx_product_category ON product (category_id);
CREATE INDEX idx_orders_customer  ON orders (customer_id);
CREATE INDEX idx_oi_product       ON order_item (product_id);

-- -----------------------------------------------------------------------------
-- order_totals: computes each order's total from its line items
-- Compare calculated_total and orders.total_amount to find any mismatches
-- -----------------------------------------------------------------------------
CREATE VIEW order_totals AS
SELECT  o.order_id,
        o.total_amount                              AS stored_total,
        COALESCE(SUM(oi.quantity * oi.unit_price), 0) AS calculated_total
FROM    orders o
LEFT JOIN order_item oi ON oi.order_id = o.order_id
GROUP BY o.order_id, o.total_amount;
