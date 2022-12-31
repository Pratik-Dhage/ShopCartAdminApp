package com.example.shopcartadminapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.databinding.ItemProductListBinding
import com.example.shopcartadminapp.model.AddProductModel

class ProductAdapter(val list: ArrayList<AddProductModel>) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderClass {

        val view: ItemProductListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_product_list, parent, false
        )
        return MyViewHolderClass(view)
    }

    override fun onBindViewHolder(holder: MyViewHolderClass, position: Int) {

        val a  = list[position]
        val context = holder.itemView.context

        holder.binding.txtProductName.text = a.productName
        holder.binding.txtProductDescription.text = a.productDescription
        holder.binding.txtProductMRP.text = a.productMrp
        holder.binding.txtProductSP.text = a.productSp
        Glide.with(context).load(a.productCoverImg).into(holder.binding.ivProductList)

    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolderClass(var binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root)


}