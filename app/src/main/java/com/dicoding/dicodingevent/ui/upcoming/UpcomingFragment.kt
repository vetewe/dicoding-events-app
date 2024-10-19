package com.dicoding.dicodingevent.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.adapter.UpcomingEventAdapter
import com.dicoding.dicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity

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
        upcomingViewModel = ViewModelProvider(requireActivity())[UpcomingViewModel::class.java]

        setupUpcomingEventsRecyclerView()
        observeEvents()

        upcomingViewModel.getUpcomingEvents()

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.searchViewUpcoming.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                upcomingViewModel.getUpcomingEvents(query)
                binding.progressBar.visibility = View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    upcomingViewModel.getUpcomingEvents()
                    binding.progressBar.visibility = View.VISIBLE
                }
                return false
            }
        })
    }

    private fun setupUpcomingEventsRecyclerView() {
        upcomingAdapter = UpcomingEventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            intent.putExtra("IS_FINISHED_EVENT", false)
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

            if (events.isEmpty()) {
                binding.tvEventNotFound.visibility = View.VISIBLE
            } else {
                binding.tvEventNotFound.visibility = View.GONE
            }
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            binding.progressBar.visibility = View.GONE
            message?.let {
                Toast.makeText(context, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
