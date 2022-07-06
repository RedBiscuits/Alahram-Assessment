package com.datastructures.pairingtask.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.datastructures.pairingtask.interfaces.UsersDAO;
import com.datastructures.pairingtask.pojo.User;

@Database(entities = User.class,version = 1)
public abstract class UsersDatabase extends RoomDatabase {
    private static UsersDatabase instance;
    public abstract UsersDAO usersDAO();

    public static synchronized UsersDatabase getInstance(Context context){
        if(instance == null){
            instance = Room
                    .databaseBuilder(context.getApplicationContext(),
                        UsersDatabase.class , "users_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }
}
