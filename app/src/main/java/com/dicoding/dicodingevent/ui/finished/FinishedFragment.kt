package com.dicoding.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.adapter.EventAdapter
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity

class FinishedFragment : Fragment() {

    private lateinit var rvFinished: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyMessage: TextView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private val viewModel: FinishedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_finished, container, false)

        // Inisialisasi View
        rvFinished = view.findViewById(R.id.rvFinished)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage)
        searchView = view.findViewById(R.id.searchView)

        // Konfigurasi RecyclerView
        rvFinished.layoutManager = LinearLayoutManager(context)
        adapter = EventAdapter()
        rvFinished.adapter = adapter

        // Observasi Data dari ViewModel
        viewModel.filteredEvents.observe(viewLifecycleOwner, Observer { eventList ->
            adapter.submitList(eventList)
            showEmptyMessage(eventList.isEmpty())
        })

        // Observasi Status Loading
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Fetch Data
        viewModel.fetchFinishedEvents()

        // Klik Item pada RecyclerView
        adapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEvent(data)
            }
        })

        // Tambahkan Listener untuk SearchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchEvents(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchEvents(it)
                }
                return true
            }
        })

        return view
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            rvFinished.visibility = View.GONE
            tvEmptyMessage.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            rvFinished.visibility = View.VISIBLE
        }
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        if (isEmpty) {
            rvFinished.visibility = View.GONE
            tvEmptyMessage.visibility = View.VISIBLE
        } else {
            rvFinished.visibility = View.VISIBLE
            tvEmptyMessage.visibility = View.GONE
        }
    }

    private fun showSelectedEvent(data: ListEventsItem) {
        val intent = Intent(context, DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT, data)
        startActivity(intent)
    }
}
