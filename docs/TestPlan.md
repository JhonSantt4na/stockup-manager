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
