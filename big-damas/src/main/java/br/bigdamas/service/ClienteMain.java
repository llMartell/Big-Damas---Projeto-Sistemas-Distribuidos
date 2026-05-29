package br.bigdamas.service;

import java.rmi.Naming;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import br.bigdamas.gui.TabuleiroJogo;

public class ClienteMain {

	public static void main(String[] args) {
			
	    try {
	        //Altere para o o IP para o do computador que vai executar o trabalho de servidor
	        String ipServidor = "10.8.185.20";
	        IJogoService jogoService = (IJogoService) Naming.lookup("rmi://" +ipServidor+ ":1099/BigDamasService");
	        System.out.println("Cliente: Conectado com sucesso ao servidor RMI!");
	        
	        SwingUtilities.invokeLater(() -> {
	            TabuleiroJogo tabuleiro = new TabuleiroJogo(jogoService);
	            tabuleiro.setVisible(true);
	        });
	        
	    } catch (Exception e) {
			//Painel mostrando mensagem de erro
	    	JOptionPane.showMessageDialog(null, "Não foi possivel conectar-se ao servidor do IP informado.\nErro: "+e.getMessage(),
	    			"Falha de conexão RMI",JOptionPane.ERROR_MESSAGE);
	    	e.printStackTrace();
		}
	}
}
