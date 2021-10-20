package br.com.davidmag.bingewatcher.presentation.view

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.davidmag.bingewatcher.GlideApp
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.app.databinding.ActivityEpisodeBinding
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

class EpisodeActivity : AppCompatActivity() {

	companion object {
		const val VIEW_LOADING = 1
		const val VIEW_CONTENT = 2
	}

	@Inject
	lateinit var viewModel : EpisodeViewModel

	private val views by lazy {
		ActivityEpisodeBinding.inflate(layoutInflater)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(views.root)

		presentationComponent.inject(this)

		viewModel = initViewModel { viewModel }

		views.contentFlipper.displayedChild = VIEW_LOADING

		views.arrowBack.setOnClickListener {
			finish()
		}

		viewModel.fatalError.observe(this){
			longToast(getString(it))
			finish()
		}

		viewModel.episode.observe(this) {
			views.contentFlipper.displayedChild = VIEW_CONTENT

			with(it){
				GlideApp.with(views.episodePoster)
					.load(imageOriginalUrl)
					.placeholder(R.drawable.placeholder_image_horizontal)
					.into(views.episodePoster)

				views.episodeNumber.text = number
				views.episodeSummary.text = summary
				views.episodeSubtitle.text = seasonTitle
				views.episodeTitle.text = name
			}
		}
	}
}