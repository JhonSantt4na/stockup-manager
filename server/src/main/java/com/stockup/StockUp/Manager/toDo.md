
- Auth - 
  - Rate limiting: Login sem limite de tentativas pode levar a brute-force. Integre com Spring Boot Actuator ou Bucket4j pra limitar por IP/usuário.
  - CORS/CSRF: Se for API consumida por frontend, configure globalmente, mas teste se o login aceita OPTIONS preflight.

- UserController
  - Email verification: No register, se tiver email, adicione token de confirmação (fora da sprint 1?).

- [ ] NextTask 3: Adicionar Teste de Integration com o Banco
- [ ] NextTask 2: Adicionar Testes Unitary nas classes (Auth, User, Roles)
- [ ] Implementar o gitHub actions passando nos testes;

Auth -> PASS
Permission -> PASS
Role -> ( list, with-with-list )
User -> ( all endpoints )