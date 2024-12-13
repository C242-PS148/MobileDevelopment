package com.checkmate.ui.signin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.checkmate.R
import com.checkmate.databinding.ActivitySignInBinding
import com.checkmate.ui.signup.SignUpActivity
import com.checkmate.ui.student.MainStudentActivity
import com.checkmate.ui.teacher.MainTeacherActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel by viewModels<SignInViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setViewModel()
        setListener()
    }

    private fun setViewModel() {
        viewModel.isLoading.observe(this){ isLoading ->
            if(isLoading){
                binding.cpLoading.isVisible = true
                binding.btnSignIn.isVisible = false
            }else{
                binding.cpLoading.isVisible = false
                binding.btnSignIn.isVisible = true
            }
        }
        viewModel.role.observe(this){ role->
            startActivity(Intent(this,
                if(role == "siswa")
                    MainStudentActivity::class.java
                else
                    MainTeacherActivity::class.java
            ))
            finish()
        }
        viewModel.errorMessage.observe(this){ errorMessage ->
            val dialog = AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()
        }
    }

    private fun setListener() {
        with(binding){
            tvSignUp.setOnClickListener{
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
                finish()
            }
            btnSignIn.setOnClickListener{
                if(isFormValid()){
                    viewModel.signIn(
                        this@SignInActivity,
                        etUsernameEmail.text.toString(),
                        etPassword.text.toString()
                    )
                }
            }
        }
    }

    private fun isFormValid():Boolean{
        var isValid = true
        with(binding){
            if(etUsernameEmail.text.toString().isEmpty()){
                tilUsernameEmail.error = "Please input this field"
                isValid = false
            }else{
                tilUsernameEmail.isErrorEnabled = false
            }
            if(etPassword.text.toString().isEmpty()){
                tilPassword.error = "Please input this field"
                isValid = false
            }else{
                tilPassword.isErrorEnabled = false
            }
        }
        return isValid
    }
}