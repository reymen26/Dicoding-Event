package com.dicoding.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.local.room.EventDatabase
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.databinding.ActivityDetailEventBinding
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.data.response.DetailResponse
import com.dicoding.dicodingevent.di.Injection
import com.dicoding.dicodingevent.viewModelFavtory.DetailEventViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val eventDao by lazy { EventDatabase.getInstance(this).eventDao() }
    private val viewModel: DetailEventViewModel by viewModels {
        DetailEventViewModelFactory(application, EventRepository.getInstance(eventDao))
    }

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel.setLoading(true)

        val event = intent.getParcelableExtra<ListEventsItem>(EXTRA_EVENT)
        event?.let {
            loadEventDetail(it.id.toString())
            viewModel.getFavoriteById(it.id.toString())

            viewModel.isFavorited.observe(this) { isFavorited ->
                binding.btnFavorite.setImageResource(
                    if (isFavorited) R.drawable.ic_favorite_24
                    else R.drawable.ic_favorite_border_24
                )
            }
        }

        binding.btnFavorite.setOnClickListener {
            event?.let { eventItem ->
                val favoriteEvent = EventEntity(
                    id = eventItem.id,
                    name = eventItem.name,
                    mediaCover = eventItem.imageLogo,
                )
                if (viewModel.isFavorited.value == true) {
                    viewModel.deleteFavorite(favoriteEvent)
                } else {
                    viewModel.addFavorite(favoriteEvent)
                }
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.event.observe(this) { event ->
            setEventData(event)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun loadEventDetail(eventId: String) {
        val apiService = Injection.provideApiService()
        val call = apiService.getDetailEvent(eventId)
        call.enqueue(object : Callback<DetailResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.event?.let { event ->
                        val listEventItem = ListEventsItem(
                            summary = event.summary,
                            mediaCover = event.mediaCover,
                            registrants = event.registrants,
                            imageLogo = event.imageLogo,
                            link = event.link,
                            description = event.description,
                            ownerName = event.ownerName,
                            cityName = event.cityName,
                            quota = event.quota,
                            name = event.name,
                            id = event.id,
                            beginTime = event.beginTime,
                            endTime = event.endTime,
                            category = event.category
                        )
                        setEventData(listEventItem)
                    }
                    showLoading(false)
                } else {
                    Log.e("DetailEventActivity", "Gagal memuat detail event.")
                    showLoading(false)
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("DetailEventActivity", "Error: ${t.message}")
                showLoading(false)
            }
        })
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

        Glide.with(this)
            .load(event.imageLogo)
            .into(binding.imgEvent)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        try {
            val eventTime = LocalDateTime.parse(event.beginTime, formatter)
            val currentTime = LocalDateTime.now()

            if (eventTime.isBefore(currentTime)) {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.text = getString(R.string.register_disable)
            } else {
                binding.btnRegister.isEnabled = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.detailEventFragment.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
}
