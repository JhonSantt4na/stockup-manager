# Planejamento e Definição de Requisitos
## Visão do Sistema

Fornecer um sistema web PWA para pequenas lojas que permita controle de estoque e vendas em tempo real, geração de relatórios diários, e operação offline com sincronização. MVP não contempla integração fiscal (NF-e) — apenas recibo não fiscal substituindo o famoso caderno de anotações.

Cada produto deve ter nome, categoria, preço de venda, custo e quantidade em estoque. Um sistema para registrar quando um produto entra (compra) e saí (venda). e relatório diário de vendas com o total vendido por produto e o valor total de vendas.

### ⏳ - A fazer
### ✅ - Feito
### ⚠️ - Alerta

# Funcionalidades Essenciais

Atualizar Produto:  Alterar a quantidade em estoque ou outros dados do produto.

Excluir Produto: Remover um produto do estoque (soft delete).

Consultar Produtos: Listar todos os produtos, com possibilidade de filtrar por nome, categoria, status ou nível de estoque.

Controle de Entrada e Saída de Estoque : 
Registrar entradas (compras), saídas (vendas) e ajustes de estoque.
Cada movimentação deve atualizar a quantidade em estoque e ser registrada na tabela de movimentações.

Relatórios de Movimentação de Estoque: Gerar relatórios completos de movimentações (entradas, saídas, ajustes), com filtros por período e tipo de operação.

Vendas de Produto: Registrar vendas, atualizar estoque automaticamente e calcular o valor total vendido.

Consultar Vendas:  Listar vendas realizadas, com filtros por data, cliente ou produto.

Relatório de Vendas: Gerar relatório diário com quantidade e valor total vendido, 
utilizando consultas SQL para agrupar por data.

Alertas de Baixo Estoque:
Ao atualizar o estoque de um produto, verificar se está abaixo do mínimo definido.
Caso esteja, gerar alerta (log ou notificação).
Dashboard / Painel Resumo:
Produtos com baixo estoque
Total de vendas do dia
Últimas movimentações de estoque
Produtos mais vendidos


Gerenciamento de Usuários: Permitir que administradores cadastrem, atualizem e removam usuários.
Autenticação: 
Efetuar login e gerar token de acesso (JWT).
Validar o token para proteger endpoints e identificar o usuário logado.

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











# 📋 Requisitos Funcionais 📋
## 📌 Cadastro de Produto 📌
### [⚠️] RF001 Cadastro de Produto
Cadastrar produtos com os campos: name, ncm, sku, salePrice, costPrice, initialStock, category, minStock.

**Critérios de Aceitação:**
* Cadastro salvo e disponível para consulta.
* SKU único.
* Validação de campos obrigatórios.

**Exceções:**
* SKU duplicado → mensagem de erro.
* Campos obrigatórios vazios → mensagem de erro.
* Falha de banco → registrar log e informar usuário.

### [⚠️] RF002 Remoção e Atualização de Produto
Permitir remoção e edição de dados do produto.

**Critérios de Aceitação:**
- Edição salva e refletida na listagem.
- Produto removido não deve aparecer nas buscas.

**Exceções:**
- Produto vinculado a venda → impedir exclusão.
- Erro de banco → informar usuário.

### [⏳] RF003 – Listagem e Paginação
Exibir página com listagem de produtos e busca, com paginação usando parâmetros page, size, sort.

**Critérios de Aceitação:**
- Permitir busca por nome, categoria ou código.
- Navegação entre páginas funcionando corretamente.
- page → número da página inicia em 0
- size → quantidade de itens por página limite máximo 20 para evitar sobrecarga
- sort → campo e direção (ex: sort=name,asc)

**Exceções:**
- Busca sem resultados → exibir mensagem.

### [⏳] RF004 – Atualização de Dados
Permitir ajuste de estoque e edição de informações do produto.

**Critérios de Aceitação:**
- Alterações salvas no banco.
- Histórico de atualização registrado.

**Exceções:**
- Valores inválidos → bloquear operação.

