package model;

import model.GameBoard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class PentagoBoard {

    public static final int NUMBER_BOARDS = 4;
    public static final int STANDARD_SIZE = 3;
    public static final int FIVE_IN_A_ROW = 4;

    private ArrayList<GameBoard> pentagoBoard;
    private int boardSize;
    private int fullBoardSize;

    public PentagoBoard() {
        // Adds a 3 X 3 board game at index i.
        this(STANDARD_SIZE);
    }

    // copies a board
    public PentagoBoard(PentagoBoard other) {
        boardSize = other.boardSize;
        fullBoardSize = boardSize * 2;
        pentagoBoard = new ArrayList<>();
        for (int i = 0; i < NUMBER_BOARDS; i++) {
            pentagoBoard.add(i , new GameBoard(boardSize, boardSize));
        }
        for(int i = 0; i < fullBoardSize; i++) {
            for(int j = 0; j < fullBoardSize; j++) {
                this.placePiece(other.getCharAtLocation(i, j), i, j);
            }
        }
    }

    public PentagoBoard(int size) {
        boardSize = size;
        fullBoardSize = boardSize * 2;
        pentagoBoard = new ArrayList<>();
        for (int i = 0; i < NUMBER_BOARDS; i++) {
            pentagoBoard.add(i , new GameBoard(size, size));
        }
    }

    public int getBoardSize() {
        return fullBoardSize;
    }

    public int getQuadrantSize() {
        return boardSize;
    }

    public int getQuadrantAtLocation(int row, int col) {
        int location;
        if(row < boardSize) { // the piece is on the top half.
            if(col < boardSize) { // the piece is on the left half.
                location = 0;
            } else { // the piece is on the right half.
                location = 1;
            }
        } else { // the piece is on the bottom half.
            if(col < boardSize) { // the piece is on the left half.
                location = 2;
            } else { // the piece is on the right half.
                location = 3;
            }
        }
        return location;
    }

    public char getCharAtLocation(int row, int col) {
        char[][] fullBoard =  getFullBoard();
        return fullBoard[row][col];
    }

    public void placePiece(char player, int row, int col) {
        int quad = getQuadrantAtLocation(row, col);
        int i = row;
        int j = col;
        if(i > boardSize - 1) {
            i = i - boardSize;
        }
        if(j > boardSize - 1) {
            j = j - boardSize;
        }
        pentagoBoard.get(quad).placePiece(player, i, j);
    }

    public void rotateBoard(int boardNumber, boolean rotateLeft) {
        pentagoBoard.get(boardNumber).rotateBoard(rotateLeft);
    }

    private char[][] getFullBoard() {
        char[][] fullBoard = new char[fullBoardSize][fullBoardSize];
        for(int i = 0; i < fullBoardSize; i++) {
            char[] row = getRow(i);
            fullBoard[i] = (new String(row)).toCharArray();
        }
        return fullBoard;
    }

    public boolean fiveInARow(char piece) {
        char[][] fullBoard =  getFullBoard();

        // horizontalCheck
        for (int j = 0; j < fullBoardSize - 4; j++ ){
            for (int i = 0; i < fullBoardSize; i++){
                if (fullBoard[i][j] == piece
                        && fullBoard[i][j+1] == piece
                        && fullBoard[i][j+2] == piece
                        && fullBoard[i][j+3] == piece
                        && fullBoard[i][j+4] == piece) {
                    return true;
                }
            }
        }
        // verticalCheck
        for (int i = 0; i < fullBoardSize-4; i++){
            for (int j = 0; j< fullBoardSize; j++){
                if (fullBoard[i][j] == piece
                        && fullBoard[i+1][j] == piece
                        && fullBoard[i+2][j] == piece
                        && fullBoard[i+3][j] == piece
                        && fullBoard[i+4][j] == piece) {
                    return true;
                }
            }
        }
        // ascendingDiagonalCheck
        for (int i=4; i<fullBoardSize; i++){
            for (int j=0; j<fullBoardSize-4; j++){
                if (fullBoard[i][j] == piece
                        && fullBoard[i-1][j+1] == piece
                        && fullBoard[i-2][j+2] == piece
                        && fullBoard[i-3][j+3] == piece
                        && fullBoard[i-4][j+4] == piece)
                    return true;
            }
        }
        // descendingDiagonalCheck
        for (int i=4; i<fullBoardSize; i++){
            for (int j=4; j<fullBoardSize; j++){
                if (fullBoard[i][j] == piece
                        && fullBoard[i-1][j-1] == piece
                        && fullBoard[i-2][j-2] == piece
                        && fullBoard[i-3][j-3] == piece
                        && fullBoard[i-4][j-4] == piece)
                    return true;
            }
        }
        return false;
    }

    public int inARows(int numInARow, char piece) {
        char[][] fullBoard =  getFullBoard();
        Set<Boolean> inARows = new HashSet<>();
        int utility = 0;
        // horizontalCheck
        for (int j = 0; j < fullBoardSize - numInARow; j++ ){
            for (int i = 0; i < fullBoardSize; i++) {
                for(int k = 0; k < numInARow; k++) {
                    inARows.add(fullBoard[i][j + k] == piece);
                }

                if(!inARows.contains(Boolean.FALSE)) {
                    utility += 1;
                }
                inARows.clear();
            }
        }

        // verticalCheck
        for (int i = 0; i < fullBoardSize - numInARow; i++){
            for (int j = 0; j< fullBoardSize; j++){
                for(int k = 0; k < numInARow; k++) {
                    inARows.add(fullBoard[i + k][j] == piece);
                }
                if(!inARows.contains(Boolean.FALSE)) {
                    utility += 1;
                }
                inARows.clear();
            }
        }
        // ascendingDiagonalCheck
        for (int i=numInARow; i<fullBoardSize; i++){
            for (int j=0; j<fullBoardSize-numInARow; j++){
                for(int k = 0; k < numInARow; k++) {
                    inARows.add(fullBoard[i - k][j + k] == piece);
                }
                if(!inARows.contains(Boolean.FALSE)) {
                    utility += 1;
                }
                inARows.clear();
            }
        }
        // descendingDiagonalCheck
        for (int i=numInARow; i<fullBoardSize; i++){
            for (int j=numInARow; j<fullBoardSize; j++){
                for(int k = 0; k < numInARow; k++) {
                    inARows.add(fullBoard[i - k][j - k] == piece);
                }
                if(!inARows.contains(Boolean.FALSE)) {
                    utility += 1;
                }
                inARows.clear();
            }
        }
        return utility;
    }

    public char[] getRow(int row) {
        int index = 0;
        if(row > boardSize - 1) {
            index = 2;
            row -= boardSize;
        }
        GameBoard left = pentagoBoard.get(index);
        GameBoard right = pentagoBoard.get(index + 1);
        return (left.getStringOfRow(row) + right.getStringOfRow(row)).toCharArray();
    }

    public char[] getCol(int col) {
        int index = 0;
        if(col > boardSize - 1) {
            index = 1;
            col -= boardSize;
        }
        GameBoard top = pentagoBoard.get(index);
        GameBoard bottom = pentagoBoard.get(index + 2);
         return (top.getStringOfCol(col) + bottom.getStringOfCol(col)).toCharArray();
    }


    public String toString() {
        String boardString = "";
        for(int i = 0; i < NUMBER_BOARDS; i += 2) {
            GameBoard left = pentagoBoard.get(i);
            GameBoard right = pentagoBoard.get(i + 1);
            for(int j = 0; j < boardSize; j++) {
                boardString = boardString + left.getStringOfRow(j) + right.getStringOfRow(j) + "\n";
            }
         }
        boardString = boardString.replace((char) 0, '.');
        return boardString;
    }
}
