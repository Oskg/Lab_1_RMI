package GameServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CheckeredGame extends Remote {
    String getWinner() throws RemoteException;

    void addPlayer(String playerName) throws RemoteException;

    boolean isReady() throws RemoteException;

    int getPlayerIndex(String playerName) throws RemoteException;

    String getCurrentPlayer() throws RemoteException;

    void makeLine(int row, int col, String ori, int playerIndex) throws RemoteException;

    int getPlayerScore(int index) throws RemoteException;

    CheckeredBoard getBoard() throws RemoteException;

    boolean isGameOver() throws RemoteException;


}
