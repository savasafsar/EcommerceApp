package com.example.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.CartProduct
import com.example.ecommerce.firebase.FirebaseCommon
import com.example.ecommerce.helper.getProductPrice
import com.example.ecommerce.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {
  private val _cartProduct = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProduct.asStateFlow()

    val productsPrice = cartProducts.map {
        when(it){
            is Resource.Success -> {
                calcutePrice(it.data!!)
            }
            else -> null
        }
    }
    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()
    private var cartProductDocuments = emptyList<DocumentSnapshot>()


    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index !=null && index !=-1){
            val documentId = cartProductDocuments[index].id
            firestore.collection("user").document(auth.uid!!).collection("cart")
                .document(documentId).delete()

        }

    }



    private fun calcutePrice(data: List<CartProduct>): Float {
       return data.sumByDouble {  cartProduct ->
           (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)*cartProduct.quantity).toDouble()
       }.toFloat()
    }


    init {
        getCartProduct()
    }
   private fun getCartProduct (){
        viewModelScope.launch {_cartProduct.emit(Resource.Loading())}
       firestore.collection("user").document(auth.uid!!).collection("cart")
           .addSnapshotListener{value,error ->
               if (error !=null || value ==null){
                   viewModelScope.launch { _cartProduct.emit(Resource.Error(error?.message.toString())) }
               }else {
                   cartProductDocuments = value.documents
                   val cartProduct = value.toObjects(CartProduct::class.java)
                   viewModelScope.launch {_cartProduct.emit(Resource.Success(cartProduct)) }
               }
           }
    }

    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ){

        val index = cartProducts.value.data?.indexOf(cartProduct)
        /**
         * index could be equal to -1 if the function [getCartProducts] delays which will also delay the result we except to inside the [_cartProduct]
         * and to prevent the app forom crashing we make a check
         */
        if (index !=null && index !=-1){
            val documentId = cartProductDocuments[index].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.INCREASE->{
                    viewModelScope.launch { _cartProduct.emit(Resource.Loading()) }
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE->{
                    if (cartProduct.quantity==1){

                        viewModelScope.launch{ _deleteDialog.emit(cartProduct)}
                        return
                    }
                    viewModelScope.launch { _cartProduct.emit(Resource.Loading()) }
                    decreaseQuantity(documentId)
                }
            }
        }



    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId){result,exception ->
            if (exception!=null) {
                viewModelScope.launch{_cartProduct.emit(Resource.Error(exception.message.toString()))}

            }
        }

    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId){result,exception ->
            if (exception!=null) {
                viewModelScope.launch{_cartProduct.emit(Resource.Error(exception.message.toString()))}

            }
        }

    }

}