package com.datastructures.pairingtask.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(@field:PrimaryKey var username: String, var password: String)