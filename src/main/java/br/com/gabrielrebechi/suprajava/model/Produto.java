package br.com.gabrielrebechi.suprajava.model;


import jakarta.persistence.*;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "unidade_medida_id")
    private UnidadeMedida unidadeMedida;

    @Column(length = 255)
    private String descricao;

    public Produto() {
    }

    public Produto(String nome, UnidadeMedida unidadeMedida, String descricao) {
        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
