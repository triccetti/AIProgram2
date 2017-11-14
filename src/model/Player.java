package model;

import javafx.scene.paint.Color;

public class Player implements Comparable {

    private char piece;
    private String playerName;

    public Player(String name, char piece) {
        this.piece = piece;
        playerName = name;
    }

    public char getPiece() {
        return piece;
    }

    public String getName() {
        return playerName;
    }

    @Override
    public String toString() {
        return "Name: " + playerName + " Piece: " + piece;
    }

    @Override
    public int compareTo(Object o) {
        Player other = (Player) o;
        if(this.playerName.equals(other.playerName)
                && this.piece == other.getPiece()) {
            return 0;
        } else {
            return this.playerName.compareTo(other.playerName);
        }
    }
}
