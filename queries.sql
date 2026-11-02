Use market_management;

--1.Display all available markets
Select * from market;


--2. Display all registered vendors
Select * from vendors;

-- 3. Display all available products
SELECT * FROM product;

-- 4. Display all product categories
SELECT * FROM category;

-- 5. Display all registered customers
SELECT * FROM customer;

-- 6. Display all customer orders
SELECT * FROM orders;

-- 7. Display vendors working in each market

SELECT 
m.market_id,
m.name AS market_name,
m.city,
v.vendor_id,
v.business_name
from
market m
join market_vendor mv

on m.market_id = mv.market_id

join vendor v

on mv.vendor_id = v.vendor_id
order by m.name,v.business_name;

-- 8. Display products with their vendor and category names
SELECT
    p.product_id,
    p.name AS product_name,
    p.price,
    p.stock_quantity,
    v.business_name AS vendor_name,
    c.name AS category_name
FROM product p
JOIN vendor v
    ON p.vendor_id = v.vendor_id
JOIN category c
    ON p.category_id = c.category_id
ORDER BY p.name;

-- 9. Display all products belonging to the Vegetables category
SELECT
    p.name,
    p.price,
    c.name AS category_name
FROM product p
JOIN category c
    ON p.category_id = c.category_id
WHERE c.name = 'Vegetables'
ORDER BY p.price;

-- 10. Display products with prices between $2 and $5
SELECT
    p.name,
    p.price,
    p.stock_quantity
FROM product p
WHERE p.price BETWEEN 2.00 AND 5.00
ORDER BY p.price;

-- 11. Display products with low stock quantities
SELECT
    p.product_id,
    p.name,
    p.stock_quantity,
    v.business_name
FROM product p
JOIN vendor v
    ON p.vendor_id = v.vendor_id
WHERE p.stock_quantity < 30
ORDER BY p.stock_quantity;

-- 12. Display customer details along with their orders
SELECT
    c.customer_id,
    CONCAT(c.first_name, ' ', c.last_name) AS customer_name,
    c.email,
    o.order_id,
    o.order_date,
    o.status,
    o.total_amount
FROM customer c
JOIN orders o
    ON c.customer_id = o.customer_id
ORDER BY o.order_date DESC;
