package br.bigdamas.gui;

import br.bigdamas.enums.Jogador;
import br.bigdamas.enums.TipoPeca;
import br.bigdamas.model.Peca;
import br.bigdamas.service.IJogoService; // Alterado para importar a Interface

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class TabuleiroJogo extends JFrame {
    // Matriz do tabuleiro
    private java.util.Map<String, ImageIcon> cacheIcones = new java.util.HashMap<>();
    private JButton[][] casas = new JButton[8][8];
    private br.bigdamas.service.IJogoService jogoService; // Alterado para a Interface

    // Controle de Jogada
    private int[] origemSelecionada = null; // [linha, coluna]

    // Interface lateral
    private JLabel labelCapturasJ1;
    private JLabel labelCapturasJ2;
    private JLabel labelStatusTurno;
    
    // Controle para o pop-up não repetir infinitamente por causa da Thread
    private boolean avisoFimDeJogoExibido = false; 

    public TabuleiroJogo(IJogoService jogoServiceConectado) {
    	this.jogoService = jogoServiceConectado;
    	
        // Titulo e definições principais
        setTitle("Big Damas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Container principal
        Container painelPrincipal = getContentPane();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBackground(new Color(30, 30, 30));

        // Container do tabuleiro
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setOpaque(false);

        JPanel painelTabuleiro = new JPanel(new GridLayout(8, 8));
        painelTabuleiro.setPreferredSize(new Dimension(700, 700));
        painelTabuleiro.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));

        // Definição das cores do tabuleiro
        Color corClara = new Color(248, 230, 206);
        Color corEscura = new Color(122, 86, 65);

        // Matriz
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
        JPanel painelEsquerda = criarPainelJogador(Jogador.JOGADOR_1, "foto_j1.jpg");
        JPanel painelDireita = criarPainelJogador(Jogador.JOGADOR_2, "foto_j2.jpg");

        painelPrincipal.add(painelEsquerda, BorderLayout.WEST);
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(painelDireita, BorderLayout.EAST);

        // Barra de Status Inferior
        labelStatusTurno = new JLabel("Aguardando início...", SwingConstants.CENTER);
        labelStatusTurno.setFont(new Font("Arial", Font.BOLD, 24));
        labelStatusTurno.setForeground(Color.WHITE);
        painelPrincipal.add(labelStatusTurno, BorderLayout.SOUTH);

        renderizarInterface();

        // ATUALIZAÇÃO REMOTA CONTINUA (Thread de sincronização)
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Verifica se o oponente jogou a cada 1 segundo
                    renderizarInterface();
                    repaint();
                } catch (Exception e) {
                    System.err.println("Erro na thread de atualização visual: " + e.getMessage());
                }
            }
        }).start();
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
        labelFoto.setPreferredSize(new Dimension(180, 180));
        labelFoto.setMaximumSize(new Dimension(180, 180));
        labelFoto.setHorizontalAlignment(SwingConstants.CENTER);
        labelFoto.setVerticalAlignment(SwingConstants.CENTER);

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

        // Chamadas
        painel.add(labelFoto);
        painel.add(Box.createVerticalStrut(20));
        painel.add(labelNome);
        painel.add(Box.createVerticalStrut(10));
        painel.add(labelCapturas);

        return painel;
    }

    private void renderizarInterface() {
        try {
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

            // Status do Turno
            labelStatusTurno.setText("VEZ DO JOGADOR " + (atual == Jogador.JOGADOR_1 ? "1" : "2"));

            if (jogoService.isJogoEncerrado()) {
                String vencedor = (pecasJ1 > 0) ? "1" : "2";
                labelStatusTurno.setText("VITÓRIA DO JOGADOR " + vencedor + "!");
                
                // Evita disparar múltiplos diálogos devido à Thread periódica
                if (!avisoFimDeJogoExibido) {
                    avisoFimDeJogoExibido = true;
                    JOptionPane.showMessageDialog(this, "FIM DE JOGO!");
                }
            }
        } catch (java.rmi.RemoteException e) {
            System.err.println("Erro de conexão remota ao tentar atualizar a interface gráfica.");
        }
    }

    private void handleClique(int linha, int coluna) {
        try {
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
                    // Chamada remota via RMI cercada por tratamento
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
        } catch (java.rmi.RemoteException e) {
            JOptionPane.showMessageDialog(this, "Instabilidade na rede ao realizar jogada: " + e.getMessage(), "Erro RMI", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int contarPecas(Jogador jogador) {
        int count = 0;
        try {
            Peca[][] matriz = jogoService.getTabuleiro().getMatriz();
            for (Peca[] linha : matriz) {
                for (Peca p : linha) {
                    if (p != null && p.getJogador() == jogador) count++;
                }
            }
        } catch (java.rmi.RemoteException e) {
            System.err.println("Erro remoto ao contar peças.");
        }
        return count;
    }

    // Método para renderizar imagens
    private ImageIcon carregarIcone(String path, int width, int height) {
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
            ImageIcon original = new ImageIcon(imgUrl);
            iconResultado = new ImageIcon(original.getImage()) {
                @Override
                public int getIconWidth() { return width; }
                @Override
                public int getIconHeight() { return height; }
                @Override
                public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
                    g.drawImage(getImage(), x, y, width, height, c);
                }
            };
        } else {
            ImageIcon original = new ImageIcon(imgUrl);
            Image img = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            iconResultado = new ImageIcon(img);
        }

        cacheIcones.put(path, iconResultado);
        return iconResultado;
    }
}