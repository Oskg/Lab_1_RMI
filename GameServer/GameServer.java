package GameServer;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class GameServer {
    public static void main(String[] args) {
        try {
            int numPlayers = 2;
            String[] playerNames = new String[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playerNames[i] = String.format("%d",i);
            }
            CheckeredGame game = new CheckeredGameImpl(numPlayers);
            LocateRegistry.createRegistry(1099);
            Naming.rebind("CheckeredGame", game);

            System.out.println("Игровой сервер запущен!");
        } catch (Exception e) {
            System.err.println("Сервер вызвал исключение:");
            e.printStackTrace();
        }
    }
}

