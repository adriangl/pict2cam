package com.adriangl.image2camera.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.adriangl.image2camera.R
import com.adriangl.image2camera.databinding.MainActivityBinding

/**
 * Main activity class that the user will see when opening the launcher icon.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        setSupportActionBar(binding.toolbar)
    }
}
