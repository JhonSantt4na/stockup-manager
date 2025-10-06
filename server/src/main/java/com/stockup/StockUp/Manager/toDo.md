- Ajustar os Testes

- Auth - 
  - Rate limiting: Login sem limite de tentativas pode levar a brute-force. Integre com Spring Boot Actuator ou Bucket4j pra limitar por IP/usuário.
  - CORS/CSRF: Se for API consumida por frontend, configure globalmente, mas teste se o login aceita OPTIONS preflight.

- UserController
  - Email verification: No register, se tiver email, adicione token de confirmação (fora da sprint 1).

- Adicionar Teste de Integration com o Banco
- Adicionar Testes Unitary nas classes controller (Auth, User, Roles)
- Implementar o gitHub actions passando nos testes;