import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val sequence = mutableListOf<Int>()
    while (true) {
        val num = reader.readLine().toInt()
        if (num == -2000000000) break
        sequence.add(num)
    }

    if (sequence.size < 2) {
        writer.write("CONSTANT")
    } else {
        var isConstant = true
        var isAscending = true
        var isWeaklyAscending = true
        var isDescending = true
        var isWeaklyDescending = true

        for (i in 1 until sequence.size) {
            if (sequence[i] != sequence[i - 1]) isConstant = false
            if (sequence[i] <= sequence[i - 1]) isAscending = false
            if (sequence[i] < sequence[i - 1]) isWeaklyAscending = false
            if (sequence[i] >= sequence[i - 1]) isDescending = false
            if (sequence[i] > sequence[i - 1]) isWeaklyDescending = false
        }

        writer.write(
            when {
                isConstant -> "CONSTANT"
                isAscending -> "ASCENDING"
                isWeaklyAscending -> "WEAKLY ASCENDING"
                isDescending -> "DESCENDING"
                isWeaklyDescending -> "WEAKLY DESCENDING"
                else -> "RANDOM"
            }
        )
    }

    reader.close()
    writer.flush()
    writer.close()
}
