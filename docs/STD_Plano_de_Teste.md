# Software Test Description (STD)

**Projeto:** StockUp Manager  
**Versão do Sistema:** 0.0.1  
**Versão do Documento:** 21/09/2025 / 0.0.1  
**Autor(es):** Santt4na  
**Cobertura mínima recomendada:** 80% de código.
---

## 1. Introdução

**Objetivo do Documento**  
Garantir a qualidade do sistema em todas as camadas — regras de negócio, persistência, API e UI. Descrever os testes que serão realizados no sistema, cobrindo requisitos funcionais e não-funcionais definidos no SRS_Documento_de_Requisitos.

**Escopo**  
- Módulos/componentes cobertos por este STD:
    - Módulo de Gestão de Produtos
    - Módulo de Controle de Estoque
    - Módulo de Vendas (PDV)
    - Módulo de Relatórios
    - Módulo de Autenticação e Autorização
    - Funcionalidade Offline/Sincronização (PWA)

- Componentes não cobertos:
    - Integração com sistemas fiscais (NF-e)
    - Módulos futuros de integração com ERPs
---

## 2. Itens a Testar

| Componente / Módulo | Versão | Responsável |
|---|---|---|
| Gestão de Produtos | v1.0 | Santt4na |
| Controle de Estoque | v1.0 | Santt4na |
| Ponto de Venda (PDV) | v1.0 | Santt4na |
| Relatórios | v1.0 | Santt4na |
| Autenticação | v1.0 | Santt4na |
| Sincronização Offline | v1.0 | Santt4na |

---

## 3. Requisitos Cobertos

- Requisitos Funcionais

    - RF001: Cadastro de Produto
    - RF002: Remoção e Atualização de Produto
    - RF006: Consulta de Saldo de Estoque
    - RF007: Registro de Entradas e Saídas
    - RF008: Ajuste Manual de Estoque
    - RF010: Alerta de Baixo Estoque
    - RF013: Busca de Produto no PDV
    - RF014: Adição de Produto à Venda
    - RF016: Seleção de Forma de Pagamento
    - RF017: Atualização de Estoque
    - RF018: Registro de Venda
    - RF019: Comprovante de Venda
    - RF021: Relatório Diário de Vendas
    - RF024: Vendas Offline
    - RF025: Login de Usuário
    - RF026: Controle de Acesso

- Requisitos Não-Funcionais

    - RNF001 (Desempenho): Tempo de processamento < 1s no p95
    - RNF003 (Usabilidade): Interface intuitiva com taxa de sucesso > 90%
    - RNF004 (Usabilidade): Responsividade em múltiplos dispositivos
    - RNF006 (Segurança): Criptografia em trânsito e conformidade LGPD
    - RNF010 (Confiabilidade): Disponibilidade mínima de 99,5%
    - RNF017 (Plataforma): Funcionamento como PWA instalável
    - RNF021 (Segurança): Proteção de dados sensíveis 

---

## 4. Estratégia de Teste
Cada produto deve ter nome, categoria, preço de venda, custo e quantidade em estoque. Um sistema para registrar quando um produto entra (compra) e saí (venda). e relatório diário de vendas com o total vendido por produto e o valor total de vendas.

### 4.1. Tipos de Teste

- Testes Unitários (cobertura mínima de 80%)

- Testes de Sistema (end-to-end)

- Testes de Integração
   - Validar interação entre API, banco de dados e serviços
    Cenários essenciais:
    - Fluxo completo de venda: criar produto → registrar entrada → vender produto → verificar estoque atualizado.
    - Concorrência: duas vendas simultâneas para o mesmo produto → validar lock/transação
    - Soft delete: deletar produto e garantir que ele não aparece em listagens, mas persiste no banco com deleted_at.

- Testes de Usabilidade
    - Simular venda offline → registrar localmente → sincronizar quando online.
    - Testar conflitos (mesmo produto vendido em paralelo em duas instâncias).
    - Garantir que a conciliação atualiza estoque corretamente.

- Testes de Performance (k6/JMeter)
    - Ferramentas sugeridas: k6, JMeter ou Locust.
    - Cenários:
        - 1000 requisições simultâneas de venda → verificar tempo de resposta < 500ms e consistência de estoque.
        - Relatórios com grande volume de dados (1M registros) → validar tempo de geração e uso de índices no DB.

