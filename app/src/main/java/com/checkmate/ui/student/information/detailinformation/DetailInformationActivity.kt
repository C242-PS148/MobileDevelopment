package com.checkmate.ui.student.information.detailinformation

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.checkmate.R
import com.checkmate.databinding.ActivityDetailInformationBinding
import com.checkmate.databinding.ActivityForgetPasswordBinding
import com.checkmate.ui.student.home.HomeStudentViewModel
import com.checkmate.utils.SharedPrefUtils

class DetailInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailInformationBinding
    private val viewModel : DetailInformationViewModel by viewModels()

    private var moodPoint = 0
    private var moodMaxPoint = 0
    private var atributePoint = 0
    private var atributeMaxPoint = 0
    private var absentPoint = 0
    private var absentMaxPoint = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val selectedDate = intent.getStringExtra("date")
        viewModel.loadAll(SharedPrefUtils.getAuthToken(this), selectedDate!!)

        setViewModel()
        setListener()
    }

    private fun setListener() {
        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    private fun setViewModel() {
        //mood
        viewModel.isLoadingMood.observe(this){ isLoading->
            if(isLoading){
                binding.cpLoadingMood.visibility = View.VISIBLE
            }else{
                binding.cpLoadingMood.visibility = View.GONE
            }
        }

        viewModel.moodPoint.observe(this){ moodPoint->
            this.moodPoint = moodPoint
            with(binding){
                tvPercentageMood.text = "Completed ${this@DetailInformationActivity.moodPoint/this@DetailInformationActivity.moodMaxPoint}%"
                pbMood.progress = moodPoint
            }
        }

        viewModel.moodMaxPoint.observe(this){ moodMaxPoint->
            this.moodMaxPoint = moodMaxPoint
            with(binding){
                tvPercentageMood.text = "Completed ${this@DetailInformationActivity.moodPoint/this@DetailInformationActivity.moodMaxPoint}%"
                pbMood.max = moodMaxPoint
            }
        }

        viewModel.errorMessageMood.observe(this){ errorMessage->
            val dialog = AlertDialog.Builder(this)
                .setTitle("Pesan Mood")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }

        //atribute
        viewModel.isLoadingTie.observe(this){ isLoading->
            if(isLoading){
                binding.cpLoadingAtribute.visibility = View.VISIBLE
            }else{
                binding.cpLoadingAtribute.visibility = View.GONE
            }
        }

        viewModel.tiePoint.observe(this){ atributePoint->
            this.atributePoint = atributePoint
            with(binding){
                tvPercentageAtribute.text = "Completed ${this@DetailInformationActivity.atributePoint/this@DetailInformationActivity.atributeMaxPoint}%"
                pbAtribute.progress = atributePoint
            }
        }

        viewModel.tieMaxPoint.observe(this){ atributeMaxPoint->
            this.atributeMaxPoint = atributeMaxPoint
            with(binding){
                tvPercentageAtribute.text = "Completed ${this@DetailInformationActivity.atributePoint/this@DetailInformationActivity.atributeMaxPoint}%"
                pbAtribute.max = atributeMaxPoint
            }
        }

        viewModel.errorMessageTie.observe(this){ errorMessage->
            val dialog = AlertDialog.Builder(this)
                .setTitle("Pesan Atribute")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }

        //absent
        viewModel.isLoadingAttendance.observe(this){ isLoading->
            if(isLoading){
                binding.cpLoadingAbsent.visibility = View.VISIBLE
            }else{
                binding.cpLoadingAbsent.visibility = View.GONE
            }
        }

        viewModel.attendancePoint.observe(this){ absentPoint->
            this.absentPoint = absentPoint
            with(binding){
                tvPercentageAbsent.text = "Completed ${this@DetailInformationActivity.absentPoint/this@DetailInformationActivity.absentMaxPoint}%"
                pbAbsent.progress = absentPoint
            }
        }

        viewModel.attendanceMaxPoint.observe(this){ absentMaxPoint->
            this.absentMaxPoint = absentMaxPoint
            with(binding){
                tvPercentageAbsent.text = "Completed ${this@DetailInformationActivity.absentPoint/this@DetailInformationActivity.absentMaxPoint}%"
                pbAbsent.max = absentMaxPoint
            }
        }

        viewModel.errorMessageAttendance.observe(this){ errorMessage->
            val dialog = AlertDialog.Builder(this)
                .setTitle("Pesan Absent")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }
    }
}