package model;

/**
 *
 * @author Taylor Riccetti
 */

public class GameBoard {

    private char[][] board;
    private int height;
    private int width;

    public GameBoard(int height, int width) {
         board = new char[height][width];
         this.height = height;
         this.width = width;
    }

    public char[][] rotateBoard(boolean twistLeft) {
        char[][] newBoard = new char[board.length][board[0].length];
        if(twistLeft) {
            // twistLeft;
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    newBoard[i][j] = board[height - j - 1][i];
                }
            }
        } else {
            // twistRight
            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    newBoard[i][j] = board[j][height - i - 1];
                }
            }
        }
        board = newBoard;
        return newBoard;
    }

    public char[][] placePiece(char player, int row, int col) {
        board[row][col] = player;
        return board;
    }

    public char[] getRow(int row) {
        return board[row];
    }

    public char[] getCol(int col) {
        StringBuilder column = new StringBuilder();
        for(int i = 0; i < height; i++) {
            column.append(board[i][col]);
        }
        return column.toString().toCharArray();
    }

    public String getStringOfRow(int row) {
        char[] rowOfBoard = getRow(row);
        String s = new String(rowOfBoard);
        s = s.replace(Character.MIN_VALUE, '.');
        return s;
    }

    public String getStringOfCol(int col) {
        char[] colOfBoard = getCol(col);
        String s = new String(colOfBoard);
        s = s.replace(Character.MIN_VALUE, '.');
        return s;
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();

        for(int i = 0; i < height + 2; i++) {
            for(int j = 0; j < width + 2; j++) {
                if((i == 0 ||  i == height + 1 ) && (j == 0 || j == width + 1)) {
                    boardString.append('+');
                } else if(i == 0 || i == height + 1) {
                    boardString.append('-');
                } else if(j == 0 || j == width + 1) {
                    boardString.append('|');
                } else {
                    char c = board[i - 1][j - 1];
                    if(c != 0) {
                        boardString.append(c);
                    } else {
                        boardString.append('.');
                    }
                }
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }

}
