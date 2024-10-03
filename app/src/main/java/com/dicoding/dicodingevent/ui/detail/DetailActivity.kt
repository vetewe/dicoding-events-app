package com.dicoding.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", 0)
        val isFinishedEvent = intent.getBooleanExtra("IS_FINISHED_EVENT", false)

        observeEventDetail(eventId, isFinishedEvent)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    @SuppressLint("SetTextI18n")
    private fun observeEventDetail(eventId: Int, isFinishedEvent: Boolean) {
        detailViewModel.eventDetail.observe(this) { event ->
            event?.let {
                binding.progressBar.visibility = View.GONE
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
                    binding.buttonRegister.text = "Selengkapnya"
                } else {
                    val remainingQuota = (it.quota ?: 0) - (it.registrants ?: 0)
                    binding.remainingQuota.text = "Remaining Quota:\n $remainingQuota"
                    binding.buttonRegister.text = "Register"
                }

                binding.buttonRegister.setOnClickListener {
                    val link = event.link
                    if (!link.isNullOrEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                }

                Glide.with(this)
                    .load(it.mediaCover)
                    .into(binding.mediaCover)
            }
        }

        detailViewModel.getEventDetail(eventId)
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