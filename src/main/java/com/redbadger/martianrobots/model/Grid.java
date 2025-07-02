package com.redbadger.martianrobots.model;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Grid {
    private final int maxX;
    private final int maxY;
    private final Set<Coordinate> scents = new HashSet<>();

    public record Coordinate(int x, int y) {}

    public Grid(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x > maxX || y < 0 || y > maxY;
    }

    public void addScent(Coordinate coordinate) {
        scents.add(coordinate);
    }

    public boolean hasScent(Coordinate coordinate) {
        return scents.contains(coordinate);
    }
}
