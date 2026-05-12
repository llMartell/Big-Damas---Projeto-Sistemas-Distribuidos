package br.bigdamas;

import br.bigdamas.service.JogoService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        JogoService jogoService = new JogoService();

        while (!jogoService.isJogoEncerrado()) {

            jogoService.getTabuleiro().imprimirTabuleiro();

            System.out.println("Turno: " + jogoService.getJogadorAtual());

            String[][] estado = jogoService.getEstadoTabuleiro();

            System.out.println("Estado [0][1]: " + estado[0][1]);

            System.out.print("Origem linha: ");
            int origemLinha = scanner.nextInt();

            System.out.print("Origem coluna: ");
            int origemColuna = scanner.nextInt();

            System.out.print("Destino linha: ");
            int destinoLinha = scanner.nextInt();

            System.out.print("Destino coluna: ");
            int destinoColuna = scanner.nextInt();

            boolean moveu = jogoService.moverPeca(origemLinha, origemColuna, destinoLinha, destinoColuna);

            if (moveu) {
                System.out.println("Movimento realizado.");

            } else {
                System.out.println("Erro ao mover.");
            }
        }

        scanner.close();
    }
}