# Tratamento de Exceptions Logs

As exceções serão usadas para controlar erros específicos que podem acontecer durante a
execução do sistema, como tentar cadastrar um produto com dados inválidos ou tentar
vender um produto que não tem estoque suficiente.

ProdutoNotFoundException : extends RuntimeException 
	@ResponseStatus(HttpStatus.NOT_FOUND)

EstoqueInsuficienteException : extends RuntimeException 
	@ResponseStatus(HttpStatus.BAD_REQUEST)

MovimentacaoInvalidException : extends RuntimeException 

# Logs
O sistema deve usar o  SLF4J com Logback ou Log4j2 para adicionar logs na aplicação.

Níveis de log:  
TRACE → Detalhes muito finos (debugging em desenvolvimento).
DEBUG → Informações de depuração, dados de entrada/saída.
INFO → Eventos normais de execução (ex: "Product created successfully").
WARN → Algo inesperado, mas que não quebrou o sistema.
ERROR → Erros ou exceções que afetam o funcionamento.

No Controller → Será Logado apenas em eventos de entrada e saída, útil para auditoria:
Quando um endpoint é chamado.
Parâmetros principais recebidos (cuidado para não logar dados sensíveis).
Resultado ou status retornado.

No Service → Será Logado os eventos de negócio e exceções:
Início e fim de métodos importantes.
Decisões de negócio (ex: "Stock level too low, cannot process sale").
Captura de exceções antes de propagá-las.
No Exception Handler (Global) → Logue stacktraces de erros críticos.
Usaremos um @ControllerAdvice para centralizar tratamento de exceções e logar.
Onde NÃO logar ?
Não logue demais — excesso de logs gera ruído e pode impactar performance.
Não logue dados sensíveis (senhas, tokens JWT, dados de cartão de crédito).
Não use log dentro de loops intensos sem necessidade.

Log no início e no fim dos métodos do Service (DEBUG/INFO)
Log no Controller apenas quando entrar e sair do endpoint (INFO)
Log erros no nível ERROR (com stacktrace) Contextualize logs com IDs, nomes de recursos, etc., para facilitar o rastreamento.

private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);
logger.info("Cadastrando produto: {}", produto.getNome());


# Endpoints API
ProductController
public class ProductController (ProductService productService)
 Base endpoint: /api/products
Method: POST
 Endpoint: /api/products
 Description: Registers a new product in the database.
 Return: ResponseEntity<Product>
 Status Code: 201 - HttpStatus.CREATED


Method: GET
 Endpoint: /api/products
 Description: Retrieves a list of all registered products.
 Return: List<Product>
 Status Code: 200 - HttpStatus.OK


Method: GET
 Endpoint: /api/products/{id}
 Description: Retrieves a single product by its ID.
 Return: ResponseEntity<Product>
 Status Code: 200 - HttpStatus.OK


Method: PUT
 Endpoint: /api/products/{id}
 Description: Updates the details of an existing product.
 Return: ResponseEntity<Product>
 Status Code: 200 - HttpStatus.OK


Method: DELETE
 Endpoint: /api/products/{id}
 Description: Deletes a product by its ID.
 Return: ResponseEntity<Void>
 Status Code: 204 - HttpStatus.NO_CONTENT
InventoryMovementController
public class InventoryMovementController (InventoryMovementService inventoryMovementService)
Base endpoint: /api/inventory/movements
Method: POST
 Endpoint: /api/inventory/movements/in
 Description: Records a product entry into the inventory.
 Return: ResponseEntity<InventoryMovement>
 Status Code: 201 - HttpStatus.CREATED


Method: POST
 Endpoint: /api/inventory/movements/out
 Description: Records a product exit from the inventory.
 Return: ResponseEntity<InventoryMovement>
 Status Code: 201 - HttpStatus.CREATED

Method: POST
 Endpoint: /api/inventory/movements/adjust
 Description: Adjusts inventory quantity for a product (e.g., stock corrections).
 Return: ResponseEntity<InventoryMovement>
 Status Code: 201 - HttpStatus.CREATED

