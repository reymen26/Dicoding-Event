package com.dicoding.dicodingevent.ui.favourite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dicodingevent.adapter.EventAdapter
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.FragmentFavouriteBinding
import com.dicoding.dicodingevent.di.Injection
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity

class FavouriteFragment : Fragment() {

    private lateinit var rvFavorite: RecyclerView
    private lateinit var tvEmptyMessage: TextView
    private lateinit var viewModel: FavouriteViewModel
    private lateinit var adapter: EventAdapter
    private lateinit var binding: FragmentFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        viewModel = Injection.provideFavoriteViewModel(this, requireContext())

        binding.rvFavorite.layoutManager = LinearLayoutManager(context)
        adapter = EventAdapter()
        binding.rvFavorite.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Observe daftar event favorit dari ViewModel
        viewModel.events.observe(viewLifecycleOwner) { favoriteEvents ->
            Log.d("FavoriteFragment", "Number of favorite events: ${favoriteEvents?.size ?: 0}")

            if (favoriteEvents.isNullOrEmpty()) {
                showEmptyMessage(true)
            } else {
                showEmptyMessage(false)
                adapter.submitFavoriteList(favoriteEvents)
            }
        }


        adapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEvent(data)
            }
        })

        viewModel.getAllFavorites()

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFavorite.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFavorite.visibility = View.VISIBLE
        }
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvFavorite.visibility = View.GONE
            binding.tvEmptyMessage.visibility = View.VISIBLE
        } else {
            binding.rvFavorite.visibility = View.VISIBLE
            binding.tvEmptyMessage.visibility = View.GONE
        }
    }

    private fun showSelectedEvent(data: ListEventsItem) {
        val intent = Intent(context, DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT, data)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavorites()
    }
}