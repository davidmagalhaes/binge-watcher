package br.com.davidmag.bingewatcher.presentation.mapper

import android.content.res.Resources
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.model.Show
import br.com.davidmag.bingewatcher.presentation.common.PresentationMapper
import br.com.davidmag.bingewatcher.presentation.common.PresentationObject
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import org.threeten.bp.format.DateTimeFormatter

class ShowPresentationMapper(
    private val resources: Resources
) : PresentationMapper<Show, ShowPresentation>() {

    override val contentMapper: (List<Show>) -> List<ShowPresentation> = {
        it.map { show ->
            with(show) {
                ShowPresentation(
                    viewType = PresentationObject.VIEWTYPE_CONTENT,
                    id = id,
                    name = name,
                    time = time,
                    days = days,
                    genres = genres,
                    summary = summary,
                    originalImage = imageOriginalUrl,
                    mediumImage = imageMediumUrl,
                    status = status,
                    ratingAvg = ratingAvg.toFloat() / 2,
                    premiered = premiered?.format(DateTimeFormatter.ofPattern(
                        resources.getString(R.string.show_premiered_format)
                    )),
                    subtitle = resources.getString(
                        R.string.show_subtitle_format,
                        time,
                        days.joinToString()
                    ),
                    seasonsTitles = (1..seasonsCount).map { season ->
                        resources.getString(
                            R.string.episode_season_list_format,
                            season
                        )
                    }
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