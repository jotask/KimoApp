package com.github.jotask.kimo.util;

/**
 * Kimo
 *
 * @author Jose Vives Iznardo
 * @since 15/06/2017
 */
public class Kanal {

    private final long time;
    private final long score;

    public Kanal(){
        this.time = 0;
        this.score = 0;
    }

    public Kanal(long score) {
        this.score = score;
        this.time = System.currentTimeMillis();
    }

    public Kanal(final long score, final long time){
        this.score = score;
        this.time = time;
    }

    public long getTime() { return time; }

    public long getScore() { return score; }

}
