import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    
    // Массив для подсчёта количества делителей
    val divisors = IntArray(n + 1)
    
    // Заполняем массив количеством делителей для каждого числа
    for (i in 1..n) {
        for (j in i..n step i) {
            divisors[j]++
        }
    }
    
    // Находим число с максимальным количеством делителей
    var maxDivisors = 0
    var result = 0
    
    for (i in 1..n) {
        if (divisors[i] >= maxDivisors) {
            maxDivisors = divisors[i]
            result = i
        }
    }
    
    // Выводим результат
    writer.write("$result\n")
    writer.write("$maxDivisors\n")

    reader.close()
    writer.close()
}
