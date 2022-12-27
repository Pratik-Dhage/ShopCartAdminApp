package com.example.shopcartadminapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.shopcartadminapp.MainActivity
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.activity.AllOrdersActivity
import com.example.shopcartadminapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding = DataBindingUtil.setContentView(context as Activity,R.layout.fragment_home)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        onClickListeners()

       return binding.root


    }

    private fun onClickListeners() {

        binding.product.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
        }
        binding.category.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }
        binding.slider.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }
        binding.allProductDetail.setOnClickListener {
            startActivity(Intent(requireContext(),AllOrdersActivity::class.java))
        }


    }


}