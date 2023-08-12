package com.example.ecommerce.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.adapters.SpecialProductsAdapter
import com.example.ecommerce.databinding.FragmentMainCategoryBinding

class MainCategoryFragment : Fragment(R.layout.fragment_main_category) {
    private lateinit var binding : FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialProductsRV()
    }

    private fun setupSpecialProductsRV() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
       adapter  = specialProductsAdapter
        }

    }
}