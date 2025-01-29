package com.dicoding.dicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevent.adapter.EventAdapter
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFavoriteBinding
import com.dicoding.dicodingevent.di.Injection
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity

class FavoriteFragment : Fragment() {
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var eventAdapter: EventAdapter
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        viewModel = Injection.provideFavoriteViewModel(this, requireContext())

        eventAdapter = EventAdapter()
        binding.rvFavorit.adapter = eventAdapter
        binding.rvFavorit.layoutManager = LinearLayoutManager(context)

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Observe daftar event favorit dari ViewModel
        viewModel.events.observe(viewLifecycleOwner) { favoriteEvents ->
            Log.d("FavoriteFragment", "Number of favorite events: ${favoriteEvents.size}")
            favoriteEvents?.let {
                eventAdapter.submitFavoriteList(it)  // Mengirim data favorit ke adapter
                binding.rvFavorit.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            } ?: run {
                Log.d("FavoriteFragment", "favoriteEvents is null")
            }
        }

        eventAdapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
            override fun onItemClicked(event: ListEventsItem) {
                Log.d("FavoriteFragment", "Item clicked: $event")
                showSelectedEvent(event)
            }
        })

        viewModel.getAllFavorites()

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFavorit.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFavorit.visibility = View.VISIBLE
        }
    }

    private fun showSelectedEvent(data: ListEventsItem) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT, data)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavorites()
    }
}