package com.abifarhan.learnpushdatatoserver

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import com.abifarhan.learnpushdatatoserver.databinding.ActivityMainBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class MainActivity : AppCompatActivity(), UploadRequestBody.UploadCallback {

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

        if (selectedImageUri == null) {
            binding.layoutRoot.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "photo", this)
        MyApi().uploadImage(
            MultipartBody.Part.createFormData(
                "photo",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), "json")
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                binding.layoutRoot.snackbar(t.message!!)
                Log.d("this","gagal upload photo karena ${t.localizedMessage}")
                binding.progressBar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    binding.layoutRoot.snackbar(it.message)
                    binding.progressBar.progress = 100
                }
            }
        })
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

    override fun onProgressUpdate(percentage: Int) {
//        TODO("Not yet implemented")
    }
}