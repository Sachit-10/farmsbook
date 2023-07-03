package com.example.farmsbook

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.farmsbook.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class MainActivity : AppCompatActivity() {

    var request_code = 1000
    var imageuri:Uri?=null

    var storage:FirebaseStorage?=null
    var storage_ref:StorageReference?=null

    var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        storage = FirebaseStorage.getInstance()
        storage_ref = storage?.getReference()

        binding?.photoBtn?.setOnClickListener(){

            var intent=Intent(Intent.ACTION_PICK)
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, request_code)
        }

        binding?.uploadBtn?.setOnClickListener(){
            uploadImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            if (requestCode == request_code) {

                imageuri=data?.data
                binding?.ivPhoto?.setImageURI(imageuri)

            }
        }
    }

    fun uploadImage() {


            val progressdialog = ProgressDialog(this)
            progressdialog.setMessage("Uploading the image")
            progressdialog.show()

        storage_ref = storage_ref?.child("images/" + UUID.randomUUID().toString())

        storage_ref?.putFile(imageuri!!)?.addOnSuccessListener {
            progressdialog.dismiss()
            Toast.makeText(applicationContext, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
        }
            ?.addOnFailureListener{
                Toast.makeText(applicationContext, "Failed to upload the image", Toast.LENGTH_SHORT).show()
            }


    }
}