package com.dicoding.dicodingevent.ui.home

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
import com.dicoding.dicodingevent.adapter.UpcomingEventAdapter
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.ui.detail.DetailEventActivity

class HomeFragment : Fragment() {

    private lateinit var rvUpcoming: RecyclerView
    private lateinit var rvFinished: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyMessage: TextView
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var upcomingAdapter: UpcomingEventAdapter
    private lateinit var finishedAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inisialisasi View
        rvUpcoming = view.findViewById(R.id.rv_home_upcoming)
        rvFinished = view.findViewById(R.id.rv_home_finished)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage)

        // Konfigurasi RecyclerView
        rvUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvFinished.layoutManager = LinearLayoutManager(context)

        upcomingAdapter = UpcomingEventAdapter()
        finishedAdapter = EventAdapter()

        rvUpcoming.adapter = upcomingAdapter
        rvFinished.adapter = finishedAdapter

        // Observasi Data
        viewModel.upcomingEvents.observe(viewLifecycleOwner, Observer { eventList ->
            upcomingAdapter.submitList(eventList)
            checkEmptyState()
        })

        viewModel.finishedEvents.observe(viewLifecycleOwner, Observer { eventList ->
            finishedAdapter.submitList(eventList)
            checkEmptyState()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            showLoading(isLoading)
        })

        // Fetch Data
        viewModel.fetchUpcomingEvents()
        viewModel.fetchFinishedEvents()

        // Klik Item pada RecyclerView
        upcomingAdapter.setOnItemClickCallback(object : UpcomingEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEvent(data)
            }
        })

        finishedAdapter.setOnItemClickCallback(object : EventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                showSelectedEvent(data)
            }
        })

        return view
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun checkEmptyState() {
        val isEmpty = upcomingAdapter.itemCount == 0 && finishedAdapter.itemCount == 0
        tvEmptyMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showSelectedEvent(data: ListEventsItem) {
        val intent = Intent(context, DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT, data)
        startActivity(intent)
    }
}
