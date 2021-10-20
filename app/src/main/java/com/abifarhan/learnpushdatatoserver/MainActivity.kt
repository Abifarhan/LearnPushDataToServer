package com.abifarhan.learnpushdatatoserver

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import okhttp3.MultipartBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import okhttp3.RequestBody




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn_take_image)

        btn.setOnClickListener {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 110)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 110 && resultCode == RESULT_OK) {

            val bitmap = data!!.extras!!.get("data") as Bitmap

//            val requestFile = File(bitmap.toString()).
//            val file = convertToFile(bitmap)
//
            uploadImagetoServer(bitmap.toString())
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