Method: GET
 Endpoint: /api/inventory/movements
 Description: Retrieves a list of all inventory movements.
 Return: List<InventoryMovement>
 Status Code: 200 - HttpStatus.OK
SalesController
public class SalesController (SalesService salesService)
 Base endpoint: /api/sales
Method: POST
 Endpoint: /api/sales
 Description: Registers a new sale and updates stock levels.
 Return: ResponseEntity<Sale>
 Status Code: 201 - HttpStatus.CREATED


Method: GET
 Endpoint: /api/sales?date=YYYY-MM-DD
 Description: Retrieves a list of sales filtered by a specific date.
 Return: List<Sale>
 Status Code: 200 - HttpStatus.OK
AuthController
public class AuthController
 Base endpoint: /api/auth
Method: POST
 Endpoint: /api/auth/login
 Description: Authenticates a user and returns an access token.
 Return: ResponseEntity<AuthResponse>
 Status Code: 200 - HttpStatus.OK
ReportsController
Access modifier & Class: public class ReportsController (private ReportsService reportsService)
 Base endpoint: /api/reports
Method: GET
 Endpoint: /api/reports/top-products
 Description: Retrieves a list of top-selling products.
 Return: List<ProductReport>
 Status Code: 200 - HttpStatus.OK


Method: GET
 Endpoint: /api/reports/low-stock
 Description: Retrieves a list of products with low stock levels.
 Return: List<ProductReport>
 Status Code: 200 - HttpStatus.OK
