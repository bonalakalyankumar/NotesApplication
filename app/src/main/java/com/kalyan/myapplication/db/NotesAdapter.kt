package com.kalyan.myapplication.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kalyan.myapplication.R


class NotesAdapter(
    private val listofNotes:List<NoteModel>,
    private val listener:ClickListener
): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val colors = listOf(
        R.color.note_color_1, // Mustard Yellow
        R.color.note_color_2, // Coral
        R.color.note_color_3, // Olive Green
        R.color.note_color_4, // Light Blue
        R.color.note_color_5,  // Blush Pink
        R.color.note_color_6,  // Mustard Yellow
        R.color.note_color_7,  // Coral
        R.color.note_color_8  // Peach
    )


    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val Title: TextView =itemView.findViewById(R.id.noteTitle)
        val description: TextView =itemView.findViewById(R.id.noteDesc)
        val cardView: CardView = itemView.findViewById(R.id.noteCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.note_layout,parent,false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listofNotes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote=listofNotes[position]
        holder.Title.text=currentNote.NoteTitle
        holder.description.text=currentNote.NoteDes


        val context = holder.itemView.context
        val colorResId = generateConsistentColor(currentNote)
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorResId))

        holder.itemView.setOnClickListener(){
            listener.onItemClick(currentNote)
        }

    }
    private fun generateConsistentColor(note: NoteModel): Int {
        // Combine title and description to create a hash
        val hash = (note.NoteTitle + note.NoteDes).hashCode()
        // Use hash to select a color index
        val index = (hash and 0x7FFFFFFF) % colors.size
        return colors[index]
    }


    interface ClickListener{
        fun onItemClick(note:NoteModel)
    }
}