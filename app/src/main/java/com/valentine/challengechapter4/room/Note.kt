package com.valentine.challengechapter4.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "note") var note: String,
) : Parcelable