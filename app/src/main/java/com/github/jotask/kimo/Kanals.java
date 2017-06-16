package com.github.jotask.kimo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.jotask.kimo.util.Dialog;
import com.github.jotask.kimo.util.Kanal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Date;

public class Kanals extends AppCompatActivity {

    public static final String TAG = "Kanal";
    private static final String table = "kanals";

    public TextView text;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public Kanals() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kanals);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        text = (TextView) findViewById(R.id.sinceLastKanal);
        final TextView counter = (TextView) findViewById(R.id.counterKanal);

        this.database = FirebaseDatabase.getInstance();
        this.ref = database.getReference(table);

        ref.keepSynced(true);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Kanal kanal = dataSnapshot.getValue(Kanal.class);
                setLast(kanal);
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
                counter.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " kanals stored");
                Toast.makeText(Kanals.this, "Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { Log.e(TAG, databaseError.getMessage()); }
        });

        Button button = (Button) findViewById(R.id.updateKanal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { newKanal(); }
        });

    }

    public void setLast(Kanal last){

        if(last == null)
            return;

        final long diff = getDiff(last.getTime());
        final int days = (int) diff / (1000 * 60 * 60 * 24);
        text.setText(String.valueOf(days) + " days");

    }

    static long getDiff(final long saved){
        final Date d2 = new Date();
        final Date d1 = new Date(saved);
        return d2.getTime() - d1.getTime();
    }

    public void newKanal(){
        Dialog.newKanal(this, ref);
    }

}
