package model;

import java.util.ArrayList;
import java.util.Random;

public class PentagoGame {

    private PentagoBoard board;
    private Player humanPlayer;
    private Player aiPlayer;
    private boolean hasPlacedPiece;
    private boolean hasRotatedBoard;
    private Player turn; // the player whos turn it is.
    private Player firstPlayer; // the first player
    private Player secondPlayer; // the first player

    private Move lastMove;

    /**
     * Constructs a pentago game with a new board.
     */
    public PentagoGame() {
        // Default size = 3.
        board = new PentagoBoard();
        lastMove = null;
        hasPlacedPiece = false;
        hasRotatedBoard = true;
    }

    /**
     * Creates a pentagoGame with the initial state of the
     * other game.
     * @param other the other PentagoGame.
     */
    public PentagoGame(PentagoGame other) {
        // Default size = 3.
        board = new PentagoBoard(other.board);
        humanPlayer = other.humanPlayer;
        aiPlayer = other.aiPlayer;
        firstPlayer = other.firstPlayer;
        secondPlayer = other.secondPlayer;
        turn = other.turn;
        lastMove = other.lastMove;
        hasPlacedPiece = other.hasPlacedPiece;
        hasRotatedBoard = other.hasRotatedBoard;
        aiPlayer = other.aiPlayer;
    }

    /**
     * Sets the Human player to the given player.
     * @param player the new human player.
     */
    public void setPlayer(Player player) {
        if(humanPlayer != null) {
            throw new IllegalArgumentException("The player has already been set to "
                    + humanPlayer.getName() + " with the game piece " + humanPlayer.getPiece());
        }
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        humanPlayer = player;
    }

