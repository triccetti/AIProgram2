package model;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class PentagoGame {

    private PentagoBoard board;
    private Player player1;
    private Player player2;
    private boolean hasPlacedPiece;
    private boolean hasRotatedBoard;
    private int turn;
    private Move lastMove;
    private int aiPlayer;

    // Onces the player who goes first is set this cannot change.
    private static int playsFirst;

    public PentagoGame() {
        // Default size = 3.
        board = new PentagoBoard();
        playsFirst = 1;
        turn = playsFirst - 1;
        lastMove = null;
        hasPlacedPiece = false;
        hasRotatedBoard = true;
        aiPlayer = 0;
    }

    public PentagoGame(PentagoGame other) {
        // Default size = 3.
        board = new PentagoBoard(other.board);
        playsFirst = other.playsFirst;
        turn = other.turn;
        player1 = other.player1;
        player2 = other.player2;
        lastMove = other.lastMove;
        hasPlacedPiece = other.hasPlacedPiece;
        hasRotatedBoard = other.hasRotatedBoard;
        aiPlayer = other.aiPlayer;
    }

    public void setPlayer(int p, Player player) {
        if(p > 2 || p < 1) {
            throw new IllegalArgumentException("Player " + p + " does not exist. This is a 2 player game.");
        }
        if (p == 1) {
            player1 = player;
        } else {
            player2 = player;
        }
    }

    public Player getPlayer(int p) {
        if(p == 1) {
            return player1;
        } else {
            return player2;
        }
    }

    /**
     * Should only return 1 or 2 anything else is an error.
     * @return
     */
    public int whoGoesFirst() {
        return playsFirst;
    }

    public int whosTurn() {
        if(turn % 2 == 0) {
            // Player 1's turn
            return 1;
        } else {
            // Players 2's turn
            return 2;
        }
    }

    public boolean validMove(int player, Move move) {
        char atMoveLocation = board.getCharAtLocation(move.getRow(), move.getCol());
        return atMoveLocation == '.';
    }

    public void playPiece(int player, Move move) {
        if(whosTurn() != player || !validMove(player, move)) {
            throw new IllegalArgumentException("DON'T CHEAT! That is an invalid move for this turn turn. ");
        }
        board.placePiece(getCurrentPlayerPiece(), move.getRow(), move.getCol());
        lastMove = move;
        hasPlacedPiece = true;
        hasRotatedBoard = false;
    }

    public char getCurrentPlayerPiece() {
        char piecePlacing;
        if(whosTurn() == 1) {
            piecePlacing = player1.getPiece();
        } else {
            piecePlacing = player2.getPiece();
        }
        return piecePlacing;
    }

    public boolean hasPlacedPiece() {
        return hasPlacedPiece;
    }

    public boolean hasRotatedBoard() {
        return hasRotatedBoard;
    }

    public void setAiPlayer(int p) {
        aiPlayer = p;
    }

    public boolean isAiTurn() {
        return whosTurn() == aiPlayer;
    }
    public void rotateBoard(Move move) {
        board.rotateBoard(move.getBoard(), move.isRotateLeft());
        lastMove = move;
        hasRotatedBoard = true;
    }

    public void turnOver(int player) {
        if(player > 2 || player < 1) {
            throw new IllegalArgumentException("Player" + player + " does not exist. This is a 2 player game.");
        }
        if(!hasPlacedPiece && !hasRotatedBoard) {
            throw new IllegalArgumentException("You must play a piece and rotate a board");
        }
        hasPlacedPiece = false;
        hasRotatedBoard = true;
        turn++;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public boolean gameOver() {
        return board.fiveInARow('B') || board.fiveInARow('W');
    }

    public char winner() {
        char result;
        boolean bResult = board.fiveInARow('B');
        boolean wResult = board.fiveInARow('W');
        if(bResult && wResult) {
            result = 'T';
        } else if (bResult && !wResult) {
            result = 'B';
        } else if (!bResult && wResult) {
            result = 'W';
        } else {
            // no one has won yet.
            result = 0;
        }
        return result;
    }

    public PentagoBoard getPentagoBoard() {
        return board;
    }

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
        char myPiece;
        char otherPiece;
        if(whosTurn() == 1) {
            myPiece = getPlayer(1).getPiece();
            otherPiece = getPlayer(2).getPiece();
        } else {
            myPiece = getPlayer(2).getPiece();
            otherPiece = getPlayer(1).getPiece();
        }
        int utility;

        boolean won = board.fiveInARow(myPiece);
        boolean lost = board.fiveInARow(otherPiece);

        if(won) {
            utility = 100;
        } else if (lost) {
            utility = -100;
        } else {

            int twoInARows = board.inARows(2, myPiece);
            int twoInARowsOpponent = board.inARows(2, otherPiece);

            int threeInARows = board.inARows(3, myPiece);
            int threeInARowsOpponent = board.inARows(3, otherPiece);

            int fourInARows = board.inARows(4, myPiece);
            int fourInARowsOpponent = board.inARows(4, otherPiece);
            utility = (twoInARows - twoInARowsOpponent)
                    + (threeInARows - threeInARowsOpponent)
                    + (fourInARows - fourInARowsOpponent);
        }
        return utility;
    }

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
                            tempGame.rotateBoard(move);
                            tempGame.turnOver(whosTurn());
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
        state.append(player1);
        state.append("\n  Player 2: ");
        state.append(player2);

        state.append("\nPlayer");
        state.append(playsFirst);
        state.append(" goes first.\n");

        state.append("Its Player");
        state.append(whosTurn());
        state.append("'s turn.\n");

        state.append("Board:\n");
        state.append(board);
        return state.toString();
    }
}