package br.com.davidmag.bingewatcher.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import br.com.davidmag.bingewatcher.app.databinding.ActivityHomeBinding
import br.com.davidmag.bingewatcher.presentation.adapter.ShowAdapter
import br.com.davidmag.bingewatcher.presentation.common.initViewModel
import br.com.davidmag.bingewatcher.presentation.di.presentationComponent
import br.com.davidmag.bingewatcher.presentation.viewmodel.HomeViewModel
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

	@Inject
	lateinit var viewModel : HomeViewModel

	private val views by lazy {
		ActivityHomeBinding.inflate(layoutInflater)
	}

	private val adapter by lazy {
		ShowAdapter(this, emptyList())
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(views.root)

		presentationComponent.inject(this)

		viewModel = initViewModel { viewModel }

		with(views) {
			swiper.setOnRefreshListener {
				viewModel.fetchShows()
			}

			homeRecycler.adapter = adapter

			viewFavorites.setOnClickListener {
				viewModel.favoriteClick()
			}

			searchView.setQuery(viewModel.query, false)
			searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
				override fun onQueryTextSubmit(query: String?): Boolean {
					viewModel.searchShows(query.orEmpty())
					return true
				}

				override fun onQueryTextChange(newText: String): Boolean {
					viewModel.searchShows(newText)
					return false
				}
			} as SearchView.OnQueryTextListener )

			viewModel.favoriteState.observe(this@HomeActivity) {
				viewFavorites.displayedChild = it
			}

			viewModel.shows.observe(this@HomeActivity){
				swiper.isRefreshing = false
				adapter.submitList(it)
			}
		}
	}
}