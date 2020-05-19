package com.glsi.atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glsi.atyourservice.adapters.BasketRvAdapter;
import com.glsi.atyourservice.fragments.QuantityDialog;
import com.glsi.atyourservice.models.Category;
import com.glsi.atyourservice.models.Product;
import com.glsi.atyourservice.models.ProductBasketItem;
import com.glsi.atyourservice.room.AppDatabase;
import com.glsi.atyourservice.room.BasketItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity implements QuantityDialog.OnChooseQuantityListener {
    private Toolbar toolbar;
    private FloatingActionButton myFab;
    private RecyclerView mBasketRV;
    private BasketRvAdapter mBasketRvAdapter;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private AppDatabase appDb;
    private List<BasketItem> basketItems = new ArrayList<>();
    private List<ProductBasketItem> productBasketItems = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.action_home:
                i = new Intent(BasketActivity.this, MainActivity.class);break;
            default: return true;
        }
        startActivity(i);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        mBasketRV = findViewById(R.id.basketRV);
        toolbar = findViewById(R.id.toolbar);
        myFab = findViewById(R.id.myFab);

        setSupportActionBar(toolbar);

        initFirebase();
        initAppDatabase();

        mBasketRvAdapter = new BasketRvAdapter(BasketActivity.this, productBasketItems);
        mBasketRV.setAdapter(mBasketRvAdapter);
        mBasketRV.setLayoutManager(new LinearLayoutManager(BasketActivity.this));

        mBasketRvAdapter.setOnBasketItemClickListener(new BasketRvAdapter.OnBasketItemClickListener() {
            @Override
            public void onItemDelete(View v, int position) {
                appDb.basketItemDao().delete(productBasketItems.get(position).getBasketItem());
                productBasketItems.remove(position);
                mBasketRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemEdit(View v, int position) {
                QuantityDialog dialog = new QuantityDialog();
                dialog.setItemPosition(position);
                dialog.setQuantity(productBasketItems.get(position).getBasketItem().getQuantity());
                dialog.setProduct(productBasketItems.get(position).getProduct());
                dialog.show(getSupportFragmentManager(), "Choose Quantity");
            }
        });

        basketItems = appDb.basketItemDao().getAll();

        dbRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(BasketItem bi: basketItems) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Category c = data.getValue(Category.class);

                        for (Product p : c.getProducts()) {
                            if (p.getId().equals(bi.getProductId())) {
                                ProductBasketItem pbi = new ProductBasketItem(p, bi);
                                productBasketItems.add(pbi);
                                mBasketRvAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child("orders").push().setValue(basketItems);
                appDb.basketItemDao().deleteAll(basketItems);
                productBasketItems.removeAll(productBasketItems);
                mBasketRvAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initAppDatabase() {
        appDb = AppDatabase.getInstance(getApplicationContext());
    }
    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    @Override
    public void passData(Product product, int quantity, int itemPosition) {
        BasketItem basketItem = productBasketItems.get(itemPosition).getBasketItem();
        basketItem.setProductId(product.getId());
        basketItem.setQuantity(quantity);
        appDb.basketItemDao().update(basketItem);
        mBasketRvAdapter.notifyDataSetChanged();
    }
}
