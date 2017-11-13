package model;

public class Move implements Comparable {
    private int row;
    private int col;
    private int boardToRotate;
    private boolean rotateLeft;
    private int utility;
    private PentagoGame state;


    public Move(int row, int col, int boardToRotate, boolean rotateLeft) {
        this.row = row;
        this.col = col;
        this.boardToRotate = boardToRotate;
        this.rotateLeft = rotateLeft;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getBoard() {
        return boardToRotate;
    }

    public boolean isRotateLeft() {
        return rotateLeft;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public void setGameState(PentagoGame state) {
        this.state = state;
    }

    public PentagoGame getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Row: " + row + " Col: " + col + " Quadrant: " + boardToRotate
                + " Rotating Left: " + rotateLeft + " Utility: " + utility;
    }

    @Override
    public int compareTo(Object o) {
        Move other = (Move) o;
        if(this.utility > other.utility) {
            return 1;
        } else if (this.utility < other.utility) {
            return -1;
        } else {
            return 0;
        }
    }
}

