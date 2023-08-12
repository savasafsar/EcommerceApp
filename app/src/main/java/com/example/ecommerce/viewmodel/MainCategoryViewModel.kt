package com.example.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.Product
import com.example.ecommerce.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainCategoryViewModel @Inject constructor(
    private val firestore : FirebaseFirestore
): ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts : StateFlow<Resource<List<Product>>> = _specialProducts

    fun fetchSpecialProducts() {
        firestore.collection("Products")
            .whereEqualTo("category","Special Products ").get()
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}