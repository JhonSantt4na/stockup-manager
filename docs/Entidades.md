
# 📘 Modelo de Dados

## 📂 Entidades

---

### 🧑‍💼 `users`

| Campo         | Tipo e Regras | Descrição |
|---------------|---------------|-----------|
| `id` | `UUID (PK, DEFAULT gen_random_uuid_v7())` | Identificador único do usuário. |
| `name` | `VARCHAR(100) NOT NULL` (mínimo 3 caracteres) | Nome do usuário. |
| `email` | `VARCHAR(255) UNIQUE NOT NULL` | E-mail único e obrigatório. |
| `password_hash` | `VARCHAR(255) NOT NULL` | Hash da senha do usuário. |
| `role` | `ENUM('ADMIN', 'SELLER') NOT NULL` | Papel do usuário no sistema. |
| `created_at` | `TIMESTAMP DEFAULT now()` | Data de criação. |
| `updated_at` | `TIMESTAMP DEFAULT now()` | Data da última atualização. |
| `deleted_at` | `TIMESTAMP NULL` | Exclusão lógica (nulo se ativo). |

#### ✅ Validações no Backend

- `name`: mínimo 3 caracteres
- `email`: formato válido (regex)
- `password`: mínimo 8 caracteres antes do hash
- `role`: valor enum válido

```sql
CREATE TYPE role_enum AS ENUM ('ADMIN', 'SELLER');

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
```

#### 📦 Exemplo JSON

```json
{
  "id": "018b5e0a-0000-7000-a000-000000000000",
  "name": "John Doe",
  "email": "john@example.com",
  "role": "SELLER",
  "created_at": "2025-09-18T10:00:00Z",
  "updated_at": "2025-09-18T10:00:00Z"
}
```

---

### 📦 `products`

| Campo         | Tipo e Regras | Descrição |
|---------------|---------------|-----------|
| `id` | `UUID (PK)` | Identificador único do produto. |
| `name` | `VARCHAR(150) NOT NULL` (min. 3) | Nome do produto. |
| `sku` | `VARCHAR(50) UNIQUE NOT NULL` | Código SKU do produto. |
| `status` | `ENUM('AVAILABLE', 'SOLD_OUT', 'OUT_OF_STOCK', 'DEPRECATED') DEFAULT 'AVAILABLE'` | Status do produto. |
| `price_sale` | `NUMERIC(12,2) NOT NULL` | Preço de venda. |
| `cost_price` | `NUMERIC(12,2) NULL` | Preço de custo. |
| `stock_quantity` | `INTEGER NOT NULL DEFAULT 0` | Quantidade em estoque. |
| `min_stock` | `INTEGER NULL DEFAULT 0` | Estoque mínimo. |
| `category` | `VARCHAR(50) NULL` | Categoria do produto. |
| `ncm` | `VARCHAR(20) NULL` | Código NCM. |
| `created_at` | `TIMESTAMP DEFAULT now()` | Data de criação. |
| `updated_at` | `TIMESTAMP DEFAULT now()` | Última atualização. |
| `deleted_at` | `TIMESTAMP` | Exclusão lógica. |

#### ✅ Validações

- `name`: mínimo 3 caracteres
- `sku`: não vazio, único
- `price_sale`: >= 0
- `cost_price`: >= 0 (se fornecido)
- `stock_quantity`: >= 0
- `min_stock`: >= 0 (se fornecido)

```sql
CREATE TYPE product_status_enum AS ENUM ('AVAILABLE', 'SOLD_OUT', 'OUT_OF_STOCK', 'DEPRECATED');

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
```

#### 📦 Exemplo JSON

```json
{
  "id": "018b5e0a-0000-7000-a000-000000000001",
  "name": "Rice 1kg",
  "sku": "RCE-1KG",
  "ncm": "01010100",
  "category": "Food",
  "price_sale": 12.50,
  "cost_price": 9.00,
  "stock_quantity": 120,
  "min_stock": 10,
  "status": "AVAILABLE",
  "created_at": "2025-09-18T10:00:00Z",
  "updated_at": "2025-09-18T10:00:00Z"
}
```

