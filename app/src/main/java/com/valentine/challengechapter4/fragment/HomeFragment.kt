package com.valentine.challengechapter4.fragment

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.valentine.challengechapter4.R
import com.valentine.challengechapter4.databinding.FragmentHomeBinding
import com.valentine.challengechapter4.room.NoteAdapter
import com.valentine.challengechapter4.room.NoteDatabase
import java.util.concurrent.Executors


class HomeFragment : Fragment() {
    //declaration
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "sfnote"
    private var mDB: NoteDatabase? = null

    //lifecycle fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        //initialization database room
        mDB = NoteDatabase.getInstance(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //casting data
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val username = sharedPreferences.getString("login", null)
        binding.txtUsername.text = "welcome, $username"
        if (username == null) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

        }
        //logout fragment
        binding.txtLogout.setOnClickListener {
            editor.clear().apply()
            Toast.makeText(
                activity, "User berhasil Logout", Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        //dialog add
        binding.fabAdd.setOnClickListener {
            val alertDialog = DialogFragment()
            alertDialog.show(requireActivity().supportFragmentManager, "add")
            //get data from database
            fetchData()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    fun fetchData() {
        //ngejalani proses di background thread agar tidak memblok ui
        //main thread ->not responding
        Executors.newSingleThreadExecutor()
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            //mengambil data note disimpan dalam variabel list note
            val listNote = mDB?.noteDao()?.getAllNote()
            val data = requireContext() as Activity
            //run data UI (main thread)
            data.runOnUiThread {
                listNote?.let {
                    //delete data using dialog positive negative
                    val adapter = NoteAdapter(it, requireContext(), ondelete = { listNote ->
                        MaterialAlertDialogBuilder(requireContext()).setPositiveButton("Yes") { p0, p1 ->
                            val mDb = NoteDatabase.getInstance(requireContext())
                            val executor = Executors.newSingleThreadExecutor()
                            executor.execute {
                                val result = mDb?.noteDao()?.deleteNote(listNote)
                                val delete = requireContext() as Activity
                                delete.runOnUiThread {
                                    if (result != 0) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Data ${listNote.title} removed",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Data ${listNote.title} Failed Removed",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        }.setNegativeButton(
                            "Cancel"
                        ) { p0, p1 ->
                            p0.dismiss()
                        }
                            .setMessage("Are you sure delete ${listNote.title}")
                            .setTitle("Delete Data").create().show()
                        refreshFragment()
                    }, onedit = { listNote ->
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToEditDialogFragment(
                                listNote
                            )
                        )
                        refreshFragment()
                        fetchData()
                    })

                    binding.recyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        NoteDatabase.destroyInstance()
    }

    private fun refreshFragment() {
        val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
        val currentFragment = fragmentManager?.findFragmentById(R.id.nav_host_fragment)
        val transaction = fragmentManager?.beginTransaction()
        currentFragment?.let {
            transaction?.detach(it)?.attach(it)?.commit()
        }
        fetchData()

    }
}
