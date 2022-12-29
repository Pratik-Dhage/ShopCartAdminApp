package com.example.shopcartadminapp.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.adapter.CategoryAdapter
import com.example.shopcartadminapp.databinding.FragmentCategoryBinding
import com.example.shopcartadminapp.helping_classes.Global
import com.example.shopcartadminapp.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private var imageUri: Uri? = null
    private lateinit var dialog: Dialog

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUri = it.data!!.data
            binding.ivCategory.setImageURI(imageUri) //set selected image from gallery
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryBinding.inflate(layoutInflater)
        getCategoryData()
        setUpProgressDialog()

        onClickListener()

        return binding.root
    }

    private fun getCategoryData() {
        val list = arrayListOf<CategoryModel>()

            //get data from FireStore
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            list.clear()
            for(doc in it.documents){

                val data = doc.toObject(CategoryModel::class.java)
                list.add(data!!)
            }

            binding.rvCategoryFragment.adapter = CategoryAdapter(requireContext(),list)
            //setUpRecyclerView Data
            // layout manager and orientation defined in CategoryFragment already
           /* val layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            binding.rvCategoryFragment.layoutManager = layoutManager*/


        }
    }

    private fun setUpProgressDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_dialog_layout)
        dialog.setCancelable(false)

    }

    private fun onClickListener() {

        binding.ivCategory.setOnClickListener {
            val intentGallery = Intent("android.intent.action.GET_CONTENT")
            intentGallery.type = "image/*"
            launchGalleryActivity.launch(intentGallery)

        }

       binding.btnCategory.setOnClickListener {
           validateData(binding.txtEdtCategory.text.toString())
       }


    }

    private fun validateData(categoryName: String) {

        if(categoryName.isEmpty()){
            Global.showSnackBar(binding.root,resources.getString(R.string.category_name_cannot_be_empty))
        }
        else if(imageUri==null){
            Global.showToast(requireContext(),resources.getString(R.string.please_select_image))
        }
        else{
            uploadImage(categoryName)
        }
    }

    private fun uploadImage(categoryName: String) {

        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg" // creates random id for selected file from gallery

        //store selected image in Firebase Storage
        val refFirebaseStorage = FirebaseStorage.getInstance().reference.child("category/$fileName")
        refFirebaseStorage.putFile(imageUri!!)
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image->
                    storeData(categoryName, image.toString()) //store image in FireStore
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Global.showSnackBar(binding.root,it.toString())
            }

    }

    //Store Image in FireStore
    private fun storeData(categoryName: String, url: String) {

        val db = Firebase.firestore
        val data = hashMapOf<String,Any>(
            "catName" to categoryName,
            "catImage" to url
        )

        db.collection("categories").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Global.showSnackBar(binding.root,resources.getString(R.string.category_image_upload_success))
                binding.ivCategory.setImageDrawable(resources.getDrawable(R.drawable.image_preview)) // make empty after category image uploaded successfully
                binding.txtEdtCategory.text = null
                getCategoryData()
            }
            .addOnFailureListener{
                dialog.dismiss()
                Global.showSnackBar(binding.root,it.toString())
            }

    }
}