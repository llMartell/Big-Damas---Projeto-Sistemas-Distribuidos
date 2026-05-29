package br.bigdamas.service;

import br.bigdamas.enums.Jogador;
import br.bigdamas.model.Tabuleiro;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IJogoService extends Remote {
    Tabuleiro getTabuleiro() throws RemoteException;
    Jogador getJogadorAtual() throws RemoteException;
    boolean isJogoEncerrado() throws RemoteException;
    String[][] getEstadoTabuleiro() throws RemoteException;
    boolean moverPeca(int origemLinha, int origemColuna, int destinoLinha, int destinoColuna) throws RemoteException;
}