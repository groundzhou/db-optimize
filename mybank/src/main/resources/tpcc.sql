-- 创建 Warehouse 表
CREATE TABLE Warehouse
(
    WarehouseID   INT PRIMARY KEY,
    WarehouseName VARCHAR(50),
    Address       VARCHAR(100),
    City          VARCHAR(50),
    State         VARCHAR(50),
    ZipCode       VARCHAR(20)
);

-- 创建 District 表
CREATE TABLE District
(
    DistrictID   INT,
    WarehouseID  INT,
    DistrictName VARCHAR(50),
    Address      VARCHAR(100),
    City         VARCHAR(50),
    State        VARCHAR(50),
    ZipCode      VARCHAR(20),
    PRIMARY KEY (WarehouseID, DistrictID),
    FOREIGN KEY (WarehouseID) REFERENCES Warehouse (WarehouseID)
);

-- 创建 Customer 表
CREATE TABLE Customer
(
    CustomerID   INT PRIMARY KEY,
    DistrictID   INT,
    WarehouseID  INT,
    CustomerName VARCHAR(50),
    Address      VARCHAR(100),
    City         VARCHAR(50),
    State        VARCHAR(50),
    ZipCode      VARCHAR(20),
    Phone        VARCHAR(20),
    Balance      DECIMAL(15, 2),
    CreditLimit  DECIMAL(15, 2),
    Discount     DECIMAL(4, 2),
    PaymentData  VARCHAR(200),
    FOREIGN KEY (DistrictID, WarehouseID) REFERENCES District (DistrictID, WarehouseID)
);

-- 创建 Order 表
CREATE TABLE OOrder
(
    OrderID      INT PRIMARY KEY,
    CustomerID   INT,
    DistrictID   INT,
    WarehouseID  INT,
    OrderDate    DATETIME,
    DeliveryDate DATETIME,
    OrderTotal   DECIMAL(15, 2),
    OrderStatus  VARCHAR(20),
    FOREIGN KEY (CustomerID, DistrictID, WarehouseID) REFERENCES Customer (CustomerID, DistrictID, WarehouseID)
);

-- 创建 OrderLine 表
CREATE TABLE OrderLine
(
    OrderID             INT,
    LineNumber          INT,
    ItemID              INT,
    SupplierWarehouseID INT,
    Quantity            INT,
    Amount              DECIMAL(10, 2),
    PRIMARY KEY (OrderID, LineNumber),
    FOREIGN KEY (OrderID) REFERENCES OOrder (OrderID)
);

-- 创建 Stock 表
CREATE TABLE Stock
(
    ItemID      INT,
    WarehouseID INT,
    Quantity    INT,
    PRIMARY KEY (ItemID, WarehouseID),
    FOREIGN KEY (WarehouseID) REFERENCES Warehouse (WarehouseID)
);

-- 创建 Item 表
CREATE TABLE Item
(
    ItemID     INT PRIMARY KEY,
    ItemName   VARCHAR(100),
    Price      DECIMAL(10, 2),
    SupplierID INT
);

-- 创建 Supplier 表
CREATE TABLE Supplier
(
    SupplierID   INT PRIMARY KEY,
    SupplierName VARCHAR(100),
    Address      VARCHAR(100),
    City         VARCHAR(50),
    State        VARCHAR(50),
    ZipCode      VARCHAR(20),
    Phone        VARCHAR(20)
);


