-- Criação dos Enums
CREATE TYPE role_enum AS ENUM ('ADMIN', 'SELLER');
CREATE TYPE product_status_enum AS ENUM ('AVAILABLE', 'SOLD_OUT', 'OUT_OF_STOCK', 'DEPRECATED');
CREATE TYPE movement_type_enum AS ENUM ('IN', 'OUT', 'ADJUST');
CREATE TYPE payment_method_enum AS ENUM ('CASH', 'CARD', 'PIX', 'OTHER');

-- Tabela users
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid_v7(),
    name VARCHAR(100) NOT NULL CHECK (LENGTH(name) >= 3),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role role_enum NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    deleted_at TIMESTAMP
);

-- Tabela products
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid_v7(),
    name VARCHAR(150) NOT NULL CHECK (LENGTH(name) >= 3),
    sku VARCHAR(50) UNIQUE NOT NULL CHECK (LENGTH(sku) > 0),
    status product_status_enum DEFAULT 'AVAILABLE',
    price_sale NUMERIC(12,2) NOT NULL CHECK (price_sale >= 0),
    cost_price NUMERIC(12,2) CHECK (cost_price >= 0 OR cost_price IS NULL),
    stock_quantity INTEGER NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    min_stock INTEGER DEFAULT 0 CHECK (min_stock >= 0 OR min_stock IS NULL),
    category VARCHAR(50),
    ncm VARCHAR(20),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    deleted_at TIMESTAMP
);

-- Tabela inventory_movements
CREATE TABLE inventory_movements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid_v7(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE NOT NULL,
    type movement_type_enum NOT NULL,
    quantity INTEGER NOT NULL,
    reason VARCHAR(255),
    user_id UUID REFERENCES users(id) NOT NULL,
    date TIMESTAMP DEFAULT now(),
    deleted_at TIMESTAMP
);

-- Tabela sales
CREATE TABLE sales (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid_v7(),
    user_id UUID REFERENCES users(id) NOT NULL,
    date TIMESTAMP DEFAULT now(),
    payment_method payment_method_enum NOT NULL,
    total NUMERIC(12,2) NOT NULL CHECK (total >= 0),
    deleted_at TIMESTAMP
);

-- Tabela sale_items
CREATE TABLE sale_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid_v7(),
    sale_id UUID REFERENCES sales(id) ON DELETE CASCADE NOT NULL,
    product_id UUID REFERENCES products(id) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
    subtotal NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0),
    deleted_at TIMESTAMP
);

-- Função para atualização de estoque
CREATE OR REPLACE FUNCTION update_stock() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.type = 'IN' THEN
        UPDATE products
        SET stock_quantity = stock_quantity + NEW.quantity
        WHERE id = NEW.product_id;

    ELSIF NEW.type = 'OUT' THEN
        UPDATE products
        SET stock_quantity = stock_quantity - NEW.quantity
        WHERE id = NEW.product_id;

    ELSIF NEW.type = 'ADJUST' THEN
        UPDATE products
        SET stock_quantity = stock_quantity + NEW.quantity
        WHERE id = NEW.product_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para atualização de estoque
CREATE TRIGGER trg_update_stock
AFTER INSERT ON inventory_movements
FOR EACH ROW
EXECUTE PROCEDURE update_stock();