package com.checkmate.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.checkmate.R
import com.checkmate.databinding.ActivityMainBinding
import com.checkmate.ui.profile.ProfileViewModel
import com.checkmate.ui.signin.SignInActivity
import com.checkmate.ui.signup.SignUpActivity
import com.checkmate.ui.student.MainStudentActivity
import com.checkmate.ui.teacher.MainTeacherActivity
import com.checkmate.utils.SharedPrefUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setViewModel()
        setListener()

        val token = SharedPrefUtils.getAuthToken(this)
        if(token!=""){
            viewModel.loadProfile(token, true)
        }
    }

    private fun setViewModel() {
        viewModel.isLoading.observe(this){ isLoading ->
            if(isLoading){
                binding.cpLoading.isVisible = true
                binding.btnSignIn.isVisible = false
                binding.btnSignUp.isVisible = false
            }else{
                binding.cpLoading.isVisible = false
                binding.btnSignIn.isVisible = true
                binding.btnSignUp.isVisible = true
            }
        }
        viewModel.profile.observe(this){ profile ->
            startActivity(Intent(this,
                if(profile.role == "siswa")
                    MainStudentActivity::class.java
                else
                    MainTeacherActivity::class.java
            ))
            finish()
        }
    }

    private fun setListener() {
        with(binding){
            btnSignIn.setOnClickListener{
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
            }
            btnSignUp.setOnClickListener{
                startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
                finish()
            }
        }
    }
}