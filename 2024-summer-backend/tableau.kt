import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем количество игроков
    val n = reader.readLine().toInt()
    
    // Читаем имена игроков
    val players = Array(n) { reader.readLine() }
    
    // Читаем количество событий
    val m = reader.readLine().toInt()
    
    // Карта для подсчёта очков каждого игрока
    val scores = mutableMapOf<String, Int>()
    
    // Обрабатываем события
    var prevA = 0
    var prevB = 0
    repeat(m) {
        val (score, player) = reader.readLine().split(" ", limit = 2)
        val (a, b) = score.split(":").map { it.toInt() }
        
        // Определяем, сколько очков заработал игрок
        val points = when {
            a > prevA -> a - prevA
            b > prevB -> b - prevB
            else -> 0  // На случай ошибки ввода
        }
        
        scores[player] = scores.getOrDefault(player, 0) + points
        prevA = a
        prevB = b
    }
    
    // Находим игрока с максимальным количеством очков
    var maxScore = 0
    var maxPlayer = ""
    
    for ((player, score) in scores) {
        if (score > maxScore || (score == maxScore && player > maxPlayer)) {
            maxScore = score
            maxPlayer = player
        }
    }
    
    writer.write("$maxPlayer $maxScore")
    writer.newLine()

    reader.close()
    writer.close()
}
