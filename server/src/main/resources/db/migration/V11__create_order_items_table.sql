CREATE TABLE order_items (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,

    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(19,2) NOT NULL CHECK (unit_price >= 0),
    discount NUMERIC(19,2) NOT NULL DEFAULT 0 CHECK (discount >= 0),
    final_price NUMERIC(19,2) NOT NULL CHECK (final_price >= 0),

    product_name VARCHAR(255),
    product_sku VARCHAR(80),
    product_image VARCHAR(500),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    enabled BOOLEAN DEFAULT TRUE,

    CONSTRAINT pk_order_items PRIMARY KEY (order_id, product_id),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);
CREATE INDEX idx_order_items_enabled ON order_items(enabled);

INSERT INTO order_items (
    order_id, product_id,
    quantity, unit_price, discount, final_price,
    product_name, product_sku, product_image
)
VALUES (
    'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
    '11111111-1111-1111-1111-111111111111',
    1,
    100.00,
    0.00,
    100.00,
    'Produto de Exemplo',
    'SKU-0001',
    'https://example.com/default.jpg'
);