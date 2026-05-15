package br.bigdamas.gui;

import javax.swing.*;
import java.awt.*;

public class TabuleiroJogo extends JFrame {
    //Matriz das casas do tabuleiro
    private JButton[][] casas = new JButton[8][8];

    public TabuleiroJogo() {
        //Titulo e definições principais
        setTitle("Big Damas");
        setSize(760, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Container da tabela
        Container painelPrincipal = getContentPane();
        painelPrincipal.setLayout(new GridLayout(8,8));

        //Definição das cores do tabuleiro
        Color corClara = new Color(248, 230, 206);
        Color corEscura = new Color(122, 86, 65);

        //Matriz
        for(int coluna = 0; coluna < 8; coluna++) {
            for(int linha = 0; linha < 8; linha++) {
                casas[linha][coluna] = new JButton();

                if ((linha+coluna) % 2 == 0) {
                    casas[linha][coluna].setBackground(corClara);

                } else{
                    casas[linha][coluna].setBackground(corEscura);
                }
                painelPrincipal.add(casas[linha][coluna]);
            }
        }
    }
}
