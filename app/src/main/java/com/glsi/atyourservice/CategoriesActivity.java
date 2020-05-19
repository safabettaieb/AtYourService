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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glsi.atyourservice.adapters.CategoriesRvAdapter;
import com.glsi.atyourservice.models.Category;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private List<Category> categories = new ArrayList<>();

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
                i = new Intent(CategoriesActivity.this, MainActivity.class);break;
            case R.id.action_basket:
                i = new Intent(CategoriesActivity.this, BasketActivity.class);break;
            default: return true;
        }
        startActivity(i);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        mRecyclerView = findViewById(R.id.categoriesRV);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        final CategoriesRvAdapter mAdapter = new CategoriesRvAdapter(CategoriesActivity.this, categories, R.layout.vertical_category_item);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(CategoriesActivity.this, 2));

        mAdapter.setOnCategoryItemClickListener(new CategoriesRvAdapter.OnCategoryItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(CategoriesActivity.this, ProductsActivity.class);
                i.putExtra("categoryId", ((TextView) view.findViewById(R.id.categoryId)).getText().toString());
                startActivity(i);
            }
        });

        initFirebase();
        dbRef.child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Category c = data.getValue(Category.class);
                    categories.add(c);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }
}
