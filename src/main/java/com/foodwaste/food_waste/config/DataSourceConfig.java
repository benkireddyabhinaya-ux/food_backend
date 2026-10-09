package com.foodwaste.food_waste.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:#{null}}")
    private String defaultUrl;

    @Value("${spring.datasource.username:#{null}}")
    private String defaultUsername;

    @Value("${spring.datasource.password:#{null}}")
    private String defaultPassword;

    @Value("${spring.datasource.driver-class-name:#{null}}")
    private String defaultDriver;

    @Bean
    @Primary
    public DataSource dataSource() {
        String envDbUrl = System.getenv("DATABASE_URL");
        if (envDbUrl == null || envDbUrl.isBlank()) {
            envDbUrl = System.getenv("SPRING_DATASOURCE_URL");
        }

        // 1. If cloud / Render / Supabase DATABASE_URL is provided, use PostgreSQL
        if (envDbUrl != null && !envDbUrl.isBlank()) {
            log.info("Detected database connection URL from environment variable");
            return createDataSourceFromUrl(envDbUrl);
        }

        // 2. If configured for local postgres, check if localhost:5432 is running
        if (defaultUrl != null && defaultUrl.contains("localhost:5432")) {
            boolean postgresRunning = isPortOpen("localhost", 5432, 600);
            if (!postgresRunning) {
                log.warn("PostgreSQL server at localhost:5432 is not running. Switching automatically to embedded H2 (PostgreSQL-compatible) database for seamless local development.");
                return createH2FallbackDataSource();
            }
        }

        // 3. If explicit external URL configured, use it
        if (defaultUrl != null && !defaultUrl.isBlank()) {
            try {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(defaultUrl);
                config.setUsername(defaultUsername != null ? defaultUsername : "postgres");
                config.setPassword(defaultPassword != null ? defaultPassword : "");
                if (defaultDriver != null && !defaultDriver.isBlank()) {
                    config.setDriverClassName(defaultDriver);
                }
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(2);
                config.setConnectionTimeout(5000);
                return new HikariDataSource(config);
            } catch (Exception e) {
                log.warn("Failed connecting to configured database, falling back to embedded H2: {}", e.getMessage());
                return createH2FallbackDataSource();
            }
        }

        // 4. Default to H2 embedded
        return createH2FallbackDataSource();
    }

    private DataSource createH2FallbackDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:file:./data/foodwaste;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        return new HikariDataSource(config);
    }

    private boolean isPortOpen(String host, int port, int timeoutMs) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private DataSource createDataSourceFromUrl(String rawUrl) {
        HikariConfig config = new HikariConfig();

        if (rawUrl.startsWith("jdbc:")) {
            config.setJdbcUrl(rawUrl);
            if (defaultUsername != null) config.setUsername(defaultUsername);
            if (defaultPassword != null) config.setPassword(defaultPassword);
        } else {
            try {
                // Parse postgres://user:password@host:port/database
                URI uri = new URI(rawUrl);
                String userInfo = uri.getUserInfo();
                String username = "";
                String password = "";

                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                    password = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                } else if (userInfo != null) {
                    username = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
                }

                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                String dbName = (path != null && path.length() > 1) ? path.substring(1) : "food_waste_db";

                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
                if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                    jdbcUrl += "?" + uri.getQuery();
                } else {
                    jdbcUrl += "?sslmode=require";
                }

                log.info("Configured PostgreSQL JDBC connection to host: {}, db: {}", host, dbName);
                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("org.postgresql.Driver");
            } catch (Exception e) {
                log.error("Failed to parse DATABASE_URL, attempting as direct JDBC url: {}", e.getMessage());
                config.setJdbcUrl(rawUrl);
            }
        }

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }
}
