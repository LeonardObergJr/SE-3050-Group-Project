package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class SampleDataInitializer implements ApplicationRunner {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;

    public SampleDataInitializer(
        JdbcTemplate jdbcTemplate,
        DataSource dataSource,
        ResourceLoader resourceLoader
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(ApplicationArguments args) {
        Integer marketCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM market",
            Integer.class
        );

        if (marketCount == 0) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                resourceLoader.getResource("classpath:data.sql")
            );
            populator.execute(dataSource);
        }
    }
}
