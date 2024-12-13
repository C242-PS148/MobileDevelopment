package com.checkmate.ui.student.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.checkmate.R
import com.checkmate.adapter.LeaderboardAdapter
import com.checkmate.databinding.FragmentHomeStudentBinding
import com.checkmate.ui.profile.ProfileViewModel
import com.checkmate.utils.SharedPrefUtils

class HomeStudentFragment : Fragment() {
    private lateinit var binding: FragmentHomeStudentBinding
    private val homeViewModel : HomeStudentViewModel by viewModels()
    private val profileViewModel : ProfileViewModel by viewModels()

    private lateinit var token: String
    private lateinit var adapter: LeaderboardAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        token = SharedPrefUtils.getAuthToken(requireContext())
        adapter = LeaderboardAdapter()

        with(binding){
            rvLeaderboard.layoutManager = LinearLayoutManager(requireContext())
            rvLeaderboard.adapter = adapter
        }

        setViewModel()
        setListener()

        homeViewModel.loadLeaderboard(token, true)
        profileViewModel.loadProfile(token, true)
    }

    private fun setListener() {
        with(binding){
            btnTop.setOnClickListener{
                btnTop.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue, requireContext().theme))
                btnTop.setTextColor(resources.getColor(R.color.white, requireContext().theme))
                btnUnder.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white, requireContext().theme))
                btnUnder.setTextColor(resources.getColor(R.color.black, requireContext().theme))

                homeViewModel.loadLeaderboard(token, true)
            }
            btnUnder.setOnClickListener{
                btnTop.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.white, requireContext().theme))
                btnTop.setTextColor(resources.getColor(R.color.black, requireContext().theme))
                btnUnder.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.blue, requireContext().theme))
                btnUnder.setTextColor(resources.getColor(R.color.white, requireContext().theme))
                homeViewModel.loadLeaderboard(token, false)
            }
            btnProfile.setOnClickListener{
                findNavController().navigate(R.id.navigation_profile)
            }
        }
    }

    private fun setViewModel() {
        profileViewModel.profile.observe(viewLifecycleOwner){profile->
            Glide.with(requireContext()).load(profile.profileImageUrl).into(binding.btnProfile)
            binding.tvUsername.text = profile.username
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner){ isLoading->
            if(isLoading){
                binding.rvLeaderboard.visibility = View.GONE
                binding.cpLoading.visibility = View.VISIBLE
            }else{
                binding.rvLeaderboard.visibility = View.VISIBLE
                binding.cpLoading.visibility = View.GONE
            }
        }

        homeViewModel.listLeaderboard.observe(viewLifecycleOwner){ listLeaderboard->
            adapter.setList(listLeaderboard)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner){ errorMessage->
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Pesan")
                .setMessage(errorMessage)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }
    }
}