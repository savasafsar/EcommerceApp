package com.example.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.User
import com.example.ecommerce.util.Constans.USER_COLLECTION
import com.example.ecommerce.util.RegisterFieldState
import com.example.ecommerce.util.RegisterValidation
import com.example.ecommerce.util.Resource
import com.example.ecommerce.util.validateEmail
import com.example.ecommerce.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val auth : FirebaseAuth,
    private val db: FirebaseFirestore
): ViewModel(){
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register:Flow<Resource<User>> = _register
    private val _validation = Channel<RegisterFieldState>()
    val validation = _validation.receiveAsFlow()
    fun createAccountWithEmailAndPassword(user:User,password:String) {

        if ( checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            auth.createUserWithEmailAndPassword(user.email,password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid,user)

                    }
                }.addOnFailureListener {
                    _register.value=Resource.Error(it.message.toString())


                }
        } else {
            val registerFieldsState = RegisterFieldState(
            validateEmail(user.email),validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }

        }
        }

    private fun saveUserInfo(userUid: String,user:User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                _register.value=Resource.Error(it.message.toString())
            }

    }


    private fun checkValidation(user: User, password: String) : Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
        return shouldRegister
    }
}