Database Normalization Report

1. Introduction

Database normalization is the process of organizing data into tables to reduce data redundancy and prevent insertion, update, and deletion anomalies.

The Market Management System database is designed using normalization principles, including First Normal Form (1NF), Second Normal Form (2NF), and Third Normal Form (3NF).

2. First Normal Form (1NF)

A table satisfies 1NF when every column contains atomic values, each row can be uniquely identified, and there are no repeating groups.

The database follows 1NF because:

- Each table has a primary key.
- Each column stores a single value.
- Multiple vendors associated with a market are stored as separate records in the "market_vendor" table.
- Multiple products in an order are stored as separate records in the "order_item" table.
- Product and customer information are stored in separate tables.

Result: The database satisfies 1NF.

3. Second Normal Form (2NF)

A table satisfies 2NF when it is in 1NF and every non-key attribute depends on the entire primary key rather than only part of a composite key.

The "market_vendor" and "order_item" tables use composite primary keys.

Market_Vendor Table

Primary key: "(market_id, vendor_id)"

The attributes "booth_number", "start_date", and "end_date" depend on the complete combination of market and vendor.

Order_Item Table

Primary key: "(order_id, product_id)"

The attributes "quantity" and "unit_price" depend on the specific product within the specific order.

The remaining tables use single-column primary keys.

Result: The database satisfies 2NF because there are no partial dependencies on composite primary keys.

4. Third Normal Form (3NF)

A table satisfies 3NF when it is in 2NF and non-key attributes do not depend transitively on other non-key attributes.

The database separates information into the following tables:

- Market: Stores market name, address, city, operating days, and description.
- Vendor: Stores vendor business name and contact information.
- Market_Vendor: Stores the association between markets and vendors.
- Category: Stores product category names.
- Product: Stores product name, description, price, stock quantity, and references to vendors and categories.
- Customer: Stores customer names, email addresses, and phone numbers.
- Orders: Stores order information and the associated customer.
- Order_Item: Stores products included in orders, along with quantities and unit prices.

Vendor details are not duplicated in the product table, category details are maintained separately, and customer information is not duplicated in every order.

Result: The schema supports 3NF by separating independent entities and their attributes into appropriate tables.

5. Relationships and Referential Integrity

Foreign keys maintain relationships between related tables:

- "market_vendor.market_id" references "market.market_id".
- "market_vendor.vendor_id" references "vendor.vendor_id".
- "product.vendor_id" references "vendor.vendor_id".
- "product.category_id" references "category.category_id".
- "orders.customer_id" references "customer.customer_id".
- "order_item.order_id" references "orders.order_id".
- "order_item.product_id" references "product.product_id".

These relationships help prevent invalid references and maintain data consistency.

6. Constraints and Data Integrity

The schema uses database constraints to maintain reliable data:

- Primary keys uniquely identify records.
- Foreign keys maintain referential integrity.
- Unique constraints prevent duplicate customer email addresses and category names.
- Check constraints can prevent negative prices, stock quantities, and order totals, while requiring positive order-item quantities.
- DECIMAL data types store monetary values more accurately than floating-point types.

7. Design Considerations

The "orders.total_amount" field can be calculated by summing "quantity * unit_price" for all items in an order.

If this calculated value is stored, the application must keep it synchronized with the order items to avoid inconsistent totals.

Similarly, if the application needs to query individual market operating days separately, "operating_days" can be represented by a separate table instead of a single text field.

8. Conclusion

The Market Management System uses separate tables for markets, vendors, products, categories, customers, orders, and order items.

The database design applies 1NF, 2NF, and 3NF principles to reduce redundant data, minimize anomalies, and maintain referential integrity. These practices make the database easier to maintain and extend as the application grows.
