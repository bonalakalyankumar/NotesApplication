package com.kalyan.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NoteModel::class],
    version = 1,
    exportSchema=false
)

abstract class NoteDatabase: RoomDatabase(){

    abstract fun getNoteDao(): NoteDao

    companion object{
        const val Database_Name="Note_Database"

        private var Instance: NoteDatabase?=null

        operator fun invoke(context: Context)= Instance ?: synchronized(Any()){
            Instance ?: buildDatabase(context).also{
                Instance =it
            }
        }
        private fun buildDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            NoteDatabase::class.java,
            Database_Name
        ).build()

    }

}