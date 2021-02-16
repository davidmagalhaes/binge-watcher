package br.com.davidmag.bingewatcher.presentation.view

import android.content.Intent
import android.database.DataSetObserver
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.davidmag.bingewatcher.GlideApp
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.app.databinding.ActivityShowBinding
import br.com.davidmag.bingewatcher.presentation.adapter.EpisodeAdapter
import br.com.davidmag.bingewatcher.presentation.adapter.GenreAdapter
import br.com.davidmag.bingewatcher.presentation.common.*
import br.com.davidmag.bingewatcher.presentation.common.decorator.HorizontalSpaceItemDecoration
import br.com.davidmag.bingewatcher.presentation.di.presentationComponent
import br.com.davidmag.bingewatcher.presentation.viewmodel.EpisodeViewModel
import br.com.davidmag.bingewatcher.presentation.viewmodel.ShowViewModel
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

	private val seasonAdapter by lazy {
		ArrayAdapter(
			this@ShowActivity,
			R.layout.season_spinner_item,
			android.R.id.text1,
			ArrayList<String>()
		)
	}

	private val genreAdapter by lazy {
		GenreAdapter(applicationContext)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(views.root)

		presentationComponent.inject(this)

		viewModel = initViewModel { viewModel }

		views.contentFlipper.displayedChild = VIEW_LOADING

		with(views.content) {
			showGenres.adapter = genreAdapter
			showGenres.addItemDecoration(HorizontalSpaceItemDecoration(resources))

			episodeAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
				override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
					views.contentFlipper.displayedChild = VIEW_CONTENT
				}
			})

			showSeasonEpisodes.adapter = episodeAdapter
			showSeasonEpisodes.addItemDecoration(
				HorizontalSpaceItemDecoration(resources, R.dimen.large_margin)
			)

			arrowBack.setOnClickListener {
				finish()
			}

			showSeasonSpinner.adapter = seasonAdapter
			showSeasonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(
					parent: AdapterView<*>?,
					view: View?,
					position: Int,
					id: Long
				) = viewModel.selectSeason(position + 1)

				override fun onNothingSelected(parent: AdapterView<*>?) {}
			}

			showFavorite.setOnClickListener {
				viewModel.favorite()
			}

			viewModel.favoriteState.observe(this@ShowActivity){
				showFavorite.displayedChild = it
			}

			viewModel.error.observe(this@ShowActivity) {
				longToast(getString(it))
			}

			viewModel.fatalError.observe(this@ShowActivity){
				longToast(getString(it))
				finish()
			}

			viewModel.episodes.observe(this@ShowActivity){
				episodeAdapter.items = it
				episodeAdapter.notifyDataSetChanged()
			}

			viewModel.show.observe(this@ShowActivity){ showList ->
				val show = showList.first()

				GlideApp.with(showPoster)
					.load(show.backgroundImage)
					.placeholder(R.drawable.placeholder_image_horizontal)
					.into(showPoster)

				showRating.rating = show.ratingAvg
				showSummary.text = show.summary
				showTitle.text = show.name
				showTimeDays.text = show.subtitle

				genreAdapter.items = show.genres
				genreAdapter.notifyDataSetChanged()

				seasonAdapter.clear()
				seasonAdapter.addAll(show.seasonsTitles)
				seasonAdapter.notifyDataSetChanged()
			}
		}
	}
}