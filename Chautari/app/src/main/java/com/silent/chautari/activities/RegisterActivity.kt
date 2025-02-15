package com.silent.chautari.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.silent.chautari.R
import com.silent.chautari.firebase.FirestoreClass
import com.silent.chautari.models.User

@Suppress("DEPRECATION")
class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        setupActionBar()

        window.setFlags(

            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    fun userRegisteredSuccess(){

        Toast.makeText(
            this,
            "You have successfully registered with the email address.",
            Toast.LENGTH_LONG
        ).show()

        hideProgressDialog()

        FirebaseAuth.getInstance().signOut()
        finish()

    }

    private fun setupActionBar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_sign_up_activity)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)

        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
        val sign = findViewById<Button>(R.id.btn_signup)
        sign.setOnClickListener{
            registerUser()
        }
    }
    private fun registerUser() {
        val etName = findViewById<EditText>(R.id.et_name)
        val etMail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val name: String = etName.text.toString().trim { it <= ' ' }
        val email: String = etMail.text.toString().trim { it <= ' ' }
        val password: String = etPassword.text.toString()

        if (validateForm(name, email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registerEmail = firebaseUser.email!!

                        val user = User(firebaseUser.uid,name, registerEmail)
                        FirestoreClass().registerUser(this, user)
                    } else {
                        // Handle error case more safely
                        val errorMessage = task.exception?.message ?: "Registration failed"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun validateForm(name: String, email: String, password: String) : Boolean{
        return when {
            TextUtils.isEmpty(name)-> {
                showErrorSnackBar("Please Enter a Name")
                false
            }
            TextUtils.isEmpty(email)-> {
                showErrorSnackBar("Please Enter an Email")
                false
            }
            TextUtils.isEmpty(password)-> {
                showErrorSnackBar("Please Enter a PassWord")
                false
            }else -> {
                true
            }
            
            
        }
    }
    
}