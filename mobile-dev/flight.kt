import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.abs

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val departureTimeStr = reader.readLine()
    val arrivalTimeStr = reader.readLine()
    val timeZoneDiffStr = reader.readLine()
    val departureParts = departureTimeStr.split(":")
    val departureHour = departureParts[0].toInt()
    val departureMinute = departureParts[1].toInt()
    val departureTotalMinutes = departureHour * 60 + departureMinute
    val arrivalParts = arrivalTimeStr.split(":")
    val arrivalHour = arrivalParts[0].toInt()
    val arrivalMinute = arrivalParts[1].toInt()
    val arrivalTotalMinutes = arrivalHour * 60 + arrivalMinute
    val timeZoneDiffHours = timeZoneDiffStr.toInt()
    val timeZoneDiffMinutes = timeZoneDiffHours * 60
    var durationMinutes = arrivalTotalMinutes - departureTotalMinutes - timeZoneDiffMinutes
    durationMinutes = (durationMinutes % (24 * 60) + (24 * 60)) % (24 * 60)
    val durationHours = durationMinutes / 60
    val durationMinutesRemainder = durationMinutes % 60
    val formattedMinutes = String.format("%02d", durationMinutesRemainder)
    writer.write("$durationHours:$formattedMinutes")
    writer.newLine()
    reader.close()
    writer.close()
}
