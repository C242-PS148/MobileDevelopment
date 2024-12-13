package com.checkmate.ui.teacher.camera.cameraresult

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.checkmate.R
import com.checkmate.databinding.ActivityCameraResultBinding
import com.checkmate.databinding.ActivityDetailInformationBinding
import com.checkmate.databinding.ActivityForgetPasswordBinding
import com.checkmate.ui.signin.SignInViewModel
import com.checkmate.ui.student.MainStudentActivity
import com.checkmate.ui.teacher.MainTeacherActivity
import com.checkmate.utils.ImageUtils
import com.checkmate.utils.SharedPrefUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraResultBinding
    private val viewModel by viewModels<CameraViewModel>()
    private var imageFile:File?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageUriString = intent.getStringExtra("imageUri")
        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            imageFile = ImageUtils.getFileFromUri(this, imageUri)
            if(imageFile==null){
                finish()
                Toast.makeText(this,"Something went wrong, please try again", Toast.LENGTH_SHORT).show()
            }
        }else{
            finish()
            Toast.makeText(this,"Something went wrong, please try again", Toast.LENGTH_SHORT).show()
        }

        Glide.with(this)
            .load(imageFile)
            .into(binding.ivCamera)

        setViewModel()

        binding.btnBack.setOnClickListener{
            finish()
        }

        viewModel.predict(SharedPrefUtils.getAuthToken(this), imageFile!!)
    }

    private fun setViewModel() {
        viewModel.isLoading.observe(this){ isLoading ->
            if(isLoading){
                binding.cpLoading.isVisible = true
                binding.llForm.isVisible = false
            }else{
                binding.cpLoading.isVisible = false
                binding.llForm.isVisible = true
            }
        }
        viewModel.predictResults.observe(this){ predictResults->
            with(binding){
                if(predictResults.predictAbsen!=null){
                    etName.setText(predictResults.predictAbsen.predictedName)
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss ", Locale.getDefault())
                    etDate.setText(inputFormat.parse(predictResults.predictAbsen.datetime)
                        ?.let { outputFormat.format(it) })
                }else{
                    tilName.error = "Absent is not detected"
                    tilDate.error = "Absent is not detected"
                }

                if(predictResults.predictDasi!=null){
                    etAtribute.setText(predictResults.predictDasi.predictedClass)
                }else{
                    tilAtribute.error = "Atribute is not detected"
                }

                if(predictResults.predictMood!=null){
                    etMood.setText(predictResults.predictMood.predictedClassName)
                }else{
                    tilMood.error = "Mood is not detected"
                }
            }
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
}