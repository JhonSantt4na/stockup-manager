CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    enabled BOOLEAN DEFAULT TRUE,

    moment TIMESTAMP NOT NULL,
    cancelled_at TIMESTAMP NULL,
    confirmed_at TIMESTAMP NULL,
    paid_at TIMESTAMP NULL,

    order_number VARCHAR(30) NOT NULL UNIQUE,

    order_status VARCHAR(20) NOT NULL CHECK (
        order_status IN (
            'PENDING','CONFIRMED','CANCELLED',
            'PAID','SHIPPED','DELIVERED'
        )
    ),

    subtotal NUMERIC(19,2) DEFAULT 0,
    discount_total NUMERIC(19,2) DEFAULT 0,
    shipping_total NUMERIC(19,2) DEFAULT 0,
    tax_total NUMERIC(19,2) DEFAULT 0,
    total NUMERIC(19,2) DEFAULT 0,
    total_paid NUMERIC(19,2) DEFAULT 0,
    total_refunded NUMERIC(19,2) DEFAULT 0
);

CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_status ON orders(order_status);
CREATE INDEX idx_orders_enabled ON orders(enabled);

INSERT INTO orders (
    id, moment, order_number, order_status,
    subtotal, discount_total, shipping_total, tax_total, total,
    total_paid, total_refunded
)
VALUES (
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    CURRENT_TIMESTAMP,
    'ORD-0001',
    'PENDING',
    100.00, 0.00, 10.00, 5.00, 115.00,
    0.00, 0.00
);
