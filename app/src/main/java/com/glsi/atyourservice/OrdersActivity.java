package com.glsi.atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glsi.atyourservice.adapters.OrdersRvAdapter;
import com.glsi.atyourservice.models.Order;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView mOrdersRV;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private List<Order> orders = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent(OrdersActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_orders);

        mOrdersRV = findViewById(R.id.ordersRV);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        initFirebase();

        final OrdersRvAdapter mOrdersAdapter = new OrdersRvAdapter(OrdersActivity.this, orders);
        mOrdersRV.setAdapter(mOrdersAdapter);
        mOrdersRV.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));

        mOrdersAdapter.setOnOrderItemClickListener(new OrdersRvAdapter.OnOrderItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent i = new Intent(OrdersActivity.this, OrderDetailsActivity.class);
                i.putExtra("orderId", orders.get(position).getId());
                startActivity(i);
            }
        });

        dbRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i =1;
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Order o = new Order(data.getKey(), "Order "+i);
                    orders.add(o);
                    mOrdersAdapter.notifyDataSetChanged();
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user == null) {
            Toast.makeText(OrdersActivity.this, "You have to sign in first", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(OrdersActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }
}
