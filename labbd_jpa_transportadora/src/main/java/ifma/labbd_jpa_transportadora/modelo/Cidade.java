package ifma.labbd_jpa_transportadora.modelo;

import jakarta.persistence.*;

@Entity
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column
    private String estado;
    
    @Column(nullable = false)
    private double taxaEntrega;

    
    public Cidade() {
    }

    public Cidade(String nome, String estado, double taxaEntrega) {
        this.nome = nome;
        this.estado = estado;
        this.taxaEntrega = taxaEntrega;
    }

    // Getters e setters
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTaxaEntrega() { return taxaEntrega; }
    public void setTaxaEntrega(double taxaEntrega) { this.taxaEntrega = taxaEntrega; }
}

