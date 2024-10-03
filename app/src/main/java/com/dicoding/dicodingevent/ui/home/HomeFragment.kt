package com.dicoding.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.databinding.FragmentHomeBinding
import com.dicoding.dicodingevent.adapter.FinishedEventAdapter
import com.dicoding.dicodingevent.adapter.UpcomingEventAdapter
import com.dicoding.dicodingevent.ui.DetailActivity


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var finishedAdapter: FinishedEventAdapter
    private lateinit var upcomingAdapter: UpcomingEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setupUpcomingEventsRecyclerView()
        setupFinishedEventsRecyclerView()
        observeEvents()

        homeViewModel.getUpcomingEvents()
        homeViewModel.getFinishedEvents()
    }

    private fun setupUpcomingEventsRecyclerView() {
        upcomingAdapter = UpcomingEventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            intent.putExtra("IS_FINISHED_EVENT", false) // Menandakan bahwa ini adalah event yang akan datang
            startActivity(intent)
        }
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }
    }

    private fun setupFinishedEventsRecyclerView() {
        finishedAdapter = FinishedEventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            intent.putExtra("IS_FINISHED_EVENT", true) // Menandakan bahwa ini adalah event yang telah selesai
            startActivity(intent)
        }
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }
    }

    private fun observeEvents() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        homeViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events.take(5))
        }

        homeViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events.take(5))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}