### [⏳] RF005 – Persistência de Dados
Todas as informações devem ser armazenadas em banco de dados para consulta futura.

**Critérios de Aceitação:**
- Dados disponíveis após reinício da aplicação.

**Exceções:**
- Falha de conexão → registrar log e exibir mensagem.

## 📌 Controle de Estoque 📌
### [⚠️] RF006 – Consulta de Saldo
Exibir saldo de estoque de cada produto.

**Critérios de Aceitação:**
- Quantidade correta apresentada.

**Exceções:**
- Produto não encontrado → mensagem de erro.

### [⚠️] RF007 – Registro de Entradas e Saídas
Registrar movimentações de estoque:
IN → entrada (compra/recebimento).
OUT → saída (venda).

**Critérios de Aceitação:**
- Movimentações refletidas no saldo.
- Registro persistido no banco.

**Exceções:**
- Quantidade negativa → bloquear operação.

### [⚠️] RF008 – Ajuste Manual de Estoque
Permitir ajuste manual (ADJUST) com motivo obrigatório.

**Critérios de Aceitação:**
- Motivo salvo junto à movimentação.

**Exceções:**
- Motivo vazio → impedir ajuste.

### [⚠️] RF009 – Histórico de Movimentações
Exibir histórico de entradas, saídas e ajustes.

**Critérios de Aceitação:**
- Histórico filtrável por período.

**Exceções:**
- Sem movimentações → exibir mensagem.

### [⚠️] RF010 – Alerta de Baixo Estoque
Gerar alerta quando quantidade < minStock.

**Critérios de Aceitação:**
- Alerta visível na tela e na listagem.

**Exceções:**
- Produto sem minStock → não exibir alerta.

### [⏳] RF011 – Registro de Transações
Salvar cada transação com data, tipo, quantidade e produto.

**Critérios de Aceitação:**
- Registro disponível em consultas.

**Exceções:**
- Falha de banco → logar e notificar usuário.

### [⏳] RF012 – Visão Geral do Estoque
Exibir lista com:
Produtos cadastrados
Quantidade atual
Quantidade mínima
Alertas de reposição

**Critérios de Aceitação:**
- Dados corretos e atualizados.

**Exceções:**
- Produto sem estoque → exibir "0".

## 📌 Mini PDV (Vendas) 📌
### [⚠️] RF013 – Busca de Produto
Buscar produtos por name ou barcode.

**Critérios de Aceitação:**
- Produto encontrado em tempo real.

**Exceções:**
- Produto inexistente → exibir mensagem.

### [⚠️] RF014 – Adição à Venda
Adicionar produto à venda informando quantidade.

**Critérios de Aceitação:**
- Quantidade refletida no subtotal.

**Exceções:**
- Quantidade > estoque → bloquear venda.

### [⚠️] RF015 – Lista de Itens
Exibir lista de itens da venda com subtotal.

**Critérios de Aceitação:**
- Subtotal calculado corretamente.

**Exceções:**
- Lista vazia → não permitir finalizar venda.

### [⚠️] RF016 – Forma de Pagamento
Permitir selecionar: dinheiro, cartão, pix, vale (definir regras para saldo/integração).

**Critérios de Aceitação:**
- Pagamento registrado no fechamento.

**Exceções:**
- Pagamento inválido → exibir mensagem.

### [⏳] RF017 – Atualização de Estoque
Ao confirmar a venda, atualizar estoque em tempo real.

**Critérios de Aceitação:**
- Estoque atualizado imediatamente.
- Sem venda parcial em caso de erro.

**Exceções:**
- Estoque insuficiente → bloquear venda.
- Falha no banco → reverter operação.

### [⏳] RF018 – Registro de Venda
Criar registro de venda e itens no backend ao finalizar.

**Critérios de Aceitação:**
- Venda salva no banco.

**Exceções:**
- Falha de persistência → exibir erro.

### [⚠️] RF019 – Comprovante de Venda
Gerar comprovante em PDF ou HTML para impressão.

**Critérios de Aceitação:**
- Comprovante com itens e valores corretos.

