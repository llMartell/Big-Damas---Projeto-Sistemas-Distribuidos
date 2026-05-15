package br.bigdamas.gui;

import javax.swing.*;
import java.awt.*;

public class TabuleiroJogo extends JFrame {
    //Matriz das casas do tabuleiro
    private JButton[][] casas = new JButton[8][8];

    public TabuleiroJogo() {
        //Titulo e definições principais
        setTitle("Big Damas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Container principal
        Container painelPrincipal = getContentPane();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(new Color(255, 255, 255)); // Fundo escuro da arena

        // Container do tabuleiro
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setOpaque(false);

        JPanel painelTabuleiro = new JPanel(new GridLayout(8, 8));
        painelTabuleiro.setPreferredSize(new Dimension(760, 700));

        //Definição das cores do tabuleiro
        Color corClara = new Color(248, 230, 206);
        Color corEscura = new Color(122, 86, 65);

        //Matriz
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                casas[linha][coluna] = new JButton();

                if ((linha + coluna) % 2 == 0) {
                    casas[linha][coluna].setBackground(corClara);
                } else {
                    casas[linha][coluna].setBackground(corEscura);
                }

                casas[linha][coluna].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                painelTabuleiro.add(casas[linha][coluna]);
            }
        }
        // Adiciona o tabuleiro montado no painel central
        painelCentral.add(painelTabuleiro);


        JPanel painelJogador1 = jogadorGUI("1");
        JPanel painelJogador2 = jogadorGUI("2");

        // Adiciona as 3 partes no container principal
        painelPrincipal.add(painelJogador1, BorderLayout.WEST);
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(painelJogador2, BorderLayout.EAST);
    }
    //Metodo para desenvolver as interfaces laterais dos jogadores
    private JPanel jogadorGUI(String numeroJogador){
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setOpaque(false);
        painel.setPreferredSize(new Dimension(300, 0));
        painel.setBorder(BorderFactory.createEmptyBorder(100,50,0,50));

        //Foto jogador
        JLabel labelFoto = new JLabel();
        labelFoto.setPreferredSize(new Dimension(150, 150));
        labelFoto.setMaximumSize(new Dimension(150, 150));
        labelFoto.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        labelFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Nome jogador
        JLabel labelNome = new JLabel("JOGADOR " + numeroJogador);
        labelNome.setFont(new Font("Arial", Font.PLAIN, 18));
        labelNome.setForeground(Color.BLACK);
        labelNome.setAlignmentX(Component.CENTER_ALIGNMENT);


        painel.add(labelFoto);
        painel.add(Box.createVerticalStrut(20));
        painel.add(labelNome);
        painel.add(Box.createVerticalStrut(30));
        return painel;
    }
}
