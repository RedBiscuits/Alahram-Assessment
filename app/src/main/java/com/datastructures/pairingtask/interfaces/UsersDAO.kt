package com.datastructures.pairingtask.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.datastructures.pairingtask.pojo.User

@Dao
interface UsersDAO {
    @Insert
    fun insertUser(user: User?)

    @get:Query("SELECT * FROM USERS")
    val users: List<User?>?
}