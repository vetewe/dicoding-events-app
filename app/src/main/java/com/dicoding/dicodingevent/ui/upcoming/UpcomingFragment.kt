package com.dicoding.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.adapter.UpcomingEventAdapter

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var upcomingAdapter: UpcomingEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingViewModel = ViewModelProvider(this).get(UpcomingViewModel::class.java)

        setupRecyclerView()
        observeEvents()

        upcomingViewModel.getUpcomingEvents()
    }

    private fun setupRecyclerView() {
        upcomingAdapter = UpcomingEventAdapter()
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingAdapter
        }
    }

    private fun observeEvents() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}