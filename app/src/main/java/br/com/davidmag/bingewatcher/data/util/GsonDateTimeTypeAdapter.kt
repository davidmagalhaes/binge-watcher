package br.com.davidmag.bingewatcher.data.util

import com.google.gson.*
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object GsonDateTimeTypeAdapter : JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
    @Throws(JsonParseException::class)
    override fun serialize(
        src: OffsetDateTime,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return context?.serialize(
            src.format(DateTimeFormatter.ISO_LOCAL_DATE)
        )!!
    }

    override fun deserialize(
        element: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): OffsetDateTime {
        try {
            return OffsetDateTime.parse(element?.asString)
        }
        catch (e: InstantiationException) {
            throw JsonParseException(e.message, e)
        } catch (e: IllegalAccessException) {
            throw JsonParseException(e.message, e)
        } catch (e: ParseException) {
            throw JsonParseException(e.message, e)
        }
    }
}