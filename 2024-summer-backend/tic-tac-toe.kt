import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем размеры поля
    val (n, m) = reader.readLine().split(" ").map { it.toInt() }
    
    // Читаем поле
    val field = Array(n) { reader.readLine().toCharArray() }
    
    // Функция проверки последовательности из 5 символов
    fun checkWin(c: Char, i: Int, j: Int, di: Int, dj: Int): Boolean {
        for (k in 0 until 5) {
            val ni = i + k * di
            val nj = j + k * dj
            if (ni !in 0 until n || nj !in 0 until m || field[ni][nj] != c) {
                return false
            }
        }
        return true
    }
    
    // Проверяем все возможные выигрышные линии
    var hasWinner = false
    for (i in 0 until n) {
        for (j in 0 until m) {
            if (field[i][j] == '.') continue
            
            val c = field[i][j]
            // Проверяем горизонталь
            if (j + 4 < m && checkWin(c, i, j, 0, 1)) {
                hasWinner = true
                break
            }
            // Проверяем вертикаль
            if (i + 4 < n && checkWin(c, i, j, 1, 0)) {
                hasWinner = true
                break
            }
            // Проверяем главную диагональ
            if (i + 4 < n && j + 4 < m && checkWin(c, i, j, 1, 1)) {
                hasWinner = true
                break
            }
            // Проверяем побочную диагональ
            if (i + 4 < n && j - 4 >= 0 && checkWin(c, i, j, 1, -1)) {
                hasWinner = true
                break
            }
        }
        if (hasWinner) break
    }
    
    writer.write(if (hasWinner) "Yes" else "No")
    writer.newLine()

    reader.close()
    writer.close()
}
