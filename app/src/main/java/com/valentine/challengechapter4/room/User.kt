package com.valentine.challengechapter4.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
@PrimaryKey(autoGenerate = true) var id: Int?,
@ColumnInfo(name = "fullname") var fullname: String,
@ColumnInfo(name = "username") var username: String,
@ColumnInfo(name = "email") var email: String,
@ColumnInfo(name = "password") var password: String,
) : Parcelable