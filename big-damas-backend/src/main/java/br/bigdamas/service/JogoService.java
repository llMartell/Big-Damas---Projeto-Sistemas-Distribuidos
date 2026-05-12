package br.bigdamas.service;

import br.bigdamas.enums.Jogador;
import br.bigdamas.enums.TipoPeca;
import br.bigdamas.model.Peca;
import br.bigdamas.model.Tabuleiro;

public class JogoService {

    private Tabuleiro tabuleiro;
    private Jogador jogadorAtual;

    public JogoService() {
        this.tabuleiro = new Tabuleiro();
        this.jogadorAtual = Jogador.JOGADOR_1;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean moverPeca(int origemLinha, int origemColuna, int destinoLinha, int destinoColuna) {

        Peca[][] matriz = tabuleiro.getMatriz();

        if (origemLinha < 0 || origemLinha > 7 || origemColuna < 0 || origemColuna > 7 || destinoLinha < 0 || destinoLinha > 7 || destinoColuna < 0 || destinoColuna > 7) {
            System.out.println("Posição inválida.");
            return false;
        }

        if (matriz[origemLinha][origemColuna] == null) {
            System.out.println("Não existe peça nessa posição.");
            return false;
        }

        Peca peca = matriz[origemLinha][origemColuna];

        if (peca.getJogador() != jogadorAtual) {
            System.out.println("Não é a vez desse jogador.");
            return false;
        }

        if (matriz[destinoLinha][destinoColuna] != null) {
            System.out.println("Destino ocupado.");
            return false;
        }

        int diferencaLinha = destinoLinha - origemLinha;
        int diferencaColuna = destinoColuna - origemColuna;

        boolean ehDama = peca.getTipo() == TipoPeca.DAMA;

        int direcao;

        if (peca.getJogador() == Jogador.JOGADOR_1) {
            direcao = 1;
        } else {
            direcao = -1;
        }

        if ((diferencaLinha == direcao || (ehDama && diferencaLinha == -direcao)) && Math.abs(diferencaColuna) == 1) {

            matriz[destinoLinha][destinoColuna] = peca;
            matriz[origemLinha][origemColuna] = null;

            verificarPromocao(peca, destinoLinha);

            trocarTurno();

            return true;
        }

        if ((diferencaLinha == direcao * 2 || (ehDama && diferencaLinha == -direcao * 2)) && Math.abs(diferencaColuna) == 2) {

            int linhaMeio = (origemLinha + destinoLinha) / 2;
            int colunaMeio = (origemColuna + destinoColuna) / 2;

            Peca pecaMeio = matriz[linhaMeio][colunaMeio];

            if (pecaMeio != null && pecaMeio.getJogador() != peca.getJogador()) {

                matriz[linhaMeio][colunaMeio] = null;

                matriz[destinoLinha][destinoColuna] = peca;
                matriz[origemLinha][origemColuna] = null;

                verificarPromocao(peca, destinoLinha);

                trocarTurno();

                return true;
            }
        }

        System.out.println("Movimento inválido.");
        return false;
    }

    private void trocarTurno() {

        if (jogadorAtual == Jogador.JOGADOR_1) {
            jogadorAtual = Jogador.JOGADOR_2;
        } else {
            jogadorAtual = Jogador.JOGADOR_1;
        }
    }

    private void verificarPromocao(Peca peca, int linhaDestino) {

        if (peca.getJogador() == Jogador.JOGADOR_1 && linhaDestino == 7) {
            peca.virarDama();
        }

        if (peca.getJogador() == Jogador.JOGADOR_2 && linhaDestino == 0) {
            peca.virarDama();
        }
    }
}