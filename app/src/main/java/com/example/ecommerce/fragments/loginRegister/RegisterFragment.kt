package com.example.ecommerce.fragments.loginRegister

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerce.R
import com.example.ecommerce.data.User
import com.example.ecommerce.databinding.FragmentRegisterBinding
import com.example.ecommerce.util.RegisterValidation
import com.example.ecommerce.util.Resource
import com.example.ecommerce.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register){
private lateinit var binding : FragmentRegisterBinding
private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = User(
                    edFirstName.text.toString().trim(),
                    edLastName.text.toString().trim(),
                    edEmail.text.toString().trim()
                )
                val password = edPassword.text.toString()
                viewModel.createAccountWithEmailAndPassword(user,password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it) {
                    is Resource.Loading->{
                        binding.buttonRegisterRegister.startAnimation()
                    }
                    is Resource.Success->{
                        Log.d("test",it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()

                    }
                    is Resource.Error-> {
                        Log.e(TAG,it.message.toString())
                        binding.buttonRegisterRegister.revertAnimation()

                    }
                    else -> Unit

                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {validation->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main) {
                        binding.edEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if (validation.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }
            }
        }
    }
}