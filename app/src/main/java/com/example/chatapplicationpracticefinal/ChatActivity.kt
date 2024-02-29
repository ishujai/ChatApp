package com.example.chatapplicationpracticefinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplicationpracticefinal.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase

    private lateinit var senderUid:String
    private lateinit var receiverUid:String

    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    private lateinit var list:ArrayList<MessageModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database=FirebaseDatabase.getInstance()

        val mobno= intent.getStringExtra("contactno")

        senderUid= mobno.toString()
        receiverUid= intent.getStringExtra("uniqueid")!!

        Log.e("senderUid","$senderUid")
        Log.e("receiverUid","$receiverUid")

        list =ArrayList()

        senderRoom= senderUid+receiverUid
        receiverRoom= receiverUid+senderUid

        Log.e("senderRoom","$senderRoom")
        Log.e("receiverRoom","$receiverRoom")

        binding.imageView2.setOnClickListener {
            if (binding.messageBox.text.isEmpty()){
                Toast.makeText(this, "Please enter your message", Toast.LENGTH_SHORT).show()
            }
            else{

                val message =MessageModel(binding.messageBox.text.toString(),senderUid, Date().time)

                val randomKey =database.reference.push().key
                randomKey?.let { it1 ->
                    database.reference.child("chats")
                        .child(senderRoom).child("message").child(it1).setValue(message).addOnSuccessListener {

                            database.reference.child("chats").child(receiverRoom).child(randomKey!!).setValue(message).addOnSuccessListener {

                                binding.messageBox.text=null
                                Toast.makeText(this, "message sent!!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }


        database.reference.child("chats").child(senderRoom).child("message")

            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = ArrayList<MessageModel>()

                    for(snapshot1 in snapshot.children){
                        val data =snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }

                    Log.e("list", "chatlist$list")

                    val layoutManagerTwo = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
                    binding.chatrecyclerView.layoutManager = layoutManagerTwo
                    binding.chatrecyclerView.adapter= MessageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}