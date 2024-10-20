package com.dicoding.dicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.adapter.FavoriteAdapter
import com.dicoding.dicodingevent.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingevent.ui.detail.DetailActivity
import com.dicoding.dicodingevent.util.Injection

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        Injection.provideViewModelFactory(requireActivity())
    }
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()

        favoriteViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter(
            { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EVENT_ID", event.id.toInt())
                intent.putExtra("IS_FINISHED_EVENT", false)
                startActivity(intent)
            },
            { event ->
                favoriteViewModel.deleteFavoriteById(event.id)
            }
        )
        binding.rvFavoriteEvents.layoutManager = LinearLayoutManager(context)
        binding.rvFavoriteEvents.adapter = adapter
    }

    private fun observeViewModel() {
        favoriteViewModel.allFavorites.observe(viewLifecycleOwner) { favorites ->
            adapter.submitList(favorites)
            binding.progressBar.visibility = View.GONE
            if (favorites.isEmpty()) {
                binding.tvNoFavorites.visibility = View.VISIBLE
                binding.rvFavoriteEvents.visibility = View.GONE
            } else {
                binding.tvNoFavorites.visibility = View.GONE
                binding.rvFavoriteEvents.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        Log.d("FavoriteFragment", "onDestroyView called")
        super.onDestroyView()
        _binding = null
    }
}
