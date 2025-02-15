package com.silent.chautari.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.silent.chautari.R
import com.silent.chautari.firebase.FirestoreClass
import com.silent.chautari.models.User
import org.checkerframework.common.subtyping.qual.Bottom
import android.annotation.SuppressLint as SuppressLint1

@Suppress("DEPRECATION")
class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {
    private val firestoreClass = FirestoreClass()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        window.setFlags(

            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN

        )

        setupActionBar()

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        firestoreClass.signInUser(this)

        val createBoard = findViewById<FloatingActionButton>(R.id.fab_create_board)
        createBoard.setOnClickListener{
            startActivity(Intent(this, CreateBoardActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupActionBar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.three_line_menu)

        toolbar.setNavigationOnClickListener {
            toogleDrawer()

        }
    }

    private fun toogleDrawer(){
        val drawer = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.main)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint1("MissingSuperCall")
    override fun onBackPressed() {
        val drawer = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.main)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
        }
    }

    @SuppressLint1("CutPasteId")
    fun updateNavigationUserDetails(user: User){
        val imageView = findViewById<ImageView>(R.id.nav_user_image)
        Glide.with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_nac_user) // While loading
            .error(R.drawable.baseline_image_search_24) // If load fails
            .into(imageView)

        val username = findViewById<TextView>(R.id.tv_username)
        username.text=user.name
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_profile -> {
                startActivity(Intent(this,MyProfileActivity::class.java))
            }

            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this, IntroActivity::class.java)

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        val drawer = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.main)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }



}