package com.github.jotask.kimo.util;

/**
 * Kimo
 *
 * @author Jose Vives Iznardo
 * @since 15/06/2017
 */
public class Kimo {

    private final long time;
    private final long score;

    public Kimo(){
        this.time = 0;
        this.score = 0;
    }

    public Kimo(long score) {
        this.score = score;
        this.time = System.currentTimeMillis();
    }

    public Kimo(final long score, final long time){
        this.score = score;
        this.time = time;
    }

    public long getTime() { return time; }

    public long getScore() { return score; }

}
