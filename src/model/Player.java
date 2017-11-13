package model;

import javafx.scene.paint.Color;

public class Player {

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
}
