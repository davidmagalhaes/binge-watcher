package br.com.davidmag.bingewatcher.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.common.orFalse
import br.com.davidmag.bingewatcher.domain.common.orZero
import br.com.davidmag.bingewatcher.presentation.common.ErrorPresentation
import br.com.davidmag.bingewatcher.presentation.common.PresentationResult
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import br.com.davidmag.bingewatcher.presentation.view.common.collectAsLazyPagingItems
import br.com.davidmag.bingewatcher.presentation.view.common.emptyLazyPagingItems
import br.com.davidmag.bingewatcher.presentation.view.theme.AppTheme
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    searchText: State<String>,
    favoriteStatus: State<PresentationResult<Boolean>>,
    items: State<PresentationResult<Flow<PagingData<ShowPresentation>>>>,
    errors: State<ErrorPresentation?>,
    onRefresh: () -> Unit,
    onFavoriteClick: () -> Unit,
    onItemClick: (ShowPresentation) -> Unit,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState: LazyListState = rememberLazyListState()
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = items.value is PresentationResult.ResultLoading
    val itemList = items.value.collectAsLazyPagingItems()
    var showingItems: LazyPagingItems<ShowPresentation> by remember { mutableStateOf(itemList) }

    showingItems = when (items.value) {
        is PresentationResult.ResultSuccess -> itemList
        is PresentationResult.ResultEmpty -> emptyLazyPagingItems()
        else -> showingItems
    }

    // Workaround for the bug on the refresh indicator
    if (!isRefreshing && refreshState.distanceFraction == 1f) {
        LaunchedEffect(false) {
            scope.launch { refreshState.animateToHidden() }
        }
    }

    PullToRefreshBox(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = refreshState
            )
        }
    ) {
        Column {
            SearchBar(searchText, favoriteStatus, onFavoriteClick, onQueryChange, onSearchSubmit)

            LazyColumn(state = listState) {
                if (showingItems.loadState.hasError) {
                    item {
                        ListingError()
                    }
                }

                if (!showingItems.loadState.hasError) {
                    items(
                        count = showingItems.itemCount.takeIf { it > 0 }?.plus(1).orZero(),
                        key = showingItems.itemKey { it.id },
                        contentType = showingItems.itemContentType { it }
                    ) { idx ->

                        val item = showingItems[idx]
                        if (item != null) {
                            ShowItem(item) { onItemClick(item) }
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (!showingItems.loadState.hasError) {}
            }
        }
    }
}

@Composable
fun ListingError() {

}

@Composable
fun SearchBar(
    text: State<String>,
    isFavorited: State<PresentationResult<Boolean>>,
    onFavoriteClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onSearchSubmit: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxHeight(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(android.R.drawable.ic_menu_search),
                tint = MaterialTheme.colorScheme.inversePrimary,
                contentDescription = "Search now"
            )
        }
        TextField(
            modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.primary),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.inversePrimary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.inversePrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,

            ),
            placeholder = { Text(text = "Search for shows") },
            value = text.value,
            onValueChange = { onQueryChange(it) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { onSearchSubmit() }
            )
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxHeight(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when(val favoriteStatus = isFavorited.value) {
                is PresentationResult.ResultError,
                is PresentationResult.ResultEmpty,
                is PresentationResult.ResultSuccess -> {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { onFavoriteClick() }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (favoriteStatus.data.orFalse())
                                    R.drawable.ic_baseline_favorite_24
                                else
                                    R.drawable.ic_baseline_favorite_border_24
                            ),
                            tint = MaterialTheme.colorScheme.inversePrimary,
                            contentDescription = "Favorite it"
                        )
                    }
                }
                is PresentationResult.ResultLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        trackColor = colorResource(R.color.textColorPrimary)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowItem(item: ShowPresentation, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        Column(modifier = Modifier.padding(end = 16.dp)) {
            GlideImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                ,
                model = item.originalImage,
                contentDescription = null
            )
        }
        Column {
            RatingBar(item.ratingAvg)
            Text(
                text = item.name.orEmpty(),
                color = MaterialTheme.colorScheme.surface,
                fontSize = 24.sp
            )
            Spacer(Modifier.padding(bottom = 16.dp))
            Text(
                text = item.premiered.orEmpty(),
                color = MaterialTheme.colorScheme.surface
            )
            Text(
                text = item.status.orEmpty(),
                color = MaterialTheme.colorScheme.surface
            )
            Spacer(Modifier.padding(bottom = 16.dp))
            Chips(item.genres)
        }
    }
}

@Composable
fun Chips(values: List<String>) {
    LazyRow {
        repeat(values.size) {
            item {
                AssistChip(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = {},
                    label = {
                        Text(
                            text = values[it],
                            color = MaterialTheme.colorScheme.surface
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun RatingBar(rating: Float, onClick: () -> Unit = {}) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(5) { index ->
            Icon (
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index <= rating) Color.Yellow else Color.Gray,
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        onClick()
                    }
                    .padding(4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewShowItem() {
    AppTheme {
        ShowItem(ShowPresentation(1))
    }
}