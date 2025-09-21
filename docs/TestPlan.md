Testes
Objetivo: Garantir a qualidade do sistema em todas as camadas — regras de negócio, persistência, API e UI.
Testes Unitários:
Escopo:
Serviços (ProductService, InventoryMovementService, SalesService etc.)
Regras de negócio (estoque mínimo, bloqueio de venda sem estoque, cálculo de total de venda).
Cobertura mínima recomendada: 80% de código.

Casos de teste principais:
Criar produto válido (esperado: produto salvo).
Criar produto com dados inválidos (esperado: exception/validação).
Registrar movimentação de entrada/saída e verificar atualização de estoque.
Registrar venda com estoque insuficiente (esperado: erro).
Atualizar produto com novo preço/estoque.


Testes de Integração: 
Objetivo: Validar interação entre API, banco de dados e serviços
Cenários essenciais:
Fluxo completo de venda: criar produto → registrar entrada → vender produto → verificar estoque atualizado.
Concorrência: duas vendas simultâneas para o mesmo produto → validar lock/transação
Soft delete: deletar produto e garantir que ele não aparece em listagens, mas persiste no banco com deleted_at.
Autenticação: login válido → gerar JWT → acessar endpoint protegido.

Testes de Regressão:
Executar após cada nova feature ou correção de bug.
Garantir que funcionalidades existentes não foram quebradas (ex.: cadastro de produto continua funcionando após refatoração de estoque).

Testes de Performance:
Ferramentas sugeridas: k6, JMeter ou Locust.
Cenários:
1000 requisições simultâneas de venda → verificar tempo de resposta < 500ms e consistência de estoque.
Relatórios com grande volume de dados (1M registros) → validar tempo de geração e uso de índices no DB.

Testes de Segurança:
Verificar autenticação JWT (tokens expirados, inválidos, manipulados).
Testar endpoints sensíveis sem token → esperado HTTP 401.
Testar permissões (SELLER não pode lançar ajustes de estoque).
Testar SQL Injection, XSS e CSRF.

Testes Offline / Sincronização (se app for PWA ou mobile):
Simular venda offline → registrar localmente → sincronizar quando online.
Testar conflitos (mesmo produto vendido em paralelo em duas instâncias).
Garantir que a conciliação atualiza estoque corretamente.

Ajustes Pós-Teste
Corrigir bugs detectados, adicionar novos casos para cada falha encontrada (evitar regressão).
Executar novamente todos os testes automatizados antes de subir para produção (CI)



Cada produto deve ter nome, categoria, preço de venda, custo e quantidade em estoque. Um sistema para registrar quando um produto entra (compra) e saí (venda). e relatório diário de vendas com o total vendido por produto e o valor total de vendas.

# Critérios de Aceitação
Registro de Venda e Atualização de Estoque
Dado que existe um produto com estoque suficiente
 Quando o usuário registra uma venda
 Então o estoque do produto deve ser reduzido imediatamente no banco de dados
 E a venda deve ser persistida com status CONFIRMADA
 E deve ser possível consultar essa venda pelo endpoint /api/sales
Registro de Entrada de Estoque
Dado que existe um produto cadastrado
Quando o usuário lança uma movimentação de entrada
Então o estoque do produto deve aumentar pela quantidade informada
E a movimentação deve ser registrada na tabela inventory_movements
Relatório de Vendas do Dia
Dado que existem vendas registradas para o dia atual
 Quando o usuário solicita o relatório diário (GET /api/reports/top-products ou endpoint de vendas diárias)
 Então o relatório deve exibir:
Quantidade total de vendas realizadas
Valor total vendido
Quebra por forma de pagamento (CASH, CARD, PIX, OTHER)
 E deve retornar em menos de 1 segundo para até 10k registros.
Suporte Offline e Sincronização (se PWA ou mobile for usado)
Dado que o usuário está offline
Quando ele registra uma venda no app
 Então a venda deve ser armazenada localmente (IndexedDB / SQLite no dispositivo)
 E quando a conexão retornar,
 Então a venda deve ser sincronizada com o servidor, estoque atualizado e movimentação registrada
 E o usuário deve ser notificado de sucesso ou conflito de sincronização
Casos de Erro / Borda
Venda não deve ser confirmada se estoque < quantidade solicitada → retornar 422 Unprocessable Entity
Se o token JWT for inválido ou expirado → retornar 401 Unauthorized
Tentativa de vendedor lançar ajuste → retornar 403 Forbidden
Se relatório for solicitado em dia sem vendas → retornar lista vazia (não erro).