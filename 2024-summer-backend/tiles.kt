import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем количество чёрных и белых плиток
    val (b, w) = reader.readLine().split(" ").map { it.toInt() }
    
    // Функция подсчёта чёрных и белых плиток для размеров n×m
    fun countTiles(n: Int, m: Int): Pair<Int, Int> {
        val total = n * m
        val black = if (n == 1 && m == 1) 1 else 2 * (n + m - 2)
        val white = total - black
        return black to white
    }
    
    // Перебираем возможные размеры
    for (m in 1..b) {  // m не может быть больше B, так как минимум 2m чёрных
        for (n in m..b) {  // n ≥ m по условию
            val (black, white) = countTiles(n, m)
            if (black == b && white == w) {
                writer.write("$n $m")
                writer.newLine()
                reader.close()
                writer.close()
                return
            }
        }
    }
}
