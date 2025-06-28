package br.com.gabrielrebechi.suprajava.model;

import jakarta.persistence.*;

@Entity
public class UnidadeMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(name = "sigla", unique = true, nullable = false, length = 10)
    private String sigla;

    @Enumerated(EnumType.STRING)
    private TipoUnidade tipo;

    @Column(length = 255)
    private String descricao;

    public UnidadeMedida() {
    }

    public UnidadeMedida(String nome, String sigla, TipoUnidade tipo, String descricao) {
        this.nome = nome;
        this.sigla = sigla;
        this.tipo = tipo;
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

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public TipoUnidade getTipo() {
        return tipo;
    }

    public void setTipo(TipoUnidade tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean permiteValorFracionado() {
        return tipo == TipoUnidade.FRACIONADA || tipo == TipoUnidade.TEMPO;
    }
}
