package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener{

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private MapGenerator map;
    private int delay = 7;
    
    private boolean ballPedalCollision;

    private int playerX = 310;

    private int ballX = 120;
    private int ballY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    public GamePlay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        map = new MapGenerator(3, 7);
       
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
     
        g.fillRect(1, 1, 692, 592);
        
        map.draw((Graphics2D) g );

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);
        
        //Score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(" "+ score, 590, 30);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballX, ballY, 20, 20);
        if(ballY > 570) {
        	play = false;
        	ballXdir = 0;
        	ballYdir = 0;
        	g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("GAME OVER", 275, 300);
            g.drawString("SCORE: "+ score, 300, 325);
            g.drawString("PRESS ENTER TO RESTART", 300 - 25*11/2, 350);
            
        }
        if(score >= 21) {
        	play = false;
        	ballXdir = 0;
        	ballYdir = 0;
        	g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("YOU WON", 275, 300);
            g.drawString("SCORE: "+ score, 300, 325);
            g.drawString("PRESS ENTER TO RESTART", 300 - 25*11/2, 350);
            
        }
        

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	timer.start();
		
    	if(play) {
    		
    		ballX += ballXdir;
    		ballY += ballYdir;
    		if(ballX < 0) {
    			ballXdir = -ballXdir;
    		}
    		if(ballY < 0) {
    			ballYdir = -ballYdir;
    		}
    		if(ballX > 670) {
    			ballXdir = -ballXdir;
    		}
    		ballPedalCollision = new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8));
    		if(ballPedalCollision) {
    			ballYdir = -ballYdir;
    		}
    		A: for(int i=0; i< map.map.length; i++) {
    			for(int j=0; j<  map.map[0].length; j++) {
    				if(map.map[i][j] > 0) {
    					int brickX = j * map.brickWidth + 80;
    					int brickY = i * map.brickHeight + 80;
    					int brickWidth = map.brickWidth;
    					int brickHeight = map.brickHeight;
    					
    					Rectangle rect = new Rectangle(brickX, brickY, brickWidth,brickHeight);
    					Rectangle ballRect = new Rectangle(ballX, ballY, 20 , 20);
    					Rectangle brickRect = rect;
    					
    					if(ballRect.intersects(brickRect)) {
    						map.setBrickValue(0, i, j);
    						score += 1;
    						
    						if(ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + brickRect.width) {
    							ballXdir = -ballXdir;
    							
    						}
    						else {
    							ballYdir = -ballYdir;
    						}
    						break A;
    					}
    					
    				}
    			}
    		}
    		
    		
    		
    	}
    	repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
        	if(!play) {
        		 play = true;
            	 ballX = 120;
            	 ballY = 350;
            	 ballXdir = -1;
            	 ballYdir = -2;
            	 playerX = 310;
            	 totalBricks = 21;
            	 map = new MapGenerator(3, 7);
           
            	 repaint();
        	}
        }
        }

    public void moveRight() {
        play = true;
        playerX += 30;
    }
    public void moveLeft() {
        play = true;
        playerX -= 30;
    }
}
