import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s = reader.readLine()

    var result = 0L
    var currentNumber = 0L
    var sign = 1

    for (char in s) {
        if (char.isDigit()) {
            currentNumber = currentNumber * 10 + (char - '0')
        } else {
            result += sign * currentNumber
            currentNumber = 0L
            sign = if (char == '+') 1 else -1
        }
    }

    result += sign * currentNumber

    writer.write(result.toString())

    reader.close()
    writer.close()
}

