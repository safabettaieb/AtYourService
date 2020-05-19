package com.glsi.atyourservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glsi.atyourservice.adapters.OrderDetailsRvAdapter;
import com.glsi.atyourservice.models.Category;
import com.glsi.atyourservice.models.Product;
import com.glsi.atyourservice.models.ProductBasketItem;
import com.glsi.atyourservice.room.BasketItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "AyS_Channel";
    private Toolbar toolbar;
    private RecyclerView mOrderDetailsRV;
    private TextView mTotalPrice;
    private ImageView btnReady;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private List<ProductBasketItem> productBasketItems = new ArrayList<>();
    private String orderId;
    private double totalPrice;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent(OrderDetailsActivity.this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.action_signout:
                auth.signOut();break;
        }
        startActivity(i);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        createNotificationChannel();
        Intent i  = getIntent();
        orderId = i.getStringExtra("orderId");


        mTotalPrice = findViewById(R.id.totalPrice);
        btnReady = findViewById(R.id.btnReady);
        mOrderDetailsRV = findViewById(R.id.orderDetailsRV);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        initFirebase();

        final OrderDetailsRvAdapter mOrderDetailsRvAdapter = new OrderDetailsRvAdapter(OrderDetailsActivity.this, productBasketItems);
        mOrderDetailsRV.setAdapter(mOrderDetailsRvAdapter);
        mOrderDetailsRV.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));

        dbRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if(data.getKey().equals(orderId)) {
                        for(DataSnapshot d: data.getChildren()) {
                            final BasketItem b = d.getValue(BasketItem.class);

                            dbRef.child("categories").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        Category c = data.getValue(Category.class);

                                        for (Product p : c.getProducts()) {
                                            if (p.getId().equals(b.getProductId())) {
                                                ProductBasketItem pbi = new ProductBasketItem(p, b);
                                                productBasketItems.add(pbi);
                                                mOrderDetailsRvAdapter.notifyDataSetChanged();
                                                totalPrice += pbi.getProduct().getPrice() * pbi.getBasketItem().getQuantity();
                                                mTotalPrice.setText(totalPrice + " TND");
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(OrderDetailsActivity.this, "Commande est prête", Toast.LENGTH_SHORT).show();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(OrderDetailsActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_shipping)
                        .setContentTitle("AyS Commande")
                        .setContentText("Votre commande est prête")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setColorized(true)
                        .setColor(Color.parseColor("#AE0A0A"));

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(OrderDetailsActivity.this);
                notificationManager.notify(12345, builder.build());

            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "AyS Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Ays Notification Channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
