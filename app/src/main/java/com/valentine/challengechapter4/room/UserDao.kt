package com.valentine.challengechapter4.room

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE username= :username and password= :password")
    fun getLogin(username:String, password:String): List <User>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Long
}