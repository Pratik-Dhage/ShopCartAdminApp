package com.example.shopcartadminapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.databinding.ActivityAllOrdersBinding

class AllOrdersActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAllOrdersBinding
    private lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeFields()
       // onClickListener()
    }

    private fun initializeFields() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_all_orders)
        view =binding.root
    }
}