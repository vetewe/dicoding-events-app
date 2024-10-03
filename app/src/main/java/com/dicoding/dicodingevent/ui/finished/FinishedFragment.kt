package com.dicoding.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.adapter.FinishedEventAdapter
import com.dicoding.dicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.dicodingevent.ui.DetailActivity

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var finishedViewModel: FinishedViewModel
    private lateinit var finishedAdapter: FinishedEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishedViewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)

        setupFinishedEventsRecyclerView()
        observeEvents()

        binding.searchViewFinished.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                finishedViewModel.getFinishedEvents(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        finishedViewModel.getFinishedEvents()
    }

    private fun setupFinishedEventsRecyclerView() {
        finishedAdapter = FinishedEventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id) // Mengirim ID event
            intent.putExtra("IS_FINISHED_EVENT", true) // Menandakan bahwa ini adalah event selesai
            startActivity(intent)
        }
        binding.rvFinishedEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = finishedAdapter
        }
    }

    private fun observeEvents() {
        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
            binding.progressBar.visibility = View.GONE // Sembunyikan loading
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}