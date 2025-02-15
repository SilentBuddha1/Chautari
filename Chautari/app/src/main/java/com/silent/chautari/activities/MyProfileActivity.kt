package com.silent.chautari.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.Manifest
import android.widget.Button
import com.silent.chautari.R
import com.silent.chautari.firebase.FirestoreClass
import com.silent.chautari.models.User
import java.io.IOException

@Suppress("DEPRECATION")
class MyProfileActivity : BaseActivity() {

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL : String = ""

    private val firestoreClass = FirestoreClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_profile)

        setupActionBar()
        firestoreClass.signInUser(this)

        val imageView = findViewById<ImageView>(R.id.iv_user_image)

        imageView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (Use READ_MEDIA_IMAGES)
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    showImageChooser()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        READ_STORAGE_PERMISSION_CODE
                    )
                }
            } else {
                // Older versions (Use READ_EXTERNAL_STORAGE)
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    showImageChooser()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_STORAGE_PERMISSION_CODE
                    )
                }
            }
        }

        val button = findViewById<Button>(R.id.btn_update)
        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            } else {
                Toast.makeText(
                    this,
                    "You denied the permission for storage",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showImageChooser() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    @Deprecated("Use ActivityResult API instead of onActivityResult")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data?.data != null) {
            mSelectedImageFileUri = data.data

            val imageView = findViewById<ImageView>(R.id.iv_user_image)
            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_nac_user)
                    .into(imageView)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_my_profile_activity)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.black, theme))

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            title = resources.getString(R.string.my_profile)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    fun setUserDataInUI(user: User) {
        val imageView = findViewById<ImageView>(R.id.iv_user_image)
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_nac_user)
            .into(imageView)

        val username = findViewById<TextView>(R.id.et_name)
        val userMail = findViewById<TextView>(R.id.et_email)
        val userNum = findViewById<TextView>(R.id.et_mobile)

        username.text = user.name
        userMail.text = user.email
        if (user.mobile != 0L) {
            userNum.text = user.mobile.toString()
        }
    }


    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }



}
