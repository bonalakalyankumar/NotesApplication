package com.kalyan.myapplication.Repo

import com.kalyan.myapplication.db.NoteDao
import com.kalyan.myapplication.db.NoteModel

class Repo (
    private val Dao: NoteDao
){
    fun insertNote(note: NoteModel){
        Dao.insertNote(note)
    }

    fun updateNote(note: NoteModel){
        Dao.updateNote(note)
    }


    fun deleteNote(note: NoteModel){
        Dao.deleteNote(note)
    }

    fun getAllNotes()=Dao.getAllNotes()
}