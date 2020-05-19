package com.glsi.atyourservice.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BasketItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "basketDatabase.db";
    private static volatile AppDatabase instance;
    public abstract BasketItemDao basketItemDao();

    public static synchronized AppDatabase  getInstance(Context context) {
        if(instance == null)
            instance = create(context);
        return instance;
    }

    private static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).allowMainThreadQueries().build();
    }

}
