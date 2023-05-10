package GameClient;

import GameServer.CheckeredGame;

import java.rmi.Naming;
import java.util.Scanner;


public class GameClient {
    private CheckeredGame game;

    public GameClient() {
        try {
            game = (CheckeredGame) Naming.lookup("CheckeredGame");
        } catch (Exception e) {
            System.err.println("Исключение при старте игры на клиенте:");
            e.printStackTrace();
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ввведите имя игрока:");
        String playerName = scanner.nextLine();

        try {
            game.addPlayer(playerName);
            System.out.println("Ждем другого игрока...");

            while (!game.isReady()) {
                Thread.sleep(1000);
            }

            System.out.println("Игроки подключились, начинаем игру...");
            int playerIndex = game.getPlayerIndex(playerName);
            boolean gameEnded = false;

            while (!gameEnded) {

                String currentPlayer = game.getCurrentPlayer();
                if (!playerName.equals(game.getCurrentPlayer())) {
                    System.out.println("Игрок " + currentPlayer + " делает ход...");
                    while (!game.getCurrentPlayer().equals(playerName)) {
                        Thread.sleep(1000);
                    }
                }
                if (playerName.equals(game.getCurrentPlayer())) {
                    System.out.println("Текущий счет:" + game.getPlayerScore(0)+ ","+ game.getPlayerScore(1));
                    System.out.println("Настала ваша очередь! Введите \"t\" для верхней линии, \"b\" для нижней линии, \"r\" для правой, \"l\" для левой:");

                    String input = scanner.nextLine();
                    if (input.equals("t")) {
                        System.out.println("Введите строку и столбец (например, '2 3'):");
                        String[] coords = scanner.nextLine().split(" ");
                        int row = Integer.parseInt(coords[0]) - 1;
                        int col = Integer.parseInt(coords[1]) - 1;
                        game.makeLine(row, col, "t", playerIndex);
                    } else if (input.equals("b")) {
                        System.out.println("Введите строку и столбец (например, '2 3'):");
                        String[] coords = scanner.nextLine().split(" ");
                        int row = Integer.parseInt(coords[0]) - 1;
                        int col = Integer.parseInt(coords[1]) - 1;
                        game.makeLine(row, col, "b", playerIndex);
                    }else if (input.equals("r")) {
                        System.out.println("Введите строку и столбец (например, '2 3'):");
                        String[] coords = scanner.nextLine().split(" ");
                        int row = Integer.parseInt(coords[0]) - 1;
                        int col = Integer.parseInt(coords[1]) - 1;
                        game.makeLine(row, col, "r", playerIndex);

                    }else if (input.equals("l")){
                        System.out.println("Введите строку и столбец (например, '2 3'):");
                        String[] coords = scanner.nextLine().split(" ");
                        int row = Integer.parseInt(coords[0]) - 1;
                        int col = Integer.parseInt(coords[1]) - 1;
                        game.makeLine(row, col, "l", playerIndex);

                    } else {
                        System.out.println("Неверный ввод. Попробуйте еще раз.");
                    }
                }

                if (game.isGameOver()) {
                    gameEnded = true;
                    String winner = game.getWinner();
                    System.out.println("Игра окончена! Победителем становится " + winner + ".");
                }
            }

        } catch (Exception e) {
            System.err.println("Исключение из клиента игры:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameClient client = new GameClient();
        client.start();
    }
}
