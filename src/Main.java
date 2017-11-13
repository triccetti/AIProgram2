import model.*;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);

        PentagoGame game = new PentagoGame();
        game.setPlayer(1, new Player("Taylor", 'B'));
        game.setPlayer(2, new AiPlayer("AI", 'W', 2, true));
        AiPlayer ai = (AiPlayer) game.getPlayer(2);

        while(!game.gameOver()) {
            System.out.println(game);

            System.out.println("Player" + game.whosTurn() + "'s turn. \n");

            if(game.isAiTurn()) { // if its the ais turn
                Move m =  ai.getMove(game);
                System.out.println(m);
            }

            System.out.print("Enter row:");
            int row = input.nextInt();
            System.out.print("Enter col:");
            int col = input.nextInt();

            Move move = new Move(row, col, 0, false);
            game.playPiece(game.whosTurn(), move);
            System.out.println("Board:\n" + game.getPentagoBoard().toString());

            if(game.gameOver()) {
                break;
            }

            System.out.print("Enter board to rotate:");
            int board = input.nextInt();

            System.out.print("Twist left:");
            boolean left = input.nextBoolean();

            move = new Move(row, col, board, left);
            game.rotateBoard(move);
            game.turnOver(game.whosTurn());
        }

        System.out.println(game);
        System.out.println("Player" + game.winner() + " has won!");
        System.out.println("Congratulations " + game.getPlayer(game.winner()) + " has won!");
    }
}