package com.github.jotask.kimo.util;

import android.os.Handler;
import android.widget.TextView;
import com.github.jotask.kimo.Kimos;

public class Timer implements Runnable {

    final TextView text;

    final Handler handler;

    Kimo kimo;

    boolean isRunning;

    private final Kimos activity;

    public Timer(final Kimos activity) {
        this.activity = activity;
        this.text = this.activity.text;
        this.handler = new Handler();
        this.isRunning = false;
    }

    @Override
    public void run() {

        long diff = System.currentTimeMillis() - this.kimo.getTime();

        final int days = (int) diff / (1000 * 60 * 60 * 24);
        long seconds = diff / 1000 % 60;
        long minutes = diff / (60 * 1000) % 60;
        long hours = diff / (60 * 60 * 1000);

        if(days > 0){
            this.activity.setLast(this.kimo);
        }

        text.setText(String.format("%dhours, %02dminutes and %02dseconds", hours, minutes, seconds));

        this.handler.postDelayed(this, 500);

    }

    public void set(final Kimo kimo){ this.kimo = kimo; }

    public void start(){
        this.handler.postDelayed(this, 0);
        this.isRunning = true;
    }

    public void stop(){
        this.handler.removeCallbacks(this);
        this.isRunning = false;
    }

    public void restart(){
        if(this.kimo != null)
            this.start();
    }

}
