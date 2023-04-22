package com.anubhav.checkboxplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.anubhav.checkboxplayground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.astroCheckBox.onAstroCheckBoxSelected = { _, selected ->
            if(selected){
                Toast.makeText(this, "Selected!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}