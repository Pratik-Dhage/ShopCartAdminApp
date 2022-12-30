package com.example.shopcartadminapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.databinding.ItemProductImageBinding
import com.example.shopcartadminapp.model.AddProductModel

class AddProductImageAdapter(val list:ArrayList<Uri>) : RecyclerView.Adapter<AddProductImageAdapter.MyViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {

        val view : ItemProductImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_product_image,parent,false)

        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {

        val a  = list[position]
        //val context = holder.itemView.context
        holder.binding.ivItemProduct.setImageURI(a)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolderClass(var binding: ItemProductImageBinding) : RecyclerView.ViewHolder(binding.root)

}