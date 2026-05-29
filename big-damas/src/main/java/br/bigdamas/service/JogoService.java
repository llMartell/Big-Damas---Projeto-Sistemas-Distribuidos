package br.bigdamas.service;

import br.bigdamas.enums.Jogador;
import br.bigdamas.enums.TipoPeca;
import br.bigdamas.model.Peca;
import br.bigdamas.model.Tabuleiro;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// 1. Estende UnicastRemoteObject e implementa a interface RMI
public class JogoService extends UnicastRemoteObject implements IJogoService {

    private Tabuleiro tabuleiro;
    private Jogador jogadorAtual;
    private boolean jogoEncerrado;

    // 2. O construtor deve lançar RemoteException e chamar super()
    public JogoService() throws RemoteException {
        super();
        this.tabuleiro = new Tabuleiro();
        this.jogadorAtual = Jogador.JOGADOR_1;
        this.jogoEncerrado = false;
    }

    // 3. Adicionado throws RemoteException nos métodos públicos da interface
    @Override
    public Tabuleiro getTabuleiro() throws RemoteException {
        return tabuleiro;
    }

    @Override
    public Jogador getJogadorAtual() throws RemoteException {
        return jogadorAtual;
    }

    @Override
    public boolean isJogoEncerrado() throws RemoteException {
        return jogoEncerrado;
    }

    @Override
    public String[][] getEstadoTabuleiro() throws RemoteException {
        return tabuleiro.gerarEstadoTabuleiro();
    }

    @Override
    public boolean moverPeca(int origemLinha, int origemColuna, int destinoLinha, int destinoColuna) throws RemoteException {

        if (jogoEncerrado) {
            System.out.println("O jogo já terminou.");
            return false;
        }

        Peca[][] matriz = tabuleiro.getMatriz();

        if (!posicaoValida(origemLinha, origemColuna) || !posicaoValida(destinoLinha, destinoColuna)) {
            System.out.println("Posição inválida.");
            return false;
        }

        Peca peca = matriz[origemLinha][origemColuna];

        if (peca == null) {
            System.out.println("Não existe peça nessa posição.");
            return false;
        }

        if (peca.getJogador() != jogadorAtual) {
            System.out.println("Não é a vez desse jogador.");
            return false;
        }

        if (matriz[destinoLinha][destinoColuna] != null) {
            System.out.println("Destino ocupado.");
            return false;
        }

        boolean existeCapturaObrigatoria = existeCapturaObrigatoria(jogadorAtual);
        boolean moveu;

        if (peca.getTipo() == TipoPeca.DAMA) {
            moveu = moverDama(origemLinha, origemColuna, destinoLinha, destinoColuna, existeCapturaObrigatoria);
        } else {
            moveu = moverPecaComum(origemLinha, origemColuna, destinoLinha, destinoColuna, existeCapturaObrigatoria);
        }

        return moveu;
    }

    // =====================================================================
    // OS MÉTODOS PRIVADOS ABAIXO CONTINUAM EXATAMENTE IGUAIS (NÃO MUDAM)
    // =====================================================================

    private boolean moverPecaComum(int origemLinha, int origemColuna, int destinoLinha, int destinoColuna, boolean capturaObrigatoria) {
        Peca[][] matriz = tabuleiro.getMatriz();
        Peca peca = matriz[origemLinha][origemColuna];

        int diferencaLinha = destinoLinha - origemLinha;
        int diferencaColuna = destinoColuna - origemColuna;
        int direcao = peca.getJogador() == Jogador.JOGADOR_1 ? 1 : -1;

        if (!capturaObrigatoria && diferencaLinha == direcao && Math.abs(diferencaColuna) == 1) {
            matriz[destinoLinha][destinoColuna] = peca;
            matriz[origemLinha][origemColuna] = null;
            verificarPromocao(peca, destinoLinha);
            verificarVencedor();
            trocarTurno();

            return true;
        }

        if (Math.abs(diferencaLinha) == 2 && Math.abs(diferencaColuna) == 2) {

            int linhaMeio = (origemLinha + destinoLinha) / 2;
            int colunaMeio = (origemColuna + destinoColuna) / 2;

            Peca pecaMeio = matriz[linhaMeio][colunaMeio];

            if (pecaMeio != null && pecaMeio.getJogador() != peca.getJogador()) {
                matriz[linhaMeio][colunaMeio] = null;
                matriz[destinoLinha][destinoColuna] = peca;
                matriz[origemLinha][origemColuna] = null;
                verificarPromocao(peca, destinoLinha);
                verificarVencedor();

                if (!podeCapturar(destinoLinha, destinoColuna)) {
                    trocarTurno();

                } else {
                    System.out.println("Captura em cadeia obrigatória!");
                }

                return true;
            }
        }

        System.out.println("Movimento inválido.");

        return false;
    }

