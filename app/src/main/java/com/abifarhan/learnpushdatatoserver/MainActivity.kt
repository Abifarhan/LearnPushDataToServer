package com.abifarhan.learnpushdatatoserver

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import com.abifarhan.learnpushdatatoserver.databinding.ActivityMainBinding
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.RequestBody




class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    companion object{
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setOnClickListener {
            openImageChooser()
        }

        binding.buttonUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {

    }

    private fun openImageChooser() {

        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImagetoServer(photoPath: String) {

//        val requestFile = File(photoPath).asRequestBody()
//        val body = MultipartBody.Part.createFormData("photo", "image", requestFile)
    }

    private fun convertToFile(bitmap: Bitmap) : File{

        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}_image.webp")

        val bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.WEBP,0,bos)
        val bitmapData = bos.toByteArray()

        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        return file
    }
}