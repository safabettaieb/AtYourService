package com.glsi.atyourservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glsi.atyourservice.adapters.CategoriesRvAdapter;
import com.glsi.atyourservice.adapters.ProductsRvAdapter;
import com.glsi.atyourservice.fragments.QuantityDialog;
import com.glsi.atyourservice.models.Category;
import com.glsi.atyourservice.models.Product;
import com.glsi.atyourservice.room.AppDatabase;
import com.glsi.atyourservice.room.BasketItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements QuantityDialog.OnChooseQuantityListener {
    private RecyclerView mCategoriesHRV;
    private RecyclerView mProductsRV;
    private Toolbar toolbar;
    private TextView viewAllLink;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    // plagin room base de données en local sqlite
    private AppDatabase appDb;
    private List<Category> categories = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private String categoryId;

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
                i = new Intent(ProductsActivity.this, MainActivity.class);break;
            case R.id.action_basket:
                i = new Intent(ProductsActivity.this, BasketActivity.class);break;
            default: return true;
        }
        startActivity(i);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");

        mCategoriesHRV = findViewById(R.id.categoriesHRV);
        mProductsRV = findViewById(R.id.productsRV);
        viewAllLink = findViewById(R.id.viewAllLink);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        initFirebase();
        initAppDatabase();
        //layout l'adapter pour chaq elements comment afficher
        final CategoriesRvAdapter mCategoriesAdapter = new CategoriesRvAdapter(ProductsActivity.this, categories, R.layout.horizontal_category_item);
        mCategoriesHRV.setAdapter(mCategoriesAdapter);
        //layout recyclerview pour tous les elements
        mCategoriesHRV.setLayoutManager(new LinearLayoutManager(ProductsActivity.this, LinearLayoutManager.HORIZONTAL, false));


        final ProductsRvAdapter mProductsAdapter = new ProductsRvAdapter(ProductsActivity.this, products);
        mProductsRV.setAdapter(mProductsAdapter);
        mProductsRV.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));

        mCategoriesAdapter.setOnCategoryItemClickListener(new CategoriesRvAdapter.OnCategoryItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                categoryId = ((TextView) view.findViewById(R.id.categoryId)).getText().toString();
                for(Category c : categories) {
                    if(c.getId().equals(categoryId)) {
                        products.clear();
                        products.addAll(c.getProducts());
                        mProductsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        mProductsAdapter.setOnProductItemClickListener(new ProductsRvAdapter.OnProductItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                QuantityDialog quantityDialog = new QuantityDialog();
                quantityDialog.setProduct(products.get(position));
                quantityDialog.show(getSupportFragmentManager(), "Choose Quantity");
            }
        });

        dbRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Category c = data.getValue(Category.class);
                    categories.add(c);
                    mCategoriesAdapter.notifyDataSetChanged();

                    if(c.getId().equals(categoryId)) {
                        products.addAll(c.getProducts());
                        mProductsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewAllLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductsActivity.this, CategoriesActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void passData(Product product, int quantity, int itemPosition) {
        BasketItem basketItem = new BasketItem(product.getId(), quantity);
        appDb.basketItemDao().insert(basketItem);
    }

    private void initAppDatabase() {
        appDb = AppDatabase.getInstance(getApplicationContext());
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }
}