- Testes de Segurança
    - Verificar autenticação JWT (tokens expirados, inválidos, manipulados).
    - Testar endpoints sensíveis sem token → esperado HTTP 401.
    - Testar permissões (SELLER não pode lançar ajustes de estoque).
    - Testar SQL Injection, XSS e CSRF.

- Testes de Regressão
    - Executar após cada nova feature ou correção de bug.
    - Garantir que funcionalidades existentes não foram quebradas (ex.: cadastro de produto continua funcionando após refatoração de estoque).

### 4.2. Estratégia de Teste

- Desenvolvimento orientado a testes (TDD) para componentes críticos
- Testes automatizados para regressão e integração contínua
- Testes manuais para usabilidade e casos complexos
- Ambiente de teste espelhando o ambiente de produção

---

## 5. Casos de Teste

| ID | Descrição | Pré-condições | Passos | Dados de Entrada | Resultado Esperado |
|----|-----------|---------------|--------|------------------|--------------------|
| CT-001 | Testar login com credenciais válidas | Sistema instalado, usuário de teste cadastrado | 1. Acessar tela de login<br>2. Inserir usuário/senha válidos<br>3. Clicar em "Entrar" | usuário: "admin", senha: "admin123" | Redirecionamento para dashboard, token JWT armazenado |
| CT-002 | Tentar login com senha incorreta | Sistema instalado | 1. Acessar tela de login<br>2. Inserir usuário válido e senha inválida<br>3. Clicar em "Entrar" | usuário: "admin", senha: "errada" | Mensagem de erro "Credenciais inválidas", HTTP 401 |
| CT-003 | Cadastrar produto válido | Usuário autenticado com permissão de admin | 1. Navegar para Gestão de Produtos<br>2. Clicar em "Novo Produto"<br>3. Preencher campos obrigatórios<br>4. Clicar em "Salvar" | nome: "Produto Teste", SKU: "TEST001", preço: 29.90, estoque: 10 | Produto salvo no banco, mensagem de sucesso, SKU único validado |
| CT-004 | Tentar cadastrar produto com SKU duplicado | Produto "TEST001" já cadastrado | 1. Navegar para Gestão de Produtos<br>2. Clicar em "Novo Produto"<br>3. Preencher campos com SKU existente<br>4. Clicar em "Salvar" | SKU: "TEST001" | Mensagem de erro "SKU já existe", HTTP 422 |
| CT-005 | Registrar venda com estoque suficiente | Produto cadastrado com estoque ≥ 5 | 1. Acessar PDV<br>2. Buscar produto<br>3. Adicionar à venda (quantidade: 3)<br>4. Selecionar forma de pagamento<br>5. Finalizar venda | quantidade: 3, pagamento: "PIX" | Estoque reduzido em 3 unidades, venda persistida com status CONFIRMADA |
| CT-006 | Tentar venda com estoque insuficiente | Produto cadastrado com estoque = 2 | 1. Acessar PDV<br>2. Buscar produto<br>3. Adicionar à venda (quantidade: 5)<br>4. Tentar finalizar | quantidade: 5 | Mensagem "Estoque insuficiente", HTTP 422, venda não confirmada |
| CT-007 | Registrar entrada de estoque | Produto cadastrado | 1. Navegar para Gestão de Estoque<br>2. Selecionar "Entrada"<br>3. Informar produto e quantidade<br>4. Confirmar | quantidade: 15, tipo: "COMPRA" | Estoque aumentado em 15 unidades, movimentação registrada |
| CT-008 | Gerar relatório diário de vendas | Vendas registradas no dia atual | 1. Navegar para Relatórios<br>2. Selecionar "Relatório Diário"<br>3. Executar relatório | data: [data atual] | Relatório com: total de vendas, valor total, quebra por forma de pagamento |
| CT-009 | Venda offline (PWA) | Dispositivo sem conexão, app instalado | 1. Desligar conexão<br>2. Registrar venda normalmente<br>3. Verificar armazenamento local | venda offline com 2 itens | Venda salva localmente (IndexedDB), sincronização pendente |
| CT-010 | Sincronização após reconexão | Venda pendente no dispositivo | 1. Reestabelecer conexão<br>2. Aguardar sincronização automática<br>3. Verificar servidor | - | Venda sincronizada, estoque atualizado no servidor |
| CT-011 | Teste de permissão de vendedor | Usuário com perfil "VENDEDOR" logado |  |  |

