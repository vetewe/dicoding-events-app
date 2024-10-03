package com.dicoding.dicodingevent.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", 0)
        val isFinishedEvent = intent.getBooleanExtra("IS_FINISHED_EVENT", false)
        getEventDetail(eventId, isFinishedEvent)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun getEventDetail(eventId: Int, isFinishedEvent: Boolean) {
        binding.progressBar.visibility = View.VISIBLE
        val client = ApiConfig.getApiService().getEvent(eventId.toString())
        client.enqueue(object : Callback<EventResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val event = response.body()?.listEvents?.firstOrNull { it?.id == eventId }
                    event?.let {
                        binding.eventName.text = it.name
                        binding.eventSummary.text = it.summary
                        binding.description.text = HtmlCompat.fromHtml(
                            it.description.toString(),
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.operator.text = "Operator:\n ${it.ownerName}"
                        binding.eventTime.text = "Event Time:\n ${it.beginTime}"
                        if (isFinishedEvent) {
                            binding.remainingQuota.text = "Event\n has\n finished"
                        } else {
                            val remainingQuota = (it.quota ?: 0) - (it.registrants ?: 0)
                            binding.remainingQuota.text = "Remaining Quota: $remainingQuota"
                        }

                        Glide.with(this@DetailActivity)
                            .load(it.mediaCover)
                            .into(binding.mediaCover)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "DetailActivity"
    }
}