package br.com.davidmag.bingewatcher.presentation.model

import android.os.Parcel
import android.os.Parcelable
import android.text.Spanned
import androidx.core.text.HtmlCompat
import br.com.davidmag.bingewatcher.presentation.common.PresentationResult

data class EpisodePresentation (
	val id : Long? = null,
	val name : String? = null,
	val subtitle : String? = null,
	val season : String? = null,
	val number : String? = null,
	val summary : Spanned? = null,
	val imageMediumUrl : String? = null,
	val imageOriginalUrl : String? = null,
	val seasonTitle : String? = null
): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readValue(Long::class.java.classLoader) as? Long,
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		HtmlCompat.fromHtml(parcel.readString().orEmpty(), 0),
		parcel.readString(),
		parcel.readString(),
		parcel.readString()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(id)
		parcel.writeString(name)
		parcel.writeString(subtitle)
		parcel.writeString(season)
		parcel.writeString(number)
		parcel.writeString(summary?.toString().orEmpty())
		parcel.writeString(imageMediumUrl)
		parcel.writeString(imageOriginalUrl)
		parcel.writeString(seasonTitle)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<EpisodePresentation> {
		override fun createFromParcel(parcel: Parcel): EpisodePresentation {
			return EpisodePresentation(parcel)
		}

		override fun newArray(size: Int): Array<EpisodePresentation?> {
			return arrayOfNulls(size)
		}
	}
}