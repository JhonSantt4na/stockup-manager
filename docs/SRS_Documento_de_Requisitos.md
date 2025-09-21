# Especificação de Requisitos de Software
## Para StockUp Manager

**Versão 0.1**  
**Preparado por JhonSantt4na**  
**Domingo, 21 de Setembro de 2025**  

Índice
=================
* [Histórico de Revisões](#histórico-de-revisões)

* 1.0 [Introdução](#1-introdução)
  * 1.1 [Propósito do Documento](#11-propósito-do-documento)
  * 1.2 [Escopo do Produto](#12-escopo-do-produto)

* 2.0 [Visão Perspectiva do Produto](#20-Visão-e-Perspectiva-do-Produto)
  * 2.1 [Funções do Produto](#21-funções-do-produto)
  * 2.2 [Restrições do Produto](#22-restrições-do-produto)

* 3.0 [Requisitos](#3-requisitos)

  * 3.1 [Requisitos Funcionais](#31-Funcionais)
    * 3.1.1 [Gestão de Produtos](#311-Gestão-de-Produtos)
    * 3.1.2 [Controle de Estoque](#312-Controle-de-Estoque)
    * 3.1.3 [Ponto de Venda](#313-Ponto-de-Venda )
    * 3.1.4 [Relatórios e Consultas](#314-Relatórios-e-Consultas)
    * 3.1.5 [Operação Offline e Sincronização](#315-Operação-Offline-e-Sincronização)
    * 3.1.6 [Autenticação e Autorização](#316-Autenticação-e-Autorização)

  * 3.2 [Requisitos Não Funcionais](#32-Requisitos-Não-Funcionais)
    * 3.2.1 [Desempenho](#321-desempenho)
    * 3.2.2 [Segurança](#322-segurança)
    * 3.2.3 [Confiabilidade](#323-confiabilidade)
    * 3.2.4 [Disponibilidade e Conformidade](#324-Disponibilidade-e-Conformidade)
    * 3.2.5 [Usabilidade](#325-Usabilidade)
  
  * 3.3 [Outros Requisitos de Qualidade](#33-Outros-Requisitos-de-Qualidade)

* 4.0 [Projeto e Implementação](#40-Projeto-e-Implementação)
    * 4.1 [Instalação](#41-Instalação)
    * 4.2 [Distribuição](#42-Distribuição)
    * 4.3 [Manutenibilidade](#43-Manutenibilidade)
    * 4.4 [Prova de Conceito](#44-Prova-de-Conceito)

* 5.0 [Verificação](#50-Verificação)

## Histórico de Revisões
| Nome   | Data     |   Motivo das Alterações     |  Versão   |
| ------ | -------- | --------------------------- | --------- |
|santt4na| 21/09/25 |Padronização e implementação | 0.0.1     |
|        |          |                             |           |
|        |          |                             |           |

## 1. Introdução
Fornecer um sistema web PWA para pequenas lojas que permita controle de estoque e vendas em tempo real, geração de relatórios diários, e operação offline com sincronização. MVP não contempla integração fiscal (NF-e) — apenas recibo não fiscal substituindo o famoso caderno de anotações.

### 1.1 Propósito do Documento
O propósito desta Especificação de Requisitos de Software (SRS) é documentar de forma clara e abrangente os requisitos funcionais e não funcionais para o desenvolvimento de um sistema web PWA destinado a pequenas lojas. Este sistema visa fornecer controle de estoque e vendas em tempo real, geração de relatórios diários e operação offline com sincronização posterior, substituindo métodos manuais como cadernos de anotações por um recibo não fiscal simples. O documento serve como base para o planejamento, implementação, teste e validação do MVP (Minimum Viable Product), garantindo que todas as partes envolvidas tenham uma compreensão comum das expectativas e funcionalidades do sistema.
O público-alvo inclui:

Equipe de Desenvolvimento: Para guiar a implementação técnica e garantir conformidade com os requisitos.
- Equipe de Testes: Para definir critérios de aceitação e validar o sistema.
- Gerentes de Projeto e Stakeholders: Para monitorar o progresso e alinhar expectativas.
- Usuários Finais (Donos de Pequenas Lojas): Para entender as funcionalidades principais e fornecer feedback durante o desenvolvimento.
- Equipe de Manutenção: Para futuras atualizações e integrações, como suporte a NF-e no pós-MVP.

### 1.2 Escopo do Produto
Este documento especifica os requisitos para o "Sistema PWA de Controle de Estoque e Vendas" (versão 1.0, MVP), um aplicativo web progressivo projetado para pequenas lojas. O produto permitirá o gerenciamento de estoque, registro de vendas em tempo real ou offline, e geração de relatórios diários simples, sem integração fiscal (NF-e) no MVP. Ele substituirá anotações manuais por recibos não fiscais, visando aumentar a eficiência operacional, reduzir erros e fornecer insights rápidos sobre vendas e estoque.
O software alinha-se a metas corporativas de democratizar ferramentas digitais para microempresas, promovendo crescimento sustentável ao otimizar processos diários e preparar para expansões futuras, como integrações com sistemas fiscais. Para visão detalhada, consulte o documento separado de Visão e Escopo (se disponível).

### 2.0 Visão e Perspectiva do Produto
O Sistema é uma solução independente e autônoma desenvolvida especificamente para micro e pequenas empresas do varejo. O produto surge como uma alternativa digital aos sistemas tradicionais de gestão que são frequentemente complexos e caros para pequenos negócios, substituindo métodos manuais como cadernos de anotações e planilhas desestruturadas.

O sistema operará como uma aplicação web progressiva (PWA) que pode ser acessada através de navegadores modernos em diversos dispositivos (computadores, tablets e smartphones), sem necessidade de instalação complexa. No contexto mais amplo do ecossistema de negócios, este produto se posiciona como uma ponte entre métodos totalmente manuais e sistemas ERP completos, oferecendo apenas funcionalidades essenciais para gestão básica de operações.

### 2.1 Funções do Produto
As principais funções do produto, em alto nível, são:

- Gestão de Cadastros: Permitir o cadastro, edição, exclusão e consulta de produtos (com nome, código, preço e quantidade em estoque).

- Registro de Vendas (PDV): Fornecer uma interface simples para registrar vendas, aplicando descontos se necessário, e emitir um recibo não fiscal.

- Controle de Estoque em Tempo Real: Atualizar automaticamente o nível de estoque após cada venda e fornecer alertas visuais para produtos com baixa quantidade.

- Operação Offline com Sincronização: Permitir que todas as funcionalidades principais (cadastro e vendas) funcionem sem conexão com a internet, sincronizando os dados automaticamente quando a conexão for restabelecida.

- Geração de Relatórios: Gerar relatórios diários simples de vendas (total vendido, quantidade de transações) e lista de produtos mais vendidos.

### 2.2 Restrições do Produto
- Interface do Usuário: O sistema deve ser uma Aplicação Web Progressiva (PWA), necessitando de uma interface responsiva e adaptável a dispositivos móveis e desktop, sem a necessidade de lojas de aplicativos.

- Interoperabilidade: Deve operar em navegadores web modernos (Chrome, Edge, Firefox, Safari) e ser compatível com diferentes sistemas operacionais (Android, iOS, Windows, macOS) através do navegador.

- Restrições de Qualidade de Serviço (QoS): O sistema deve ser capaz de funcionar offline indefinidamente, armazenando dados localmente com confiabilidade. A sincronização de dados ao retornar ao online deve ser robusta e evitar conflitos.

- Conformidade com Padrões: A aplicação deve seguir os padrões web (HTML5, CSS3, JavaScript ES6+) e utilizar Service Workers para funcionalidade offline, conforme definido pelo W3C.

- Restrições de Implementação: O MVP não contempla integração com sistemas fiscais (emissão de NF-e). A geração de recibos é de caráter não fiscal, servindo apenas como comprovante interno para o cliente e controle da loja.

## 3 Requisitos

### 3.1 Funcionais
Nesta seção, são apresentados os requisitos funcionais do software, que definem as capacidades e comportamentos esperados do sistema em seu ambiente operacional. Esses requisitos descrevem as funcionalidades principais, incluindo as interações com usuários, processamento de dados, integrações com outros sistemas e respostas a entradas específicas, garantindo que o software atenda aos objetivos de negócio e às necessidades dos stakeholders. Cada requisito é especificado de forma clara, mensurável e verificável, priorizando aspectos como usabilidade, desempenho e conformidade com padrões.

### 3.1.1 Gestão de Produtos

- **RF001** - Cadastro de Produto: O sistema deve permitir o cadastro de produtos com os campos: nome, NCM, SKU (único), preço de venda, preço de custo, estoque inicial, categoria e estoque mínimo. Deve validar campos obrigatórios e impedir SKUs duplicados.

- **RF002** - Remoção e Atualização de Produto: O sistema deve permitir a edição de dados do produto e sua remoção (exclusão lógica). A exclusão deve ser impedida se o produto estiver vinculado a uma venda.

- **RF003** – Listagem e Paginação: O sistema deve exibir uma listagem de produtos com busca (por nome, categoria ou código) e paginação utilizando os parâmetros page, size e sort.

- **RF004** – Atualização de Dados: O sistema deve permitir ajustes de estoque e edição de informações do produto, registrando um histórico de atualizações.

- **RF005** – Persistência de Dados: O sistema deve armazenar todas as informações em um banco de dados para consulta futura e consistência após reinícios.

#### 3.1.2 Controle de Estoque

- **RF006** – Consulta de Saldo: O sistema deve exibir o saldo de estoque atualizado para cada produto.

- **RF007** – Registro de Entradas e Saídas: O sistema deve registrar movimentações de estoque do tipo IN (entrada/compra) e OUT (saída/venda), refletindo-as imediatamente no saldo.

- **RF008** – Ajuste Manual de Estoque: O sistema deve permitir ajustes manuais (ADJUST) que exijam um motivo obrigatório para a operação.

- **RF008** – Histórico de Movimentações: O sistema deve exibir um histórico filtrável de todas as movimentações (entradas, saídas, ajustes) por período.

- **RF010** – Alerta de Baixo Estoque: O sistema deve gerar alertas visuais quando a quantidade em estoque de um produto for inferior ao seu estoque mínimo definido.

- **RF011** – Registro de Transações: O sistema deve salvar cada transação com data, tipo, quantidade e identificação do produto.

- **RF012** – Visão Geral do Estoque: O sistema deve exibir uma visão consolidada com produtos, quantidades atuais, mínimas e alertas de reposição.

#### 3.1.3 Ponto de Venda

- **RF013** – Busca de Produto: O sistema deve permitir a busca de produtos em tempo real por nome ou código de barras para composição de uma venda.

- **RF014** – Adição à Venda: O sistema deve permitir adicionar produtos à venda informando a quantidade, desde que não exceda o estoque disponível.

- **RF015** – Lista de Itens: O sistema deve exibir a lista de itens da venda corrente com o cálculo correto do subtotal.

- **RF016** – Forma de Pagamento: O sistema deve permitir a seleção da forma de pagamento (dinheiro, cartão, PIX, vale) e registrar essa informação no fechamento da venda.

- **RF017** – Atualização de Estoque: O sistema deve atualizar o estoque em tempo real imediatamente após a confirmação de uma venda, garantindo consistência (opera atômica).

- **RF018** – Registro de Venda: O sistema deve criar um registro persistente da venda e de seus itens no backend ao finalizar a transação.

- **RF019** – Comprovante de Venda: O sistema deve gerar um comprovante de venda (não fiscal) em formato HTML ou PDF para impressão ou envio ao cliente.

#### 3.1.4 Relatórios e Consultas

- **RF020** – Relatório de Movimentações: O sistema deve gerar um relatório com todas as movimentações de estoque em um período filtrável.

- **RF021** – Relatório Diário de Vendas: O sistema deve gerar um relatório diário opcional, sumarizando produtos vendidos, quantidades, totais e formas de pagamento.

- **RF022** – Produtos Mais Vendidos: O sistema deve gerar um relatório com o ranking de produtos mais vendidos em um período.

- **RF023** – Exibição Sob Demanda: O sistema deve permitir a geração e exibição de relatórios sob demanda, a qualquer momento.

#### 3.1.5 Operação Offline e Sincronização

- **RF024** – Vendas Offline: O sistema deve permitir o registro completo de vendas sem conexão com a internet, armazenando-as localmente e sincronizando-as automaticamente com o servidor quando a conexão for restabelecida. Deve implementar uma estratégia de resolução de conflitos (e.g., last-write-wins) e uma fila de sincronização robusta.

### 3.1.6 Autenticação e Autorização

- **RF025** – Login de Usuário: O sistema deve permitir a autenticação de usuários via JWT ou OAuth2 Password Flow.

- **RF026** – Controle de Acesso: O sistema deve implementar controle de acesso baseado em perfis (ex: Admin - acesso total; Vendedor - apenas PDV).

- **RF027** – Auditoria e Log: O sistema deve registrar em log eventos relevantes (cadastros, vendas, ajustes, erros) para auditoria, incluindo data/hora, usuário e ação realizada.

### 3.2 Requisitos Não Funcionais

#### 3.2.1. Desempenho

- **RNF001** – Tempo de Processamento: O sistema deve processar entradas e saídas de estoque em menos de 1 segundo no p95 (95º percentil), considerando carga de até 100 usuários concorrentes em rede local ou conexão estável.

- **RNF002** – Escalabilidade: O sistema deve suportar crescimento do número de produtos e transações sem perda significativa de desempenho (degradação < 20% no tempo de resposta ao dobrar a carga).

- **RNF003** - Tempo de Resposta: A interface do PDV deve responder a interações do usuário (busca, adição de produto) em menos de 200ms para garantir fluidez durante as vendas.

- **RNF004** - Sincronização: O processo de sincronização de dados realizados offline deve ser concluído em segundo plano em até 5 minutos após a reconexão, sem bloquear a interface do usuário.

- **RNF005** - Carga de Dados: A listagem de produtos deve ser carregada e exibida, mesmo com centenas de itens cadastrados, em menos de 2 segundos.

#### 3.2.2. Segurança

- **RNF006** – Segurança dos Dados: Garantir segurança dos dados, incluindo criptografia em trânsito (HTTPS/TLS 1.2+) e em repouso, em conformidade com a LGPD.

- **RNF007** – Controle de Acesso: Restringir acesso a dados sensíveis usando RBAC (Role-Based Access Control). Usuário sem permissão não acessa dados restritos (deve retornar erro 403).

- **RNF008** – Segurança dos Dados (Complementar): Proteção de dados sensíveis com criptografia, conformidade com LGPD (retenção, exportação, exclusão de dados pessoais).

- **RNF009** – Requisitos de Autenticação e Autorização: Inclui autenticação segura, tokens JWT com expiração e refresh, revogação de tokens, rate limit, validação de entrada, proteção de endpoints (CORS/CSRF), audit logs e conformidade LGPD.

- **RNF010** - Autenticação: Todas as requisições às APIs, exceto a de login, devem exigir um token JWT válido.

- **RNF011** - Proteção de Dados: Dados sensíveis, como preços e informações de vendas, devem trafegar exclusivamente por conexões criptografadas (HTTPS/TLS).

- **RNF012** - Privacidade: Os dados armazenados localmente (offline) no dispositivo do usuário devem ser protegidos contra acesso não autorizado, preferencialmente utilizando mecanismos de segurança do navegador ou criptografia.

#### 3.2.3. Confiabilidade

- **RNF013** – Backup e Persistência: Armazenar dados de forma persistente e permitir backup regular. RTO (Recovery Time Objective) < 30 min; RPO (Recovery Point Objective) < 15 min.

- **RNF014** – Alta Disponibilidade: Garantir disponibilidade mínima de 99,5% (SLA). Em caso de falha, permitir recuperação do estado dos dados.

- **RNF015** – Logs e Tratamento de Erros: Centralizar logs e tratamento de erros, com nível de severidade (INFO, WARN, ERROR). Erros críticos devem gerar alerta para o time de suporte.

- **RNF016** – Monitoramento e Métricas: Disponibilizar métricas de uso e saúde do sistema (CPU, memória, latência, erros) e dashboards em tempo real.

- **RNF017** - Tolerância a Falhas (Offline): O sistema deve ser capaz de operar ininterruptamente em modo offline por períodos prolongados (ex: um turno comercial de 8 horas) sem perda de dados ou corrupção do armazenamento local.

- **RNF018** - Integridade de Dados: O sistema deve garantir a integridade dos dados durante a sincronização, prevenindo a criação de registros duplicados ou inconsistentes (ex: venda registrada, mas estoque não baixado).

#### 3.2.4. Disponibilidade e Conformidade

- **RNF019** – Backup e Persistência: Armazenar dados de forma persistente e permitir backup regular. RTO (Recovery Time Objective) < 30 min; RPO (Recovery Point Objective) < 15 min.

- **RNF020** – Alta Disponibilidade: Garantir disponibilidade mínima de 99,5% (SLA). Em caso de falha, permitir recuperação do estado dos dados.

- **RNF021** – Logs e Tratamento de Erros: Centralizar logs e tratamento de erros, com nível de severidade (INFO, WARN, ERROR). Erros críticos devem gerar alerta para o time de suporte.

- **RNF022** – Monitoramento e Métricas: Disponibilizar métricas de uso e saúde do sistema (CPU, memória, latência, erros) e dashboards em tempo real.

- **RNF023** - Operação Contínua: A funcionalidade central de PDV deve estar disponível 100% do tempo, garantida pela operação offline. A disponibilidade dos serviços online (sincronização, relatórios) deve seguir o SLA definido para o servidor (ex: 99.5%).

- **RNF024** - Padrões de Desenvolvimento: O frontend deve aderir aos padrões PWA (Progressive Web App) para garantir funcionamento offline e experiência similar a um aplicativo.

- **RNF025** - Rastreamento de Auditoria: O sistema deve manter um log de auditoria de todas as operações críticas (alterações de estoque, vendas, ajustes) conforme especificado no RF027, atendendo a boas práticas de rastreabilidade.

- **RNF035** - Formato de Recibo: O comprovante de venda não fiscal (RF019) deve conter, no mínimo: nome da loja, data/hora da transação, lista de itens com valores, totais e forma de pagamento, atendendo a convenções básicas de recibo.

#### 3.2.5 Usabilidade

- **RNF027** – Interface Intuitiva: Interface simples, com campos de texto claros, botões de ação destacados e alertas visíveis. Teste de usabilidade deve apresentar taxa de sucesso > 90% para tarefas básicas.

- **RNF028** – Responsividade: O sistema deve ser responsivo e funcionar corretamente em desktop, tablet e celular.

- **RNF029** – Acessibilidade: Seguir diretrizes WCAG 2.1, garantindo contraste adequado, navegação por teclado e textos alternativos em imagens.

### 3.3 Outros Requisitos de Qualidade

- **RNF030** – Retenção de Dados: Definir prazo de retenção para dados históricos (ex.: vendas, logs) com rotina de arquivamento e limpeza.

- **RNF031** – Qualidade de Código: Código-fonte organizado, modular e documentado, seguindo boas práticas (SOLID, Clean Code). Cobertura de testes unitários > 70%.

- **RNF032** – Testes Automatizados: Cobertura mínima de testes unitários, integração e E2E. CI deve falhar se cobertura < 70%.

- **RNF033** – Integrações Futuras: Arquitetura preparada para integração com APIs externas (pagamentos, ERP).

- **RNF034** – Internacionalização (Opcional): Sistema preparado para tradução de textos e formatos de data/moeda.

- **RNF035** – API Documentada: Disponibilizar documentação com Swagger/OpenAPI.

- **RNF036** – Banco de Dados: Utilizar banco relacional (PostgreSQL), com integridade referencial e índices para performance. Consultas críticas com tempo < 500ms em p95.

- **RNF037** – Deploy em Nuvem: Hospedar aplicação em Railway, Render ou Vercel, com pipeline de CI/CD. Critérios de custo e uso de recursos documentados.

### 4.0 Projeto e Implementação

#### 4.1 Instalação
-   O software deve ser implantado como uma **PWA (Progressive Web App)**, acessível via navegadores web modernos (Chrome 90+, Edge 90+, Firefox 88+, Safari 14+) sem necessidade de instalação complexa.
-   O backend deve ser implantado em uma plataforma de nuvem (ex: Railway, Render, Vercel) com suporte a Node.js/Linux.
-   O banco de dados (PostgreSQL) deve estar acessível para o backend a partir da mesma nuvem ou de um serviço gerenciado (ex: AWS RDS, Supabase).
-   A instalação e configuração do ambiente devem ser automatizadas via scripts ou pipeline CI/CD, sem dependência de intervenção manual para garantir consistência.

#### 4.2 Distribuição
-   A arquitetura deve ser **centralizada**, com um único backend na nuvem responsável por servir a aplicação web (frontend PWA) e gerenciar o banco de dados.
-   Os dados são processados centralmente no backend, mas a aplicação frontend deve ser capaz de operar de forma **distribuída e offline** em múltiplos dispositivos (celulares, tablets) de usuários, sincronizando dados com o servidor central quando online.
-   A distribuição da aplicação para os usuários finais se dá via URL, tornando-a instantaneamente acessível em qualquer dispositivo compatível.

#### 4.3 Manutenibilidade
-   O código-fonte deve ser **modularizado** por funcionalidades (ex: módulo de produtos, módulo de vendas) para permitir manutenção e evolução isolada.
-   A complexidade ciclomática do código deve ser mantida baixa (limite recomendado de 15 por função/método) para facilitar testes e compreensão.
-   As APIs devem seguir padrões RESTful bem definidos e ser **devidamente documentadas** (OpenAPI/Swagger) para simplificar integrações futuras e debugging.
-   A base de código deve incluir um conjunto abrangente de **testes automatizados** (unitários, de integração) que permitam refatorações com confiança.

#### 4.4 Prova de Conceito
-  Antes do desenvolvimento completo, uma PoC deve ser desenvolvida para validar a **arquitetura de sincronização offline/online**, que é crítica para o negócio.
-   A PoC deve demonstrar:
    -   O registro de uma venda em modo offline.
    -   O armazenamento local seguro dessa venda.
    -   A sincronização automática e bem-sucedida dos dados com o servidor ao restabelecer a conexão.
    -   A resolução básica de conflitos (ex: last-write-wins).
-   A PoC também deve validar o desempenho básico da aplicação PWA em dispositivos móveis de baixo custo.

## 5.0 Verificação
A qualificação do software será realizada através dos seguintes métodos de verificação, alinhados aos requisitos da Seção 3:

| **Método de Verificação**       | **Aplicação**                                                                                               | **Critério de Êxito**                                                                                                                              |
| :------------------------------ | :---------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Revisão de Documentação**     | Verificar se os requisitos (Seção 3) são claros, completos, não ambíguos e testáveis.                       | Documento SRS aprovado em revisão formal com stakeholders.                                                                                         |
| **Testes Unitários**            | Verificar a lógica interna de funções/métodos individuais (ex: cálculo de total da venda, validação de SKU). | Cobertura de testes ≥ 70% para código crítico. Todos os testes unitários passam (sucesso 100%).                                                      |
| **Testes de Integração**        | Verificar a interação entre módulos (ex: PDV → Atualização de Estoque) e com o banco de dados.               | Todos os fluxos integrados funcionam conforme esperado. Dados são persistidos e recuperados corretamente.                                          |
| **Testes de Sistema (E2E)**     | Verificar os requisitos funcionais e não-funcionais do sistema completo, simulando o uso real.               | Todos os cenários de uso principal (Happy Path) e de exceção são executados com sucesso. Os requisitos de desempenho (RNF001, RNF002) são atendidos. |
| **Testes de Usabilidade**       | Verificar os requisitos de usabilidade (RNF027 - RNF029) com usuários reais.                                  | Taxa de sucesso > 90% em tarefas básicas (cadastrar produto, registrar venda) em testes com 5+ usuários.                                            |
| **Testes de Segurança**         | Verificar requisitos de segurança (RNF006-RNF012) via análise estática e testes dinâmicos.          | Não são encontradas vulnerabilidades críticas em ferramentas de scan (ex: OWASP ZAP). Tokens e dados sensíveis são manipulados com segurança.       |
| **Testes de Offline/Sincronização** | Verificar especificamente o RF024 e a usabilidade em condições de rede instável ou inexistente.            | Todas as operações são executadas offline. Os dados são sincronizados corretamente ao voltar ao online, e conflitos são resolvidos conforme definido. |
| **Testes de Carga e Desempenho**| Verificar requisitos de desempenho (RNF001, RNF002) e escalabilidade.                                        | 95% das transações são processadas em < 1s sob carga de 100 usuários. A aplicação se degrada gracefulmente sob carga excessiva.                    |
| **Verificação de Conformidade** | Verificar atendimento a restrições de conformidade (Seção 3.2.4).                                              | A PWA é instalável e funciona offline. Os logs de auditoria (RF027) são gerados. O comprovante contém todas as informações exigidas.               |