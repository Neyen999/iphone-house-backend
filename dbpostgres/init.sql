-- Conectarse a la base de datos iphone_house
\connect iphone_house

-- Crear la tabla de roles si no existe
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    is_delete BOOLEAN DEFAULT FALSE,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Insertar roles
INSERT INTO roles (name) VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('USER') ON CONFLICT (name) DO NOTHING;