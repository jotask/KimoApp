package com.github.jotask.kimo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.github.jotask.kimo.util.Dialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Points extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Points";
    public static final String table = "points";

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        this.database = FirebaseDatabase.getInstance();
        this.ref = database.getReference(table);

        findViewById(R.id.goodgirl).setOnClickListener(this);
        findViewById(R.id.badgirl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goodgirl:
                Log.d(TAG, "GoodGirl");
                Dialog.newBehaviour(true, this, ref);
                break;
            case R.id.badgirl:
                Log.d(TAG, "BadGirl");
                Dialog.newBehaviour(false, this, ref);
                break;
        }
    }
}
