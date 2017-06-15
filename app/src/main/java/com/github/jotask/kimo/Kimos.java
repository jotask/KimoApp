package com.github.jotask.kimo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.jotask.kimo.util.Dialog;
import com.github.jotask.kimo.util.Kimo;
import com.github.jotask.kimo.util.Timer;
import com.github.jotask.kimo.util.notification.Alarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Kimos extends AppCompatActivity {

    public static final String TAG = "Kimos";
    private static final String table = "kimos";

    public TextView text;

    private Timer timer;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kimos);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        text = (TextView) findViewById(R.id.sinceLast);
        final TextView counter = (TextView) findViewById(R.id.counter);

        this.database = FirebaseDatabase.getInstance();
        this.ref = database.getReference(table);

        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Kimo kimo = dataSnapshot.getValue(Kimo.class);
                setLast(kimo);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { Log.e(TAG, databaseError.getMessage()); }

        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " kimos stored");
                Toast.makeText(Kimos.this, "Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { Log.e(TAG, databaseError.getMessage()); }
        });

        Button button = (Button) findViewById(R.id.update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { newKimo(); }
        });

        this.timer = new Timer(this);

        {
            Calendar updateTime = Calendar.getInstance();
            updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
            updateTime.set(Calendar.HOUR_OF_DAY, 11);
            updateTime.set(Calendar.MINUTE, 45);

            // Create alarm
            Intent intent = new Intent(this, Alarm.class);
            PendingIntent pendingIntent = PendingIntent.getService(Kimos.this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
            Toast.makeText(Kimos.this, "I Love You Kimo!", Toast.LENGTH_LONG).show();

        }

    }

    public void setLast(Kimo last){

        if(last == null)
            return;

        final long diff = getDiff(last.getTime());
        final int days = (int) diff / (1000 * 60 * 60 * 24);
        this.timer.stop();
        if(days > 0){
            text.setText(String.valueOf(days) + " days");
        }else{
            this.timer.set(last);
            this.timer.start();
        }
    }

    static long getDiff(final long saved){
        final Date d2 = new Date();
        final Date d1 = new Date(saved);
        return d2.getTime() - d1.getTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.timer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.timer.restart();
    }

    public void newKimo(){
        Dialog.newKimoJob(this, ref);
    }

}
