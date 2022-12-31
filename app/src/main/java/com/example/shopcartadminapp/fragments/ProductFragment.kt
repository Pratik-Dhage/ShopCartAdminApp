package com.example.shopcartadminapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.adapter.CategoryAdapter
import com.example.shopcartadminapp.adapter.ProductAdapter
import com.example.shopcartadminapp.databinding.FragmentProductBinding
import com.example.shopcartadminapp.model.AddProductModel
import com.example.shopcartadminapp.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class ProductFragment : Fragment() {

   private lateinit var binding : FragmentProductBinding
   private lateinit var adapter : ProductAdapter
   private val list : ArrayList<AddProductModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBinding.inflate(layoutInflater)
        getProductListData()
        setUpProductListRecyclerView()
         onClickListener()
        return binding.root
    }

    private fun setUpProductListRecyclerView(){
        adapter = ProductAdapter(list)
        binding.rvProductFragment.adapter = adapter
    }

    private fun getProductListData(){
        //  val list = arrayListOf<AddProductModel>()
        //get data from FireStore
        Firebase.firestore.collection("products").get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){

                val data = doc.toObject(AddProductModel::class.java)
                list.add(data!!)
            }

            binding.rvProductFragment.adapter = ProductAdapter(list)
    }

    }


    private fun onClickListener() {
        // Product Fragment to Add Product Fragment on clicking Floating action button
        binding.floatingActionButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_productFragment_to_addProductFragment)
        }
    }


}