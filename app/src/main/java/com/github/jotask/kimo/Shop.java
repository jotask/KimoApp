package com.github.jotask.kimo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.github.jotask.kimo.util.Point;
import com.github.jotask.kimo.util.ShopItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class Shop extends AppCompatActivity {

    public static final String TAG = "Shop";
    public static final String table = "shop";
    public static final String YellowSticker = "utils/yellowsticker";

    private FirebaseDatabase database;
    private DatabaseReference shop;
    private DatabaseReference discount;
    private DatabaseReference points;

    private HashMap<String, ShopButton> shopBtn;

    public float discountPrice = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        final Shop instance = this;

        shopBtn = new HashMap<>();

        this.database = FirebaseDatabase.getInstance();
        this.shop = database.getReference(table);
        this.points = database.getReference(Points.table);
        this.discount = database.getReference(YellowSticker);

        discount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Integer dsc = Integer.valueOf(dataSnapshot.getValue().toString());

                instance.discountPrice = (dsc) / 100f;

                Log.d(TAG, "updated: " + dsc + " Shop: " + instance.discountPrice);

                for(Map.Entry<String, ShopButton> b: shopBtn.entrySet())
                {
                    b.getValue().updateBtn();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final LinearLayout view = (LinearLayout) findViewById(R.id.shopitems);

        shop.keepSynced(true);
        shop.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {

                ShopItem i = dataSnapshot.getValue(ShopItem.class);

                ShopButton btn = new ShopButton(instance);
                btn.shop = instance;

                btn.shopItem = i;

                btn.updateBtn();

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buyItem(dataSnapshot.getKey());
                    }
                });

                view.addView(btn);

                shopBtn.put(dataSnapshot.getKey(), btn);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ShopItem shopItem = dataSnapshot.getValue(ShopItem.class);

                final ShopButton btn = shopBtn.get(dataSnapshot.getKey());
                btn.shopItem = shopItem;
                btn.updateBtn();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { Log.e(TAG, databaseError.getMessage()); }

        });

    }

    private void buyItem(final String key)
    {

        final ShopButton btn = shopBtn.get(key);

        points.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                Point point = mutableData.getValue(Point.class);
                if(point == null){
                    Toast.makeText(Shop.this, "You don't have enough points.", Toast.LENGTH_LONG).show();
                    return Transaction.abort();
                }

                int cost = Math.round(btn.shopItem.price * discountPrice);

                if(point.points < cost)
                {
                    Toast.makeText(Shop.this, "You don't have enough points.", Toast.LENGTH_LONG).show();
                    return Transaction.abort();
                }

                point.add(-cost);
                mutableData.setValue(point);

                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                if(!b)
                {
                    // Transaction not completed
                    Log.d(TAG, "Some error: " + databaseError.toString());
                    return;
                }

                btn.shopItem.quantity++;

                shop.child(key).setValue(btn.shopItem);

            }

        });

    }

    static class ShopButton extends Button{

        public ShopItem shopItem;
        public Shop shop;



        public ShopButton(Context context) {
            super(context);
        }

        public void updateBtn()
        {
            int price = Math.round(shopItem.price * shop.discountPrice);
            this.setText(shopItem.description + " \n " + price + " points." + " \n " + "x" + shopItem.quantity);

            if(shop.discountPrice < 1.0f) {
                this.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            }else if(shop.discountPrice > 1.0f){
                this.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            }else{
                this.getBackground().clearColorFilter();
            }

        }

    }

}