## Entidade: inventory_movements

### Descrição
Movimentações de estoque, como entrada, saída e ajustes.

### Campos

| Campo        | Tipo / Restrição                                        | Descrição                                                                 |
|--------------|----------------------------------------------------------|---------------------------------------------------------------------------|
| id           | UUID (PK, DEFAULT gen_random_uuid_v7())                 | Identificador único da movimentação.                                     |
| product_id   | UUID (FK → products, ON DELETE CASCADE)                 | Produto relacionado à movimentação.                                      |
| type         | ENUM('IN', 'OUT', 'ADJUST') NOT NULL                    | Tipo da movimentação.                                                    |
| quantity     | INTEGER NOT NULL                                        | Quantidade movimentada. > 0 para IN/OUT; pode ser negativa no ADJUST.    |
| reason       | VARCHAR(255) NULL (obrigatório se type = 'ADJUST')     | Motivo da movimentação.                                                  |
| user_id      | UUID (FK → users)                                       | Usuário responsável.                                                     |
| date         | TIMESTAMP DEFAULT now()                                 | Data e hora da movimentação.                                             |
| deleted_at   | TIMESTAMP (NULL por padrão)                             | Exclusão lógica.                                                         |

### Validações Backend

- `quantity`: > 0 (IN/OUT); qualquer valor para ADJUST.
- `type`: Enum válido.
- `reason`: Obrigatório se tipo for `ADJUST`.
- `product_id` e `user_id`: devem existir.

### SQL

```sql
CREATE TYPE movement_type_enum AS ENUM ('IN', 'OUT', 'ADJUST');

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
````

### Exemplo JSON

```json
{
  "id": "018b5e0a-0000-7000-a000-000000000002",
  "product_id": "018b5e0a-0000-7000-a000-000000000001",
  "type": "IN",
  "quantity": 50,
  "reason": null,
  "user_id": "018b5e0a-0000-7000-a000-000000000000",
  "date": "2025-09-18T10:00:00Z"
}
```

Exemplo com ADJUST:

```json
{
  "type": "ADJUST",
  "quantity": -5,
  "reason": "Correção de estoque"
}
```

---

## Entidade: sales

### Descrição

Representa uma venda realizada no sistema.

### Campos

| Campo           | Tipo / Restrição                              | Descrição                                  |
| --------------- | --------------------------------------------- | ------------------------------------------ |
| id              | UUID (PK, DEFAULT gen\_random\_uuid\_v7())    | Identificador único da venda.              |
| user\_id        | UUID (FK → users)                             | Usuário (vendedor) responsável.            |
| date            | TIMESTAMP DEFAULT now()                       | Data e hora da venda.                      |
| payment\_method | ENUM('CASH', 'CARD', 'PIX', 'OTHER') NOT NULL | Método de pagamento.                       |
| total           | NUMERIC(12,2) NOT NULL CHECK (total >= 0)     | Valor total da venda (soma dos subtotais). |
| deleted\_at     | TIMESTAMP                                     | Exclusão lógica (soft delete).             |

### Validações Backend

* `payment_method`: Enum válido.
* `total`: >= 0, calculado a partir dos itens.
* Pelo menos 1 `sale_item` relacionado.
* `user_id`: deve existir.

### SQL

```sql
CREATE TYPE payment_method_enum AS ENUM ('CASH', 'CARD', 'PIX', 'OTHER');

