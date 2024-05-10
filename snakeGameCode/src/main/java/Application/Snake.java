package Application;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Snake extends JPanel implements ActionListener, KeyListener {
    // private class Tile where an instance of a method called Tile
    // can access these variables.
    private static class Tile {
        int x;
        int y;

        // Constructor class stores the x and y coordinates of a tile
        Tile (int x, int y) {
            this.x = x; // refers to the current object x
            this.y = y; // refers to the current object y
        }
    }
    // board Dimensions
    int boardWidth;
    int boardHeight;
    int tile = 25;

    // Snake Parts
    Tile headOfSnake;
    ArrayList<Tile> bodySnake; // Created an array list to store the body parts of the snake

    // Apple
    Tile apple1;
    Tile apple2;
    Random random = new Random();

    //Timer
    Timer timer;
    int moveX;
    int moveY;
    boolean gameOver = false;

    Snake(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth; // refers to the field boardWidth
        this.boardHeight = boardHeight; // refers to the field boardHeight
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        headOfSnake = new Tile(tile - 10, tile - 10);
        bodySnake = new ArrayList<Tile>();

        apple1 = new Tile(10,10);
        apple2 = new Tile(15,15);
        apple1();
        apple2();

        moveX = 0;
        moveY = 0;

        timer = new Timer(100, this);
        timer.start(); // start the timer
        addKeyListener(this);
        setFocusable(true);
    }
    public void paint(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    //we invoke a method that generates a new Apple when called
    public Tile newApple() {
        Tile newApple;
        do {
            newApple = new Tile(random.nextInt(boardWidth/tile), random.nextInt(boardHeight/tile)); //generates a tile within the boundaries set
        } while (touch(newApple, apple2) || touch(newApple, apple1)); //to make sure that the generated apple doesn't overlap with one another
            return newApple;
    }
    public void apple1() {
        apple1 = newApple();
    }
    public void apple2() {
        apple2 = newApple();
    }
    //to either increase or decrease the speed of the snake based on the amount of apples it consumed
    public void initialSpeed() {
        if (bodySnake.size() >= 40 )  {
            timer.setDelay(100);
        } else {
            timer.setDelay(timer.getDelay() - 1); //speed increases the more apples is consumed
        }
    }
    public boolean touch(Tile tile1, Tile tile2) {
        if (tile1.x == tile2.x && tile1.y == tile2.y) {
            return true;
        } else {
            return false;
        }
    }
    public void draw(Graphics g) {
        super.paintComponent(g);
        //head
        g.setColor(Color.GREEN);
        g.fillRect(headOfSnake.x*tile, headOfSnake.y*tile, tile, tile);

        //apple
        g.setColor(Color.RED);
        g.fillRect(apple1.x * tile, apple1.y * tile, tile, tile);
        g.fillRect(apple2.x * tile, apple2.y * tile, tile, tile);

        //Drawing the tail of the head of the snake
        for (Tile tailPart : bodySnake) {
            g.setColor(Color.GREEN);
            g.fillRect(tailPart.x * 25, tailPart.y * 25, tile, tile);
        }
        for (Tile tailPart : bodySnake) {
            if (touch(headOfSnake, tailPart)) {
                gameOver = true;
            }
        }
        //if the snake goes out of bounds, gameOver
        if (headOfSnake.x*tile < 0 || headOfSnake.x*tile>boardWidth || headOfSnake.y*tile < 0 || headOfSnake.y*tile>boardHeight) {
            gameOver = true;
        }
        g.setFont(new Font("ARIAL", Font.BOLD, 20));
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over :( ", tile-10,tile);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Score: " + String.valueOf(bodySnake.size()), tile - 10, tile);
        }

        g.setFont(new Font("ARIAL", Font.BOLD, 40));
        if (gameOver) {
            g.drawString("High Score: " + String.valueOf(bodySnake.size()), tile*7, tile*13);
        }
    }
    public void move() {
        // Move the head
        headOfSnake.x += moveX;
        headOfSnake.y += moveY;
        if (touch(headOfSnake, apple1) || touch(headOfSnake, apple2)) {
            bodySnake.add(new Tile(headOfSnake.x, headOfSnake.y)); // Extend the body by adding a new tile at the old position of the head
            if (touch(headOfSnake,apple1)) {
                apple1();
                initialSpeed(); // speed of the snake after eating an apple
            } else {
                apple2();
                initialSpeed(); // speed of the snake after eating an apple
            }
        }
        for (int i = bodySnake.size() - 1; i >= 0; i--) {
            Tile tailPart = bodySnake.get(i);
            if (i == 0) {
                tailPart.x = headOfSnake.x; //get the x position of the headOfSnake
                tailPart.y = headOfSnake.y; //get the y position of the headOfSnake
            } else {
                Tile tail = bodySnake.get(i - 1); //To get the body before the increased tile of the snake
                tailPart.x = tail.x; //stores the x position of the tile before the snake grows
                tailPart.y = tail.y; //stores the y position of the tile before the snake grows
            }
        }
        //makes a new tile follow the head of the snake
        if (!bodySnake.isEmpty()) {
            bodySnake.getFirst().x = headOfSnake.x - moveX; //
            bodySnake.getFirst().y = headOfSnake.y - moveY;
        }
        //generates a new apple if the tail overlaps with an apple
        for(Tile bodySnake : bodySnake) {
            if (touch(bodySnake, apple1)) {
                apple1();
            } else if (touch(bodySnake, apple2)){
                apple2();
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        while (gameOver) {
            timer.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && moveY != 1) {
            moveX = 0;
            moveY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && moveY != -1) {
            moveX = 0;
            moveY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && moveX != -1) {
            moveX = 1;
            moveY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && moveX != 1) {
            moveX = -1;
            moveY = 0;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
