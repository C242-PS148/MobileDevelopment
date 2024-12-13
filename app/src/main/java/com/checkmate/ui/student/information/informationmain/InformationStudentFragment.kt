package com.checkmate.ui.student.information.informationmain

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.checkmate.R
import com.checkmate.databinding.FragmentInformationStudentBinding
import com.checkmate.ui.signup.SignUpActivity
import com.checkmate.ui.student.information.detailinformation.DetailInformationActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class InformationStudentFragment : Fragment() {
    private lateinit var binding: FragmentInformationStudentBinding
    var selectedDate:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        selectedDate = SimpleDateFormat("yyyy-MM-dd").format(Date(binding.cvCalendar.date))
    }

    private fun setListener() {
        with(binding){
            cvCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            }
            btnCheck.setOnClickListener{
                if(selectedDate==null){
                    val dialog = AlertDialog.Builder(requireContext())
                        .setTitle("Alert")
                        .setMessage("Please select the date first")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()

                    dialog.show()
                }else{
                    startActivity(
                        Intent(requireContext(), DetailInformationActivity::class.java).putExtra("date",selectedDate)
                    )
                }
            }
            btnBack.setOnClickListener{
                findNavController().navigate(R.id.navigation_home)
            }
        }
    }
}