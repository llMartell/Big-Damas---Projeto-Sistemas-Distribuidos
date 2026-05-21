package br.bigdamas.gui;
import br.bigdamas.enums.Jogador;
import br.bigdamas.enums.TipoPeca;
import br.bigdamas.model.Peca;
import br.bigdamas.service.JogoService;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TabuleiroJogo extends JFrame {
    //Matriz do tabuleiro
    private java.util.Map<String, ImageIcon> cacheIcones = new java.util.HashMap<>();
    private JButton[][] casas = new JButton[8][8];
    private br.bigdamas.service.JogoService jogoService;

    // Controle de Jogada
    private int[] origemSelecionada = null; // [linha, coluna]

    // Interface lateral
    private JLabel labelCapturasJ1;
    private JLabel labelCapturasJ2;
    private JLabel labelStatusTurno;

    public TabuleiroJogo() {
        this.jogoService = new JogoService();
        //Titulo e definições principais
        setTitle("Big Damas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Container principal
        Container painelPrincipal = getContentPane();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 30)); // Mudei para destacar

        // Container do tabuleiro
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setOpaque(false);

        JPanel painelTabuleiro = new JPanel(new GridLayout(8, 8));
        painelTabuleiro.setPreferredSize(new Dimension(700, 700));
        painelTabuleiro.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

        //Definição das cores do tabuleiro
        Color corClara = new Color(248, 230, 206);
        Color corEscura = new Color(122, 86, 65);

        //Matriz
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                JButton botao = new JButton();
                casas[linha][coluna] = botao;

                // Estilo do tabuleiro
                botao.setBackground((linha + coluna) % 2 == 0 ? corClara : corEscura);
                botao.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
                botao.setFocusPainted(false);

                final int r = linha;
                final int c = coluna;
                botao.addActionListener(e -> handleClique(r, c));

                painelTabuleiro.add(botao);
            }
        }
        painelCentral.add(painelTabuleiro);

        // Interface dos jogadores
        JPanel painelEsquerda = criarPainelJogador(Jogador.JOGADOR_1, "meme_p1.png");
        JPanel painelDireita = criarPainelJogador(Jogador.JOGADOR_2, "meme_p2.png");

        painelPrincipal.add(painelEsquerda, BorderLayout.WEST);
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(painelDireita, BorderLayout.EAST);

        // Barra de Status Inferior
        labelStatusTurno = new JLabel("Aguardando início...", SwingConstants.CENTER);
        labelStatusTurno.setFont(new Font("Arial", Font.BOLD, 24));
        labelStatusTurno.setForeground(Color.WHITE);
        painelPrincipal.add(labelStatusTurno, BorderLayout.SOUTH);

        renderizarInterface();
    }

    private JPanel criarPainelJogador(Jogador jogador, String nomeArquivoMeme) {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setPreferredSize(new Dimension(300, 0));
        painel.setBackground(new Color(45, 45, 45));
        painel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        // Foto do Jogador
        JLabel labelFoto = new JLabel();
        labelFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelFoto.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        labelFoto.setPreferredSize(new Dimension(200, 200));
        labelFoto.setMaximumSize(new Dimension(200, 200));

        // Tenta carregar imagem do jogador
        labelFoto.setIcon(carregarIcone("/img/" + nomeArquivoMeme, 180, 180));

        // Identificação
        JLabel labelNome = new JLabel("JOGADOR " + (jogador == Jogador.JOGADOR_1 ? "1" : "2"));
        labelNome.setFont(new Font("Arial", Font.BOLD, 28));
        labelNome.setForeground(Color.WHITE);
        labelNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Contador de Capturas
        JLabel labelCapturas = new JLabel("Capturas: 0");
        labelCapturas.setFont(new Font("Arial", Font.BOLD, 18));
        labelCapturas.setForeground(Color.WHITE);
        labelCapturas.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (jogador == Jogador.JOGADOR_1) labelCapturasJ1 = labelCapturas;
        else labelCapturasJ2 = labelCapturas;

        //Chamadas
        painel.add(labelFoto);
        painel.add(Box.createVerticalStrut(20));
        painel.add(labelNome);
        painel.add(Box.createVerticalStrut(10));
        painel.add(labelCapturas);

        return painel;
    }

    private void renderizarInterface() {
        Peca[][] matriz = jogoService.getTabuleiro().getMatriz();
        Jogador atual = jogoService.getJogadorAtual();

        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                Peca peca = matriz[l][c];
                JButton botao = casas[l][c];
                botao.setIcon(null);

                // Verifica casa selecionada
                boolean isSelecionada = (origemSelecionada != null && origemSelecionada[0] == l && origemSelecionada[1] == c);

                if (peca != null) {
                    String baseName = (peca.getJogador() == Jogador.JOGADOR_1 ? "j1" : "j2");
                    baseName += (peca.getTipo() == TipoPeca.DAMA ? "_dama" : "_normal");

                    // Se selecionada, usa .gif, senão .png
                    String extensao = isSelecionada ? ".gif" : ".png";
                    String path = "/img/" + baseName + extensao;

                    botao.setIcon(carregarIcone(path, 70, 70));
                }

                // Borda da peça selecionada
                if (isSelecionada) {
                    botao.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
                } else {
                    botao.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
            }
        }

        // Peças comidas
        int pecasJ1 = contarPecas(Jogador.JOGADOR_1);
        int pecasJ2 = contarPecas(Jogador.JOGADOR_2);
        labelCapturasJ1.setText("Peças Comidas: " + (12 - pecasJ2));
        labelCapturasJ2.setText("Peças Comidas: " + (12 - pecasJ1));

        // 3. Status do Turno
        labelStatusTurno.setText("VEZ DO JOGADOR " + (atual == Jogador.JOGADOR_1 ? "1" : "2"));

        if (jogoService.isJogoEncerrado()) {
            labelStatusTurno.setText("VITÓRIA DO JOGADOR " + (atual == Jogador.JOGADOR_1 ? "1" : "2") + "!");
            JOptionPane.showMessageDialog(this, "GAME OVER: Memes venceram!");
        }
    }

    private void handleClique(int linha, int coluna) {
        if (jogoService.isJogoEncerrado()) return;

        Peca[][] matriz = jogoService.getTabuleiro().getMatriz();
        Peca pecaClicada = matriz[linha][coluna];

        // Origem
        if (origemSelecionada == null) {
            if (pecaClicada != null && pecaClicada.getJogador() == jogoService.getJogadorAtual()) {
                origemSelecionada = new int[]{linha, coluna};
            }
        }
        // Destino
        else {
            int oL = origemSelecionada[0];
            int oC = origemSelecionada[1];

            // Se clicar na mesma casa, cancela seleção
            if (oL == linha && oC == coluna) {
                origemSelecionada = null;
            } else {
                // Aqui ocorre a chamada do RMI
                boolean sucesso = jogoService.moverPeca(oL, oC, linha, coluna);

                if (sucesso) {
                    origemSelecionada = null; // Reset para o próximo turno
                } else {
                    // Feedback visual de erro ou troca de seleção se clicar em outra peça sua
                    if (pecaClicada != null && pecaClicada.getJogador() == jogoService.getJogadorAtual()) {
                        origemSelecionada = new int[]{linha, coluna};
                    }
                }
            }
        }
        renderizarInterface();
    }

    private int contarPecas(Jogador jogador) {
        int count = 0;
        Peca[][] matriz = jogoService.getTabuleiro().getMatriz();
        for (Peca[] linha : matriz) {
            for (Peca p : linha) {
                if (p != null && p.getJogador() == jogador) count++;
            }
        }
        return count;
    }

     //Método para renderizar imagens
    private ImageIcon carregarIcone(String path, int width, int height) {
        // Se a imagem já foi carregada antes, retorna ela direto da memória (Rápido!)
        if (cacheIcones.containsKey(path)) {
            return cacheIcones.get(path);
        }

        URL imgUrl = getClass().getResource(path);
        if (imgUrl == null) {
            System.err.println("ERRO: Arquivo não encontrado: " + path);
            return null;
        }

        ImageIcon iconResultado;

        if (path.toLowerCase().endsWith(".gif")) {
            // Para GIFs: Carregamos o original e criamos uma versão que se auto-ajusta no desenho
            ImageIcon original = new ImageIcon(imgUrl);
            iconResultado = new ImageIcon(original.getImage()) {
                @Override
                public int getIconWidth() { return width; }
                @Override
                public int getIconHeight() { return height; }
                @Override
                public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                    // Desenha a imagem redimensionada em tempo real sem travar
                    g.drawImage(getImage(), x, y, width, height, c);
                }
            };
        } else {
            // Para PNG/JPG: Redimensionamento padrão
            ImageIcon original = new ImageIcon(imgUrl);
            Image img = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            iconResultado = new ImageIcon(img);
        }

        // Guarda na memória para a próxima vez
        cacheIcones.put(path, iconResultado);
        return iconResultado;
    }
}

