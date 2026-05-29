package br.bigdamas;

import br.bigdamas.service.JogoService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorMain {
    public static void main(String[] args) {
        try {
            // Coloque aqui o IP da rede local da máquina que vai rodar o Servidor
            String ipServidor = "127.0.0.1"; 
            System.setProperty("java.rmi.server.hostname", ipServidor);

            JogoService jogoService = new JogoService();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BigDamasService", jogoService);

            System.out.println("Servidor RMI do Big Damas Rodando no IP: " + ipServidor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}