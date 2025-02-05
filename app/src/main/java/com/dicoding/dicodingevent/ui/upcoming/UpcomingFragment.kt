package com.dicoding.dicodingevent.ui.upcoming

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

class UpcomingFragment : Fragment() {

    private lateinit var rvUpcoming: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyMessage: TextView
    private val viewModel: UpcomingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming, container, false)

        // Inisialisasi View
        rvUpcoming = view.findViewById(R.id.rvUpcoming)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage)

        // Konfigurasi RecyclerView
        rvUpcoming.layoutManager = LinearLayoutManager(context)
        adapter = EventAdapter()
        rvUpcoming.adapter = adapter

        // Observasi Data dari ViewModel
        viewModel.events.observe(viewLifecycleOwner, Observer { eventList ->
            adapter.submitList(eventList)
            showEmptyMessage(eventList.isEmpty())
        })

        // Observasi Status Loading
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Fetch Data
        viewModel.fetchUpcomingEvents()

        // Klik Item pada RecyclerView
        adapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEvent(data)
            }
        })

        return view
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            rvUpcoming.visibility = View.GONE
            tvEmptyMessage.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            rvUpcoming.visibility = View.VISIBLE
        }
    }

    private fun showEmptyMessage(isEmpty: Boolean) {
        if (isEmpty) {
            rvUpcoming.visibility = View.GONE
            tvEmptyMessage.visibility = View.VISIBLE
        } else {
            rvUpcoming.visibility = View.VISIBLE
            tvEmptyMessage.visibility = View.GONE
        }
    }

    private fun showSelectedEvent(data: ListEventsItem) {
        val intent = Intent(context, DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT, data)
        startActivity(intent)
    }
}
