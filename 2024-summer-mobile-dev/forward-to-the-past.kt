import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val N = reader.readLine()!!.toInt()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val startDate = LocalDate.of(1970, 1, 1)
    val endDate = LocalDate.of(2020, 12, 31)
    var startEpoch = startDate.toEpochDay()
    var endEpoch = endDate.toEpochDay()
    writer.write("? ${endDate.format(formatter)}")
    writer.newLine()
    writer.flush()
    var resp = reader.readLine()!!
    if (resp.first() == '!') return
    val total = resp.split(' ')[1].toLong()
    val targetPos = (total + 1) / 2
    var answerEpoch = startEpoch
    while (startEpoch <= endEpoch) {
        val mid = (startEpoch + endEpoch) / 2
        val midDate = LocalDate.ofEpochDay(mid).format(formatter)
        writer.write("? $midDate")
        writer.newLine()
        writer.flush()
        resp = reader.readLine() ?: break
        if (resp.first() == '!') return
        val k = resp.split(' ')[1].toLong()
        if (k >= targetPos) {
            answerEpoch = mid
            endEpoch = mid - 1
        } else {
            startEpoch = mid + 1
        }
    }
    val answer = LocalDate.ofEpochDay(answerEpoch).format(formatter)
    writer.write("! $answer")
    writer.newLine()
    writer.flush()
}
