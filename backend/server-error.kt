import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val probabilities = mutableListOf<Pair<Double, Double>>()
    repeat(n) {
        val (a, b) = reader.readLine().split(" ").map { it.toDouble() }
        probabilities.add(a / 100.0 to b / 100.0)
    }
    val totalErrorProbability = probabilities.sumOf { it.first * it.second }
    probabilities.forEach { (serverLoad, errorChance) ->
        val result = (serverLoad * errorChance) / totalErrorProbability
        writer.write("%.12f\n".format(result))
    }
    writer.flush()
    reader.close()
    writer.close()
}
