package sample;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Food {
    private int score = -50;
    private int length = 0;
    private Position position;
    private Rectangle rectangle;
    private Color color = Color.color(Math.random(), Math.random(), Math.random());
    private AnchorPane pane;
    private Random random = new Random();
    private int size;


    public Food(double xPos, double yPos, AnchorPane pane, double size) {
        this.pane = pane;
        this.size = (int) size;
        position = new Position(xPos,yPos);
        rectangle = new Rectangle(position.getXPos(),position.getYPos(),size,size);
        rectangle.setFill(color);
        pane.getChildren().add(rectangle);
    }

    public Position getPosition() {
        return position;
    }

    public void moveFood(){
        getRandomSpotForFood();
    }

    public int getScore() {
        return score;
    }

    public int getLength() {
        return length;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void getRandomSpotForFood(){
        int positionX = random.nextInt(12);
        int positionY = random.nextInt(12);
        rectangle.setX(positionX * size);
        rectangle.setY(positionY * size);

        position.setXPos(positionX * size);
        position.setYPos(positionY * size);
        /* System.out.println((positionX * size) + "> FOOD <" +(positionY * size)); */

        length++;
        score += 50;
        /* System.out.println("*** Your Score: " + score + " ***II*** Snake's Length: " + length + " ***"); */
    }
}