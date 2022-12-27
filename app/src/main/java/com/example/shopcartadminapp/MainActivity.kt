package com.example.shopcartadminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.shopcartadminapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var view : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initializeFields()
        onClickListeners()

    }

    private fun initializeFields() {
     binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        view = binding.root
    }

    private fun onClickListeners() {


    }
}