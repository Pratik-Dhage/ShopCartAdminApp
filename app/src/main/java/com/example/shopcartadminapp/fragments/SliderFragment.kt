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
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.databinding.FragmentSliderBinding
import com.example.shopcartadminapp.helping_classes.Global
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class SliderFragment : Fragment() {

    private lateinit var binding : FragmentSliderBinding
    private var imageUri : Uri?=null
    private lateinit var dialog : Dialog

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            imageUri = it.data!!.data
            binding.ivPreview .setImageURI(imageUri) //set selected image from gallery
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSliderBinding.inflate(layoutInflater)

        setUpProgressDialog()
        onClickListener()

        return binding.root

    }

    private fun setUpProgressDialog(){
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_dialog_layout)
        dialog.setCancelable(false)

    }

    private fun onClickListener() {

        binding.ivPreview.setOnClickListener{
            val intentGallery = Intent("android.intent.action.GET_CONTENT")
            intentGallery.type = "image/*"
            launchGalleryActivity.launch(intentGallery)

        }

         binding.btnUpload.setOnClickListener {
             if(imageUri != null){
                 uploadImage(imageUri!!)
             }
             else Global.showToast(requireContext(),resources.getString(R.string.please_select_image))
         }
    }

    private fun uploadImage(uri: Uri) {

        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg" // creates random id for selected file from gallery

        //store selected image in Firebase Storage
        val refFirebaseStorage = FirebaseStorage.getInstance().reference.child("slider/$fileName")
        refFirebaseStorage.putFile(uri)
            .addOnSuccessListener {

               it.storage.downloadUrl.addOnSuccessListener { image->
                   storeData(image.toString()) //store image in FireStore
               }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Global.showSnackBar(binding.root,it.toString())
            }
    }

    //Store Image in FireStore
    private fun storeData(image: String) {

        val db = Firebase.firestore
        val data = hashMapOf<String,Any>(
            "img" to image
        )

        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                Global.showSnackBar(binding.root,resources.getString(R.string.slider_image_upload_success))
            }
            .addOnFailureListener{
                dialog.dismiss()
                Global.showSnackBar(binding.root,it.toString())
            }

    }


}