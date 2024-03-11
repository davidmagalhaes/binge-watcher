package br.com.davidmag.bingewatcher.data.util

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object GsonSimpleDateTypeAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    @Throws(JsonParseException::class)
    override fun serialize(
        src: LocalDate,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return context?.serialize(src.format(DateTimeFormatter.ISO_DATE))!!
    }

    override fun deserialize(
        element: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        try {
            return LocalDate.parse(element?.asString)
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