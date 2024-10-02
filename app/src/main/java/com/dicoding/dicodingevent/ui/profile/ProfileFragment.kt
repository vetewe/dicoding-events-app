package com.dicoding.dicodingevent.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.dicodingevent.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load("https://media.licdn.com/dms/image/v2/D4E03AQFgYC7hCq-aTA/profile-displayphoto-shrink_800_800/profile-displayphoto-shrink_800_800/0/1725525872621?e=1733356800&v=beta&t=eG7Fa6gm_SBnfL8WKBMq3iYpKgGxcO9mQNKPSOd5hd4")
            .into(binding.profileImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


