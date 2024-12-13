package com.checkmate.ui.student

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.checkmate.R
import com.checkmate.databinding.ActivityMainStudentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainStudentBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }
}