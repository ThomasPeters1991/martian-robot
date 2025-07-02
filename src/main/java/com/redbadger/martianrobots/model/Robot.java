package com.redbadger.martianrobots.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Robot {
    private int x;
    private int y;
    private Orientation orientation;
    private boolean isLost = false;

    private static final Logger logger = LoggerFactory.getLogger(Robot.class);

    public Robot(int x, int y, Orientation orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        logger.debug("New robot created at ({}, {}) facing {}", x, y, orientation);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Orientation getOrientation() { return orientation; }
    public boolean isLost() { return isLost; }

    public void move(Instruction instruction, Grid grid) {
        if (isLost) {
            return;
        }

        switch (instruction) {
            case L -> this.orientation = this.orientation.turnLeft();
            case R -> this.orientation = this.orientation.turnRight();
            case F -> {
                int nextX = this.x;
                int nextY = this.y;

                switch (this.orientation) {
                    case N -> nextY++;
                    case E -> nextX++;
                    case S -> nextY--;
                    case W -> nextX--;
                }

                if (grid.isOutOfBounds(nextX, nextY)) {
                    Grid.Coordinate currentPosition = new Grid.Coordinate(this.x, this.y);
                    if (!grid.hasScent(currentPosition)) {
                        this.isLost = true;
                        grid.addScent(currentPosition);
                        logger.warn("Robot at ({}, {}) with orientation {} is now LOST.", this.x, this.y, this.orientation);
                    } else {
                        logger.info("Robot at ({}, {}) ignored 'Forward' instruction due to scent.", this.x, this.y);
                    }
                } else {
                    this.x = nextX;
                    this.y = nextY;
                }
            }
        }
    }

    @Override
    public String toString() {
        return x + " " + y + " " + orientation.name() + (isLost ? " LOST" : "");
    }
}
