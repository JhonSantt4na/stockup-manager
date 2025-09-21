# Documento de Arquitetura de Software (DAS)

## 1. Introdução

### 1.1. Propósito
Este documento descreve a arquitetura do Sistema de Gerenciamento de Estoque e Vendas, fornecendo uma visão abrangente das decisões arquiteturais, componentes e padrões utilizados no desenvolvimento.

### 1.2. Escopo
O documento cobre a arquitetura do sistema completo, incluindo backend, frontend, banco de dados e estratégias de implantação.

### 1.3. Público-Alvo
- Equipe de desenvolvimento
- Arquitetos de software
- Gerentes de projeto
- Stakeholders técnicos

## 2. Representação da Arquitetura

### 2.1. Diagrama de Implantação

![Diagrama de Implantação](/docs/diagrams/DiagramaImplantação.png)

### 2.2. Diagrama de Componentes

![Diagrama de Componentes](/docs/diagrams/DiagramaComponentes.png)

## 3. Metas e Restrições da Arquitetura

### 3.1. Metas
- Alta disponibilidade (99.5% SLA)
- Tempo de resposta inferior a 1 segundo
- Operação offline com sincronização
- Escalabilidade horizontal
- Segurança de dados

### 3.2. Restrições
- Compatibilidade com navegadores modernos
- Conformidade com LGPD
- Limite de orçamento para infraestrutura
- Compatibilidade com dispositivos móveis

## 4. Visão de Caso de Uso

### 4.1. Casos de Uso Principais
1. **Registro de Venda**: Vendedor registra venda com atualização automática de estoque
2. **Gestão de Produtos**: Administrador gerencia cadastro de produtos
3. **Relatórios**: Gerente gera relatórios de desempenho
4. **Sincronização Offline**: Sistema sincroniza dados quando conexão retorna

### 4.2. Diagrama de Casos de Uso

![Casos de Uso](/docs/diagrams/CasosDeUso.png)

## 5. Visão Lógica

### 5.1. Diagrama de Classes (Simplificado)

![Diagrama de Classe](/docs/diagrams/DiagramaClasse.png)

## 6. Visão de Processo

### 6.1. Fluxo de Registro de Venda

![Fluxo de Registro de Venda](/docs/diagrams/FluxoVendas.png)

### 6.2. Fluxo de Sincronização Offline

![Fluxo de Sincronização Offline](/docs/diagrams/FluxoOffline.png)

## 7. Visão de Implantação

### 7.1. Especificações Técnicas
- **Aplicação Backend**: 2+ instâncias (2 vCPU, 4GB RAM cada)
- **Banco de Dados**: PostgreSQL 12+ (4 vCPU, 16GB RAM, SSD)
- **Cache**: Redis (1 vCPU, 2GB RAM)
- **Frontend**: CDN com armazenamento estático

## 8. Visão de Implementação

### 8.1. Estrutura de Pacotes (Backend)
```
com.estoque.vendas/
├── config/
├── controller/
├── service/
├── repository/
├── model/
├── dto/
├── exception/
├── security/
└── util/
```

### 8.2. Estrutura de Pastas (Frontend)
```
src/
├── app/
│   ├── components/
│   ├── services/
│   ├── models/
│   ├── guards/
│   ├── interceptors/
│   └── utils/
├── assets/
└── environments/
```

## 9. Dimensionamento e Performance

### 9.1. Requisitos de Performance
- Tempo de resposta API: < 500ms (p95)
- Suporte a 100 usuários concorrentes
- Processamento de 1000 vendas/hora
- Sincronização offline em < 5 minutos

### 9.2. Estratégias de Otimização
- Cache de consultas frequentes
- Paginação de resultados
- Indexação adequada do banco
- Compressão de dados
- Conexões persistentes

## 10. Decisões Arquiteturais

### 10.1. Padrões Utilizados
- **RESTful API**: Para comunicação entre frontend e backend
- **MVC**: Para organização do código frontend
- **Repository Pattern**: Para abstração de acesso a dados
- **JWT**: Para autenticação stateless
- **PWA**: Para funcionamento offline

### 10.2 Tratamento de Exceptions
As exceções serão usadas para controlar erros específicos que podem acontecer durante a execução do sistema, como tentar cadastrar um produto com dados inválidos ou tentar vender um produto que não tem estoque suficiente.

```java
ProdutoNotFoundException : extends RuntimeException 
	@ResponseStatus(HttpStatus.NOT_FOUND)

EstoqueInsuficienteException : extends RuntimeException 
	@ResponseStatus(HttpStatus.BAD_REQUEST)

MovimentacaoInvalidException : extends RuntimeException 
```

### 10.3. Tecnologias Selecionadas
| Camada | Tecnologia | Justificativa |
|--------|------------|---------------|
| Backend | Spring Boot | Ecossistema robusto, produtividade |
| Frontend | Angular | PWA nativo, estrutura sólida |
| Banco | PostgreSQL | ACID, JSONB, confiabilidade |
| Cache | Redis | Performance, estruturas de dados ricas |

# 10.4. Logs do sistema

O sistema deve usar o  SLF4J com Logback ou Log4j2 para adicionar logs na aplicação.

## **Níveis de log:**  
- TRACE → Detalhes muito finos (debugging em desenvolvimento).
- DEBUG → Informações de depuração, dados de entrada/saída.
- INFO → Eventos normais de execução (ex: "Product created successfully").
- WARN → Algo inesperado, mas que não quebrou o sistema.
- ERROR → Erros ou exceções que afetam o funcionamento.

**No Controller:**
- Será Logado apenas em eventos de entrada e saída, útil para auditoria:
Quando um endpoint é chamado.
Parâmetros principais recebidos (cuidado para não logar dados sensíveis).
Resultado ou status retornado.

**No Service:** 
- Será Logado os eventos de negócio e exceções:
Início e fim de métodos importantes.
- Decisões de negócio (ex: "Stock level too low, cannot process sale").
- Captura de exceções antes de propagá-las.
- No Exception Handler (Global) → Logue stacktraces de erros críticos.

Usaremos um **@ControllerAdvice** para centralizar tratamento de exceções e logar.

**Onde NÃO logar** ?
- Não logue demais — excesso de logs gera ruído e pode impactar performance.
- Não logue dados sensíveis (senhas, tokens JWT, dados de cartão de crédito).
- Não use log dentro de loops intensos sem necessidade.

**Logs Permitidos :**
- Log no início e no fim dos métodos do Service (DEBUG/INFO)
- Log no Controller apenas quando entrar e sair do endpoint (INFO)
- Log erros no nível ERROR (com stacktrace) Contextualize logs com IDs, nomes de recursos, etc., para facilitar o rastreamento.

```java
private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

logger.info("Cadastrando produto: {}", produto.getNome());
```
## 11. Riscos e Mitigação

### 11.1. Riscos Técnicos
- **Perda de dados offline**: Mitigado com confirmação de sincronização
- **Conflitos de concorrência**: Mitigado com transações e locks otimistas
- **Vulnerabilidades de segurança**: Mitigado com revisões de código e testes

### 11.2. Riscos de Performance
- **Degradação com crescimento**: Mitigado com escalabilidade horizontal
- **Problemas de sincronização**: Mitigado com filas e retry mechanism

## 12. Referências

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação Angular](https://angular.io/docs)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Padrões PWA](https://web.dev/progressive-web-apps/)

**Última atualização**: 18/09/2025  
**Versão do Documento**: 1.0