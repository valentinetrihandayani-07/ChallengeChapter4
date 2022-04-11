package com.valentine.challengechapter4.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Note::class, User::class], version = 1)

abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao


    companion object {
        private var INSTANCE: NoteDatabase? = null
        fun getInstance(context: Context): NoteDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "Note_db.db"
                    ).build()

                }

            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}