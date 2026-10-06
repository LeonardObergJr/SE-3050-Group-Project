# SE-3050-Group-Project
Members: Leonard Oberg, MD Aarish Khan

<a href="https://github.com/users/LeonardObergJr/projects/2">Project Management Board</a>
9/27/26
<img width="1079" height="531" alt="image" src="https://github.com/user-attachments/assets/4ad2392a-bd2a-4cb8-9877-33e63c7bc2f9" />


# Farmers Market Management System

A database-driven application for managing local markets, the vendors who sell at them, the products they offer, and the orders customers place.

> **Course:** SE 3050 – Fall 2026
> **Current milestone:** Milestone 2 – Database Modeling & Design

---

## Project Proposal

### The Problem

Local markets are usually run with spreadsheets, paper sign-up sheets, and a lot of back-and-forth messages. It's hard to know which vendors are at which market, who has which booth, what's actually in stock, or what a customer ordered. Information ends up scattered, out of date, or lost.

### What This Project Does

This application brings everything into one organized system. Market organizers can keep track of their markets and vendors, vendors can manage what they sell, and customers can browse products and place orders. All of it sits on a clean relational database so the data stays consistent and easy to query.

### Goals

- Keep one reliable source of truth for markets, vendors, products, and orders.
- Let a vendor take part in more than one market, and let a market host many vendors.
- Make it easy to find products by category, vendor, or price.
- Record orders accurately, including the price paid at the time of purchase.
- Build a design that's simple to understand and easy to extend later.

### Planned Features

**For market organizers**
- Add and update markets (name, address, city, operating days, description).
- Assign vendors to a market with a booth number and start/end dates.
- See which vendors are active at a market at any given time.

**For vendors**
- Create a vendor profile (business name, contact person, email, phone, description).
- Add, edit, and remove products with a price, description, and stock quantity.
- Join one or more markets and keep track of their booth assignments.

**For customers**
- Create an account and browse products across markets.
- Filter products by category or vendor.
- Place orders with multiple items and view order history and status.

**Across the system**
- Products are organized into categories (produce, baked goods, honey, etc.).
- Stock quantity is tracked per product.
- Order totals and line-item prices are stored for accurate records.

---

## Database Design

The full Entity-Relationship Diagram is included in this repository:

![Entity Relationship Diagram](./Milestone_2_ERD.jpg)


### Entities

| Entity | What it stores |
|---|---|
| **MARKET** | Each market's name, address, city, operating days, and description |
| **VENDOR** | Business name, contact name, email, phone, and description |
| **MARKET_VENDOR** | Which vendor is at which market, with booth number and start/end dates |
| **CATEGORY** | Product categories (e.g., produce, dairy, crafts) |
| **PRODUCT** | Name, description, price, and stock quantity; linked to a vendor and a category |
| **CUSTOMER** | First and last name, unique email, and phone |
| **ORDERS** | Order date, status, and total amount; linked to a customer |
| **ORDER_ITEM** | The products in each order, with quantity and unit price |

### Relationships

- **Market → Market_Vendor** (one-to-many, "hosts"): a market can host many vendor entries.
- **Vendor → Market_Vendor** (one-to-many, "joins"): a vendor can join many markets.
- **Vendor → Product** (one-to-many, "sells"): a vendor sells many products, and each product belongs to one vendor.
- **Category → Product** (one-to-many, "classifies"): a category groups many products.
- **Customer → Orders** (one-to-many, "places"): a customer can place many orders.
- **Orders → Order_Item** (one-to-many, "contains"): an order contains one or more items.
- **Product → Order_Item** (one-to-many, "appears in"): a product can appear in many order items.

### Design Decisions

- **Many-to-many relationships are broken up with junction tables.** Markets and vendors are many-to-many, so `MARKET_VENDOR` sits between them. Orders and products are also many-to-many, so `ORDER_ITEM` sits between those. Both use a composite primary key made of their two foreign keys.
- **`unit_price` lives on `ORDER_ITEM`.** Product prices change over time, so each order item saves the price paid when the order was placed. Old orders never change by accident.
- **Booth details belong to `MARKET_VENDOR`.** A booth number and a start/end date only make sense for a specific vendor at a specific market, so they're stored on the relationship and not on either entity.
- **Customer emails are unique.** This prevents duplicate accounts.
- **Categories are their own table.** This avoids typos and inconsistent labels and makes filtering easy.

---

## Tech Stack

> Fill this in with whatever your team is using, for example:

- **Database:** MySQL / PostgreSQL
- **Backend:** Java
- **Frontend:** React

---

## Project Roadmap

- [x] **Milestone 1:** Project selection and setup
- [x] **Milestone 2:** Database modeling and ERD *(this milestone)*
- [ ] **Milestone 3:** Create the database schema (tables, keys, constraints)
- [ ] **Milestone 4:** Load sample data and write queries
- [ ] **Milestone 5:** Build the application on top of the database

---

## Repository Contents

```
├── README.md      # Project proposal and database overview
└── erd.png        # Entity-Relationship Diagram (Milestone 2)
```

---

## Future Ideas

- Payment tracking and receipts
- Vendor ratings and customer reviews
- Seasonal availability for products
- Low-stock alerts for vendors
- Sales reports per market and per vendor

