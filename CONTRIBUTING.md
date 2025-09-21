# Guia de Contribuição

Obrigado por seu interesse em contribuir com nosso projeto! Este documento fornece diretrizes e padrões para contribuições.

## 🚀 Primeiros Passos

### Pré-requisitos
- Node.js 16+ (para frontend)
- Java 17+ (para backend)
- Git 2.25+
- Sua IDE preferida (VS Code, IntelliJ, Eclipse)

### Configuração do Ambiente
1. Faça um fork do repositório
2. Clone seu fork: `git clone https://github.com/seu-usuario/nome-do-projeto.git`
3. Instale as dependências:
```bash
# Backend
cd backend
mvn clean install

# Frontend
cd frontend
npm install
```
#### Configure as variáveis de ambiente (copie .env.example para .env)

## 🔧 Processo de Desenvolvimento
### Convenção de Branches
- **main :** Branch de produção (só recebe merges via PR)
- **develop :** Branch de desenvolvimento principal
- **feature/*:** Novas funcionalidades (ex: feature/add-user-auth)
- **fix/*:** Correções de bugs (ex: fix/stock-calculation)
- **hotfix/*:** Correções urgentes para produção
- **docs/*:** Melhorias na documentação

### Convenção de Commits
Seguimos o padrão Conventional Commits:

```md
<tipo>(escopo): descrição breve

[corpo opcional]

[rodapé(s) opcional]
```
#### Tipos permitidos:

- feat: Nova funcionalidade
- fix: Correção de bug
- docs: Alterações na documentação
- style: Mudanças de formatação (espaços, vírgulas)
- refactor: Refatoração de código
- test: Adição ou correção de testes
- chore: Tarefas de manutenção

**Exemplos:**

```bash
git commit -m "feat(auth): add JWT authentication"
git commit -m "fix(stock): correct inventory calculation"
git commit -m "docs: update API documentation"
```

## ✅ Critérios de Aceitação

- Código segue os padrões do projeto
- Testes passam localmente
- Testes adicionados para novas funcionalidades
- Documentação atualizada
- Não quebra builds existentes

## 🧪 Testes
**Executando Testes**
```bash
# Backend
mvn test

# Frontend
npm test

# Com cobertura
mvn jacoco:report
npm run test:coverage
```
### Padrões de Teste

- Use nomes descritivos para testes
- Siga o padrão AAA (Arrange, Act, Assert)
- Mock dependências externas
- Teste casos de sucesso e erro

### 🐛 Reportando Bugs

- Use o template de issue
- Descreva o comportamento esperado vs atual
- Inclua steps para reproduzir
- Adicione screenshots ou logs relevantes
- Especifique versão do sistema e ambiente

### 💡 Sugestões de Melhoria

    - Descreva a feature ou melhoria
    - Explique o benefício para o projeto
    - Inclui exemplos de uso quando aplicável

### 📞 Dúvidas?

    Discord: 
    Email: jhonnSantt4na@gmail.com
    Issues: Use a tag "question"

# Agradecemos sua contribuição! ✨