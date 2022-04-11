package com.valentine.challengechapter4.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.valentine.challengechapter4.R
import com.valentine.challengechapter4.databinding.FragmentRegisterBinding
import com.valentine.challengechapter4.room.NoteDatabase
import com.valentine.challengechapter4.room.User



class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var dbuser: NoteDatabase?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        sharedPref = SharedPreference(view.context)
//


        dbuser = NoteDatabase.getInstance(requireContext())
        binding.btnRegister.setOnClickListener {
            val fullname = binding.edtFullname.text.toString()
            val username = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val objectUser  = User(null, fullname, username, email, password)
            if (binding.edtFullname.text.isNullOrEmpty() || binding.edtUsername.text.isNullOrBlank() || binding.edtEmail.text.isNullOrBlank() || binding.edtPassword.text.isNullOrBlank()) {
                Toast.makeText(
                    activity, "Please Check the field and type again", Toast.LENGTH_LONG
                ).show()
            } else {
                (binding.edtFullname.text!!.isNotEmpty() && binding.edtUsername.text!!.isNotEmpty() && binding.edtEmail.text!!.isNotEmpty() && binding.edtPassword.text!!.isNotEmpty())

                Thread{
                    val resultdb =  dbuser?.userDao()?.insertUser(objectUser)
                    Log.d("login", resultdb.toString())
                    activity?.runOnUiThread {
                        if (resultdb == 0.toLong())
                        {
                            Toast.makeText(
                                requireContext(),
                                "Data berhasil ditambahkan", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(requireContext(),
                            "Data gagal ditambahkan", Toast.LENGTH_LONG).show()
                        }

                    }
                }
                    .start()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

            }
        }
    }

}