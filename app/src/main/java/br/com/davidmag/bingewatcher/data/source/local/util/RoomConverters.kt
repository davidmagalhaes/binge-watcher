package br.com.davidmag.bingewatcher.data.source.local.util

import androidx.room.TypeConverter
import br.com.davidmag.bingewatcher.data.util.GsonDateTimeTypeAdapter
import br.com.davidmag.bingewatcher.data.util.GsonSimpleDateTypeAdapter
import com.google.gson.GsonBuilder
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object RoomConverters {
    private val localDateFormatter = DateTimeFormatter.ISO_DATE
    private val offsetDateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    val gson by lazy {
        val gsonBuilder = GsonBuilder().setDateFormat("yyyy-MM-dd")

        gsonBuilder.registerTypeAdapter(OffsetDateTime::class.java, GsonDateTimeTypeAdapter)
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, GsonSimpleDateTypeAdapter)

        gsonBuilder.create()
    }

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return offsetDateTimeFormatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(offsetDateTimeFormatter)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            return localDateFormatter.parse(value, LocalDate::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(localDateFormatter)
    }

    @TypeConverter
    @JvmStatic
    fun fromList(list: List<*>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun toStringList(string: String?): List<String>? {
        return gson.fromJson(string, List::class.java) as List<String>
    }

    @TypeConverter
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun toLongList(string: String?): List<Long>? {
        return gson.fromJson(string, List::class.java) as List<Long>
    }
}