package br.com.davidmag.bingewatcher.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.davidmag.bingewatcher.GlideApp
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.app.databinding.ActivityHomeBinding
import br.com.davidmag.bingewatcher.app.databinding.ActivityShowBinding
import br.com.davidmag.bingewatcher.presentation.adapter.EpisodeAdapter
import br.com.davidmag.bingewatcher.presentation.adapter.GenreAdapter
import br.com.davidmag.bingewatcher.presentation.common.*
import br.com.davidmag.bingewatcher.presentation.common.decorator.HorizontalSpaceItemDecoration
import br.com.davidmag.bingewatcher.presentation.di.presentationComponent
import br.com.davidmag.bingewatcher.presentation.viewmodel.EpisodeViewModel
import br.com.davidmag.bingewatcher.presentation.viewmodel.HomeViewModel
import br.com.davidmag.bingewatcher.presentation.viewmodel.ShowViewModel
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class ShowActivity : AppCompatActivity() {

	companion object {
		const val VIEW_LOADING = 1
		const val VIEW_CONTENT = 2
	}

	@Inject
	lateinit var viewModel : ShowViewModel

	private val views by lazy {
		ActivityShowBinding.inflate(layoutInflater)
	}

	private val genreAdapter by lazy {
		GenreAdapter(applicationContext)
	}

	private val episodeAdapter by lazy {
		EpisodeAdapter(applicationContext) {
			startActivity(
				Intent(
					this,
					EpisodeActivity::class.java
				).apply {
					putExtra(EpisodeViewModel.ARG_EPISODE, it)
				}
			)
		}
	}

	private val seasonItems = ArrayList<String>()

	private val seasonAdapter by lazy {
		ArrayAdapter(
			this@ShowActivity,
			android.R.layout.simple_spinner_item,
			android.R.id.text1,
			seasonItems
		)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(views.root)

		presentationComponent.inject(this)

		viewModel = initViewModel { viewModel }

		views.rootFlipper.displayedChild = VIEW_LOADING

		views.showGenres.adapter = genreAdapter
		views.showGenres.addItemDecoration(HorizontalSpaceItemDecoration(resources))

		views.showSeasonEpisodes.adapter = episodeAdapter
		views.showSeasonEpisodes.addItemDecoration(
			HorizontalSpaceItemDecoration(resources, R.dimen.large_margin)
		)

		views.arrowBack.setOnClickListener {
			finish()
		}

		views.showSeasonSpinner.adapter = seasonAdapter
		views.showSeasonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?,
				view: View?,
				position: Int,
				id: Long
			) = viewModel.selectSeason(position + 1)

			override fun onNothingSelected(parent: AdapterView<*>?) {}
		}

		views.showFavorite.setOnClickListener {
			viewModel.favorite()
		}

		viewModel.favoriteState.observe(this){
			views.showFavorite.displayedChild = it
		}

		viewModel.error.observe(this) {
			handleException(it)
		}

		viewModel.fatalError.observe(this){
			handleException(it)
			finish()
		}

		viewModel.episodes.observe(this){
			episodeAdapter.items = it
			episodeAdapter.notifyDataSetChanged()
		}

		viewModel.show.observe(this){ show ->
			views.rootFlipper.displayedChild = VIEW_CONTENT

			with(show.first()) {
				GlideApp.with(views.showPoster)
					.load(mediumImage)
					.placeholder(R.drawable.placeholder_image_horizontal)
					.into(views.showPoster)

				views.showRating.rating = ratingAvg
				views.showSummary.text = summary
				views.showTitle.text = name
				views.showTimeDays.text = subtitle

				genreAdapter.items = genres
				genreAdapter.notifyDataSetChanged()

				seasonItems.clear()
				seasonItems.addAll(seasonsTitles)
				seasonAdapter.notifyDataSetChanged()
			}
		}
	}
}