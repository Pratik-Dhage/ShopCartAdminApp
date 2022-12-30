package com.example.shopcartadminapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import com.example.shopcartadminapp.R
import com.example.shopcartadminapp.adapter.AddProductImageAdapter
import com.example.shopcartadminapp.databinding.FragmentAddProductBinding
import com.example.shopcartadminapp.helping_classes.Global
import com.example.shopcartadminapp.model.AddProductModel
import com.example.shopcartadminapp.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList


class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var categoryList: ArrayList<String>
    private lateinit var adapter: AddProductImageAdapter
    private lateinit var dialog: Dialog
    private lateinit var listUri: ArrayList<Uri>
    private lateinit var listImages: ArrayList<String>

    private var coverImage: Uri? = null
    private var coverImgUrl: String? = ""


    //for cover Image
    private var launchGalleryActivityForCoverImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            coverImage = it.data!!.data
            binding.ivProductCoverImage.setImageURI(coverImage) //set selected image from gallery
            binding.ivProductCoverImage.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val imageUri = it.data!!.data
            listUri.add(imageUri!!)
            adapter.notifyDataSetChanged()

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddProductBinding.inflate(layoutInflater)
        initializeFields()
        setUpProductRecyclerView()
        setCategoryDropDownList()
        setUpProgressDialog()
        onClickListener()
        return binding.root
    }

    private fun initializeFields() {
       listUri = ArrayList()
       listImages = ArrayList()
    }

    private fun setUpProductRecyclerView(){
        adapter = AddProductImageAdapter(listUri)
        binding.rvAddProductFragment.adapter = adapter
    }

    private fun setUpProgressDialog() {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_dialog_layout)
        dialog.setCancelable(false)

    }

    private fun onClickListener() {

       binding.btnSelectCoverImg.setOnClickListener {
           val intentGallery = Intent("android.intent.action.GET_CONTENT")
           intentGallery.type = "image/*"
           launchGalleryActivityForCoverImage.launch(intentGallery)
       }

      binding.btnProductImage.setOnClickListener {
          val intentGallery = Intent("android.intent.action.GET_CONTENT")
          intentGallery.type = "image/*"
          launchProductActivity.launch(intentGallery)
      }
        setUpProductRecyclerView() // set Product RecyclerView after choosing Product Image

      binding.btnAddProduct.setOnClickListener {
        if(validateData()){
            uploadImage()
        }
        else {  Global.showToast(requireContext(),resources.getString(R.string.please_fill_required_details))  }
      }

    }

    private fun uploadImage() {

        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg" // creates random id for selected file from gallery

        //store selected image in Firebase Storage
        val refFirebaseStorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refFirebaseStorage.putFile(coverImage!!)
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image->
                 coverImgUrl = image.toString()
                    uploadProductImage()
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Global.showSnackBar(binding.root,it.toString())
            }
    }

    private var i = 0
    private fun uploadProductImage() {

        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg" // creates random id for selected file from gallery

        //store selected image in Firebase Storage
        val refFirebaseStorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refFirebaseStorage.putFile(listUri[i])
            .addOnSuccessListener {

                it.storage.downloadUrl.addOnSuccessListener { image->
                    listImages.add(image.toString())
                  if(listUri.size == listImages.size){
                         storeProductData()
                  }
                    else{
                        i+=1  // i= i+1
                        uploadProductImage() // Recursion
                    }
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Global.showSnackBar(binding.root,it.toString())
            }
    }

    private fun storeProductData() {

        val db = Firebase.firestore.collection("products")
        val key = db.document().id //will also be productId

        // 8 parameters in AddProductModel
        val data = AddProductModel(
            binding.txtEdtProductName.text.toString(),
            binding.txtEdtProductDescription.text.toString(),
            coverImgUrl.toString(),
            categoryList[binding.productCategoryDropdown.selectedItemPosition],
            key,
            binding.txtEdtProductMRP.text.toString(),
            binding.txtEdtProductSP.text.toString(),
            listImages
        )

        db.document(key).set(data).addOnSuccessListener {
               dialog.dismiss()
            Global.showSnackBar(binding.root,resources.getString(R.string.product_added_successfully))
            clearAllFields()

        }
            .addOnFailureListener {
                dialog.dismiss()
                Global.showSnackBar(binding.root,it.toString())
            }
    }

    private fun clearAllFields() {
        binding.txtEdtProductName.text = null
        binding.txtEdtProductDescription.text = null
        binding.txtEdtProductMRP.text = null
        binding.txtEdtProductSP.text = null
        binding.ivProductCoverImage.visibility = View.GONE
        listUri.clear()
        binding.rvAddProductFragment.visibility = View.GONE
        binding.productCategoryDropdown.setSelection(0)
    }

    private fun validateData() : Boolean {

        if(binding.txtEdtProductName.text.toString().isEmpty()){
          //  Global.showSnackBar(binding.root,resources.getString(R.string.product_name_empty))
            binding.txtEdtProductName.error = resources.getString(R.string.product_name_empty)
            return false
        }

        if(binding.txtEdtProductDescription.text.toString().isEmpty()){
           // Global.showSnackBar(binding.root,resources.getString(R.string.product_description_empty))
            binding.txtEdtProductDescription.error = resources.getString(R.string.product_description_empty)
            return false
        }

        if(binding.txtEdtProductMRP.text.toString().isEmpty()){
           // Global.showSnackBar(binding.root,resources.getString(R.string.product_mrp_empty))
            binding.txtEdtProductMRP.error = resources.getString(R.string.product_mrp_empty)
            return false
        }

        if(binding.txtEdtProductSP.text.toString().isEmpty()){
           // Global.showSnackBar(binding.root,resources.getString(R.string.product_sp_empty))
            binding.txtEdtProductSP.error = resources.getString(R.string.product_sp_empty)
            return false
        }

        if(coverImage == null){
            Global.showSnackBar(binding.root,resources.getString(R.string.please_select_cover_image))
            return false
        }

        if(listUri.size < 1){
            Global.showSnackBar(binding.root,resources.getString(R.string.please_select_product_image))
            return false
        }

        if(binding.productCategoryDropdown.selectedItemPosition==0){
            Global.showSnackBar(binding.root,resources.getString(R.string.please_select_category))
            return false
        }

        return true
    }


    private fun setCategoryDropDownList() {

        categoryList = ArrayList()
        //get data from FireStore
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            categoryList.clear()
            for (doc in it.documents) {

                val data = doc.toObject(CategoryModel::class.java)
                categoryList.add(data!!.catName!!)
            }
            categoryList.add(0,resources.getString(R.string.select_category)) // this will be 1st element for better UI

            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_add_product_layout,categoryList)
            binding.productCategoryDropdown.adapter = arrayAdapter // set arrayAdapter to product category dropdown
        }
    }
}