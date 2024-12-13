package com.checkmate.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.checkmate.R
import com.checkmate.databinding.ActivityMainStudentBinding
import com.checkmate.databinding.FragmentProfileBinding
import com.checkmate.ui.MainActivity
import com.checkmate.ui.student.MainStudentActivity
import com.checkmate.ui.teacher.MainTeacherActivity
import com.checkmate.utils.SharedPrefUtils

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel : ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setViewModel()

        viewModel.loadProfile(SharedPrefUtils.getAuthToken(requireContext()), false)
    }

    private fun setViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            if(isLoading){
                binding.cpLoading.isVisible = true
                binding.llForm.isVisible = false
            }else{
                binding.cpLoading.isVisible = false
                binding.llForm.isVisible = true
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){ errorMessage->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Pesan")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }
        viewModel.profile.observe(viewLifecycleOwner){ profile ->
            with(binding){
                etEmail.setText(profile.email)
                etUsername.setText(profile.username)
                etStudentNumber.setText(profile.studentNumber)
                Glide.with(requireContext()).load(profile.profileImageUrl).into(ivImageProfile)
            }
        }
    }

    private fun setListener() {
        with(binding){
            btnBack.setOnClickListener{
                findNavController().navigate(R.id.navigation_home)
            }
            btnLogout.setOnClickListener{
                requireActivity().finish()
                SharedPrefUtils.removeAuthToken(requireContext())
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }
}