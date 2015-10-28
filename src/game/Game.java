/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.Snake.Food;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Adam
 */
public class Game extends JFrame implements Runnable, KeyListener{
    
    public Thread thread;
    
    private Snake snake;
    private int score;
    private boolean randomBackground;
    
    public static Dimension dim;    
    public static boolean gameOver;
    private static Food food;
    
    private final int SNAKE_SIZE;
    private final Graphics2D g2;
    private final int FPS;
    private final Random rand = new Random();
    
    public Game(){
        setTitle("Rainbow Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dim = new Dimension(400,400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width/2 - dim.width/2, screenSize.height/2 - dim.height/2);
        setSize(dim.width,dim.height);
        setResizable(false);
        setVisible(true);
        
        SNAKE_SIZE = 5;
        FPS = 10;
        g2 = (Graphics2D) this.getGraphics();
        thread = new Thread(this);
        addKeyListener(this);
        snake = new Snake(SNAKE_SIZE);
        food = new Food();
        food.setLocation(new Point(30+rand.nextInt(30)*Snake.SCALE, 30+rand.nextInt(30)*Snake.SCALE));
    }

    @Override
    public void run() {
        gameOver = true;
        while (thread.isAlive()){
            score = 0;
            
            //Variables for game clock
            long start, now, elapsedTime;
            long targetTime = 1000000000/FPS;
            long waitTime=0;
            long totalTime = 0;
            int fpsCount = 0;
            while (!gameOver){
                start = System.nanoTime();

                gameUpdate();
                gameRender();

                now = System.nanoTime();
                
                //Game clock based on targetTime (FPS)
                elapsedTime = now-start;
                waitTime = targetTime - elapsedTime;
                try{
                    Thread.sleep(waitTime/1000000);
                }catch(Exception e){}
                fpsCount++;
                totalTime += System.nanoTime() - start;
                if (totalTime > 1000000000){
                    fpsCount = 0;
                    totalTime = 0;
                }
            }
            while (gameOver){
                //Shows end screen for 3 seconds
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
                endGame();   
            }
        }
        
    }
    
    
    //Renders game elements and background
    private void gameRender() {
        
        //Switches between Colorful Background and Plain Background
        if (randomBackground == true)
            g2.setColor(Snake.getRandomColor());
        else
            g2.setColor(Color.WHITE);
        
        g2.fillRect(0, 0, dim.width, dim.height);
        
        snake.draw(g2);
        food.draw(g2);
        
    }
    
    //Moves snake, if food eaten: snake part added, new food created.
    private void gameUpdate() {
        snake.move();
        if (snake.head.contains(Game.food.pos)){
                snake.parts.add(new Rectangle(snake.head.x, snake.head.y, snake.SCALE, snake.SCALE));
                snake.head = (new Rectangle(Game.food.pos.x, Game.food.pos.y, snake.SCALE, snake.SCALE));
                Point p;
                food.setLocation(p = new Point(30+rand.nextInt(30)*Snake.SCALE, 30+rand.nextInt(30)*Snake.SCALE));
                score += 10;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Not implemented
    }

    @Override   //Sets WASD and arrow keys as controls.
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (snake.dir != Snake.Direction.DOWN)
                    snake.dir = Snake.Direction.UP;
                break;
                
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (snake.dir != Snake.Direction.UP)
                    snake.dir = Snake.Direction.DOWN;
                break;
            
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (snake.dir != Snake.Direction.LEFT)
                    snake.dir = Snake.Direction.RIGHT;
                break;
                
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (snake.dir != Snake.Direction.RIGHT)
                    snake.dir = Snake.Direction.LEFT;
                break;
                
            //Key for starting game
            case KeyEvent.VK_SPACE:
                if (gameOver){
                    gameOver = false;
                    snake = new Snake(SNAKE_SIZE);
                }
                break;
            
            //Changes state of background
            case KeyEvent.VK_T:
                if (randomBackground)
                    randomBackground = false;
                else
                    randomBackground = true;
        }
        //Clock to ensure that key can only be pressed once every 10 centiseconds
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      //Not implemented
    }

    //Shows score and waits for new game
    private void endGame() {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, dim.width, dim.height);
        g2.setColor(Color.black);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        
        //Strings printed in reverse order
        String over[] = {"Colorful Background: "+randomBackground,"Press 'T' to toggle background","Press Space to Play Again" ,"Score: "+score, "Game Over"};
        for (int i = 0; i < over.length; i++){
            double x = g2.getFontMetrics().getStringBounds(over[i], g2).getWidth();
            double y = g2.getFontMetrics().getStringBounds(over[i], g2).getHeight();
            g2.drawString(over[i], (int) (dim.width-x)/2, (int) (dim.height/2-i*y));
        }
        

    }


    
}
