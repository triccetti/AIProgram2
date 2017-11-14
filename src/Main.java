import model.*;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);

        PentagoGame game = new PentagoGame();
        game.setPlayer(new Player("Taylor", 'B'));
        game.setAiPlayer(new AiPlayer("AI", 'W', 2, true));
        AiPlayer ai = (AiPlayer) game.getAiPlayer();

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
            game.rotateBoard(game.whosTurn(), move);
            game.turnOver();
        }
        Player result = game.winner();
        if(result != null) {
            if (result.getName().equals("Tie")) {
                System.out.println("Game over! There was a tie!");
            } else {
                System.out.println("Congratulations " + game.winner() + " has won!");
            }
        }
    }
}