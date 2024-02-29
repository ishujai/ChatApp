package com.example.chatapplicationpracticefinal

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mobno= intent.getStringExtra("contactno")
        intent.putExtra("contactno", mobno)

        Log.e("contact","$mobno")
        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()

        val add: FloatingActionButton = findViewById(R.id.addNote)
        add.setOnClickListener {
            val view1: View = LayoutInflater.from(this).inflate(R.layout.add_note_dialog, null)
            val titleLayout: TextInputLayout = view1.findViewById(R.id.nameLayout)
            val contentLayout: TextInputLayout = view1.findViewById(R.id.emailLayout)
            val uniqueIdLayout: TextInputLayout = view1.findViewById(R.id.uniqueIdLayout)
            val titleET: TextInputEditText = view1.findViewById(R.id.nameET)
            val contentET: TextInputEditText = view1.findViewById(R.id.emailET)
            val uniqueIdET: TextInputEditText = view1.findViewById(R.id.uniqueIdET)


            val alertDialog: AlertDialog = AlertDialog.Builder(this)
                .setTitle("Add")
                .setView(view1)
                .setPositiveButton("Add") { dialogInterface: DialogInterface, i: Int ->
                    if (titleET.text.toString().isEmpty()) {
                        titleLayout.error = "This field is required!"
                    } else if (contentET.text.toString().isEmpty()) {
                        contentLayout.error = "This field is required!"
                    }else if (uniqueIdET.text.toString().isEmpty()) {
                        uniqueIdLayout.error = "This field is required!"
                    }
                    else {
                        val dialog = ProgressDialog(this)
                        dialog.setMessage("Storing in Database...")
                        dialog.show()

                        val user = MainModel()
                        user.name = titleET.text.toString()
                        user.email = contentET.text.toString()
                        user.uniqueid=uniqueIdET.text.toString()

                        database.reference.child("users").child(user.uniqueid!!).setValue(user)
                            .addOnSuccessListener {
                                dialog.dismiss()
                                dialogInterface.dismiss()
                                Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                dialog.dismiss()
                                Toast.makeText(this, "There was an error while saving data", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
                .create()
            alertDialog.show()
        }

        val empty: TextView = findViewById(R.id.empty)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        database.reference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val arrayList = ArrayList<MainModel>()

                for (dataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(MainModel::class.java)
                    user?.key = dataSnapshot.key
                    arrayList.add(user!!)
                }

                Log.e("list", "noteslist$arrayList")
                if (arrayList.isEmpty()) {
                    empty.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    empty.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

                val layoutManagerTwo = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                recyclerView.layoutManager = layoutManagerTwo
                val adapter = MainAdapter(this@MainActivity, arrayList)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }
}