**Exceções:**
- Falha na geração → exibir mensagem.

## 📌 Relatórios e Consultas 📌
### [⚠️] RF020 – Relatório de Movimentações
Gerar relatório com todas as movimentações em um período.

**Critérios de Aceitação:**
- Dados filtráveis por data.

**Exceções:**
- Sem movimentações → exibir mensagem.

### [⏳] RF021 – Relatório Diário de Vendas
Gerar relatório opcional do dia com:
Produto vendido
Quantidade
Total de vendas
Forma de pagamento

**Critérios de Aceitação:**
- Valores somados corretamente.

**Exceções:**
- Dia sem vendas → relatório vazio.

### [⏳] RF022 – Produtos Mais Vendidos
Gerar relatório com ranking de produtos mais vendidos.

**Critérios de Aceitação:**
- Ranking ordenado corretamente.

**Exceções:**
- Nenhum produto vendido → ranking vazio.

### [⏳] RF023 – Exibição Sob Demanda
Permitir exibição do relatório ao final do dia ou sob solicitação.

**Critérios de Aceitação:**
- Relatório acessível sob demanda.

**Exceções:**
- Falha na geração → exibir mensagem.

## 📌 Offline / PWA 📌
### [⚠️] RF024 – Vendas Offline
Permitir registrar vendas offline e sincronizar quando internet retornar.

**Critérios de Aceitação:**
- Vendas sincronizadas corretamente.
- Conflitos resolvidos por last-write-wins.
- Retenção local: quanto tempo guardar vendas offline antes de limpar?
- Conflitos: se o estoque mudou no servidor enquanto estava offline, o que fazer? avisar o usuário
- Fila de sincronização: reprocessa em caso de erro

**Exceções:**
- Falha de sincronização → alertar usuário.

## 📌 Autenticação e Autorização 📌
### [⚠️] RF025 – Login de Usuário
Permitir autenticação via JWT ou OAuth2 Password Flow.

**Critérios de Aceitação:**
- Sessão válida e segura.

**Exceções:**
- Login inválido → mensagem de erro.

### [⏳] RF026 – Controle de Acesso
Controle de permissões:
Admin → pode cadastrar, lançar entradas e ajustar estoque
Vendedor → só pode registrar vendas

**Critérios de Aceitação:**
- Usuário sem permissão não acessa funções restritas.

**Exceções:**
- Tentativa de acesso não autorizado → retornar erro 403.

### [⏳] RF027 – Auditoria e Log
Registrar eventos relevantes do sistema (cadastros, vendas, ajustes de estoque e erros) para auditoria.

**Critérios de Aceitação:**
- Logs armazenados com data/hora, usuário e ação.
- Consultáveis por administradores.
    
**Exceções:**
- Falha no log não deve impedir a operação principal.


# 📋 Requisitos Não Funcionais 📋
## 📌 Performance e Escalabilidade 📌
### [⚠️] RNF001 – Tempo de Processamento
O sistema deve processar entradas e saídas de estoque em menos de 1 segundo no p95 (95º percentil), considerando carga de até 100 usuários concorrentes em rede local ou conexão estável.

**Critérios de Aceitação:**
- Testes de carga confirmam que 95% das transações são processadas em < 1s.

**Exceções:**
- Rede instável → permitir reprocessamento automático.

### [⏳] RNF002 – Escalabilidade
O sistema deve suportar crescimento do número de produtos e transações sem perda significativa de desempenho.

**Critérios de Aceitação:**
- Testes de stress mostram degradação < 20% no tempo de resposta ao dobrar a carga.

**Exceções:**
- Carga acima da capacidade projetada → registrar log e emitir alerta.

## 📌 Usabilidade 📌
### [⏳] RNF003 – Interface Intuitiva
Interface simples, com campos de texto claros, botões de ação destacados e alertas visíveis.

**Critérios de Aceitação:**
- Teste de usabilidade com usuários apresenta taxa de sucesso > 90% para tarefas básicas (cadastrar produto, registrar venda).

**Exceções:**
- Dispositivos muito antigos → layout pode degradar, mas manter funcionalidade.

