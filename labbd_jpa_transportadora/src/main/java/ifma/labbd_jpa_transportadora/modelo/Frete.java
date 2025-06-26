package ifma.labbd_jpa_transportadora.modelo;

import jakarta.persistence.*;

@Entity
public class Frete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String descricao;
    
    @Column(nullable = false)
    private double peso;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cidade_origem_id")
    private Cidade cidadeOrigem;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "cidade_destino_id")
    private Cidade cidadeDestino;

    
    public static final double VALOR_FIXO_POR_KG = 10.0;

    
    public Frete() {
    }

    public Frete(String descricao, double peso, Cliente cliente, Cidade cidadeOrigem, Cidade cidadeDestino) {
        this.descricao = descricao;
        this.peso = peso;
        this.cliente = cliente;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
    }

    // Getters e setters
    public Long getId() { return id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Cidade getCidadeOrigem() { return cidadeOrigem; }
    public void setCidadeOrigem(Cidade cidadeOrigem) { this.cidadeOrigem = cidadeOrigem; }

    public Cidade getCidadeDestino() { return cidadeDestino; }
    public void setCidadeDestino(Cidade cidadeDestino) { this.cidadeDestino = cidadeDestino; }

    public double calcularValorFrete() {
        return (peso * VALOR_FIXO_POR_KG) + cidadeDestino.getTaxaEntrega();
    }
}