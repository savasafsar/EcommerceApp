package com.example.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.User
import com.example.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _login  = MutableSharedFlow<Resource<FirebaseUser>>()

    val login = _login.asSharedFlow()
    fun login(email:String,password:String) {
        viewModelScope.launch{
            _login.emit(Resource.Loading())
        }
        auth.signInWithEmailAndPassword(
            email,password
        ).addOnSuccessListener {
        viewModelScope.launch {
           it.user?.let {
               _login.emit(Resource.Success(it))
           }
        }
        }.addOnFailureListener {
            viewModelScope.launch {
                    _login.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}