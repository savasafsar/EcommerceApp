package com.example.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.Product
import com.example.ecommerce.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore : FirebaseFirestore
): ViewModel() {
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts : StateFlow<Resource<List<Product>>> = _specialProducts

    init {
        fetchSpecialProducts()
    }
    fun fetchSpecialProducts() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
            println("mükemmel")
        }
        firestore.collection("Products")
            .whereEqualTo("category","Special Products").get()
            .addOnSuccessListener {result->
                val specialProductsList = result.toObjects(Product::class.java)
               viewModelScope.launch {
                   _specialProducts.emit(Resource.Success(specialProductsList))
               }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}