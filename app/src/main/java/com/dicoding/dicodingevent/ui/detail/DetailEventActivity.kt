package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val viewModel: DetailEventViewModel by viewModels()

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Ambil data dari Intent dan masukkan ke ViewModel
        val event = intent.getParcelableExtra<ListEventsItem>(EXTRA_EVENT)
        if (event != null) {
            viewModel.setEvent(event)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        // Observasi data event
        viewModel.event.observe(this) { event ->
            setEventData(event)
        }

        // Observasi state loading
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setEventData(event: ListEventsItem) {
        binding.tvName.text = event.name
        binding.tvOwnerName.text = event.ownerName
        binding.tvBeginTime.text = event.beginTime
        binding.tvQuota.text = (event.quota - event.registrants).toString()
        binding.tvDescription.text = HtmlCompat.fromHtml(
            event.description.toString(),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        Glide.with(this@DetailEventActivity)
            .load(event.imageLogo)
            .into(binding.imgEvent)

        binding.btnRegister.setOnClickListener {
            val eventUrl = event.link
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(eventUrl)
            }
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}