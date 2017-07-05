package com.github.jotask.kimo.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.github.jotask.kimo.Kanals;
import com.github.jotask.kimo.Kimos;
import com.github.jotask.kimo.Points;
import com.google.firebase.database.*;

/**
 * Dialog
 *
 * @author Jose Vives Iznardo
 * @since 29/04/2017
 */
public class Dialog {

    public static void newKimoJob(final Kimos activity, final DatabaseReference ref) {

//        final DatabaseReference r = ref.push();
//        r.setValue(new Blowjob(new Random().nextInt(10)));

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Rate the kimo!");

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final SeekBar bar = new SeekBar(activity);
        bar.setProgress(50);
        layout.addView(bar);

        final TextView view = new TextView(activity);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setText(String.valueOf(bar.getProgress() / 10));
        layout.addView(view);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.setText(String.valueOf(bar.getProgress() / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        builder.setView(layout);

//        // Set up the input
////        final EditText input = new EditText(activity);
//
//        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
////        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
////        builder.setView(input);
//
        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Kimo kimo = new Kimo(bar.getProgress() * 10);
                final DatabaseReference r = ref.push();
                r.setValue(kimo);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void newKanal(final Kanals activity, final DatabaseReference ref) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Such a good girl!");

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final SeekBar bar = new SeekBar(activity);
        bar.setProgress(50);
        layout.addView(bar);

        final TextView view = new TextView(activity);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setText(String.valueOf(bar.getProgress() / 10));
        layout.addView(view);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.setText(String.valueOf(bar.getProgress() / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        builder.setView(layout);
//
        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Kanal kanal = new Kanal(bar.getProgress() * 10);
                final DatabaseReference r = ref.push();
                r.setValue(kanal);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void newBehaviour(final boolean good, final Points activity, final DatabaseReference ref) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if(good) {
            builder.setTitle("Such a good girl!");
        }else{
            builder.setTitle("Such a bad girl!");
        }

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);

        final SeekBar bar = new SeekBar(activity);
        bar.setProgress(50);
        layout.addView(bar);

        final TextView view = new TextView(activity);
        view.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setText(String.valueOf(bar.getProgress() / 10));
        layout.addView(view);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.setText(String.valueOf(bar.getProgress() / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        builder.setView(layout);
//
        // Set up the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Point point = mutableData.getValue(Point.class);
                        if(point == null){
                            Point p = new Point();
                            mutableData.setValue(p);
                            return Transaction.success(mutableData);
                        }
                        int toADD = bar.getProgress() / 10;
                        if(!good){
                            toADD *= -1;
                        }
                        point.add(toADD);
                        mutableData.setValue(point);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        Log.d("TAG","postTransaction:onComplete:" + databaseError );
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
}