### [⏳] RNF004 – Responsividade
O sistema deve ser responsivo e funcionar corretamente em desktop, tablet e celular.

**Critérios de Aceitação:**
- Testado em navegadores modernos e tamanhos de tela comuns.

**Exceções:**
- Navegadores desatualizados (<2 versões atrás) podem não ter suporte total.

### [⏳] RNF005 – Acessibilidade
Seguir diretrizes WCAG 2.1, garantindo:
Contraste adequado
Navegação por teclado
Textos alternativos em imagens

**Critérios de Aceitação:**
- Validação automática de acessibilidade sem erros críticos.

**Exceções:**
- Funcionalidades avançadas podem ter suporte parcial, mas devem ter alternativas.

## 📌 Segurança e Conformidade 📌
### [⚠️] RNF006 – Segurança dos Dados
Garantir segurança dos dados, incluindo:
- Criptografia em trânsito (HTTPS/TLS 1.2+)
- Criptografia em repouso (senhas com hash seguro, dados sensíveis criptografados)
- Conformidade com LGPD.

**Critérios de Aceitação:**
- Testes de vulnerabilidade sem falhas críticas.
- Política de privacidade publicada.

**Exceções:**
- Falhas detectadas → devem gerar issue e correção priorizada.

### [⏳] RNF007 – Controle de Acesso
Restringir acesso a dados sensíveis (estoque, relatórios financeiros) usando RBAC (Role-Based Access Control).

**Critérios de Aceitação:**
- Usuário sem permissão não acessa dados restritos (erro 403).

**Exceções:**
- Sessão expirada → forçar logout e redirecionar para login.

### [⚠️] RNF008 – Backup e Persistência
Armazenar dados de forma persistente e permitir backup regular.
- RTO (Recovery Time Objective): < 30 min
- RPO (Recovery Point Objective): < 15 min

**Critérios de Aceitação:**
- Restauração testada com sucesso em ambiente de homologação.

**Exceções:**
- Falha no backup → alerta para administrador.

### [⏳] RNF009 – Retenção de Dados
Definir prazo de retenção para dados históricos (ex.: vendas, logs) com rotina de arquivamento e limpeza.

**Critérios de Aceitação:**
- Dados mais antigos que o prazo definido são removidos ou arquivados automaticamente.

**Exceções:**
- Dados marcados como "não apagar" por compliance devem ser preservados.

## 📌 Confiabilidade e Disponibilidade 📌
### [⚠️] RNF010 – Alta Disponibilidade
Disponibilidade mínima de 99,5% (SLA). Em caso de falha, permitir recuperação do estado dos dados.

**Critérios de Aceitação:**
- Monitoramento confirma uptime ≥ 99,5% mensal.

**Exceções:**
- Manutenções programadas comunicadas com antecedência.

### [⏳] RNF011 – Logs e Tratamento de Erros
Centralizar logs e tratamento de erros, com nível de severidade (INFO, WARN, ERROR).

**Critérios de Aceitação:**
- Erros críticos geram alerta para o time de suporte.

**Exceções:**
- A falha no log não deve impedir a operação principal.

### [⚠️] RNF012 – Monitoramento e Métricas
Disponibilizar métricas de uso e saúde do sistema (CPU, memória, latência, erros) e dashboards em tempo real.

**Critérios de Aceitação:**
- Alertas configurados para quedas de disponibilidade e picos de erro.

**Exceções:**
- Falha temporária no monitoramento não deve impactar o funcionamento do sistema.

## 📌 Arquitetura e Código 📌
### [⚠️] RNF013 – Qualidade de Código
Código-fonte organizado, modular e documentado, seguindo boas práticas (SOLID, Clean Code).

**Critérios de Aceitação:**
- Cobertura de testes unitários > 70%.
- Documentação de classes e métodos.

**Exceções:**
- Funcionalidades críticas devem ter revisão obrigatória antes do merge.

### [⚠️] RNF014 – Testes Automatizados
Cobertura mínima de testes unitários
- integração e E2E
- CI falha se cobertura < 70%.

