/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cottonmouth;

import environment.LocationValidatorIntf;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author audreykellogg
 */
public class Snake {

    private ArrayList<Point> body = new ArrayList<>();
    private Direction direction = Direction.RIGHT;
    private GridDrawData drawData;
    private LocationValidatorIntf locationValidator;
    private int growthCounter;

    private boolean pause = false;

    public void draw(Graphics graphics) {
//        ArrayList<Point> temp = getSafeBody();

//        for (Point bodySegmentLocation : getBody()) {
        for (Point bodySegmentLocation : getSafeBody()) {

//            System.out.println("Location =" + bodySegmentLocation);
//            System.out.println("System Loc =" + drawData.getCellSystemCoordinate(bodySegmentLocation));
            graphics.setColor(Color.YELLOW);

            Point topLeft = drawData.getCellSystemCoordinate(bodySegmentLocation);
//            System.out.println(topLeft);
            graphics.fillOval(topLeft.x, topLeft.y, drawData.getCellWidth(), drawData.getCellHeight());
        }
    }

    /**
     * @return the body
     */
    public ArrayList<Point> getSafeBody() {
        ArrayList<Point> safeBody = new ArrayList<>();
        
        for (Point segmentLocation : body) {
            safeBody.add(segmentLocation);
        }
        return safeBody;
    }

    /**
     * @return the body
     */
    public ArrayList<Point> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return the drawData
     */
    public GridDrawData getDrawData() {
        return drawData;
    }

    /**
     * @param drawData the drawData to set
     */
    public void setDrawData(GridDrawData drawData) {
        this.drawData = drawData;
    }

    private final int HEAD_POSITION = 0;

    public void move() {
        //make the snake move, please!!!!
        if (!pause) {
            Point newHead = (Point) getHead().clone();

            if (direction == Direction.DOWN) {
                newHead.y++;
            } else if (direction == Direction.UP) {
                newHead.y--;
            } else if (direction == Direction.LEFT) {
                newHead.x--;
            } else if (direction == Direction.RIGHT) {
                newHead.x++;
            }

            if (locationValidator != null) {
                body.add(HEAD_POSITION, locationValidator.validateLocation(newHead));
            }

            if (growthCounter <= 0) {
                body.remove(body.size() - 1);
            } else {
                growthCounter--;
            }
        }

    }

    public Point getHead() {
        return body.get(HEAD_POSITION);
    }

    /**
     * @return the locationValidator
     */
    public LocationValidatorIntf getLocationValidator() {
        return locationValidator;
    }

    /**
     * @param locationValidator the locationValidator to set
     */
    public void setLocationValidator(LocationValidatorIntf locationValidator) {
        this.locationValidator = locationValidator;
    }

    public void togglePause() {
        pause = !pause;
    }

    /**
     * @return the pause
     */
    public boolean isPause() {
        return pause;
    }

    /**
     * @param pause the pause to set
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * @return the growthCounter
     */
    public int getGrowthCounter() {
        return growthCounter;
    }

    /**
     * @param growthCounter the growthCounter to set
     */
    public void setGrowthCounter(int growthCounter) {
        this.growthCounter = growthCounter;
    }

    public void grow(int length) {
        growthCounter += length;
    }
}
