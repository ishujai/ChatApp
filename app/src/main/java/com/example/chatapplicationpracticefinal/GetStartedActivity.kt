package com.example.chatapplicationpracticefinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.chatapplicationpracticefinal.databinding.ActivityChatBinding
import com.example.chatapplicationpracticefinal.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetStartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val mobno: String= binding.contactnoET.text.toString()
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("contactno", mobno)
            startActivity(intent)
        }

    }
}