package br.com.davidmag.bingewatcher.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import br.com.davidmag.bingewatcher.app.databinding.ActivityHomeBinding
import br.com.davidmag.bingewatcher.presentation.adapter.ShowAdapter
import br.com.davidmag.bingewatcher.presentation.common.decorator.VerticalSpaceItemDecoration
import br.com.davidmag.bingewatcher.presentation.common.getString
import br.com.davidmag.bingewatcher.presentation.common.initViewModel
import br.com.davidmag.bingewatcher.presentation.common.longToast
import br.com.davidmag.bingewatcher.presentation.di.presentationComponent
import br.com.davidmag.bingewatcher.presentation.util.UiUtils
import br.com.davidmag.bingewatcher.presentation.viewmodel.HomeViewModel
import br.com.davidmag.bingewatcher.presentation.viewmodel.ShowViewModel
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

	companion object {
		const val VIEW_CONTENT = 0
		const val VIEW_LOADING = 1
	}

	@Inject
	lateinit var viewModel : HomeViewModel

	private val views by lazy {
		ActivityHomeBinding.inflate(layoutInflater)
	}

	private val adapter by lazy {
		ShowAdapter(this) {
			startActivity(
				Intent(
					this,
					ShowActivity::class.java
				).apply {
					putExtra(ShowViewModel.ARG_SHOW, it.id)
				}
			)
		}
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(views.root)

		presentationComponent.inject(this)

		viewModel = initViewModel { viewModel }

		with(views) {
			contentFlipper.displayedChild = VIEW_LOADING

			swiper.setOnRefreshListener {
				UiUtils.closeKeyboard(applicationContext, root)
				viewModel.updateShows()
			}

			adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
				override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
					contentFlipper.displayedChild = VIEW_CONTENT
				}
			})

			homeRecycler.adapter = adapter
			homeRecycler.addItemDecoration(
				VerticalSpaceItemDecoration(applicationContext.resources)
			)

			homeRecycler.addOnScrollListener(object : OnScrollListener() {
				override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
					super.onScrolled(recyclerView, dx, dy)
					swiper.isEnabled = !homeRecycler.canScrollVertically(-1)
				}
			})

			viewFavorites.setOnClickListener {
				viewModel.showFavoritesClick()
			}

			searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
				override fun onQueryTextSubmit(query: String?): Boolean {
					UiUtils.closeKeyboard(applicationContext, root)
					viewModel.submitSearch(query.orEmpty())
					return true
				}
				override fun onQueryTextChange(newText: String): Boolean {
					viewModel.onSearchChange(newText)
					return false
				}
			})

			searchView.setOnCloseListener {
				viewModel.updateShows()
				false
			}

			viewModel.query.observe(this@HomeActivity){
				searchView.setQuery(it, false)
			}

			viewModel.favoriteState.observe(this@HomeActivity) {
				viewFavorites.isSelected = it
			}

			viewModel.errors.observe(this@HomeActivity){ exception ->
				longToast(getString(exception))
			}

			viewModel.shows.observe(this@HomeActivity){
				swiper.isRefreshing = false
				adapter.submitData(this@HomeActivity.lifecycle, it)
			}
		}
	}
}