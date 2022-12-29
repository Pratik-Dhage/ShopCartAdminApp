package com.example.shopcartadminapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.databinding.ItemCategoryLayoutBinding
import com.example.shopcartadminapp.model.CategoryModel

class CategoryAdapter(val context : Context, val list : ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.MyViewHolderClass>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {
        val view : ItemCategoryLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_category_layout,parent,false)

        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {

        val a = list[position]
        val context = holder.itemView.context

        holder.binding.txtCategoryName.text = a.catName
        Glide.with(context).load(a.catImage).into(holder.binding.ivItemCategory)

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolderClass(var binding: ItemCategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}