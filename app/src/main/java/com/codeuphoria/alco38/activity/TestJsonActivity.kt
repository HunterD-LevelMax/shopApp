package com.codeuphoria.alco38.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codeuphoria.alco38.databinding.ActivityTestJsonBinding
import com.codeuphoria.alco38.jsonString

class TestJsonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestJsonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestJsonBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.jsonStringTextView.text = jsonString

    }
}