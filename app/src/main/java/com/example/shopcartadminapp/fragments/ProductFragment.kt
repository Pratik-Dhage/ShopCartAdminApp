package com.example.shopcartadminapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.databinding.FragmentProductBinding
import java.util.*


class ProductFragment : Fragment() {

   private lateinit var binding : FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBinding.inflate(layoutInflater)
         onClickListener()
        return binding.root
    }

    private fun onClickListener() {
        // Product Fragment to Add Product Fragment on clicking Floating action button
        binding.floatingActionButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment)
        }
    }


}