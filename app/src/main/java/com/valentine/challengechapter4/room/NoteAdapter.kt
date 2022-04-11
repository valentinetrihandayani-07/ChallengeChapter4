package com.valentine.challengechapter4.room

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.valentine.challengechapter4.MainActivity
import com.valentine.challengechapter4.R
import com.valentine.challengechapter4.databinding.ItemNoteBinding
import com.valentine.challengechapter4.fragment.EditDialogFragment
import com.valentine.challengechapter4.fragment.EditDialogFragmentArgs
import com.valentine.challengechapter4.fragment.HomeFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.concurrent.Executors

class NoteAdapter(
    val listNote: List<Note>,

    val context: Context,
    //penggunaan lambda hor function
    private var ondelete: (Note) -> Unit,
    private var onedit: (Note) -> Unit
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listNote.size
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.txtID.text = listNote[position].id.toString()
        holder.binding.txtTitle.text = listNote[position].title
        holder.binding.txtNote.text = listNote[position].note


        holder.binding.icEdit.setOnClickListener {
            onedit(listNote[position])

        }
        holder.binding.icDelete.setOnClickListener {
            ondelete(listNote[position])

        }


    }
}



