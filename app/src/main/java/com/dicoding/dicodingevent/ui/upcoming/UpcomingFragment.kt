package com.dicoding.dicodingevent.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.SearchView
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.adapter.UpcomingEventAdapter
import com.dicoding.dicodingevent.ui.DetailActivity

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

        upcomingViewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        setupRecyclerView()
        observeEvents()

        binding.searchViewUpcoming.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                upcomingViewModel.getUpcomingEvents(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        upcomingViewModel.getUpcomingEvents()
    }

    private fun setupRecyclerView() {
        upcomingAdapter = UpcomingEventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            intent.putExtra("IS_FINISHED_EVENT", false) // Menandakan bahwa ini adalah event yang akan datang
            startActivity(intent)
        }
        binding.rvUpcomingEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = upcomingAdapter
        }
    }

    private fun observeEvents() {
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)
            binding.progressBar.visibility = View.GONE
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}