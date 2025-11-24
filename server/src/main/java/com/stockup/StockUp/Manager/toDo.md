- Ajustar os Testes

- Auth - 
  - Rate limiting: Login sem limite de tentativas pode levar a brute-force. Integre com Spring Boot Actuator ou Bucket4j pra limitar por IP/usuário.
  - CORS/CSRF: Se for API consumida por frontend, configure globalmente, mas teste se o login aceita OPTIONS preflight.

- UserController
  - Email verification: No register, se tiver email, adicione token de confirmação (fora da sprint 1).

- Adicionar Teste de Integration com o Banco
- Adicionar Testes Unitary nas classes controller (Auth, User, Roles)
- Implementar o gitHub actions passando nos testes;




- O UserService funciona, mas está:

muito grande
repetitivo
com validações duplicadas
com lógica de roles inconsistente
sem @Transactional
com pontos que podem quebrar facilmente
Se você quiser, posso:

Gerar a versão ideal e refatorada
OU
Criar os métodos privados auxiliares
OU
Quebrar em classes menores



1.3 Brand (opcional)
id
name


1.4 UnitOfMeasure (UN, CX, KG etc.)
id
abbreviation
description


Quotation / Budget (orçamento)

Refund / Return

Shipment / Delivery (entregas)

ShippingMethod (Transportadoras, retirada, frete etc.)

Invoice / NF-e




3.3 Payment
Pode ser 1 pedido → N pagamentos.
id
order
paymentMethod
paidAmount
paidAt
transactionCode
status

3.4 PaymentMethod
id
name (pix, cartão, dinheiro)
enabled

Payable (Contas a pagar)
Receivable (Contas a receber)
CashFlow (Fluxo de caixa)
CashRegister / PDV Session (Abertura/fechamento de caixa)

















2.1 Stock
Representa o estoque atual de um produto em um local.
id
product
quantity
minimumQuantity
location
updatedAt



2.2 StockMovement
Histórico de movimentações.
id
product
movementType (IN/OUT)
quantity
previousQuantity
finalQuantity
reason (SALE, PURCHASE, ADJUSTMENT)
relatedDocumentId (orderId, purchaseId, etc.)
timestamp




2.3 Warehouse / Location
id
name
description


Lot / Batch (lote de produtos — muito comum em alimentos/farmacêutico)
ExpirationDate / PerishableControl (controle de validade)























// Fornecedor

5.1 Supplier
id
name
cnpj
email
phone




5.2 PurchaseOrder
Pedido de compra para fornecedores.
id
supplier
orderNumber
expectedArrivalDate
status
items
total



5.3 PurchaseItem
id
purchaseOrder
product
quantity
costPrice
subtotal

