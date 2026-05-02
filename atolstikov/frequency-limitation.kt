import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine().replace(" ", "")
    val parts = firstLine.split("/")
    val X = parts[0].toLong()
    val Y = parts[1].toLong()

    val N = reader.readLine().toInt()
    val requests = reader.readLine().split(" ").map { it.toLong() }

    val result = LongArray(N)

    if (Y == 1L) {
        for (i in 0 until N) {
            if (i < X)
                result[i] = requests[i]
            else
                result[i] = if (requests[i] > result[i - X.toInt()] + 1_000_000_000L)
                    requests[i]
                else
                    result[i - X.toInt()] + 1_000_000_000L
        }
    } else if (X == 1L) {
        result[0] = requests[0]
        for (i in 1 until N) {
            result[i] = if (requests[i] > result[i - 1] + Y * 1_000_000_000L)
                requests[i]
            else
                result[i - 1] + Y * 1_000_000_000L
        }
    }

    writer.write(result.joinToString(" "))
    writer.flush()
    writer.close()
    reader.close()
}
