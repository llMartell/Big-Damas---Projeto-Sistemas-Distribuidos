package br.bigdamas;

import java.rmi.Naming;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.bigdamas.gui.TabuleiroJogo;
import br.bigdamas.service.IJogoService;

public class Main {

	public static void main(String[] args) {
		try {
			//Alterar o IP sempre que for testar (coloque o IP da maquina que vai servir de servidor)
			//Basta solicitar que o responsavel pelo servidor apresente a IP assim que rodor o servidor
			String ipServidor = "10.8.185.20";
			IJogoService jogoService = (IJogoService) Naming.lookup("rmi://" + ipServidor + ":1099/BigDamasService");
			System.out.println("Cliente: Conectado com sucesso ao servidor RMI!");

			SwingUtilities.invokeLater(() -> {
				TabuleiroJogo tabuleiro = new TabuleiroJogo(jogoService);
				tabuleiro.setVisible(true);
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel conectar-se ao servidor do IP informado.\nErro: " + e.getMessage(),
					"Falha de conexão RMI", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
	}
}
