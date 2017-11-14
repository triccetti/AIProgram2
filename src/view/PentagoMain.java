package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import model.*;

import java.util.ArrayList;
import java.util.Random;

public class PentagoMain extends Application {
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


    private double mouseDragStartX;
    private double mouseDragEndX;

    private Move move;

    ArrayList<PentagoBoardTile> squares;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        squares = new ArrayList<>();

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
        System.out.println(game);
        boardSetup(game);
    }

    private void boardSetup(PentagoGame game) {
        // creates a pane to hold the game state
        BorderPane gameDisplay = new BorderPane();
        mainScreen.setScene(new Scene(gameDisplay, SCENE_WIDTH, SCENE_HEIGHT));


        Label playerTurn = new Label("It's " + game.whosTurn().getName() + " turn.");
        Label info = new Label("");
        GridPane textPane = new GridPane();
        textPane.add(playerTurn, 0,0);
        textPane.add(info, 0,1);
        gameDisplay.setTop(textPane);


        gameDisplay.setCenter(getBoardDisplay(game, info, gameDisplay));

        Button endTurn = new Button("End Turn");
        gameDisplay.setRight(endTurn);
        endTurn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!game.isAiTurn()) {
                    endTurn.setDisable(true); // button is disabled on ai's turn.
                    playerTurn.setText("Its " + game.getAiPlayer().getName() + "'s turn.");
                    game.turnOver();
                    aiTurn(game, endTurn, info, gameDisplay);
                    endTurn.setDisable(false);
                    gameDisplay.setCenter(getBoardDisplay(game, info, gameDisplay));

                } else { // its now the ais turn
                    playerTurn.setText("Its " + game.getPlayer().getName() + "'s turn.");

                    if(game.hasPlacedPiece() && game.hasRotatedBoard()) {
                        game.turnOver();
                        endTurn.setDisable(false);
                        gameDisplay.setCenter(getBoardDisplay(game, info, gameDisplay));
                    }
                }
            }
        });

        // after setup if its the ais turn first.. start ai
        if(game.whosTurn().equals(game.getAiPlayer())) {
            aiTurn(game, endTurn, info, gameDisplay);
        }
    }

    private void aiTurn(PentagoGame game, Button turn, Label info, BorderPane gameDisplay) {
        move = ((AiPlayer) game.getAiPlayer()).getMove(game);
        System.out.println("ai is making: " + move);
        int board = game.getPentagoBoard().getCharAtLocation(move.getRow(), move.getCol());
        squares.get((move.getRow() * 6 ) + move.getCol()).setPiece(game.whosTurn().getPiece());
        game.playPiece(game.whosTurn(), move);
        if(game.gameOver()) {
            gameOver(game);
        }
        game.rotateBoard(game.getAiPlayer(), move);
        gameDisplay.setCenter(getBoardDisplay(game, info, gameDisplay));

      //  turn.fire();
    }

    private void gameOver(PentagoGame game) {
        if(game.winner().getName().equals("Tie")) {
            mainScreen.setScene(new Scene(new TextField("GAME OVER!\nIt was a Tie!")));
        } else {
            mainScreen.setScene(new Scene(new TextField("GAME OVER!\n" + game.winner().getName() + " won!")));
        }
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

    public FlowPane getBoardDisplay(PentagoGame game, Label info, BorderPane gameDisplay) {
        FlowPane boardDisplay = new FlowPane();
        boardDisplay.setHgap(5);
        boardDisplay.setVgap(5);

        PentagoBoard board = game.getPentagoBoard();
        int boardSize = board.getBoardSize();
        int quadSize = board.getQuadrantSize();
        boardDisplay.setPrefWrapLength(boardSize * PentagoBoardTile.PIECE_SIZE);

        ArrayList<FlowPane> quads = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            FlowPane f = new FlowPane();
            f.setPrefWrapLength(quadSize * PentagoBoardTile.PIECE_SIZE);
            f.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mouseDragStartX = mouseEvent.getX();
                    mouseEvent.consume();
                }
            });
            f.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(game.hasPlacedPiece() && !game.isAiTurn()) {
                        f.setBackground(HOVER_BACKGROUND);
                    }
                }
            });
            f.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(game.hasPlacedPiece() && !game.isAiTurn()) {
                        f.setBackground(null);
                    }
                }
            });

            f.setOnMouseReleased(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent mouseEvent) {
                    mouseDragEndX = mouseEvent.getX();
                    if(game.hasPlacedPiece() && !game.isAiTurn() && !game.hasRotatedBoard()) {
                        int board = quads.indexOf((FlowPane) mouseEvent.getSource());
                        move = new Move(0,0, board, (mouseDragStartX < mouseDragEndX));
                        game.rotateBoard(game.getPlayer(), move);
                        gameDisplay.setCenter(getBoardDisplay(game, info, gameDisplay));

                        System.out.println("You are rotating " + board + " left? - " + !move.isRotateLeft());
                    }
                }
            });
            quads.add(f);
            boardDisplay.getChildren().add(quads.get(i));
        }
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                PentagoBoardTile t = new PentagoBoardTile(board.getCharAtLocation(i, j), i, j);
                squares.add(t);
                t.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!game.hasPlacedPiece()) {
                            t.setHoverIcon();
                        }
                    }
                });

                t.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(!game.hasPlacedPiece()) {
                            t.setRegularIcon();
                        }
                    }
                });

                t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // if its the users turn, not the ai's.
                        if(game.whosTurn().equals(game.getPlayer()) && !game.hasPlacedPiece()) {
                            // board should not be rotated here.
                            Move move = new Move(t.getRow(), t.getCol(), 0, false);
                            if(game.validMove(game.getPlayer(), move)) {
                                System.out.println("you are putting a piece at " + t.getRow() + " " + t.getCol());
                                t.setPiece(game.whosTurn().getPiece());
                                game.playPiece(game.whosTurn(), move);
                            } else {
                                info.setText("There is already a piece in that spot!");
                            }
                        }
                    }
                });
                boardDisplay.getChildren().add(t);
                int quad = board.getQuadrantAtLocation(i,j);
                quads.get(quad).getChildren().add(t);

            }
        }
        return boardDisplay;
    }
}
