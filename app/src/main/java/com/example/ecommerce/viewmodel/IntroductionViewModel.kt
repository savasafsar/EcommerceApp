package com.example.ecommerce.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.R
import com.example.ecommerce.util.Constans.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val auth: FirebaseAuth
)
    : ViewModel() {
    private val _navigate = MutableStateFlow(0)
    val navigate : StateFlow<Int> = _navigate
    companion object{
        const val SHOPPING_ACTIVITY = 23


    }
    val accountOptionsFragment: Int = R.id.action_introductionFragment_to_accountOptionsFragment

        init {
            val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY,false)
            val user = auth.currentUser
            if (user !=null) {
                viewModelScope.launch {
                    _navigate.emit(SHOPPING_ACTIVITY)
                }

            } else if (isButtonClicked) {
                viewModelScope.launch {
                    _navigate.emit(accountOptionsFragment)
                }

            } else {
                Unit

            }

            }
    fun startButtonClick() {
        sharedPreferences.edit().putBoolean(INTRODUCTION_KEY,true).apply()
        }
}