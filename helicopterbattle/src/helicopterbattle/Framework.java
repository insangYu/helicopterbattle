package helicopterbattle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import datafile.R;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED, HOWTOPLAY, RANKING}
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    
    private Font font;
    
    // Images for menu.
    private BufferedImage gameTitleImg;
    private BufferedImage menuBorderImg;
    private BufferedImage skyColorImg;
    private BufferedImage cloudLayer1Img;
    private BufferedImage cloudLayer2Img;
    
    //버튼 구현
    private ImageIcon startButtonEnteredImage = new ImageIcon(R.class.getResource("../datafile/button/startButtonEntered.png"));
    private ImageIcon startButtonBasicImage = new ImageIcon(R.class.getResource("../datafile/button/startButtonBasic.png"));
    private ImageIcon quitButtonEnteredImage = new ImageIcon(R.class.getResource("../datafile/button/quitButtonEntered.png"));
    private ImageIcon quitButtonBasicImage = new ImageIcon(R.class.getResource("../datafile/button/quitButtonBasic.png"));
    private ImageIcon HowButtonEnteredImage = new ImageIcon(R.class.getResource("../datafile/button/tutorialButtonEntered.png"));
    private ImageIcon HowButtonBasicImage = new ImageIcon(R.class.getResource("../datafile/button/tutorialButtonBasic.png"));
    private ImageIcon settingButtonEnteredImage = new ImageIcon(R.class.getResource("../datafile/button/settingButtonEntered.png"));
    private ImageIcon settingButtonBasicImage = new ImageIcon(R.class.getResource("../datafile/button/settingButtonBasic.png"));
    private ImageIcon homeButtonEnteredImage = new ImageIcon(R.class.getResource("../datafile/button/homeButtonEntered.png"));
    private ImageIcon homeButtonBasicImage = new ImageIcon(R.class.getResource("../datafile/button/homeButtonBasic.png"));
    
    private JButton startButton = new JButton(startButtonBasicImage);
    private JButton quitButton = new JButton(quitButtonBasicImage);
    private JButton HowButton = new JButton(HowButtonBasicImage);
    private JButton settingButton = new JButton(settingButtonBasicImage);
    private JButton homeButton = new JButton(homeButtonBasicImage);
    
    
    
    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize()
    {
        font = new Font("monospaced", Font.BOLD, 28);
    }
    
    /**
     * Load files (images).
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent()
    {
        try 
        {
            URL menuBorderImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/menu_border.png");
            menuBorderImg = ImageIO.read(menuBorderImgUrl);
            
            URL skyColorImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/sky_color.jpg");
            skyColorImg = ImageIO.read(skyColorImgUrl);
            
            URL gameTitleImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/helicopter_battle_title.png");
            gameTitleImg = ImageIO.read(gameTitleImgUrl);
            
            URL cloudLayer1ImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/cloud_layer_1.png");
            cloudLayer1Img = ImageIO.read(cloudLayer1ImgUrl);
            URL cloudLayer2ImgUrl = this.getClass().getResource("/helicopterbattle/resources/images/cloud_layer_2.png");
            cloudLayer2Img = ImageIO.read(cloudLayer2ImgUrl);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                	homeButton.setBounds(frameWidth-100,0, 100, 100);
                	homeButton.setBorderPainted(false);
                	homeButton.setContentAreaFilled(false);
                	homeButton.setFocusPainted(false);
                	homeButton.addMouseListener(new MouseAdapter(){
                		@Override
                		public void mouseEntered(MouseEvent e){
                			homeButton.setIcon(homeButtonEnteredImage);
                			homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                		}
                		@Override
                		public void mouseExited(MouseEvent e){
                			homeButton.setIcon(homeButtonBasicImage);
                			homeButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                		}
                		@Override
                		public void mousePressed(MouseEvent e){
                			gameState = GameState.MAIN_MENU;
                			startButton.setVisible(true);
                			HowButton.setVisible(true);
                			settingButton.setVisible(true);
                			quitButton.setVisible(true);
                			
                		}
                	});
                	add(homeButton);
                	
                	startButton.setBounds(frameWidth/2 -150, frameHeight/4, 300, 150);
                	startButton.setBorderPainted(false);
                	startButton.setContentAreaFilled(false);
                	startButton.setFocusPainted(false);
                	startButton.addMouseListener(new MouseAdapter(){
                		@Override
                		public void mouseEntered(MouseEvent e){
                			startButton.setIcon(startButtonEnteredImage);
                			startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                		}
                		@Override
                		public void mouseExited(MouseEvent e){
                			startButton.setIcon(startButtonBasicImage);
                			startButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                		}
                		@Override
                		public void mousePressed(MouseEvent e){
                			//게임 시작 구현
                			startButton.setVisible(false);
                			HowButton.setVisible(false);
                			settingButton.setVisible(false);
                			quitButton.setVisible(false);
                			homeButton.setVisible(false);
                			newGame();
                		}
                	});
                	add(startButton);
                	
                	quitButton.setBounds(frameWidth/2 -150, frameHeight/4+450, 300, 150);
                	quitButton.setBorderPainted(false);
                	quitButton.setContentAreaFilled(false);
                	quitButton.setFocusPainted(false);
                	quitButton.addMouseListener(new MouseAdapter(){
                		@Override
                		public void mouseEntered(MouseEvent e){
                			quitButton.setIcon(quitButtonEnteredImage);
                			quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                		}
                		@Override
                		public void mouseExited(MouseEvent e){
                			quitButton.setIcon(quitButtonBasicImage);
                			quitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                		}
                		@Override
                		public void mousePressed(MouseEvent e){
                			try{
                				Thread.sleep(500);
                			}catch(InterruptedException ex){
                				ex.printStackTrace();
                			}
                			System.exit(0);
                		}
                	});
                	add(quitButton);
                	
                	HowButton.setBounds(frameWidth/2 -150, frameHeight/4+150, 300, 150);
                	HowButton.setBorderPainted(false);
                	HowButton.setContentAreaFilled(false);
                	HowButton.setFocusPainted(false);
                	HowButton.addMouseListener(new MouseAdapter(){
                		@Override
                		public void mouseEntered(MouseEvent e){
                			HowButton.setIcon(HowButtonEnteredImage);
                			HowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                		}
                		@Override
                		public void mouseExited(MouseEvent e){
                			HowButton.setIcon(HowButtonBasicImage);
                			HowButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                		}
                		@Override
                		public void mousePressed(MouseEvent e){
                			startButton.setVisible(false);
                			HowButton.setVisible(false);
                			settingButton.setVisible(false);
                			quitButton.setVisible(false);
                			
                			gameState = GameState.HOWTOPLAY;
                		}
                	});
                	add(HowButton);
                	
                	settingButton.setBounds(frameWidth/2 -150, frameHeight/4+300, 300, 150);
                	settingButton.setBorderPainted(false);
                	settingButton.setContentAreaFilled(false);
                	settingButton.setFocusPainted(false);
                	settingButton.addMouseListener(new MouseAdapter(){
                		@Override
                		public void mouseEntered(MouseEvent e){
                			settingButton.setIcon(settingButtonEnteredImage);
                			settingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                		}
                		@Override
                		public void mouseExited(MouseEvent e){
                			settingButton.setIcon(settingButtonBasicImage);
                			settingButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                		}
                		@Override
                		public void mousePressed(MouseEvent e){
                			startButton.setVisible(false);
                			HowButton.setVisible(false);
                			settingButton.setVisible(false);
                			quitButton.setVisible(false);
                			gameState = GameState.OPTIONS;
                		}
                	});
                	add(settingButton);
                	
                break;
                case OPTIONS:
                	homeButton.setBounds(frameWidth-100,0, 100, 100);
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    // Sets variables and objects.
                    Initialize();
                    // Load files - images, sounds, ...
                    LoadContent();

                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                    // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
                case HOWTOPLAY:
                	homeButton.setBounds(frameWidth-100,0, 100, 100);
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition(), gameTime);
            break;
            case GAMEOVER:
                drawMenuBackground(g2d);
                g2d.setColor(Color.black);
                g2d.drawString("Press ENTER to restart or ESC to exit.", frameWidth/2 - 113, frameHeight/4 + 30);
                game.DrawStatistic(g2d, gameTime);
                g2d.setFont(font);
                g2d.drawString("GAME OVER", frameWidth/2 - 90, frameHeight/4);
            break;
            case MAIN_MENU:
                drawMenuBackground(g2d);
                g2d.drawImage(gameTitleImg, frameWidth/2 - gameTitleImg.getWidth()/2, frameHeight/4-170, null);
               
            break;
            case HOWTOPLAY:
               	 drawMenuBackground(g2d);
            	 g2d.setColor(Color.black);
                 g2d.drawString("Use w, a, d or arrow keys to move the helicopter.", frameWidth / 2 - 117, frameHeight / 2 - 30);
                 g2d.drawString("Use left mouse button to fire bullets and right mouse button to fire rockets.", frameWidth / 2 - 180, frameHeight / 2);
                 g2d.drawString("Press any key to start the game or ESC to exit.", frameWidth / 2 - 114, frameHeight / 2 + 30);
            break;
            case OPTIONS:
            	drawMenuBackground(g2d);
           	 	g2d.setColor(Color.white);
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth/2 - 50, frameHeight/2);
            break;
        }
    }
    
    
    /**
     * Starts new game.
     */
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        
        switch(gameState)
        {
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
            break;
            case MAIN_MENU:
                newGame();
            break;
        }
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }
    
    private void drawMenuBackground(Graphics2D g2d){
        g2d.drawImage(skyColorImg,    0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(cloudLayer1Img, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(cloudLayer2Img, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(menuBorderImg,  0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.setColor(Color.white);
        g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
    }
}