CREATE TABLE sales (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid_v7(),
    user_id UUID REFERENCES users(id) NOT NULL,
    date TIMESTAMP DEFAULT now(),
    payment_method payment_method_enum NOT NULL,
    total NUMERIC(12,2) NOT NULL CHECK (total >= 0),
    deleted_at TIMESTAMP
);
```

### Exemplo JSON

```json
{
  "id": "018b5e0a-0000-7000-a000-000000000003",
  "user_id": "018b5e0a-0000-7000-a000-000000000000",
  "date": "2025-09-18T10:00:00Z",
  "payment_method": "PIX",
  "total": 25.00
}
```

---

## Entidade: sale\_items

### Descrição

Itens individuais de uma venda, vinculando produtos à venda.

### Campos

| Campo       | Tipo / Restrição                                | Descrição                           |
| ----------- | ----------------------------------------------- | ----------------------------------- |
| id          | UUID (PK, DEFAULT gen\_random\_uuid\_v7())      | Identificador do item de venda.     |
| sale\_id    | UUID (FK → sales, ON DELETE CASCADE) NOT NULL   | Venda relacionada.                  |
| product\_id | UUID (FK → products) NOT NULL                   | Produto vendido.                    |
| quantity    | INTEGER NOT NULL CHECK (quantity > 0)           | Quantidade vendida.                 |
| unit\_price | NUMERIC(12,2) NOT NULL CHECK (unit\_price >= 0) | Preço unitário do produto.          |
| subtotal    | NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0)    | Subtotal = quantity \* unit\_price. |
| deleted\_at | TIMESTAMP                                       | Exclusão lógica.                    |

### Validações Backend

* `quantity`: > 0
* `unit_price`: >= 0
* `subtotal`: quantity \* unit\_price
* `sale_id` e `product_id`: devem existir
* Verificar se há estoque suficiente antes da venda

### SQL

```sql
CREATE TABLE sale_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid_v7(),
    sale_id UUID REFERENCES sales(id) ON DELETE CASCADE NOT NULL,
    product_id UUID REFERENCES products(id) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
    subtotal NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0),
    deleted_at TIMESTAMP
);
```

### Exemplo JSON

```json
{
  "id": "018b5e0a-0000-7000-a000-000000000004",
  "sale_id": "018b5e0a-0000-7000-a000-000000000003",
  "product_id": "018b5e0a-0000-7000-a000-000000000001",
  "quantity": 2,
  "unit_price": 12.50,
  "subtotal": 25.00
}
```

## Relacionamentos entre Entidades

### Diagrama Conceitual (Resumido)

- **users (1) → sales (N)**  
  Um usuário pode realizar várias vendas.

- **products (1) → sales_items (N)**  
  Um produto pode estar presente em diversos itens de venda.

- **products (1) → inventory_movements (N)**  
  Um produto pode possuir múltiplas movimentações de estoque.

- **sales (1) → sale_items (N)**  
  Uma venda pode conter múltiplos itens de venda.


### Relacionamentos no Código Java (JPA/Hibernate)

#### `Sales`
```java
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
````

#### `InventoryMovements`

```java
@ManyToOne
@JoinColumn(name = "product_id")
private Product product;

@ManyToOne
@JoinColumn(name = "user_id")
private User user;
```

#### `SaleItems`

```java
@ManyToOne
@JoinColumn(name = "sale_id")
private Sale sale;

@ManyToOne
@JoinColumn(name = "product_id")
private Product product;
```

### Considerações

* Todas as relações `@ManyToOne` indicam que várias entidades estão ligadas a uma principal.
* Recomenda-se **uso de transações** para garantir integridade:

  * Criação de uma venda inclui:

    * Itens de venda
    * Verificação e atualização do estoque (movimentação `OUT`)
    * Cálculo correto do `total`
* Soft delete deve ser tratado via filtros no repositório ou por uso de `@Where` (Hibernate).

---

## Trigger para Atualização de Estoque (PostgreSQL)

### Objetivo

Atualizar o campo `stock_quantity` da tabela `products` automaticamente após cada movimentação de estoque (`inventory_movements`).

### SQL: Função e Trigger

```sql
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
```

```sql
CREATE TRIGGER trg_update_stock
AFTER INSERT ON inventory_movements
FOR EACH ROW
EXECUTE PROCEDURE update_stock();
```

### Observações

* A trigger **é executada automaticamente** após cada inserção em `inventory_movements`.
* Para o tipo `ADJUST`, o valor de `quantity` pode ser **positivo** ou **negativo**.
* Se necessário, você pode alterar a lógica para impedir que o estoque fique negativo.