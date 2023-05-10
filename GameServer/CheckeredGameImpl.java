package GameServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



public class CheckeredGameImpl extends UnicastRemoteObject implements CheckeredGame {
    private static final int MAX_PLAYERS = 2;
    private CheckeredBoard board;
    private final AtomicInteger currentPlayer = new AtomicInteger(-1);
    private int numPlayers;
    private final List<Player> players = new ArrayList<>();
    private int[] playerScores;
    private boolean gameEnded;
    private boolean gameStarted = false;
    Object lock;

    @Override
    public CheckeredBoard getBoard() {
        if (this.board != null) return this.board;
        else return null;
    }

    @Override
    public int getPlayerScore(int index) {
        return this.playerScores[index];
    }

    public CheckeredGameImpl(int numPlayers) throws RemoteException {
        super();
        this.board = new CheckeredBoard(8,8);
        this.currentPlayer.set(0);
        this.numPlayers = numPlayers;
        this.playerScores = new int[numPlayers];
        this.gameEnded = false;
        lock = new Object();
    }
    @Override
    public String getWinner() throws RemoteException {
        if (!gameEnded) {
            return null;
        }

        int maxScore = -1;
        int winnerIndex = -1;
        for (int i = 0; i < numPlayers; i++) {
            if (playerScores[i] > maxScore) {
                maxScore = playerScores[i];
                winnerIndex = i;
            }
        }

        return players.get(winnerIndex).getName();
    }


    @Override
    public void addPlayer(String playerName) throws RemoteException {
        if (gameStarted) {
            throw new RemoteException("Игра уже началась!");
        }

        if (players.size() == MAX_PLAYERS) {
            throw new RemoteException("Максимум игроков достигнут");
        }

        if (players.stream().anyMatch(p -> p.getName().equals(playerName))) {
            throw new RemoteException("Имя уже существует");
        }

        players.add(new Player(playerName));
        System.out.println("Игрок '" + playerName + "' присоединился.");

        if (players.size() == MAX_PLAYERS) {
            gameStarted = true;
            System.out.println("Все подключились - начинаем!");
        }
    }
    @Override
    public boolean isReady() throws RemoteException {
            return (players.size() == numPlayers);
    }

    public int getPlayerIndex(String playerName) throws RemoteException {
        synchronized (lock) {
            for (int i = 0; i < this.numPlayers; i++) {
                if (this.players.get(i).getName().equals(playerName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public String getCurrentPlayer() throws RemoteException {
        synchronized (lock) {
            return players.get(currentPlayer.get()).getName();
        }
    }


    public void makeLine(int row, int col, String ori, int playerIndex) throws RemoteException {
        synchronized (lock) {
            System.out.print(playerScores[0]);
            System.out.println(","+ playerScores[1]);
            if (playerIndex != this.currentPlayer.get()) {
                throw new RemoteException("Сейчас не ваш ход!");
            }
            if (playerIndex == this.currentPlayer.get()){
                board.setBorder(row,col,ori,true);
                if(board.getSquare(row, col).getRightBorder() && board.getSquare(row, col).getLeftBorder() && board.getSquare(row, col).getTopBorder() && board.getSquare(row, col).getBottomBorder() && (board.getSquare(row,col).getHasCheck() == -1)){
                    board.getSquare(row, col).setHasCheck(currentPlayer.get());
                    playerScores[currentPlayer.get()] +=1;
                }
            }
            System.out.println(board.toString());
            if ((playerScores[currentPlayer.get()] >= 1) || (playerScores[0] + playerScores[1] == 8)) {
                gameEnded = true;
            }

            passTurn();

        }
    }


    void passTurn(){
        synchronized (lock) {
            if (currentPlayer.get() == 0) {
                currentPlayer.set(1);
            } else {
                currentPlayer.set(0);
            }
        }
    }

    public boolean isGameOver() throws RemoteException {
        return gameEnded;
    }
}
