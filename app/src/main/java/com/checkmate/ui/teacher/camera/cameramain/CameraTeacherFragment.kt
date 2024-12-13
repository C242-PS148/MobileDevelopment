package com.checkmate.ui.teacher.camera.cameramain

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.checkmate.databinding.FragmentCameraTeacherBinding
import com.checkmate.ui.teacher.camera.cameraresult.CameraResultActivity
import com.checkmate.utils.ImageUtils
import java.io.IOException
import java.util.concurrent.ExecutorService

class CameraTeacherFragment : Fragment() {
    private lateinit var binding: FragmentCameraTeacherBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var imageUri:Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        with(binding){
            btnCapture.setOnClickListener { takePhoto() }
            btnUpload.setOnClickListener { openGallery() }
            btnSubmit.setOnClickListener{
                if(imageUri==null){
                    val dialog = AlertDialog.Builder(requireContext())
                        .setTitle("Alert")
                        .setMessage("Image not found, please try again")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    dialog.show()
                    return@setOnClickListener
                }
                startActivity(Intent(requireContext(), CameraResultActivity::class.java).putExtra("imageUri", imageUri.toString()))
            }
            btnCancel.setOnClickListener { cancelPreview() }
        }

    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = ImageUtils.imageProxyToBitmap(image)
                    image.close()

                    // Show the preview
                    showPhotoPreview(bitmap)
                    val file = ImageUtils.saveBitmapToFile(requireContext(), bitmap)
                    imageUri = Uri.fromFile(file)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Capture failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun showPhotoPreview(bitmap: Bitmap) {
        binding.ivPhotoPreview.visibility = View.VISIBLE
        binding.pvCamera.visibility = View.GONE
        binding.btnCapture.visibility = View.GONE
        binding.btnCancel.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.VISIBLE
        binding.btnUpload.visibility = View.GONE
        binding.ivPhotoPreview.setImageBitmap(bitmap)
    }

    private fun cancelPreview() {
        binding.ivPhotoPreview.visibility = View.GONE
        binding.pvCamera.visibility = View.VISIBLE
        binding.btnCapture.visibility = View.VISIBLE
        binding.btnCancel.visibility = View.GONE
        binding.btnSubmit.visibility = View.GONE
        binding.btnUpload.visibility = View.VISIBLE

        imageUri = null
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            if (selectedImageUri != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, selectedImageUri
                    )
                    binding.ivPhotoPreview.visibility = View.VISIBLE
                    binding.pvCamera.visibility = View.GONE
                    binding.btnCapture.visibility = View.GONE
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.btnSubmit.visibility = View.VISIBLE
                    binding.btnUpload.visibility = View.GONE
                    binding.ivPhotoPreview.setImageBitmap(bitmap)

                    imageUri = selectedImageUri
                } catch (e: IOException) {
                    Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Initialize the Preview use case
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.pvCamera.surfaceProvider
                }

            // Initialize the ImageCapture use case
            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind all use cases before rebinding
                cameraProvider.unbindAll()

                // Bind the Preview and ImageCapture use cases
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(requireContext(),"Use case binding failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
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
                    requireContext(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startCamera()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        if(::cameraExecutor.isInitialized){
            cameraExecutor.shutdown()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        ).toTypedArray()

    }
}
