package com.valentine.challengechapter4.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.valentine.challengechapter4.R
import com.valentine.challengechapter4.databinding.FragmentLoginBinding
import com.valentine.challengechapter4.room.NoteDatabase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var dbUser: NoteDatabase? = null
    lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "sfnote"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbUser = NoteDatabase.getInstance(requireContext())

        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            val user = StringBuffer()
            val pass = StringBuffer()
            Thread {
                val resultUser = dbUser?.userDao()?.getLogin(username, password)
                Log.d("cekuser", resultUser.toString())
                activity?.runOnUiThread {
                    resultUser?.forEach {
                        user.append(it.username)
                        pass.append(it.password)
                    }
                        if (username == user.toString() && password == pass.toString()) {
                            Toast.makeText(
                                requireActivity(),
                                "Login Success", Toast.LENGTH_LONG
                            ).show()
                            editor.putString("login", username)
                            editor.apply()
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "User not found", Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }
                .start()

        }
        binding.txtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

    }
}


