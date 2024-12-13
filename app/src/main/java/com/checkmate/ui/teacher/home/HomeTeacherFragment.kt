package com.checkmate.ui.teacher.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.checkmate.R
import com.checkmate.databinding.FragmentHomeTeacherBinding
import com.checkmate.ui.profile.ProfileViewModel
import com.checkmate.utils.SharedPrefUtils

class HomeTeacherFragment : Fragment() {
    private lateinit var binding: FragmentHomeTeacherBinding
    private val homeViewModel : HomeTeahcerViewModel by viewModels()
    private val profileViewModel : ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setViewModel()
        homeViewModel.loadAll(SharedPrefUtils.getAuthToken(requireContext()))
        profileViewModel.loadProfile(SharedPrefUtils.getAuthToken(requireContext()), true)
    }

    private fun setViewModel() {
        profileViewModel.profile.observe(viewLifecycleOwner){profile->
            Glide.with(requireContext()).load(profile.profileImageUrl).into(binding.btnProfile)
            binding.tvUsername.text = profile.username
        }

        //mood
        homeViewModel.isLoadingMood.observe(viewLifecycleOwner){ isLoading->
            if(isLoading){
                binding.cpLoadingMood.visibility = View.VISIBLE
            }else{
                binding.cpLoadingMood.visibility = View.GONE
            }
        }

        homeViewModel.moodPoint.observe(viewLifecycleOwner){ moodPoint->
            with(binding){
                tvPercentageMood.text = "Completed ${moodPoint}%"
                pbMood.progress = moodPoint
            }
        }

        homeViewModel.errorMessageMood.observe(viewLifecycleOwner){ errorMessage->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Pesan Mood")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }

        //atribute
        homeViewModel.isLoadingTie.observe(viewLifecycleOwner){ isLoading->
            if(isLoading){
                binding.cpLoadingAtribute.visibility = View.VISIBLE
            }else{
                binding.cpLoadingAtribute.visibility = View.GONE
            }
        }

        homeViewModel.tiePoint.observe(viewLifecycleOwner){ atributePoint->
            with(binding){
                tvPercentageAtribute.text = "Completed ${atributePoint}%"
                pbAtribute.progress = atributePoint
            }
        }

        homeViewModel.errorMessageTie.observe(viewLifecycleOwner){ errorMessage->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Pesan Atribute")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }

        //absent
        homeViewModel.isLoadingAttendance.observe(viewLifecycleOwner){ isLoading->
            if(isLoading){
                binding.cpLoadingAbsent.visibility = View.VISIBLE
            }else{
                binding.cpLoadingAbsent.visibility = View.GONE
            }
        }

        homeViewModel.attendancePoint.observe(viewLifecycleOwner){ absentPoint->
            with(binding){
                tvPercentageAbsent.text = "Completed ${absentPoint}%"
                pbAbsent.progress = absentPoint
            }
        }

        homeViewModel.errorMessageAttendance.observe(viewLifecycleOwner){ errorMessage->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Pesan Absent")
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
            btnProfile.setOnClickListener{
                findNavController().navigate(R.id.navigation_profile)
            }
        }
    }
}