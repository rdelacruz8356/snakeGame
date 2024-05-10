package Application;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;

        JFrame board = new JFrame("Snake Game");
        board.setSize(boardWidth, boardHeight);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setVisible(true);
        board.setResizable(true);

        Snake snake = new Snake(boardWidth, boardHeight);
        board.add(snake);
        board.pack();
        snake.requestFocus(); //this statement listens to the keys pressed
    }
}