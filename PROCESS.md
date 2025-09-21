
## 📅 PROCESS.md

# Processos de Desenvolvimento
Este documento descreve os processos e fluxos de trabalho da equipe.

## 🎯 Metodologia
Utilizamos Scrum adaptado com elementos do Kanban:

- **Sprints**: 2 semanas
- **Daily Meetings**: Diárias às 10h00
- **Planning**: Segunda-feira inicial da sprint
- **Review**: Sexta-feira final da sprint
- **Retrospective**: Após a review

## 📊 Fluxo de Trabalho

### Ciclo de Desenvolvimento

![Ciclo De Desenvolvimento](/docs/diagrams/CicloDesenvolvimento.png)

## 🔧 Definição de Pronto (DoD)

Uma task é considerada pronta quando:

- Código desenvolvido
- Testes unitários implementados
- Testes passando
- Code review aprovado
- Documentação atualizada
- Deploy em homologação
- QA aprovado

## 📈 Métricas da Sprint

- Velocity: Pontuação completada por sprint
- Burndown: Progresso diário das tasks
- Lead Time: Tempo desde o início até conclusão
- Cycle Time: Tempo em desenvolvimento ativo

## 🛠 Ferramentas
Finalidade	Ferramenta
Versionamento	GitHub
CI/CD	GitHub Actions
Gerenciamento de Tasks	Jira/Trello
Documentação	GitHub Wiki
Comunicação	Discord/Slack
Monitoramento	New Relic

## 🚀 Deploy e Releases
Fluxo de Deploy
Versionamento

Seguimos Semantic Versioning:

### MAJOR.MINOR.PATCH
- MAJOR: Mudanças incompatíveis
- MINOR: Novas funcionalidades compatíveis
- PATCH: Correções de bugs compatíveis

## 👥 Papéis e Responsabilidades
### Product Owner

- Definir prioridades do backlog
- Validar funcionalidades implementadas
- Representar os stakeholders

### Scrum Master

- Facilitar ceremonies
- Remover impedimentos
- Garantir adoção dos processos

### Desenvolvedores

- Desenvolver funcionalidades
- Escrever testes
- Realizar code reviews
- Manter documentação

## 🚨 Gestão de Incidentes

#### P1: Crítico (sistema indisponível)
#### P2: Alto (funcionalidade principal quebrada)
#### P3: Médio (funcionalidade secundária quebrada)
#### P4: Baixo (melhoria ou bug menor)

Última atualização: 21/09/2025