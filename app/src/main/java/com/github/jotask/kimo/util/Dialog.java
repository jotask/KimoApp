package com.github.jotask.kimo.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.github.jotask.kimo.Kimos;
import com.google.firebase.database.DatabaseReference;

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
}