Lista de Endpoints
Products
POST /api/products : Cria um novo produto
GET /api/products : Lista produtos (com filtros, paginação e ordenação)
GET /api/products/{id} : Retorna produto específico
PUT /api/products/{id} : Atualiza produto parcialmente
DELETE /api/products/{id} :Soft delete (atributo deleted_at)
Inventory Movements
POST /api/inventory/movements/in : Entrada de estoque
POST /api/inventory/movements/out : Saída de estoque
POST /api/inventory/movements/adjust : Ajuste de estoque
GET /api/inventory/movements : Listagem de movimentos (com filtros e paginação)
Sales
POST /api/sales : Registrar venda
GET /api/sales?date=YYYY-MM-DD : Listagem de vendas (filtro por data: ?date=YYYY-MM-DD)
Authentication
POST /api/auth/login : Login e emissão de token JWT
Reports
GET /api/reports/top-products : Produtos mais vendidos
GET /api/reports/low-stock : Produtos com baixo estoque
Query Params
page e limit para paginação sort para ordenação (ex.: ?sort=name,-price)
Filtros específicos (ex.: ?category=eletronics&active=true)
Retornos:
Body JSON consistente:
  "data": {...},
  "meta": {
    "page": 1,
    "limit": 20,
    "total": 100

Erros padronizados:
  "error": {
    "code": 404,
    "message": "Produto não encontrado"
  }

Headers
Authorization: Bearer <token>
X-Rate-Limit-Limit, X-Rate-Limit-Remaining (opcional)

# Regras de Negócio

RN-001 - Estoque sempre atualizado por movimentações.
RN-002 - Venda só pode ser confirmada se houver estoque suficiente.
RN-003 - Ajuste de estoque exige motivo.
RN-004 - Usuário vendedor não pode lançar entrada ou ajuste.

Os serviços serão responsáveis pela lógica principal da aplicação. Eles interagem
diretamente com o banco de dados e executam as funcionalidades do sistema.

AuthService.java
Responsável por autenticação e emissão de tokens.

AuthResponse login(String email, String password)
User getAuthenticatedUser(String token) (opcional – usado para validar sessão e retornar usuário logado)

UserService.java
Gerencia os usuários do sistema (ADMIN, SELLER).

createUser(User user)
getUserById(UUID id)
listUsers()
updateUser(User user)
softDeleteUser(UUID id)

ProdutoService.java
Manipula as operações CRUD para produtos e lógica relacionada.

createProduct(Product product);
getProductById(UUID id);
List<Product> listProducts();
updateProduct(Product product);
deleteProduct(UUID id);


MovimentacaoService.java
Gerencia as entradas e saídas do estoque.

registerMovement(InventoryMovement movement);
List<InventoryMovement> listMovements();

VendaService.java
Gerencia as vendas de produtos.
registerSale(Sale sale);
List<Sale> listSales();
BigDecimal calculateTotalSales();
RelatorioService.java
Gera relatórios, como vendas diárias e movimentações.

List<Sale> generateDailySalesReport();
List<InventoryMovement> generateInventoryMovementsReport();

AlertaService.java
Lógica para verificar e gerar alertas de estoque baixo.

checkLowStockAlerts(List<Product> products);




# Estrutura do Projeto (Spring Boot)

Arquitetura em Camadas:

src/
└── main/
├── java/
│ ├── com/
│ │ ├── exemplo/
│ │ │ ├── controller/
│ │ │ │ ├── ProdutoController.java
│ │ │ │ ├── MovimentacaoController.java
│ │ │ │ └── VendaController.java
│ │ │ ├── service/
│ │ │ │ ├── ProdutoService.java
│ │ │ │ ├── MovimentacaoService.java
│ │ │ │ └── VendaService.java
│ │ │ ├── model/
│ │ │ │ ├── Produto.java
│ │ │ │ ├── Movimentacao.java
│ │ │ │ └── Venda.java
│ │ │ ├── repository/
│ │ │ │ ├── ProdutoRepository.java
│ │ │ │ ├── MovimentacaoRepository.java
│ │ │ │ └── VendaRepository.java
│ │ │ ├── exception/
│ │ │ │ ├── ProdutoNotFoundException.java
│ │ │ │ ├── EstoqueInsuficienteException.java
│ │ │ │ └── MovimentacaoInvalidException.java
│ │ │ └── util/
│ │ │ └── LogUtil.java
│ └── resources/
│ └── application.properties
└── test/
└── java/
├── com/
│ ├── exemplo/
│ │ ├── controller/
│ │ └── service/
│ └── resources/



# Frontend

Menu Interativo: Catálogo, Vendas , estoque e Relatório

Tela de Login
Objetivo: Autenticação de usuários para acesso ao sistema.
Funcionalidades:
Campos para usuário e senha.
Botão de login.
Mensagem de erro caso as credenciais sejam incorretas.
Redirecionamento para o dashboard (se o login for bem-sucedido).

Tela de Dashboard (Principal)
Objetivo: Visão geral do estoque, com acesso rápido a informações importantes como
vendas do dia, alertas de estoque e gráficos.
Funcionalidades:
Resumo de Vendas: Gráficos e totais de vendas diárias, semanais e mensais.
Alertas de Estoque: Lista de produtos com estoque abaixo do limite mínimo.
Atividades Recentes: Exibição de movimentações de estoque (entradas/saídas).
Links rápidos para outras telas (Cadastro de produtos, Movimentação de estoque, Relatórios).
Botão de logout
Notificações em tempo real sobre alertas de estoque.

Tela de Cadastro de Produtos
Objetivo: Permitir o cadastro de novos produtos no sistema, com as informações
detalhadas.
Funcionalidades:
Campos de Entrada: Nome do produto, categoria, preço de venda, custo, quantidade inicial e estoque mínimo.
Botão de salvar: Para registrar o produto no banco de dados.
Lista de produtos cadastrados: Exibir uma lista de produtos já cadastrados com a possibilidade de editar ou excluir.

Tela de Listagem de Produtos
Objetivo: Exibir todos os produtos cadastrados e permitir a edição ou exclusão.
Funcionalidades:
Tabela de produtos: Exibir dados como nome, categoria, quantidade em estoque, preço e ações (editar/excluir).
Botão de filtrar: Para buscar produtos por nome, categoria ou quantidade.
Opções de edição e exclusão.
Visualizar detalhes de um produto (como histórico de movimentações).


Tela de Movimentação de Estoque
Objetivo: Registrar entradas e saídas de produtos no estoque.
Funcionalidades:
Campos de Entrada:
Selecione o produto.
Informe a quantidade.
Selecione o tipo de movimentação (entrada ou saída).
Campos adicionais para **motivo** ou **observações**.
Botão de salvar: Para registrar a movimentação.
Histórico de movimentações: Exibir as movimentações realizadas para cada produto (com data, tipo e quantidade).

Tela de Relatórios de Vendas
Objetivo: Gerar relatórios sobre as vendas realizadas, de acordo com filtros de tempo.
Funcionalidades:
Filtros de Data: Selecione o intervalo de datas (diário, semanal, mensal).
Relatório em formato de gráfico: Exibir as vendas totais por produto, por categoria e
por método de pagamento (cartão, dinheiro, Pix).
Tabela de Vendas: Exibir as vendas realizadas no período, com detalhes (produto,
quantidade, valor total, forma de pagamento).
Botão para exportar: Exportar o relatório para formato CSV ou PDF.

Tela de Relatórios de Estoque
Objetivo: Exibir o estado atual do estoque, com destaque para produtos que estão
abaixo do limite mínimo.
Funcionalidades:
Tabela de produtos: Exibir todos os produtos no estoque, com quantidade disponível
e limite mínimo de estoque.
Filtro de Estoque Baixo: Exibir apenas os produtos cujo estoque está abaixo do
mínimo.
Opções de alertas: Notificação de quando é necessário fazer reposição de algum
produto.
Visualizar histórico de vendas: Para verificar o desempenho do produto ao longo do
tempo.

Tela de Alertas de Estoque
Objetivo: Exibir todos os alertas de baixo estoque para os administradores.
Funcionalidades:
Lista de produtos com estoque baixo**: Exibir nome, quantidade atual e quantidade
mínima.
Notificação de urgência**: Destacar os produtos que precisam ser comprados
imediatamente.
Ação rápida: Botões para reabastecer o estoque diretamente da tela.


Tela de Configurações (Opcional)
Objetivo: Permitir ajustes de configurações do sistema.
Funcionalidades:
Alterar dados do usuário: Nome, senha e e-mail.
Configuração de limites de estoque: Definir o valor mínimo para o alerta de estoque
baixo.
Configuração de notificações: Definir se deseja receber alertas por e-mail ou
mensagem.

# Componentes e Serviços Angular

Componentes:
LoginComponent: Para autenticação de usuário.
DashboardComponent: Para exibição dos dados gerais.
ProductListComponent: Para a listagem de produtos.
ProductFormComponent: Para o cadastro de novos produtos.
StockMovementComponent: Para registrar as movimentações de estoque.
SalesReportComponent: Para exibir o relatório de vendas.
StockReportComponent: Para exibir o relatório de estoque.
StockAlertsComponent: Para exibir os alertas de estoque.
SettingsComponent: Para configurações de usuário e do sistema.

Serviços (Services):
AuthService: Para autenticação de usuários.
ProductService: Para interagir com a API de produtos.
StockMovementService: Para interagir com a API de movimentação de estoque.
SalesReportService: Para gerar relatórios de vendas.
StockReportService: Para gerar os relatórios de estoque.
StockAlertsService: Para gerenciar alertas de baixo estoque.

Modelos (Models):
Product: Representação de um produto (nome, categoria, preço, quantidade, etc).
StockMovement: Representação de uma movimentação de estoque (entrada ou
saída).
Sale: Representação de uma venda (produto, quantidade, forma de pagamento).
Report: Representação de um relatório (dados agregados de vendas ou estoque).



# Estrutura de Pastas no Angular:

src/
├── app/
├── components/
├── dashboard/
├── product/
├── stock-movement/
├── sales-report/
├── stock-report/
├── stock-alerts/
└── settings/
├── services/
├── auth.service.ts
├── product.service.ts
├── stock-movement.service.ts
├── sales-report.service.ts
├── stock-report.service.ts
└── stock-alerts.service.ts
├── models/
├── product.ts
├── stock-movement.ts
├── sale.ts
└── report.ts
├── app.module.ts
└── app-routing.module.ts




# Documentação e Divulgação

README.md com descrição, tecnologias, instruções de setup e screenshots.
Vídeo de Demonstração mostrando fluxo completo (venda → estoque → relatório).
Post no LinkedIn contando a história do problema e impacto resolvido.

Swagger/OpenAPI:
Objetivo: Documentar todos os endpoints da API com o uso de Swagger ou OpenAPI.
Requisitos Técnicos: Use Springfox Swagger (no caso de backend em Java com Spring Boot) para gerar a documentação interativa da API.
Detalhe os métodos HTTP (GET, POST, PUT, DELETE), parâmetros, e tipos de
resposta para cada endpoint.

Postman:
Objetivo: Criar uma coleção de testes para a API usando o Postman.
Passos:
Importe ou crie as requisições no Postman com todas as rotas do seu sistema.
Documente o que cada endpoint faz e como ele deve ser consumido.
Crie variáveis no Postman para facilitar o teste de diferentes ambientes
(desenvolvimento, homologação, produção).

Layout Readme.md:

Título do Repositório:
Sistema de Gerenciamento de Estoque com Alertas e Relatórios de Vendas

Descrição Curta:
Um sistema de gerenciamento de estoque inteligente, com funcionalidades de controle de
entradas e saídas, alertas de baixo estoque, e relatórios detalhados de vendas. A aplicação
permite melhorar a tomada de decisões de compra, reposição e monitoramento de produtos
com base no histórico de vendas.

Objetivo do Projeto:
Este projeto foi desenvolvido para automatizar o controle de estoque de uma pequena
ou média empresa, permitindo o gerenciamento de produtos, movimentações de estoque,
vendas e relatórios detalhados de desempenho. Além disso, o sistema oferece alertas em
tempo real sobre a necessidade de reposição de estoque e gera relatórios semanais/dia
para facilitar a visualização das métricas de vendas e fluxo de caixa.

Funcionalidades Principais:
Cadastro de Produtos: Permite o cadastro de produtos no estoque, com detalhes como
nome, categoria, preço de venda, custo e quantidade disponível.
Controle de Movimentações: Registra as entradas e saídas de produtos, atualizando
automaticamente a quantidade no estoque.
Alertas de Baixo Estoque: Notifica automaticamente quando o estoque de um produto
atinge o limite mínimo definido.
Relatórios de Vendas: Gera relatórios de vendas diárias, semanais e mensais, com
detalhamento por produto, total vendido, formas de pagamento (cartão, dinheiro, Pix), e
análise de tendências de vendas.
Integração com Frontend (Futuro): A API está pronta para integração com qualquer
frontend (React, Angular, Vue.js, etc.), permitindo uma interface gráfica de fácil uso.

Tecnologias Usadas:
Backend:
Java (com Spring Boot) para a construção da API RESTful.
JPA/JDBC para interação com banco de dados (MySQL ou SQLite).
Spring Data JPA para persistência de dados.
SLF4J/Logback para logging.
Banco de Dados: MySQL ou SQLite (para persistir dados de produtos, movimentações e vendas).
Ferramentas de Teste: JUnit 5, Mockito e Postman para testar as APIs.
Futuro Frontend (Opcional): React ou Angular para desenvolvimento de uma interface gráfica (a ser integrado posteriormente).

Como Executar o Projeto Locamente:

1. Clone o Repositório:

```bash
git clone https://github.com/SEU-USERNAME/nome-do-repositorio.git
```

2. Configuração do Banco de Dados:
Se você estiver usando o MySQL, crie um banco de dados chamado `estoque` ou
altere o arquivo `application.properties` para apontar para o seu banco de dados.

3. Build e Execução do Backend:
Certifique-se de que o Java 17 ou superior está instalado.
Navegue até a pasta do projeto e execute:

```bash
./mvnw spring-boot:run
```
4. Testando as APIs:
Abra o Postman e utilize os endpoints disponíveis para testar a API, como:

POST /api/produtos - Para cadastrar um produto.
GET /api/produtos/{id} - Para buscar um produto específico.
POST /api/movimentacoes - Para registrar uma movimentação de estoque.
GET /api/vendas - Para listar todas as vendas.

Endpoints da API:
1. Product:
POST /api/produtos – Cadastrar um novo produto
GET /api/produtos/{id} – Buscar um produto por ID
PUT /api/produtos/{id} – Atualizar informações de um produto
DELETE /api/produtos/{id} – Deletar um produto
2. Movimentação:
POST /api/movimentacoes – Registrar uma movimentação de estoque
GET /api/movimentacoes – Listar todas as movimentações
3. Venda:
POST /api/vendas – Registrar uma venda
GET /api/vendas – Listar todas as vendas
4. Relatórios:
GET /api/relatorios/vendas – Gerar relatórios de vendas

Logs:
Os logs são gerados com **SLF4J** e **Logback**. Eles fornecem informações sobre as
interações com o sistema, como o cadastro de produtos, movimentações e vendas. Os logs
são úteis para monitoramento e depuração.

Exceções e Erros:
O sistema implementa exceções personalizadas para garantir a integridade dos dados:
ProdutoNotFoundException– Caso o produto não seja encontrado.
EstoqueInsuficienteException – Quando não há quantidade suficiente de produto no
estoque para uma venda ou movimentação.

As exceções são tratadas e retornam códigos de status apropriados (ex: `404 Not Found`,
`400 Bad Request`).

Melhorias Futuras:
Implementação de frontend em React ou Angular.
Integração com sistemas de pagamento (como MercadoPago ou PayPal) para registrar
diferentes formas de pagamento.
Sistema de Previsão de Vendas baseado no histórico de dados (usando modelos de
Machine Learning ou Análise Preditiva).

Contribuições:
Este projeto está aberto para contribuições! Se você tiver sugestões ou melhorias, fique à
vontade para abrir um Pull Request. Para colaborar, siga estas etapas:

1. Fork este repositório.
2. Crie uma branch para a sua feature (`git checkout -b feature-nome`).
3. Faça suas alterações e commit (`git commit -am 'Adicionando nova feature'`).
4. Envie para o repositório remoto (`git push origin feature-nome`).
5. Abra um Pull Request!

Licença:
Este projeto está sob a licença [MIT](LICENSE).


# Deploy / Infraestrutura
Provedores sugeridos

Aplicação: Railway, Render ou Vercel (para o front).
Banco de Dados: PostgreSQL gerenciado (Railway, Supabase, Neon ou RDS se for AWS).
Configuração de Banco

Criar banco em provider gerenciado.
Configurar backups automáticos diários.
Utilizar migrações de schema com Flyway ou Liquibase para versionamento de estrutura.
Variáveis de ambiente: DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD.

Pipeline CI/CD

CI (GitHub Actions)
Executar build e testes a cada push/PR.
Rodar migrações em ambiente de staging antes do deploy.


CD (Continuous Delivery)
Deploy automático para ambiente de staging.
Deploy manual (com aprovação) para produção.


Segredos:
Usar GitHub Secrets para armazenar credenciais de banco, tokens de deploy e JWT secret.
Rotacionar segredos regularmente.

Variáveis de Ambiente Essenciais

DATABASE_URL
JWT_SECRET
PORT
ENVIRONMENT (dev/staging/prod)
Observabilidade

Logs: Centralizar logs (ex.: Logtail, Grafana Loki).
Métricas: Prometheus para métricas + Grafana para dashboards.
APM: OpenTelemetry ou New Relic para rastrear performance e erros.

Alertas: Configurar alertas de erro ou indisponibilidade no provider ou Grafana.
Infra mínima recomendada

1 container (app) + 1 banco Postgres gerenciado.
Escalonamento horizontal simples (1-2 instâncias) em produção.
Backup diário + retenção mínima de 7 dias.
Monitoramento básico (uptime e métricas de uso).
Sprints e Tempo Estimado

Sprint 1 (40h): Backend com CURD de produto + autenticação.
Sprint 2 (50h): Movimentações de estoque + endpoints de venda.
Sprint 3 (50h): Frontend Mini PDV + atualização estoque em tempo real.
Sprint 4 (30h): Relatórios + ajustes UX + PWA offline.
Sprint 5 (30h): Deploy + testes + documentação (README, vídeo demo).

Total estimado: ~200h (aprox. 6-8 semanas dedicando 25-30h/semana).