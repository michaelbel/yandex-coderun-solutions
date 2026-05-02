import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем размеры участка
    val (n, m) = reader.readLine().split(" ").map { it.toInt() }
    
    // Читаем матрицу участка
    val grid = Array(n) { reader.readLine().split(" ").map { it.toInt() }.toIntArray() }
    
    // dp[i][j] - размер максимального квадрата с правым нижним углом в (i,j)
    val dp = Array(n) { IntArray(m) }
    
    // Инициализируем первую строку и столбец
    for (i in 0 until n) dp[i][0] = grid[i][0]
    for (j in 0 until m) dp[0][j] = grid[0][j]
    
    // Заполняем dp таблицу
    for (i in 1 until n) {
        for (j in 1 until m) {
            if (grid[i][j] == 1) {
                dp[i][j] = minOf(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
            } else {
                dp[i][j] = 0
            }
        }
    }
    
    // Находим максимальный квадрат и его координаты
    var maxSize = 0
    var maxI = 0
    var maxJ = 0
    
    for (i in 0 until n) {
        for (j in 0 until m) {
            if (dp[i][j] > maxSize) {
                maxSize = dp[i][j]
                maxI = i
                maxJ = j
            }
        }
    }
    
    // Координаты левого верхнего угла (1-based)
    val topLeftI = maxI - maxSize + 2  // +1 для 1-based, +1 из-за индексации
    val topLeftJ = maxJ - maxSize + 2
    
    writer.write("$maxSize\n")
    writer.write("$topLeftI $topLeftJ\n")

    reader.close()
    writer.close()
}
