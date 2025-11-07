
- ENTIDADES
  SystemConfig
  id — UUID
  key — String única (ex: "nome_sistema")
  value — String (valor da configuração)
  label — Nome legível (ex: "Nome do Sistema")
  category — String (ex: "geral", "aparencia", "financeiro")
  type — String (text, number, boolean, select, email)
  options — String JSON (ex: ["Escuro","Claro"])
  createdBy — UUID (usuário que criou)
  updatedBy — UUID (usuário que atualizou)
  createdAt — Timestamp
  updatedAt — Timestamp

UserConfig
id — UUID
userId — UUID (referência para User)
key — String
value — String
category — String
type — String
createdAt — Timestamp
updatedAt — Timestamp

REPOSITÓRIOS
SystemConfigRepository
findAll() → lista todas as configs globais
findByKey(String key) → busca uma config pelo nome único
findByCategory(String category) → lista configs de uma categoria
existsByKey(String key) → verifica se já existe a config
deleteByKey(String key) → remove uma config pelo nome

UserConfigRepository
findByUserId(UUID userId) → lista todas as configs do usuário
findByUserIdAndKey(UUID userId, String key) → busca config específica do usuário
existsByUserIdAndKey(UUID userId, String key) → verifica duplicidade
deleteByUserIdAndKey(UUID userId, String key) → remove config específica do usuário

SERVICES
SystemConfigService
getAllConfigs() → retorna lista completa de configs globais
getConfigByKey(String key) → retorna config específica
createConfig(CreateConfigRequest dto, User admin) → cria nova config global
updateConfig(String key, UpdateConfigRequest dto, User admin) → atualiza valor ou metadados da config
deleteConfig(String key) → exclui config global
validateKey(String key) → valida unicidade e formato da key

UserConfigService
getUserConfigs(UUID userId) → retorna todas as configs pessoais
getUserConfig(UUID userId, String key) → busca config específica
createOrUpdateUserConfig(UUID userId, UpdateConfigRequest dto) → cria ou atualiza config
deleteUserConfig(UUID userId, String key) → remove config do usuário

CONTROLLERS
SystemConfigController
Base URL: /api/configs/system

GET /api/configs/system
Retorno: Lista de SystemConfigResponse
Acesso: Somente ADMIN
Descrição: Retorna todas as configurações globais

GET /api/configs/system/{key}
Retorno: SystemConfigResponse
Acesso: Somente ADMIN
Descrição: Retorna configuração específica

POST /api/configs/system
Entrada: CreateConfigRequest
Retorno: SystemConfigResponse
Acesso: Somente ADMIN
Descrição: Cria nova configuração global

PUT /api/configs/system/{key}
Entrada: UpdateConfigRequest
Retorno: SystemConfigResponse
Acesso: Somente ADMIN
Descrição: Atualiza valor de configuração existente

DELETE /api/configs/system/{key}
Retorno: 204 No Content
Acesso: Somente ADMIN
Descrição: Remove uma configuração

UserConfigController
Base URL: /api/configs/user

GET /api/configs/user
Retorno: Lista de UserConfigResponse
Acesso: Usuário autenticado
Descrição: Retorna preferências do usuário logado

GET /api/configs/user/{key}
Retorno: UserConfigResponse
Acesso: Usuário autenticado
Descrição: Busca configuração específica do usuário

PUT /api/configs/user/{key}
Entrada: UpdateConfigRequest
Retorno: UserConfigResponse
Acesso: Usuário autenticado
Descrição: Cria ou atualiza configuração pessoal

DELETE /api/configs/user/{key}
Retorno: 204 No Content
Acesso: Usuário autenticado
Descrição: Remove uma configuração do usuário

DTOS
CreateConfigRequest
key — String
label — String
value — String
category — String
type — String
options — List<String> (opcional)

UpdateConfigRequest
value — String
label — String (opcional)
category — String (opcional)
type — String (opcional)
options — List<String> (opcional)

SystemConfigResponse
id
key
label
value
category
type
options
updatedAt

UserConfigResponse
id
key
value
category
type
updatedAt

MIGRATIONS
Migration 1 — Create Table: system_configs
Cria tabela system_configs
Define key como UNIQUE
Adiciona colunas:
id (UUID)
key
value
label
category
type
options (JSON)
created_by
updated_by
created_at
updated_at

Migration 2 — Create Table: user_configs
Cria tabela user_configs
Adiciona colunas:
id (UUID)
user_id (FK → users.id)
key
value
category
type
created_at
updated_at
Cria constraint unique (user_id, key)

Fluxo Esperado
Admins manipulam /configs/system → persistido em system_configs
Usuários manipulam /configs/user → persistido em user_configs
Frontend carrega ambos no login
Permissões via @PreAuthorize("hasRole('ADMIN')") garantem segurança