---

## 6. Matriz de Rastreabilidade

| Requisito | Casos de Teste Relacionados |
|---|---|
| RF001 | CT-003, CT-004 |
| RF002 | CT-011 |
| RF006 | CT-005, CT-006, CT-007 |
| RF007 | CT-007 |
| RF008 | CT-011 |
| RF010 | CT-007 |
| RF013 | CT-005 |
| RF014 | CT-005, CT-006 |
| RF016 | CT-005 |
| RF017 | CT-005 |
| RF018 | CT-005 |
| RF019 | CT-008 |
| RF021 | CT-009, CT-010 |
| RF024 | CT-001, CT-002 |
| RF025 | CT-011 |
| RF026 | CT-008 |
| RNF001 | CT-001, CT-003, CT-005 |
| RNF003 | CT-001, CT-002 |
| RNF010 | Todos os casos online |
| RNF017 | CT-009, CT-010 |
| RNF021 | CT-001, CT-002 |
---

## 7. Critérios de Aceitação / Saída





# Critérios de Aceitação
Registro de Venda e Atualização de Estoque:  
Dado que existe um produto com estoque suficiente
- Quando o usuário registra uma venda
- Então o estoque do produto deve ser reduzido imediatamente no banco de dados
- E a venda deve ser persistida com status CONFIRMADA
- E deve ser possível consultar essa venda pelo endpoint /api/sales

Registro de Entrada de Estoque :  
Dado que existe um produto cadastrado 
- Quando o usuário lança uma movimentação de entrada
Então o estoque do produto deve aumentar pela quantidade informada
- A movimentação deve ser registrada na tabela inventory_movements

Relatório de Vendas do Dia :
Dado que existem vendas registradas para o dia atual
- Quando o usuário solicita o relatório diário (GET /api/reports/top-products ou endpoint de vendas diárias)
- Então o relatório deve exibir:
    - Quantidade total de vendas realizadas
    - Valor total vendido
    - Quebra por forma de pagamento (CASH, CARD, PIX, OTHER)
- E deve retornar em menos de 1 segundo para até 10k registros.

Suporte Offline e Sincronização (se PWA ou mobile for usado)
- Dado que o usuário está offline
- Quando ele registra uma venda no app
    - Então a venda deve ser armazenada localmente (IndexedDB / SQLite no dispositivo)
    - Quando a conexão retornar,
        - Então a venda deve ser sincronizada com o servidor, estoque atualizado e movimentação registrada
    - O usuário deve ser notificado de sucesso ou conflito de sincronização

Casos de Erro / Borda:  
- Venda não deve ser confirmada se estoque < quantidade solicitada → retornar 422 Unprocessable Entity
- Se o token JWT for inválido ou expirado → retornar 401 Unauthorized
- Tentativa de vendedor lançar ajuste → retornar 403 Forbidden
- Se relatório for solicitado em dia sem vendas → retornar lista vazia (não erro).

- Todos os casos de teste críticos devem passar (100%)
- Cobertura de código mínima de 80% nos módulos principais
- Tempo de resposta dentro dos limites especificados nos RNFs
- Zero bugs críticos ou bloqueadores
- Aprovação formal dos testes de aceitação pelo cliente
- Criar produto válido (esperado: produto salvo).
- Criar produto com dados inválidos (esperado: exception/validação).
- Registrar movimentação de entrada/saída e verificar atualização de estoque.
- Registrar venda com estoque insuficiente (esperado: erro).
- Atualizar produto com novo preço/estoque.

---

## 8. Riscos e Dependências

| Risco | Impacto | Mitigação  |
|---|---|---|
| Dados de teste inadequados | Alto | Criar dataset realista antes do início dos testes |
| Ambiente não representativo | Médio | Espelhar configuração de produção |
| Falha na automação | Médio | Manter suíte de testes manuais para casos críticos |
| Cronograma apertado | Alto | Priorizar testes baseados em risco e criticialidade |

---

## 9. Aprovação

| Nome | Cargo / Papel | Data | Assinatura / Comentários |
|---|---|---|---|
| StockUp Manager | Gerente de Projeto | 21/09/2025 |  |
| Santt4na | Líder de Desenvolvimento | 21/09/2025 |  |
---