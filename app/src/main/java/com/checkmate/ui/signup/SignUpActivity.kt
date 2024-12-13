package com.checkmate.ui.signup

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.checkmate.R
import com.checkmate.databinding.ActivitySignUpBinding
import com.checkmate.ui.signin.SignInActivity
import com.checkmate.utils.ImageUtils
import java.io.File
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel>()

    private var imageProfile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
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
        viewModel.isLoading.observe(this@SignUpActivity){ isLoading ->
            if(isLoading){
                binding.cpLoading.isVisible = true
                binding.btnSignUp.isVisible = false
            }else{
                binding.cpLoading.isVisible = false
                binding.btnSignUp.isVisible = true
            }
        }
        viewModel.isSuccess.observe(this@SignUpActivity){isSuccess->
            with(binding){
                etUsername.setText(null)
                etPassword.setText(null)
                etEmail.setText(null)
                etStudentNumber.setText(null)
                Glide.with(this@SignUpActivity).load(R.drawable.profile_image).into(ivImageProfile)
            }

        }
        viewModel.message.observe(this@SignUpActivity){ errorMessage ->
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
            tvSignIn.setOnClickListener{
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                finish()
            }
            btnSignUp.setOnClickListener{
                if(isFormValid()){
                    viewModel.signUp(
                        etUsername.text.toString(),
                        etEmail.text.toString(),
                        etStudentNumber.text.toString(),
                        etPassword.text.toString(),
                        imageProfile!!
                    )
                }
            }
            cvImageProfile.setOnClickListener{
                if (allPermissionsGranted()) {
                    openGallery()
                } else {
                    requestPermissions()
                }
            }
        }
    }

    private fun isFormValid():Boolean{
        var isValid = true
        with(binding){
            if(etUsername.text.toString().isEmpty()){
                tilUsername.error = "Please input this field"
                isValid = false
            }else{
                tilUsername.isErrorEnabled = false
            }

            if(etEmail.text.toString().isEmpty()){
                tilEmail.error = "Please input this field"
                isValid = false
            } else if(!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
                tilEmail.error = "Email is not valid"
                isValid = false
            } else {
                tilEmail.isErrorEnabled = false
            }

            if(etStudentNumber.text.toString().isEmpty()){
                tilStudentNumber.error = "Please input this field"
                isValid = false
            }else{
                tilStudentNumber.isErrorEnabled = false
            }

            if(etPassword.text.toString().isEmpty()){
                tilPassword.error = "Please input this field"
                isValid = false
            }else{
                tilPassword.isErrorEnabled = false
            }

            if(imageProfile==null){
                isValid = false
                val dialog = AlertDialog.Builder(this@SignUpActivity)
                    .setTitle("Alert")
                    .setMessage("Please upload profile image first")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()

                dialog.show()
            }
        }
        return isValid
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            if (selectedImageUri != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver, selectedImageUri
                    )

                    binding.ivImageProfile.setImageBitmap(bitmap)

                    imageProfile = ImageUtils.getFileFromUri(this, selectedImageUri)
                } catch (e: IOException) {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    this,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                openGallery()
            }
        }

    companion object {
        private val REQUIRED_PERMISSIONS = mutableListOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        ).toTypedArray()

    }
}