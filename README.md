# Farmers Market Management System

SE 3050 group project by Leonard Oberg and MD Aarish Khan.

The application connects to a relational database and provides a simple interface for browsing farmers markets and products, viewing participating vendors, and placing product pre-orders.

## Features

- Browse markets and open a market to see its details and vendors.
- Search products by product name, vendor, or category across the product catalog.
- Place a pre-order with customer details and a requested quantity.
- Save the order, order-item price, total, and stock change in one database transaction. An order is rejected if the requested stock is unavailable.

## Technology

- Java 17 and Spring Boot
- Spring MVC, Spring Data JPA, and Jakarta Bean Validation
- H2 file database
- HTML, CSS, and vanilla JavaScript

## Run locally

Requirements: Java 17 or newer.

From the project root:

```powershell
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). The database is stored under `data/market_management`. On first startup, the app creates the schema and loads the sample data. Later startups preserve existing records; the sample data is only loaded when the market table is empty.

To run the tests:

```powershell
.\mvnw.cmd test
```

The tests use a separate in-memory database and do not modify the local database file.

## Main API routes

| Method | Route | Purpose |
|---|---|---|
| `GET` | `/api/markets` | List markets |
| `GET` | `/api/markets/{id}` | View a market |
| `GET` | `/api/markets/{id}/vendors` | List vendors at a market |
| `GET` | `/api/products?search={term}` | List or search products |
| `GET` | `/api/products/{id}` | View a product |
| `POST` | `/api/orders` | Place a pre-order |

The H2 console is available at `/h2-console` during local development. Use JDBC URL `jdbc:h2:file:./data/market_management`, username `Aarish_Khan`, and leave the password blank.

## Submission checklist

- [ ] Push the complete project source to the group's GitHub repository.
- [ ] Add the group's narrated 5–7 minute feature-demo video link here before submission:
  _Video link not supplied yet._

The demo should show browsing markets, viewing vendors, searching products, placing a valid pre-order, and confirming that an unavailable quantity is rejected.

