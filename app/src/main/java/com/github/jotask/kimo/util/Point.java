package com.github.jotask.kimo.util;

/**
 * Point
 *
 * @author Jose Vives Iznardo
 * @since 16/06/2017
 */
public class Point {

    public int points;
    public long time;

    public Point() {
        this(0);
    }

    public Point(int points) {
        this.points = points;
        this.time = System.currentTimeMillis();
    }

    public void add(int p){
        this.points += p;
        this.time = System.currentTimeMillis();
    }

}
