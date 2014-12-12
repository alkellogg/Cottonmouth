/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cottonmouth;

import audio.AudioPlayer;
import environment.Environment;
import environment.GraphicsPalette;
import environment.LocationValidatorIntf;
import grid.Grid;
import images.ResourceTools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author audreykellogg
 */
class CottonMouthEnvironment extends Environment implements GridDrawData, LocationValidatorIntf {

    public Point randomPoint() {
        return new Point((int) (Math.random() * grid.getColumns() + 1), ((int) (Math.random() * grid.getRows() + 1)));
    }

    //trying to make the snake grow!!!!
    //SNAKE_START_LENGTH = 4
    //if snake = 4
    //then + (x+1)
    private Grid grid;
    private Snake snake;

    public final int SLOW_SPEED = 7;
    public final int MEDIUM_SPEED = 5;
    public final int HIGH_SPEED = 3;
    private Image gG; 

    private int moveDelayLimit = MEDIUM_SPEED;
    private int moveDelayCounter = 0;

    private ArrayList<GridObject> gridObjects;
    
    private boolean gameOver = false;

    public CottonMouthEnvironment() {
    }

    @Override
    public void initializeEnvironment() {
        gG = ResourceTools.loadImageFromResource("resources/gameover.png");
        this.setBackground(ResourceTools.loadImageFromResource("resources/junglebook.jpg").getScaledInstance(1000, 800, Image.SCALE_FAST));
        grid = new Grid(30, 20, 25, 25, new Point(50, 100), Color.GREEN);
        grid.setColor(Color.GRAY);

        snake = new Snake();
        snake.setDirection(Direction.LEFT);
        snake.setDrawData(this);
        snake.setLocationValidator(this);

        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(3, 1));
        body.add(new Point(3, 2));
        body.add(new Point(2, 2));
        body.add(new Point(2, 3));

        snake.setBody(body);

        gridObjects = new ArrayList<>();
        gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, new Point(5, 10)));

        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(20, 5)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, randomPoint()));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(5, 2)));
        gridObjects.add(new GridObject(GridObjectType.APPLE, new Point(15, 15)));

        gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, new Point(10, 4)));
        gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, new Point(20, 10)));

    }

    @Override
    public void timerTaskHandler() {
        if (snake != null) {
            // if the counter is >= limit then reset counter and move snake
            //else increment counter
            if (moveDelayCounter >= moveDelayLimit) {
                moveDelayCounter = 0;
                snake.move();

            } else {
                moveDelayCounter++;
            }

        }
    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            grid.setShowCellCoordinates(!grid.getShowCellCoordinates());
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            snake.setDirection(Direction.LEFT);
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            snake.setDirection(Direction.RIGHT);
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            snake.setDirection(Direction.UP);
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            snake.setDirection(Direction.DOWN);
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            snake.togglePause();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            snake.grow(2);
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            AudioPlayer.play(TOOL_TIP_TEXT_KEY);
        }
    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {

    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {
    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        if (grid != null) {
            grid.paintComponent(graphics);
        }

        if (snake != null) {
            snake.draw(graphics);
        }

        if (gridObjects != null) {
            for (GridObject gridObject : gridObjects) {
                if (gridObject.getType() == GridObjectType.POISON_BOTTLE) {
                    GraphicsPalette.drawPoisonBottle(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.yellow);
                }
                if (gridObject.getType() == GridObjectType.APPLE) {
                    GraphicsPalette.drawApple(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.RED);

                }
            }

        }
        
        if (gameOver){
            graphics.drawImage(gG, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        
    }

    //<editor-fold defaultstate="collapsed" desc="GridDrawData Interface">
    @Override
    public int getCellHeight() {
        return grid.getCellHeight();
    }

    @Override
    public int getCellWidth() {
        return grid.getCellHeight();
    }

    @Override
    public Point getCellSystemCoordinate(Point cellCoordinate) {
        return grid.getCellSystemCoordinate(cellCoordinate);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="LocationValidatorIntf">
    @Override
    public Point validateLocation(Point point) {

        if (point.x >= this.grid.getColumns()) {
            point.x = 0;
        } else if (point.x < 0) {
            point.x = this.grid.getColumns() - 1;
        } else if (point.y < 0) {
            point.y = this.grid.getRows() - 1;

        } else if (point.y >= this.grid.getRows()) {
            point.y = 0;

        }

        // check if the snake hit a gridObject, then take appropriate action:
        // - Apple - grow the snake by 3
        // - Poison bottle - make a sound, kill snake
        // look at all the locations stored in the gridObject ArrayList
        // for each, compare it to the head location stored
        // in the "point" parameter 
        for (GridObject object : gridObjects) {
            if (object.getLocation().equals(point)) {
                System.out.println("HIT = " + object.getType());
                object.setLocation(getRandomPoint());

                if (object.getType() == GridObjectType.APPLE) {

                    snake.grow(2);

                } else if (object.getType() == GridObjectType.POISON_BOTTLE) {
                    System.out.println("POISON");
                    snake.setPause(true);
                    //decrease score?
                    //kill the snake?
                    System.out.println("HIT = " + object.getType());
                    //this.setBackground(ResourceTools.loadImageFromResource("resources/gameover.png").getScaledInstance(1000, 800, Image.SCALE_FAST));
                    ResourceTools.loadImageFromResource("resources/gameover.png").getScaledInstance(1000, 800, Image.SCALE_FAST);
                    this.gameOver = true;
                    
                    

                }

            }

        }
        return point;
    }

    private Point getRandomPoint() {
        return new Point((int) (grid.getRows() * Math.random()), (int) (grid.getColumns() * Math.random()));
    }

    /**
     * @return the gameOver
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @param gameOver the gameOver to set
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

}
//</editor-fold>