    /**
     * Sets the ai player to the player passed in.
     * @param player the ai player.
     */
    public void setAiPlayer(Player player) {
        if(aiPlayer != null) {
            throw new IllegalArgumentException("The player has already been set to "
                    + aiPlayer.getName() + " with the game piece " + aiPlayer.getPiece());
        }
        if(player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        aiPlayer = player;
    }

    /**
     * Returns the human player.
     * @return the player moves are being decided by a person.
     */
    public Player getPlayer() {
        return humanPlayer;
    }

    /**
     * Returns the ai player.
     * @return the player moves are being decided by a computer.
     */
    public Player getAiPlayer() {
        return aiPlayer;
    }

    /**
     * Returns the player who was chosen to go first.
     * @return the first player.
     */
    public Player getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * Returns the player who goes second.
     * @return the second player.
     */
    public Player getSecondPlayer() {
        return secondPlayer;
    }

    /**
     * Takes in who goest first and sets the turn to the firstPlayer.
     * @param playerToGoFirst the first player
     */
    public void setWhoGoesFirst(Player playerToGoFirst) {
        if(playerToGoFirst.equals(humanPlayer)) {
            firstPlayer = humanPlayer;
            secondPlayer = aiPlayer;
        } else {
            firstPlayer = aiPlayer;
            secondPlayer = humanPlayer;
        }
        turn = firstPlayer;
    }

    /**
     * 'Flips a coin' to decide who goes first.
     */
    public void setWhoGoesFirstRandom() {
        Random ran = new Random();
        int i = ran.nextInt(2) + 1;
        if(i == 1) {
            setWhoGoesFirst(humanPlayer);
        } else {
            setWhoGoesFirst(aiPlayer);
        }
    }

    /**
     * Returns the player who was chosen to go first.
     * @return the player who gets to go first.
     */
    public Player whoGoesFirst() {
        return firstPlayer;
    }

    /**
     * Returns the player whos turn it is.
     * @return the player to make a move.
     */
    public Player whosTurn() {
        return turn;
    }

    /**
     * True if the char piece at the given location is '.' signifying an
     * empty space. False otherwise, there is a players piece
     * @param move
     * @return
     */
    public boolean validMove(Player playerToMakeMove, Move move) {
        if(playerToMakeMove.equals(whosTurn())) {
            // the player can make the move!
            // is the move valid
            char atMoveLocation = board.getCharAtLocation(move.getRow(), move.getCol());
            return atMoveLocation == '.';
        } else {
            return false;
        }
    }

    /**
     * Plays a piece at the given row and col in the move.
     * With the given players piece.
     * @param player the player with a game piece.
     * @param move the move to place the piece on the board.
     */
    public void playPiece(Player player, Move move) {
        // if whos turn it is does not equal the player to make a move...
        if(!validMove(player, move)) {
            throw new IllegalArgumentException("DON'T CHEAT! That is an invalid move for this turn. ");
        }
        board.placePiece(turn.getPiece(), move.getRow(), move.getCol());
        lastMove = move;
        hasPlacedPiece = true;
        hasRotatedBoard = false;
    }

    /**
     * If the user has played a piece or not.
     * @return  if the user played a piece.
     */
    public boolean hasPlacedPiece() {
        return hasPlacedPiece;
    }

    /**
     * If the player has already rotate this board or not in the given turn.
     * @return true if a board has been rotated, false otherwise.
     */
    public boolean hasRotatedBoard() {
        return hasRotatedBoard;
    }

    /**
     * Returns true if the its the Ai's turn.
     * @return if the ai is up.
     */
    public boolean isAiTurn() {
        return whosTurn() == aiPlayer;
    }

    /**
     * Rotates the move according to the given move.
     * @param move the move containing how to move the board.
     */
    public void rotateBoard(Player makingMover, Move move) {
        turnOver();
        board.rotateBoard(move.getBoard(), move.isRotateLeft());
        lastMove = move;
        hasRotatedBoard = true;
    }

    /**
     * After the player has placed a piece and the board has been
     * rotated turnOver should be called to set it to the next turn.
     */
    public void turnOver() {
        if(firstPlayer == whosTurn()) {
            turn = secondPlayer;
        } else {
            turn = firstPlayer;
        }
        hasPlacedPiece = false;
        hasRotatedBoard = false;
    }

    /**
     * Returns the last move made on this board.
     * @return the last move.
     */
    public Move getLastMove() {
        return lastMove;
    }

    /**
     * Returns true if the game is over, false otherwise
     * @return if the game is over or not.
     */
    public boolean gameOver() {
        return winner() != null;
    }

    /**
     * Returns the winning player, if Player.getName == tie, a
     * tie occured. If the player is null no one has won yet.
     *
     * @return a player.
     */
    public Player winner() {
        Player result = null;
        boolean humanResult = board.fiveInARow(humanPlayer.getPiece());
        boolean player2Result = board.fiveInARow(aiPlayer.getPiece());
        if(humanResult && player2Result) {
            result = new Player("Tie", 'T');
        } else if (humanResult && !player2Result) {
            result = humanPlayer;
        } else if (!humanResult && player2Result) {
            result = aiPlayer;
        }
        return result;
    }

    /**
     * Returns the pentago board object of the game.
     * @return the board.
     */
    public PentagoBoard getPentagoBoard() {
        return board;
    }

    /**
     * Calculates the next board state after the given move is made.
     * @param nextMove the next move
     * @return the utility of the new board after the move.
     */
    public int calulateUtility(Move nextMove) {
        PentagoBoard nextBoard = nextMove.getState().getPentagoBoard();
        return getUtility(nextBoard);
    }

    /**
     * Calculates the expected utility (score) from the 'next move'
     * passed in if it was added to the board. For all possible
     * streaks. i.e. 2 in a row, 4 in a row.
     * @param board the board to calculatte
     */
    public int getUtility(PentagoBoard board) {
        char humanPiece = humanPlayer.getPiece();
        char aiPiece = aiPlayer.getPiece();
        int utility = 0;
        boolean won = board.fiveInARow(humanPiece);
        boolean lost = board.fiveInARow(aiPiece);

        if(won) {
            utility = 100;
        } else if (lost) {
            utility = -100;
        } else {
            for(int i = 2; i < 5; i++) {
                int inARows = board.inARows(2, humanPiece);
                int inARowsAi = board.inARows(2, aiPiece);
                utility += (inARows - inARowsAi);
            }
        }
        return utility;
    }

    /**
     * Checks all possible child states of the current game state.
     * @return An arrayList of pentagoGame states.
     */
    public ArrayList<PentagoGame> nextPossibleMoves() {
        ArrayList<PentagoGame> children = new ArrayList<>();
        for(int i = 0; i < 6; i++) { // for each row I can place a piece
            for(int j = 0; j < 6; j++) { // for each column
                for(int k = 0; k < 4; k++ ) { // for each board to rotate
                    for(int d = 0; d < 2; d ++) {// for each direction to rotate
                        boolean leftRotate = false;
                        if(d == 0) {
                            leftRotate = true;
                        }
                        Move move = new Move(i, j, k, leftRotate);
                        if(validMove(whosTurn(), move)) {
                            PentagoGame tempGame = new PentagoGame(this);
                            tempGame.playPiece(whosTurn(), move);
                            tempGame.rotateBoard(whosTurn(), move);
                            tempGame.turnOver();
                            move.setGameState(tempGame);
                            move.setUtility(calulateUtility(move));
                            // todo
                            children.add(tempGame);
                        }
                    }
                }
            }
        }

        return children;
    }

    @Override
    public String toString() {
        StringBuilder state = new StringBuilder();
        state.append("*====* Pentago Game State *====*\n");
        state.append("Players:\n");
        state.append("  Player 1: ");
        state.append(getFirstPlayer());
        state.append("\n  Player 2: ");
        state.append(getSecondPlayer());

        state.append("\n\nIts Player ");
        state.append(whosTurn().getName());
        state.append("'s turn.\n\n");

        state.append("Board:\n");
        state.append(board);
        return state.toString();
    }
}