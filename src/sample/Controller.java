package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Connection connn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    private Stage stage;
    private Scene scene;
    private Parent root;

    //A snake body part is 50x50
    private final Double snakeSize = 50.;
    //The head of the snake is created, at position (250,250)
    private Rectangle snakeHead;
    //First snake tail created behind the head of the snake
    private Rectangle snakeTail_1;
    //x and y position of the snake head different from starting position
    double xPos;
    double yPos;


    //Food
    Food food;

    //Direction snake is moving at start
    private Direction direction;

    //List of all position of thew snake head
    private final List<Position> positions = new ArrayList<>();

    //List of all snake body parts
    private final ArrayList<Rectangle> snakeBody = new ArrayList<>();

    //Game ticks is how many times the snake have moved
    private int gameTicks;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button startButton;

    @FXML
    private Button addButton;

    @FXML
    private TextField txt_name;

    @FXML
    private Label showMessageLabel;

    @FXML
    private AnchorPane regPane;

    @FXML
    private Button goBackButton;

    @FXML
    private Button regButton;

    @FXML
    private Button topScoresButton;

    @FXML
    private Button leaveButton;

    Timeline timeline;

    private boolean canChangeDirection;

    @FXML
    void switchToTopScores(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("topScore.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void start(MouseEvent event) {


        for (Rectangle snake : snakeBody) {
            anchorPane.getChildren().remove(snake);
        }

        showMessageLabel.setText("");

        gameTicks = 0;
        positions.clear();
        snakeBody.clear();
        snakeHead = new Rectangle(250, 250, snakeSize, snakeSize);
        snakeTail_1 = new Rectangle(snakeHead.getX() - snakeSize, snakeHead.getY(), snakeSize, snakeSize);
        xPos = snakeHead.getLayoutX();
        yPos = snakeHead.getLayoutY();
        direction = Direction.RIGHT;
        canChangeDirection = true;
        food.moveFood();

        snakeBody.add(snakeHead);
        snakeHead.setFill(Color.DARKGREEN);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        snakeBody.add(snakeTail_1);

        anchorPane.getChildren().addAll(snakeHead, snakeTail_1);
        HideButton();
    }

    @FXML
    void cancel(MouseEvent event) {
        regPane.setVisible(false);
        startButton.setVisible(true);
        topScoresButton.setVisible(true);

        food.setScore(-50);
        food.setLength(0);
    }


    @FXML
    void regButton(MouseEvent event) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx-project", "faridun", "faridun12332112");
            psInsert = connection.prepareStatement("INSERT INTO users (name, score) VALUES (?, ?)");
            psInsert.setString(1, txt_name.getText());
            psInsert.setString(2, String.valueOf(food.getScore()));
            psInsert.execute();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reg");
            alert.setHeaderText(null);
            alert.setContentText("Successfully added!");
            alert.showAndWait();

            regPane.setVisible(false);
            startButton.setVisible(true);
            topScoresButton.setVisible(true);

            food.setScore(-50);
            food.setLength(0);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reg");
            alert.setHeaderText(null);
            alert.setContentText("NOT ADDED");
            alert.showAndWait();

            regPane.setVisible(false);
            startButton.setVisible(true);
            topScoresButton.setVisible(true);

            food.setScore(-50);
            food.setLength(0);
        }
    }

    public void showRegPane() {
        startButton.setVisible(false);
        addButton.setVisible(false);
        regPane.setVisible(true);
    }

    public void HideButton() {
        startButton.setVisible(false);
        topScoresButton.setVisible(false);
        addButton.setVisible(true);
        snakeHead.setVisible(true);

        startButton.setText("RESTART");
    }

    public void ShowButtons() {
        startButton.setVisible(true);
        addButton.setVisible(false);
        snakeHead.setVisible(false);
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        connn = DBConnect.getConnection();
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            positions.add(new Position(snakeHead.getX() + xPos, snakeHead.getY() + yPos));
            moveSnakeHead(snakeHead);
            for (int i = 1; i < snakeBody.size(); i++) {
                moveSnakeTail(snakeBody.get(i), i);
            }
            canChangeDirection = true;
            //System.out.println((xPos + snakeHead.getX()) + "-----" + (yPos + snakeHead.getY()));
            eatFood();
            gameTicks++;
            if(checkIfGameIsOver(snakeHead)){
                timeline.stop();
            }
        }));
        food = new Food(-50,-50,anchorPane,snakeSize);
    }

    //Change position with key pressed
    @FXML
    void moveSquareKeyPressed(KeyEvent event) {
        if(canChangeDirection){
            if (event.getCode().equals(KeyCode.W) && direction != Direction.DOWN) {
                direction = Direction.UP;
            } else if (event.getCode().equals(KeyCode.S) && direction != Direction.UP) {
                direction = Direction.DOWN;
            } else if (event.getCode().equals(KeyCode.A) && direction != Direction.RIGHT) {
                direction = Direction.LEFT;
            } else if (event.getCode().equals(KeyCode.D) && direction != Direction.LEFT) {
                direction = Direction.RIGHT;
            }
            canChangeDirection = false;
        }
    }

    //Create another snake body part
    @FXML
    void addBodyPart(ActionEvent event) {
        addSnakeTail();
        food.setLength(food.getLength() + 1);
    }

    //Snake head is moved in the direction specified
    private void moveSnakeHead(Rectangle snakeHead) {
        if (direction.equals(Direction.RIGHT)) {
            xPos = xPos + snakeSize;
            snakeHead.setTranslateX(xPos);
        } else if (direction.equals(Direction.LEFT)) {
            xPos = xPos - snakeSize;
            snakeHead.setTranslateX(xPos);
        } else if (direction.equals(Direction.UP)) {
            yPos = yPos - snakeSize;
            snakeHead.setTranslateY(yPos);
        } else if (direction.equals(Direction.DOWN)) {
            yPos = yPos + snakeSize;
            snakeHead.setTranslateY(yPos);
        }
    }

    //A specific tail is moved to the position of the head x game ticks after the head was there
    private void moveSnakeTail(Rectangle snakeTail, int tailNumber) {
        double yPos = positions.get(gameTicks - tailNumber + 1).getYPos() - snakeTail.getY();
        double xPos = positions.get(gameTicks - tailNumber + 1).getXPos() - snakeTail.getX();
        snakeTail.setTranslateX(xPos);
        snakeTail.setTranslateY(yPos);
    }

    //New snake tail is created and added to the snake and the anchor pane
    private void addSnakeTail() {
        Rectangle rectangle = snakeBody.get(snakeBody.size() - 1);
        Rectangle snakeTail = new Rectangle(
                snakeBody.get(1).getX() + xPos + snakeSize,
                snakeBody.get(1).getY() + yPos,
                snakeSize, snakeSize);
        snakeBody.add(snakeTail);
        anchorPane.getChildren().add(snakeTail);
    }

    public boolean checkIfGameIsOver(Rectangle snakeHead) {
        if (xPos > 300 || xPos < -250 ||yPos < -250 || yPos > 300) {
            System.out.println("Out of Arena. Game over!");
            showMessageLabel.setText(String.valueOf("Your Score: " + food.getScore()));
            showRegPane();
            for(Rectangle i:snakeBody) {
                i.setVisible(false);
            }
            return true;
        } else if (snakeHitItSelf()){
            return true;
        }
        return false;
    }

    public boolean snakeHitItSelf(){
        int size = positions.size() - 1;
        if(size > 2){
            for (int i = size - snakeBody.size(); i < size; i++) {
                if(positions.get(size).getXPos() == (positions.get(i).getXPos())
                        && positions.get(size).getYPos() == (positions.get(i).getYPos())){
                    System.out.println("Hit. Game Over!");
                    showMessageLabel.setText(String.valueOf("Your Score: " + food.getScore()));
                    showRegPane();

                    for(Rectangle j:snakeBody) {
                        j.setVisible(false);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void eatFood(){
        if(xPos + snakeHead.getX() == food.getPosition().getXPos() && yPos + snakeHead.getY() == food.getPosition().getYPos()){
            foodCantSpawnInsideSnake();
            addSnakeTail();
        }
    }

    private void foodCantSpawnInsideSnake(){
        food.moveFood();
        while (isFoodInsideSnake()){
            food.moveFood();
        }


    }

    private boolean isFoodInsideSnake(){
        int size = positions.size();
        if(size > 2){
            for (int i = size - snakeBody.size(); i < size; i++) {
                if(food.getPosition().getXPos() == (positions.get(i).getXPos())
                        && food.getPosition().getYPos() == (positions.get(i).getYPos())){
                    /* System.out.println("CHECK"); */
                    return true;
                }
            }
        }
        return false;
    }


}