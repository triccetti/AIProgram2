package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import model.AiPlayer;
import model.Move;
import model.PentagoGame;
import model.Player;

public class PentagoGui extends Application {
    private final Background HOVER_BACKGROUND = new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY));

    /** A constant for the font of header text. */
    private static final Font HEADER_FONT = Font.font("Century Gothic", FontWeight.NORMAL, 20);

    /** The scenes width. */
    private static final int SCENE_WIDTH = 650;

    /** The scenes height. */
    private static final int SCENE_HEIGHT = 600;

    /** The the pieces size. */
    private static final int PIECE_SIZE = 80;

    /** The main stage for the gui. */
    private Stage mainScreen;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainScreen = primaryStage;
        mainScreen.setTitle("Pentago!");
        mainScreen.setScene(new Scene(setup(), SCENE_WIDTH, SCENE_HEIGHT));
        mainScreen.show();
    }

    private GridPane setup() {
        GridPane startPane = new GridPane();
        GridPane setUpPane = new GridPane();
        setUpPane.setAlignment(Pos.CENTER);
        setUpPane.setVgap(10);
        setUpPane.setHgap(10);
        setUpPane.setPadding(new Insets(25));
        // Header
        Text setUpMessage = new Text("Setup Game");
        setUpMessage.setFont(HEADER_FONT);

        // Player name
        Label playerLabel = new Label("Your Name:");
        TextField playerName = new TextField();

        setUpPane.add(setUpMessage, 0, 0, 2, 1);
        setUpPane.add(playerLabel, 0, 1);
        setUpPane.add(playerName, 1, 1);


        // pieces
        Label pieceLabel = new Label("Your game piece:");

        ToggleGroup pieceGroup = new ToggleGroup();
        RadioButton pieceW = new RadioButton("White");
        RadioButton pieceB = new RadioButton("Black");
        pieceW.setToggleGroup(pieceGroup);
        pieceW.setGraphic(buildIcon("./view/whitePiece.png"));

        pieceB.setToggleGroup(pieceGroup);
        pieceB.setGraphic(buildIcon("./view/blackPiece.png"));


        pieceW.setSelected(true);

        setUpPane.add(pieceLabel, 0, 2);
        setUpPane.add(pieceW, 1, 2);
        setUpPane.add(pieceB, 1, 3);

        // order
        Label orderLabel = new Label("Do you want to be the:");
        ToggleGroup order = new ToggleGroup();
        RadioButton firstPlayer = new RadioButton("First Player");
        RadioButton secondPlayer = new RadioButton("Second Player");
        RadioButton random = new RadioButton("You decide! (Random)");
        firstPlayer.setToggleGroup(order);
        secondPlayer.setToggleGroup(order);
        random.setToggleGroup(order);
        random.setSelected(true);

        setUpPane.add(orderLabel, 0, 4);
        setUpPane.add(firstPlayer, 1, 5);
        setUpPane.add(secondPlayer, 1, 6);
        setUpPane.add(random, 1, 7);

        // Ai
        Label aiLabel = new Label("Ai:");
        ToggleGroup ai = new ToggleGroup();
        RadioButton miniMax = new RadioButton("MiniMax");
        RadioButton alphaBeta = new RadioButton("Alphabeta");
        miniMax.setToggleGroup(ai);
        alphaBeta.setToggleGroup(ai);
        miniMax.setSelected(true);

        setUpPane.add(aiLabel, 0, 8);
        setUpPane.add(miniMax, 1, 9);
        setUpPane.add(alphaBeta, 1, 10);

        // Difficultly level
        Label depth = new Label("Difficulty (depth level, 1 is easiest 5 is hardest.):");
        TextField depthTextField = new TextField();
        depthTextField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        depthTextField.setText("2");
        setUpPane.add(depth, 0, 11);
        setUpPane.add(depthTextField, 1,11);

        Button startButton = new Button("Start Game");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setUpGame(playerName.getText(),
                        Integer.parseInt(depthTextField.getText()),
                        pieceGroup.getSelectedToggle(),
                        order.getSelectedToggle(),
                        ai.getSelectedToggle());
            }
        });

        setUpPane.add(startButton, 1, 12);
        startPane.add(setUpPane, 0, 1);
        return startPane;
    }

    private void setUpGame(String name, int depth, Toggle piece, Toggle order, Toggle ai) {
        Player currentPlayer;
        Player aiPlayer;
        char aiPiece;
        if(piece.toString().contains("Black")) {
            currentPlayer = new Player(name, 'B');
            aiPiece = 'W';
        } else {
            currentPlayer = new Player(name, 'W');
            aiPiece = 'B';
        }

        // Sets the aiPlayer to the other options.
        aiPlayer = new AiPlayer("Comp", aiPiece, depth, ai.toString().contains("MiniMax"));

        // Makes the game.
        PentagoGame game = new PentagoGame();
        game.setPlayer(currentPlayer);
        game.setAiPlayer(aiPlayer);
        if(order.toString().contains("First Player")) {
            game.setWhoGoesFirst(currentPlayer);
        } else if(order.toString().contains("Second Player")) {
            game.setWhoGoesFirst(aiPlayer);
        } else { // random
            game.setWhoGoesFirstRandom();
        }
        showGameState(game);

        // wait

    }

    private void showGameState(PentagoGame game) {
        GridPane gameDisplay = new GridPane();

        gameDisplay.add(new Label("It is " + game.whosTurn().getName() + "'s turn!"), 0,0);

        mainScreen.setScene(new Scene(gameDisplay,SCENE_WIDTH, SCENE_HEIGHT));
    }

    private void showGameOver() {
    }


    private static ImageView buildIcon(String path) {
        Image i = new Image(path);
        ImageView imageView = new ImageView();
        // You can set width and height
        imageView.setFitHeight(PIECE_SIZE);
        imageView.setFitWidth(PIECE_SIZE);
        imageView.setImage(i);
        return imageView;
    }
}
