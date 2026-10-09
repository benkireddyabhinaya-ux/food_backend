-- =========================================================================
-- Food Waste Reduction System - PostgreSQL Schema & Seed Data
-- =========================================================================

-- Optional: Create Database
-- CREATE DATABASE food_waste_db;
-- \c food_waste_db;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    phone VARCHAR(50),
    address VARCHAR(255),
    organization_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Food Donations Table
CREATE TABLE IF NOT EXISTS food_donations (
    id BIGSERIAL PRIMARY KEY,
    food_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    quantity VARCHAR(100) NOT NULL,
    quantity_kg DOUBLE PRECISION DEFAULT 5.0,
    dietary_type VARCHAR(50) DEFAULT 'Vegetarian',
    location VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    pickup_time VARCHAR(100) NOT NULL,
    expiry_time VARCHAR(100),
    urgency VARCHAR(50) DEFAULT 'NORMAL',
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    image_url VARCHAR(1000),
    storage_instructions VARCHAR(255),
    notes VARCHAR(1000),
    donor_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    claimed_by_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Pickups Table
CREATE TABLE IF NOT EXISTS pickups (
    id BIGSERIAL PRIMARY KEY,
    donation_id BIGINT UNIQUE NOT NULL REFERENCES food_donations(id) ON DELETE CASCADE,
    organization_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    pickup_time VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    driver_name VARCHAR(255),
    driver_contact VARCHAR(100),
    vehicle_number VARCHAR(100),
    notes VARCHAR(1000),
    verification_code VARCHAR(10),
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Note: Spring Boot with spring.jpa.hibernate.ddl-auto=update will automatically
-- generate or update these tables. DataInitializer will automatically seed initial demo accounts
-- if the database is empty.
