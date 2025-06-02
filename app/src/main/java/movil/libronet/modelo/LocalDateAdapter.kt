import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @ToJson
    fun toJson(date: LocalDate): String = formatter.format(date)

    @FromJson
    fun fromJson(dateString: String): LocalDate = LocalDate.parse(dateString, formatter)
}