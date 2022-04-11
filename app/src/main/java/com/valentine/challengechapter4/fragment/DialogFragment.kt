package com.valentine.challengechapter4.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.valentine.challengechapter4.databinding.FragmentDialogBinding
import com.valentine.challengechapter4.room.Note
import com.valentine.challengechapter4.room.NoteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class DialogFragment : DialogFragment() {
    //declaration
    private lateinit var binding: FragmentDialogBinding
    var mDb: NoteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        mDb = NoteDatabase.getInstance(requireActivity())
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSave.setOnClickListener {
            val objectNote = Note(
                null,
                binding.edtTitle.text.toString(),
                binding.edtNotes.text.toString()
            )

            GlobalScope.async {
                val result = mDb?.noteDao()?.insertNote(objectNote)
                activity?.runOnUiThread {
                    if (result != 0.toLong()) {

                        //sukses
                        Toast.makeText(
                            activity, "Success added ${objectNote.title}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        //gagal
                        Toast.makeText(
                            activity, "Failed added ${objectNote.title}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    dismiss()
                }
            }
        }
    }
}






