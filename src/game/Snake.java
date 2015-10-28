/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Adam
 */
public class Snake {
    
    public enum Direction{UP, DOWN, LEFT, RIGHT};
    
    public ArrayList<Rectangle> parts;
    public Rectangle head;
    public Direction dir = Direction.UP; //Sets snake's initial travel direction to up
    
    public final static int SCALE = 10; //How large the snake is
    
    public Snake(int size){
        parts = new ArrayList<>();
        
        head = (new Rectangle(Game.dim.width/2, Game.dim.height/2, SCALE, SCALE));
        for (int i = 0, j = SCALE; i < size-1; i++){
            parts.add(new Rectangle(Game.dim.width/2, Game.dim.height/2+j, SCALE, SCALE));
            j += SCALE;
        }
    }
    public void move(){
        
        parts.add(new Rectangle(head.x, head.y, SCALE, SCALE));
        switch(dir){
            case UP:
                Rectangle r = new Rectangle(head.x, head.y-SCALE, SCALE, SCALE);
                
                if (head.y-SCALE > 20 && checkTailCollision(r)){
                    head = r;
                }
                else
                    Game.gameOver = true;
                break;
                
            case DOWN:
                r = new Rectangle(head.x, head.y+SCALE, SCALE, SCALE);
                
                if (head.y+SCALE < Game.dim.height-10 && checkTailCollision(r))
                    head = r;
                else
                    Game.gameOver = true;
                break;
                
            case LEFT:
                r = new Rectangle(head.x-SCALE, head.y, SCALE, SCALE);
                if (head.x > 10 && checkTailCollision(r))
                    head = r;
                else
                    Game.gameOver = true;
                break;
                
            case RIGHT:
                r = new Rectangle(head.x+SCALE, head.y, SCALE, SCALE);
                if (head.x+SCALE < Game.dim.width-10  && checkTailCollision(r))
                    head = r;
                else
                    Game.gameOver = true;
                break;
                
            default:
                Game.gameOver = true;
                    
        }
        parts.remove(0);
    }
    
    //Checks if head collides with tail
    public boolean checkTailCollision(Rectangle r){
        for (Rectangle rect : parts){
            if (r.equals(rect))
                return false;
        }
        return true;
    }

    //Draw snake head and body
    public void draw(Graphics2D g){
        g.setColor(Color.white);
        g.fill(head);
        g.setColor(Color.BLACK);
        g.draw(head);
        for (Rectangle rect: parts){
            g.setColor(getRandomColor());
            g.fill(rect);
            g.setColor(Color.BLACK);
            g.draw(rect);
        }
        
    }
    //Gets a random color from the list of colors added
    public static Color getRandomColor(){
        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.blue);
        colors.add(Color.red);
        colors.add(Color.yellow);
        colors.add(Color.orange);
        colors.add(Color.magenta);
        colors.add(Color.black);
        colors.add(Color.green);
        colors.add(Color.gray);
        colors.add(Color.cyan);
        colors.add(Color.white);
        
        Random rand = new Random();
        return colors.get(rand.nextInt(colors.size()-1));
        
        
    }
    
    public static class Food{
        
        public Point pos;      
        public void setLocation(Point p){
            pos = p;
        }
        
        public void draw(Graphics2D g){
            g.setColor(getRandomColor());
            g.fillRect(pos.x, pos.y, SCALE, SCALE);
            g.setColor(Color.BLACK);
            g.drawRect(pos.x, pos.y, SCALE, SCALE);
        }
    }
    
    
}
