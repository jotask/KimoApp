package com.github.jotask.kimo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.github.jotask.kimo.util.Point;
import com.github.jotask.kimo.util.notification.Alarm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final MainActivity instance = this;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Points.table);

                final StringBuilder sb = new StringBuilder();

                ref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Point p = mutableData.getValue(Point.class);
                        if(p == null){
                            return Transaction.success(mutableData);
                        }
                        sb.append(String.valueOf(p.points));
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        Log.d("CACA", sb.toString());
                        Snackbar.make(view, sb.toString() + " points.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                });

            }
        });



        findViewById(R.id.kimos).setOnClickListener(this);
        findViewById(R.id.kanal).setOnClickListener(this);
        findViewById(R.id.points).setOnClickListener(this);

        {
            // Notifications
            Calendar updateTime = Calendar.getInstance();
            updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
            updateTime.set(Calendar.HOUR_OF_DAY, 11);
            updateTime.set(Calendar.MINUTE, 45);

            // Create alarm
            Intent intent = new Intent(this, Alarm.class);
            PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);

            Toast.makeText(MainActivity.this, "I Love You Kimo!", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kimos:
                Log.d(TAG, "Kimo cliked");
                startActivity(new Intent(this, Kimos.class));
                break;
            case R.id.kanal:
                Log.d(TAG, "Kanal clicked");
                startActivity(new Intent(this, Kanals.class));
                break;
            case R.id.points:
                Log.d(TAG, "Points clicked");
                startActivity(new Intent(this, Points.class));
                break;
            default:
                Log.d(TAG, "Unkown action");
        }
    }
}
