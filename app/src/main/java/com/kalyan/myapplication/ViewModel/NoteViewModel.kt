package com.kalyan.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalyan.myapplication.Repo.Repo
import com.kalyan.myapplication.db.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repo: Repo
): ViewModel(
) {

    fun getAllNotes()=repo.getAllNotes()

    fun insertNote(note: NoteModel){
        viewModelScope.launch (Dispatchers.IO) {
            repo.insertNote(note)
        }
    }

    fun updateNote(note:NoteModel){
        viewModelScope.launch (Dispatchers.IO) {
            repo.updateNote(note)
        }
    }

    fun deleteNote(note:NoteModel){
        viewModelScope.launch (Dispatchers.IO) {
            repo.deleteNote(note)
        }
    }

}