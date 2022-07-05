package com.datastructures.pairingtask.interfaces

import android.database.Observable
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.datastructures.pairingtask.pojo.User

@Dao
interface UsersDAO {
    @Insert
    suspend fun insertUser(user: User?)

    @Query("select password from users where username = :username")
    suspend fun getPasswordByUsername(username:String) : String
}