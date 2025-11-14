- Ajustar os Testes

- Auth - 
  - Rate limiting: Login sem limite de tentativas pode levar a brute-force. Integre com Spring Boot Actuator ou Bucket4j pra limitar por IP/usuário.
  - CORS/CSRF: Se for API consumida por frontend, configure globalmente, mas teste se o login aceita OPTIONS preflight.

- UserController
  - Email verification: No register, se tiver email, adicione token de confirmação (fora da sprint 1).

- Adicionar Teste de Integration com o Banco
- Adicionar Testes Unitary nas classes controller (Auth, User, Roles)
- Implementar o gitHub actions passando nos testes;




-product
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    private String nome;
    private String codigoBarras;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private String ncm;
    private String cfop;
    private String cst;
    private Boolean ativo;

    // Relacionamento
    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
    private Stock estoque;
}



- Stock
  @Entity
  @Table(name = "stocks")
  public class Stock {

  @Id
  @GeneratedValue
  private UUID id;

  @OneToOne
  @JoinColumn(name = "produto_id", nullable = false)
  private Product produto;

  private BigDecimal quantidadeAtual;
  private BigDecimal quantidadeMinima;
  private String localizacao;
  private LocalDate dataUltimaEntrada;
  private LocalDate dataUltimaSaida;
  }


stock moviment

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "estoque_id")
    private Stock estoque;

    private BigDecimal quantidade;
    private String tipo; // ENTRADA, SAIDA, AJUSTE
    private LocalDateTime dataMovimento;
    private String motivo;
}