**Critérios de Aceitação:**
- Relatório de cobertura gerado a cada build.

**Exceções:**
- Testes instáveis devem ser isolados até correção.

### [⏳] RNF015 – Integrações Futuras
Arquitetura preparada para integração com APIs externas (pagamentos, ERP).

**Critérios de Aceitação:**
- Disponibilizar endpoints RESTful desacoplados.

**Exceções:**
- Falha de integração → retorno controlado sem quebrar fluxo principal.

### [⏳] RNF016 – Internacionalização (Opcional)
Sistema preparado para tradução de textos e formatos de data/moeda.

**Critérios de Aceitação:**
- Troca de idioma sem necessidade de alteração no código.

**Exceções:**
- Idiomas não suportados → fallback para português.

## 📌 Plataforma e Infraestrutura 📌
### [⏳] RNF017 – PWA Instalável
Disponibilizar versão PWA com:
- Service Worker para funcionamento offline
- IndexedDB para armazenamento local
- Limite de armazenamento de acordo com browser

**Critérios de Aceitação:**
- Aplicação instalável e funcional offline para vendas.

**Exceções:**
- Falta de espaço no dispositivo → exibir mensagem ao usuário.

### [⏳] RNF018 – API Documentada
Disponibilizar documentação com Swagger/OpenAPI.

**Critérios de Aceitação:**
- Documentação acessível em ambiente de homologação e produção.

**Exceções:**
- Documentação fora do ar → não deve impedir uso da API.

### [⏳] RNF019 – Banco de Dados
Utilizar banco relacional (PostgreSQL), com integridade referencial e índices para performance.

**Critérios de Aceitação:**
- Consultas críticas com tempo < 500ms em p95.

**Exceções:**
- Falhas de replicação → gerar alerta.

### [⏳] RNF020 – Deploy em Nuvem
Hospedar aplicação em Railway, Render ou Vercel, com pipeline de CI/CD.
Critérios de custo e uso de recursos documentados.

**Critérios de Aceitação:**
- Deploy automático em ambiente de homologação e produção.

**Exceções:**
- Falha no pipeline → gerar notificação para devs.

### [⚠️] RNF021 – Segurança dos Dados
Proteção de dados sensíveis com:
- Criptografia em trânsito (HTTPS/TLS 1.2+) e em repouso (hash seguro de senhas).
- Conformidade com LGPD (retenção, exportação, exclusão de dados pessoais).

**Critérios de Aceitação:**
- Testes de segurança sem falhas críticas.
- Solicitações de exclusão ou exportação de dados atendidas corretamente.

**Exceções:**
- Falha de criptografia → registrar log e alertar administrador.

### [⚠️] RNF022 – Controle de Acesso
Uso de RBAC para restringir acesso a dados sensíveis e relatórios financeiros.

**Critérios de Aceitação:**
- Usuário sem permissão não acessa dados restritos.

**Exceções:**
- Sessão expirada → forçar logout.

### [⚠️] RNF023 – Requisitos de Autenticação e Autorização
Inclui autenticação segura, tokens JWT com expiração e refresh, revogação de tokens, rate limit, validação de entrada, proteção de endpoints (CORS/CSRF), audit logs e conformidade LGPD.

**Critérios de Aceitação:**
- Senhas nunca em texto puro.
- Bloqueio temporário após tentativas de login falhas.
- Logs de auditoria corretos.
- Solicitação de exclusão ou anonimização de dados atendida.

**Exceções:**
- Expiração de token → login requerido.
- Falha de email de recuperação → alertar usuário.

### [⚠️] RNF024 – Procedimentos de Segurança
Fluxos adicionais:
- Troca de senha (change password)
- Recuperação de senha (forgot/reset password)
- MFA opcional (TOTP, e-mail ou SMS) para usuários de alto privilégio

**Critérios de Aceitação:**
- Todos os fluxos testados e aprovados em QA.
- MFA funcional para administradores.

**Exceções:**
- Usuário sem e-mail cadastrado → validação manual pelo administrador.