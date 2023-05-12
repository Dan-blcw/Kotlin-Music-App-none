package com.dan.musicapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SlashActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this@SlashActivity,HomeActivity::class.java)
        val btn = findViewById<Button>(R.id.btn_Slash)
        btn.setOnClickListener {
            startActivity(intent)
            finish()
        }
    }
}