    private boolean moverDama(int origemLinha, int origemColuna, int destinoLinha, int destinoColuna, boolean capturaObrigatoria) {
        Peca[][] matriz = tabuleiro.getMatriz();
        Peca dama = matriz[origemLinha][origemColuna];

        int diferencaLinha = destinoLinha - origemLinha;
        int diferencaColuna = destinoColuna - origemColuna;

        if (Math.abs(diferencaLinha) != Math.abs(diferencaColuna)) {
            System.out.println("Movimento inválido.");

            return false;
        }

        int passoLinha = diferencaLinha > 0 ? 1 : -1;
        int passoColuna = diferencaColuna > 0 ? 1 : -1;

        int linha = origemLinha + passoLinha;
        int coluna = origemColuna + passoColuna;

        Peca pecaEncontrada = null;

        int linhaPeca = -1;
        int colunaPeca = -1;

        while (linha != destinoLinha && coluna != destinoColuna) {
            Peca pecaAtual = matriz[linha][coluna];

            if (pecaAtual != null) {

                if (pecaAtual.getJogador() == dama.getJogador()) {
                    System.out.println("Caminho bloqueado.");

                    return false;
                }

                if (pecaEncontrada != null) {
                    System.out.println("Não pode pular duas peças ou mais.");

                    return false;
                }

                pecaEncontrada = pecaAtual;
                linhaPeca = linha;
                colunaPeca = coluna;
            }

            linha += passoLinha;
            coluna += passoColuna;
        }

        if (pecaEncontrada == null) {

            if (capturaObrigatoria) {
                System.out.println("Existe captura obrigatória.");

                return false;
            }

            matriz[destinoLinha][destinoColuna] = dama;
            matriz[origemLinha][origemColuna] = null;
            trocarTurno();

            return true;
        }

        matriz[linhaPeca][colunaPeca] = null;
        matriz[destinoLinha][destinoColuna] = dama;
        matriz[origemLinha][origemColuna] = null;
        verificarVencedor();

        if (!podeCapturar(destinoLinha, destinoColuna)) {
            trocarTurno();

        } else {

            System.out.println("Captura em cadeia obrigatória!");
        }

        return true;
    }

    private boolean existeCapturaObrigatoria(Jogador jogador) {
        Peca[][] matriz = tabuleiro.getMatriz();

        for (int linha = 0; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = matriz[linha][coluna];

                if (peca != null && peca.getJogador() == jogador) {

                    if (podeCapturar(linha, coluna)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean podeCapturar(int linha, int coluna) {
        Peca[][] matriz = tabuleiro.getMatriz();

        Peca peca = matriz[linha][coluna];

        if (peca == null) {
            return false;
        }

        if (peca.getTipo() == TipoPeca.DAMA) {
            int[] direcoes = {-1, 1};

            for (int dl : direcoes) {

                for (int dc : direcoes) {

                    int l = linha + dl;
                    int c = coluna + dc;

                    boolean encontrouInimigo = false;

                    while (posicaoValida(l, c)) {
                        Peca atual = matriz[l][c];

                        if (atual == null) {

                            if (encontrouInimigo) {
                                return true;
                            }

                        } else {

                            if (atual.getJogador() == peca.getJogador()) {
                                break;
                            }

                            if (encontrouInimigo) {
                                break;
                            }

                            encontrouInimigo = true;
                        }

                        l += dl;
                        c += dc;
                    }
                }
            }

            return false;
        }

        int[] direcoes = {-2, 2};

        for (int dl : direcoes) {

            for (int dc : direcoes) {

                int destinoLinha = linha + dl;
                int destinoColuna = coluna + dc;

                if (!posicaoValida(destinoLinha, destinoColuna)) {
                    continue;
                }

                if (matriz[destinoLinha][destinoColuna] != null) {
                    continue;
                }

                int linhaMeio = (linha + destinoLinha) / 2;

                int colunaMeio = (coluna + destinoColuna) / 2;

                Peca meio = matriz[linhaMeio][colunaMeio];

                if (meio != null && meio.getJogador() != peca.getJogador()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < 8 && coluna >= 0 && coluna < 8;
    }

    private void trocarTurno() {
        jogadorAtual = jogadorAtual == Jogador.JOGADOR_1 ? Jogador.JOGADOR_2 : Jogador.JOGADOR_1;
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