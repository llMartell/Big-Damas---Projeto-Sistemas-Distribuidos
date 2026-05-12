package br.bigdamas.service;

import br.bigdamas.enums.Jogador;
import br.bigdamas.enums.TipoPeca;
import br.bigdamas.model.Peca;
import br.bigdamas.model.Tabuleiro;

public class JogoService {

    private Tabuleiro tabuleiro;
    private Jogador jogadorAtual;
    private boolean jogoEncerrado;

    public JogoService() {
        this.tabuleiro = new Tabuleiro();
        this.jogadorAtual = Jogador.JOGADOR_1;
        this.jogoEncerrado = false;
    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    public boolean isJogoEncerrado() {
        return jogoEncerrado;
    }

    public boolean moverPeca(int origemLinha, int origemColuna, int destinoLinha, int destinoColuna) {

        if (jogoEncerrado) {

            System.out.println("O jogo já terminou.");

            return false;
        }

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

            verificarVencedor();

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

                verificarVencedor();

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

    private int contarPecas(Jogador jogador) {

        int quantidade = 0;

        Peca[][] matriz = tabuleiro.getMatriz();

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                Peca peca = matriz[linha][coluna];

                if (peca != null && peca.getJogador() == jogador) {

                    quantidade++;
                }
            }
        }

        return quantidade;
    }

    private void verificarVencedor() {

        int pecasJogador1 = contarPecas(Jogador.JOGADOR_1);

        int pecasJogador2 = contarPecas(Jogador.JOGADOR_2);

        if (pecasJogador1 == 0) {

            jogoEncerrado = true;

            System.out.println();
            System.out.println("JOGADOR_2 venceu!");
            System.out.println();
        }

        if (pecasJogador2 == 0) {

            jogoEncerrado = true;

            System.out.println();
            System.out.println("JOGADOR_1 venceu!");
            System.out.println();
        }
    }
}