package br.com.davidmag.bingewatcher.presentation.mapper

import android.content.res.Resources
import androidx.core.text.HtmlCompat
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.model.Show
import br.com.davidmag.bingewatcher.presentation.common.PresentationMapper
import br.com.davidmag.bingewatcher.presentation.common.PresentationObject
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import org.threeten.bp.format.DateTimeFormatter

class ShowPresentationMapper(
    private val resources: Resources
) : PresentationMapper<Show, ShowPresentation>() {

    override val contentMapper: (List<Show>) -> List<ShowPresentation> = { shows ->
        shows.map { show ->
            with(show) {
                ShowPresentation(
                    viewType = PresentationObject.VIEWTYPE_CONTENT,
                    id = id,
                    name = name,
                    time = time,
                    days = days,
                    genres = genres.map { it.id },
                    summary = HtmlCompat.fromHtml(
                        summary,
                        HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_BLOCKQUOTE
                    ),
                    originalImage = imageOriginalUrl,
                    mediumImage = imageMediumUrl,
                    backgroundImage = imageBackgroundUrl,
                    status = status,
                    ratingAvg = ratingAvg.toFloat() / 2,
                    premiered = premiered?.format(DateTimeFormatter.ofPattern(
                        resources.getString(R.string.show_premiered_format)
                    )),
                    subtitle = resources.getString(
                        R.string.show_subtitle_format,
                        time,
                        days.joinToString { each -> each.take(3) }
                    ),
                    favored = favored,
                    seasonsTitles = (1..seasonsIds.size).map { season ->
                        resources.getString(
                            R.string.episode_season_list_format,
                            season
                        )
                    },
                    seasonsIds = seasonsIds
                )
            }
        }
    }

    override val errorMapper: (Throwable) -> ShowPresentation = {
        ShowPresentation(
            viewType = PresentationObject.VIEWTYPE_ERROR,
            id = -1
        )
    }
}