package com.example.sunelevationcalculator;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.sunelevationcalculator.User;
import com.example.sunelevationcalculator.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
