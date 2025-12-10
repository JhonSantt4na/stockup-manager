- Ajustar os Testes

- Auth - 
  - Rate limiting: Login sem limite de tentativas pode levar a brute-force. Integre com Spring Boot Actuator ou Bucket4j pra limitar por IP/usuário.
  - CORS/CSRF: Se for API consumida por frontend, configure globalmente, mas teste se o login aceita OPTIONS preflight.

- UserController
  - Email verification: No register, se tiver email, adicione token de confirmação (fora da sprint 1).

- Adicionar Teste de Integration com o Banco
- Adicionar Testes Unitary nas classes controller (Auth, User, Roles)
- Implementar o gitHub actions passando nos testes;

___________________________________________________________________________-

- O UserService funciona, mas está:

muito grande
repetitivo
com validações duplicadas
com lógica de roles inconsistente
sem @Transactional
com pontos que podem quebrar facilmente
Se você quiser, posso:

Gerar a versão ideal e refatorada
Criar os métodos privados auxiliares
Quebrar em classes menores


___________________________________________________________________________-

Lot / Batch (lote de produtos — muito comum em alimentos/farmacêutico)

ExpirationDate / PerishableControl (controle de validade)

Quotation / Budget (orçamento)

Refund / Return

Shipment / Delivery (entregas)

ShippingMethod (Transportadoras, retirada, frete etc.)

Invoice / NF-e
_____________________________________________________________________________-

Dtos e mappers e repositorios e service
controllers aqui embaixo


CashMovementControllerDocs





__________________________________________________________________-

// Fornecedor

Falta Migrations para isso + População : 
5.1 Supplier
5.2 PurchaseOrder
5.3 PurchaseItem
5.4 CashEntry
5.5 CashMovement
5.6 CashRegister
5.7 CashRegisterSession
5.8 Payable
5.9 Payments
5.10 PaymentsGatewayTransaction
5.11 PaymentsMethod