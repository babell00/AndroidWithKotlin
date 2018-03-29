package com.example.kuba.uproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.kuba.uproject.db.HabitDbTable
import kotlinx.android.synthetic.main.activity_create_habit.*
import kotlinx.android.synthetic.main.single_card.*
import java.io.IOException

class CreateHabitActivity : AppCompatActivity() {

    private val TAG = "CreateHabitActivity"
    private val CHOOSE_IMAGE_REQUEST_CODE = 1

    private var imageBitmap: Bitmap? = null
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_habit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            Log.d(TAG, "An image has been chosen by the user")

            val bitmap = tryReadBitmap(data.data)
            bitmap?.let {
                imageBitmap = it
                iv_image.setImageBitmap(it)
                Log.d(TAG, "Image has be read and view updated")
            }
        }
    }

    fun chooseImage(view: View) {
        Log.d(TAG, "Chooser clicked")
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val chooser = Intent.createChooser(intent, "Choose image for habit")
        startActivityForResult(chooser, CHOOSE_IMAGE_REQUEST_CODE)
    }

    fun storeHabit(view: View) {
        if (et_title.isBlank() || et_description.isBlank()) {
            Log.d(TAG, "No habit stored: title or description missing")
            displayErrorMessage("Your habit needs an engaging title and description.")
            return
        } else if (imageBitmap == null) {
            Log.d(TAG, "No habit stored: image missing")
            displayErrorMessage("Add a motivating picture to you habit.")
            return
        }
        //Store habit
        val title = et_title.text.toString()
        val description = et_description.text.toString()
        val habit = Habit(title, description, imageBitmap!! )

        val id = HabitDbTable(this).store(habit)

        if(id == -1L){
            displayErrorMessage("Habit cannot be stored")
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun displayErrorMessage(message: String) {
        tv_error.text = message
        tv_error.visibility = View.VISIBLE
    }

    private fun tryReadBitmap(data: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(contentResolver, data)
        } catch (ex: IOException) {
            Log.e(TAG, "Cannot read image", ex)
            null
        }
    }

    private fun EditText.isBlank() = this.text.toString().isBlank()
}
