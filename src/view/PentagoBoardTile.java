package view;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import model.Move;
import model.PentagoGame;
import model.Player;


public class PentagoBoardTile extends TilePane {

    /** The the pieces size. */
    public static final int PIECE_SIZE = 80;

    private char piece;
    private ImageView tile;
    private int row;
    private int col;

    public PentagoBoardTile(char piece, int row, int col) {
        super();
        setHeight(PIECE_SIZE);
        setWidth(PIECE_SIZE);
        setPrefSize(PIECE_SIZE, PIECE_SIZE);
        this.piece = piece;
        this.row = row;
        this.col = col;
        tile = getIcon(piece);
        getChildren().add(tile);
        setRegularIcon();
    }

    public void setPiece(char piece) {
        this.piece = piece;
        tile = getIcon(piece);
        refresh();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setHoverIcon() {
        tile = getHoverIcon(piece);
        refresh();
    }

    public void setRegularIcon() {
        tile = getIcon(piece);
        refresh();
    }

    private void refresh() {
        getChildren().clear();
        getChildren().add(tile);
    }

    private ImageView getIcon(char piece) {
        if(piece == 'W') {
            return buildIcon("./view/whitePiece.png");
        } else if(piece == 'B') {
            return buildIcon("./view/blackPiece.png");
        } else {
            return buildIcon("./view/emptySpace.png");
        }
    }

    private ImageView getHoverIcon(char piece) {
        if(piece == 'W') {
            return buildIcon("./view/whitePieceHover.png");
        } else if(piece == 'B') {
            return buildIcon("./view/blackPieceHover.png");
        } else {
            return buildIcon("./view/emptySpaceHover.png");
        }
    }

    private static ImageView buildIcon(String path) {
        Image i = new Image(path);
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
        // You can set width and height
        imageView.setFitHeight(PIECE_SIZE);
        imageView.setFitWidth(PIECE_SIZE);
        imageView.setImage(i);
        return imageView;
    }


}
