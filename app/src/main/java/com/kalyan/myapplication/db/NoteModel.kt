package com.kalyan.myapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="Notes")
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val NoteTitle:String,
    val NoteDes:String

)