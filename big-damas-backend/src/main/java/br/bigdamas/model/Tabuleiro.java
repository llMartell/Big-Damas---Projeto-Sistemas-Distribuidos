package br.bigdamas.model;

import br.bigdamas.enums.Jogador;
import br.bigdamas.enums.TipoPeca;

public class Tabuleiro {

    private Peca[][] matriz;

    public Tabuleiro() {

        matriz = new Peca[8][8];

        iniciarTabuleiro();
    }

    private void iniciarTabuleiro() {

        for (int linha = 0; linha < 3; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                if ((linha + coluna) % 2 != 0) {

                    matriz[linha][coluna] =
                            new Peca(Jogador.JOGADOR_1);
                }
            }
        }

        for (int linha = 5; linha < 8; linha++) {

            for (int coluna = 0; coluna < 8; coluna++) {

                if ((linha + coluna) % 2 != 0) {

                    matriz[linha][coluna] =
                            new Peca(Jogador.JOGADOR_2);
                }
            }
        }
    }

    public void imprimirTabuleiro() {

        System.out.println();

        System.out.print("  ");

        for (int coluna = 0; coluna < 8; coluna++) {
            System.out.print(coluna + " ");
        }

        System.out.println();

        for (int linha = 0; linha < 8; linha++) {
            System.out.print(linha + " ");

            for (int coluna = 0; coluna < 8; coluna++) {

                if (matriz[linha][coluna] == null) {
                    System.out.print("- ");

                } else {

                    Peca peca = matriz[linha][coluna];

                    if (peca.getJogador() == Jogador.JOGADOR_1) {

                        if (peca.getTipo() == TipoPeca.DAMA) {
                            System.out.print("D ");

                        } else {
                            System.out.print("A ");
                        }

                    } else {

                        if (peca.getTipo() == TipoPeca.DAMA) {
                            System.out.print("E ");

                        } else {
                            System.out.print("B ");
                        }
                    }
                }
            }

            System.out.println();
        }

        System.out.println();
    }

    public Peca[][] getMatriz() {
        return matriz;
    }
}