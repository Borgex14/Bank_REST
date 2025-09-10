-- docker/postgres/init.sql
CREATE DATABASE card_db;

\c card_db;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE card_db TO postgres;