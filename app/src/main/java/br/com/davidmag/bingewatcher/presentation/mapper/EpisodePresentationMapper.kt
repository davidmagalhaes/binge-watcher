package br.com.davidmag.bingewatcher.presentation.mapper

import android.content.res.Resources
import androidx.core.text.HtmlCompat
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.model.Episode
import br.com.davidmag.bingewatcher.presentation.common.PresentationMapper
import br.com.davidmag.bingewatcher.presentation.common.PresentationObject
import br.com.davidmag.bingewatcher.presentation.model.EpisodePresentation
import java.time.format.DateTimeFormatter

class EpisodePresentationMapper(
	private val resources: Resources
) : PresentationMapper<Episode, EpisodePresentation>() {
	override val contentMapper: (List<Episode>) -> List<EpisodePresentation> = { episodes ->
		episodes.map {
			with(it){
				EpisodePresentation(
					viewType = PresentationObject.VIEWTYPE_CONTENT,
					id = id,
					name = name,
					subtitle = resources.getString(
						R.string.episode_subtitle_format,
						season, number,
						premiered?.format(
							DateTimeFormatter.ofPattern(
							resources.getString(R.string.show_premiered_format))).orEmpty()
					),
					season = season.toString(),
					number = number.toString(),
					summary = HtmlCompat.fromHtml(
						summary,
						HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
					),
					imageMediumUrl = imageMediumUrl,
					imageOriginalUrl = imageOriginalUrl,
					seasonTitle = resources.getString(
						R.string.episode_season_list_format,
						season
					)
				)
			}
		}
	}

	override val errorMapper: (Throwable) -> EpisodePresentation = {
		EpisodePresentation(
			viewType = PresentationObject.VIEWTYPE_ERROR,
		)
	}
}