package com.glsi.atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;



public class MainActivity extends AppCompatActivity {
    private ImageView mAnimatedImg;
    private CardView mCategoriesBtn, mOrdersBtn;
    private Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.action_basket:
                i = new Intent(MainActivity.this, BasketActivity.class);break;
            default: return true;
        }
        startActivity(i);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnimatedImg = findViewById(R.id.animatedImg);
        mCategoriesBtn = findViewById(R.id.categoriesBtn);
        mOrdersBtn = findViewById(R.id.ordersBtn);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        Glide.with(this)
                .load("https://media.giphy.com/media/5XLPWTWfj7h6M/giphy.gif")
                .fitCenter()
                .into(mAnimatedImg);

        mCategoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(i);
            }
        });

        mOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(i);
            }
        });
    }
}
