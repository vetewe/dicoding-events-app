package com.dicoding.dicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.databinding.ActivityDetailBinding
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.ui.ViewModelFactory

@Suppress("SameParameterValue")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra("EVENT_ID", 0)
        val isFinishedEvent = intent.getBooleanExtra("IS_FINISHED_EVENT", false)

        observeEventDetail(eventId, isFinishedEvent)
        setupFavoriteButton(eventId.toString())

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
                    binding.remainingQuota.text = getString(R.string.event_has_finished)
                    binding.buttonRegister.text = getString(R.string.go_to_website)
                } else {
                    val remainingQuota = (it.quota ?: 0) - (it.registrants ?: 0)
                    binding.remainingQuota.text = getString(R.string.remaining_quota_detail, remainingQuota)
                    binding.buttonRegister.text = getString(R.string.register)
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

    private fun setupFavoriteButton(eventId: String) {
        detailViewModel.getFavoriteEventById(eventId).observe(this) { favoriteEvent ->
            val isFavorite = favoriteEvent != null
            binding.fabFavorite.setImageResource(
                if (isFavorite) R.drawable.favorite_full else R.drawable.favorite_border
            )

            binding.fabFavorite.setOnClickListener {
                if (isFavorite) {
                    detailViewModel.deleteFavoriteById(eventId)
                    Toast.makeText(this, "Deleted From favorite", Toast.LENGTH_SHORT).show()
                } else {
                    val eventDetail = detailViewModel.eventDetail.value
                    if (eventDetail?.name != null && eventDetail.imageLogo != null) {
                        detailViewModel.insertFavorite(FavoriteEvent(eventId, eventDetail.name, eventDetail.imageLogo))
                        Toast.makeText(this, "Added To favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
}
