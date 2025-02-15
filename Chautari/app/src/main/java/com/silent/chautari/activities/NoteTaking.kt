package com.silent.chautari.activities
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.silent.chautari.DataBase.DataBaseSQLite
import com.silent.chautari.R

class NoteTaking : BaseActivity() {

    private lateinit var dbHelper: DataBaseSQLite
    private lateinit var editTextName: EditText
    private lateinit var editTextMember: EditText
    private lateinit var editTextUpdate: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_note_taking)


        dbHelper = DataBaseSQLite(this)
        editTextMember = findViewById(R.id.editTextAge)
        editTextName = findViewById(R.id.editTextName)
        editTextUpdate = findViewById(R.id.editTextId)


        val btnInsert: Button = findViewById(R.id.btn_insert)
        btnInsert.setOnClickListener{insertData()}

        val btnUpdate: Button = findViewById(R.id.btn_update)
        btnUpdate.setOnClickListener{updateData()}

        val btnRead: Button = findViewById(R.id.btn_read)
        btnRead.setOnClickListener{displayData()}

        val btnDelete: Button = findViewById(R.id.btn_delete)
        btnDelete.setOnClickListener{deleteData()}

        val btnBack: Button = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
    

    private fun insertData() {
        val name = editTextName.text.toString()
        val age = editTextMember.text.toString()
        if(name.isNotBlank() &&  age.isNotBlank()){
            val id = dbHelper.insertData(name, age.toInt())

            if(id > 0){
                editTextName.text.clear()
                editTextMember.text.clear()
                hideKeyboard()
                displayData()
            }
        }
        editTextUpdate.visibility = View.GONE
    }

    private fun displayData() {
        val cursor = dbHelper.readData()
        val columns =
            arrayOf(DataBaseSQLite.COL_ID, DataBaseSQLite.COL_NAME, DataBaseSQLite.COL_MEMBER)
        val toViews = intArrayOf(R.id.textId, R.id.textName, R.id.editTextAge)

        val adapter = SimpleCursorAdapter(
            this, R.layout.list_item, cursor,columns,toViews, 0
        )
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter

    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if(view != null){
            val key = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            key.hideSoftInputFromWindow(view.windowToken, 0)
        }
       
    }

    private fun updateData(){
        editTextUpdate.visibility = View.VISIBLE
        val name = editTextName.text.toString()
        val age = editTextMember.text.toString()
        val id = editTextUpdate.text.toString()

        if(name.isNotBlank() && age.isNotBlank() && id.isNotBlank()){
            val updateRows = dbHelper.updateData(id,name,age.toInt())
            if(updateRows>0){
                editTextName.text.clear()
                editTextMember.text.clear()
                editTextUpdate.text.clear()
                hideKeyboard()
                displayData()
            }
        }
    }

    private fun deleteData(){
        editTextUpdate.visibility = View.VISIBLE
        val id = editTextUpdate.text.toString()
        val idToDelete = id
        val deleteRow = dbHelper.deleteData(idToDelete)

        if(deleteRow > 0){
            editTextName.text.clear()
            editTextMember.text.clear()
            editTextUpdate.text.clear()
            hideKeyboard()
            displayData()
        }
    }
}