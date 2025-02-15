package com.silent.chautari.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.silent.chautari.R

class CreateBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_board)

        setupActionBar()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setupActionBar() {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_create_board_activity)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(resources.getColor(R.color.black, theme))

        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
            title = resources.getString(R.string.board_name)
        }

        val noBtn = findViewById<Button>(R.id.btn_no)
        noBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val yesBtn = findViewById<Button>(R.id.btn_yes)
        yesBtn.setOnClickListener {
            startActivity(Intent(this, NoteTaking::class.java))
            finish()
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}