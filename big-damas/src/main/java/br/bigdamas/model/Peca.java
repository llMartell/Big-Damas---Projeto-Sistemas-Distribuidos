package br.bigdamas.model;

import br.bigdamas.enums.Jogador;
import br.bigdamas.enums.TipoPeca;

import java.io.Serializable;

public class Peca implements Serializable {
    private static final long serialVersionUID = 1L;

    private Jogador jogador;
    private TipoPeca tipo;

    public Peca(Jogador jogador) {
        this.jogador = jogador;
        this.tipo = TipoPeca.NORMAL;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public TipoPeca getTipo() {
        return tipo;
    }

    public void virarDama() {
        this.tipo = TipoPeca.DAMA;
    }
}