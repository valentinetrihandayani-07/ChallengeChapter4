package com.valentine.challengechapter4.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.valentine.challengechapter4.R
import com.valentine.challengechapter4.databinding.FragmentEditDialogBinding
import com.valentine.challengechapter4.room.Note
import com.valentine.challengechapter4.room.NoteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class EditDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentEditDialogBinding
    private val arguments: EditDialogFragmentArgs by navArgs()
    var mDb: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //casting data
        mDb = NoteDatabase.getInstance(requireContext())
        val titleEditText = view.findViewById<EditText>(R.id.edtTitle)
        val noteEditText = view.findViewById<EditText>(R.id.edtNote)
        //get
        titleEditText.setText(arguments.argumentNote.title)
        noteEditText.setText(arguments.argumentNote.note)
        val atribute = arguments.argumentNote


        binding.btnSave.setOnClickListener {
            val objectNote = Note(
                arguments.argumentNote.id,
                binding.edtTitle.text.toString(),
                binding.edtNote.text.toString()

            )
            GlobalScope.async {
                val update = mDb?.noteDao()?.updateNote(objectNote)
                activity?.runOnUiThread {
                    if (update != 0) {
                        Snackbar.make(view, "Data changed success", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(view, "Data changed failed", Snackbar.LENGTH_LONG).show()
                    }

                }

            }
            findNavController().navigate(R.id.action_editDialogFragment_to_homeFragment)

        }

    }
}





