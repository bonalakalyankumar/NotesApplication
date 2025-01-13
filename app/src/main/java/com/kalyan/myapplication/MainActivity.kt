package com.kalyan.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kalyan.myapplication.Repo.Repo
import com.kalyan.myapplication.ViewModel.NoteViewModel
import com.kalyan.myapplication.ViewModel.NotesViewModelFactory
import com.kalyan.myapplication.db.NoteDatabase
import com.kalyan.myapplication.db.NoteModel
import com.kalyan.myapplication.db.NotesAdapter

class MainActivity : AppCompatActivity(), NotesAdapter.ClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var notesViewModelFactory: NotesViewModelFactory
    private lateinit var repo: Repo
    private lateinit var database: NoteDatabase
    private lateinit var noteadapter: NotesAdapter
    private lateinit var rv: RecyclerView
    private lateinit var fabhome: FloatingActionButton
    private lateinit var dialog: Dialog
    private lateinit var edtNoteTitle: EditText
    private lateinit var edtNoteDesc: EditText
    private lateinit var fabadd: FloatingActionButton
    private lateinit var btnDelete: FloatingActionButton // Added Delete Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        noteViewModel.getAllNotes().observe(this) {
            noteadapter = NotesAdapter(it, this)
            rv.adapter = noteadapter
            rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        fabhome.setOnClickListener {
            openDialog(note = null) // Create a new note
        }
    }

    private fun openDialog(note: NoteModel?) {
        dialog = Dialog(this, R.style.FullScreenDialog).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.add_note_dialog)
        }

        edtNoteTitle = dialog.findViewById(R.id.addNoteTitle)
        edtNoteDesc = dialog.findViewById(R.id.addNoteDesc)
        fabadd = dialog.findViewById(R.id.addNoteFab)
        btnDelete = dialog.findViewById(R.id.deleteNoteFab) // Reference the Delete button

        // If editing an existing note, populate fields and show the Delete button
        if (note != null) {
            edtNoteTitle.setText(note.NoteTitle)
            edtNoteDesc.setText(note.NoteDes)
            btnDelete.visibility = Button.VISIBLE
        } else {
            btnDelete.visibility = Button.GONE // Hide the Delete button when creating a new note
        }

        // Add/Update Note Logic
        fabadd.setOnClickListener {
            val title = edtNoteTitle.text.toString().trim()
            val desc = edtNoteDesc.text.toString().trim()

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newNote = note?.copy(NoteTitle = title, NoteDes = desc) ?: NoteModel(NoteTitle = title, NoteDes = desc)

            if (note == null) {
                noteViewModel.insertNote(newNote) // Insert new note
            } else {
                noteViewModel.updateNote(newNote) // Update existing note
            }

            dialog.dismiss()
        }

        // Delete Note Logic
        btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it) // Delete the note
                Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun init() {
        database = NoteDatabase(this)
        repo = Repo(database.getNoteDao())
        notesViewModelFactory = NotesViewModelFactory(repo)
        noteViewModel = ViewModelProvider(this, notesViewModelFactory).get(NoteViewModel::class.java)
        rv = findViewById(R.id.homeRecyclerView)
        fabhome = findViewById(R.id.homeNoteFab)
    }

    override fun onItemClick(note: NoteModel) {
        openDialog(note) // Open the dialog for editing an existing note
    }
}