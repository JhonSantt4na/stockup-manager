# 1. Conectar e limpar o banco manualmente
docker exec -it stockup-postgres psql -U stockup_user -d stockup -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

# 2. só rodar o migrate
./mvnw flyway:migrate -Dflyway.url=jdbc:postgresql://localhost:5433/stockup -Dflyway.user=stockup_user -Dflyway.password=stockup_pass