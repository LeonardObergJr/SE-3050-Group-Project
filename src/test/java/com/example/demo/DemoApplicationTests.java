package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.datasource.url=jdbc:h2:mem:market_management_test;DB_CLOSE_DELAY=-1"
)
class DemoApplicationTests {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment environment;

    @Test
    void listsMarkets() throws Exception {
        HttpResponse<String> response = get("/api/markets");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"marketId\":1"));
    }

    @Test
    void showsVendorsForMarket() throws Exception {
        HttpResponse<String> response = get("/api/markets/1/vendors");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"businessName\""));
    }

    @Test
    void searchesProductsAcrossMarkets() throws Exception {
        HttpResponse<String> response = get("/api/products?search=tomatoes");

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"name\":\"Tomatoes\""));
    }

    @Test
    void placingPreOrderSavesOrderAndDecreasesStock() throws Exception {
        int stockBefore = jdbcTemplate.queryForObject(
            "SELECT stock_quantity FROM product WHERE product_id = 1",
            Integer.class
        );
        HttpRequest request = HttpRequest.newBuilder(uri("/api/orders"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""
                {
                  "customer": {
                    "firstName": "Test",
                    "lastName": "Customer",
                    "email": "submission-test@example.com",
                    "phone": ""
                  },
                  "items": [
                    {
                      "productId": 1,
                      "quantity": 2
                    }
                  ]
                }
                """))
            .build();
        HttpResponse<String> response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("\"totalAmount\":4.98"));

        int stockAfter = jdbcTemplate.queryForObject(
            "SELECT stock_quantity FROM product WHERE product_id = 1",
            Integer.class
        );
        assertEquals(stockBefore - 2, stockAfter);
    }

    @Test
    void rejectedPreOrderDoesNotChangeStockOrSaveCustomer() throws Exception {
        int stockBefore = jdbcTemplate.queryForObject(
            "SELECT stock_quantity FROM product WHERE product_id = 1",
            Integer.class
        );
        HttpRequest request = HttpRequest.newBuilder(uri("/api/orders"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""
                {
                  "customer": {
                    "firstName": "Rollback",
                    "lastName": "Customer",
                    "email": "rollback-test@example.com",
                    "phone": ""
                  },
                  "items": [
                    {
                      "productId": 1,
                      "quantity": 9999
                    }
                  ]
                }
                """))
            .build();
        HttpResponse<String> response = httpClient.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(400, response.statusCode());
        assertEquals(
            stockBefore,
            jdbcTemplate.queryForObject(
                "SELECT stock_quantity FROM product WHERE product_id = 1",
                Integer.class
            )
        );
        assertEquals(
            0,
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM customer WHERE email = 'rollback-test@example.com'",
                Integer.class
            )
        );
    }

    @Test
    void contextLoads() {
    }

    private HttpResponse<String> get(String path) throws Exception {
        return httpClient.send(
            HttpRequest.newBuilder(uri(path)).GET().build(),
            HttpResponse.BodyHandlers.ofString()
        );
    }

    private URI uri(String path) {
        int port = environment.getProperty("local.server.port", Integer.class);
        return URI.create("http://localhost:" + port + path);
    }
}
