import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем количество точек
    val n = reader.readLine().toInt()
    // Массив координат y (координаты x не нужны для решения)
    val points = Array(n) {
        val (x, y) = reader.readLine().split(" ").map { it.toInt() }
        y
    }

    // Вычисляем префиксные суммы для подъёмов при движении слева направо
    // forward[i] = суммарный подъём от точки 1 до точки i+1 (i от 0 до n-1)
    val forward = IntArray(n)
    forward[0] = 0
    for (i in 1 until n) {
        // Если движение вперед приводит к увеличению высоты, добавляем разницу
        forward[i] = forward[i - 1] + if (points[i] > points[i - 1]) (points[i] - points[i - 1]) else 0
    }

    // Вычисляем префиксные суммы для подъёмов при движении справа налево
    // rev[i] = суммарный подъём при движении в обратном направлении от точки i до точки 1
    // Здесь подъём определяется как разница: если при движении назад высота увеличивается,
    // то есть если points[i-1] > points[i].
    val rev = IntArray(n)
    rev[0] = 0
    for (i in 1 until n) {
        rev[i] = rev[i - 1] + if (points[i - 1] > points[i]) (points[i - 1] - points[i]) else 0
    }

    // Читаем количество трасс
    val m = reader.readLine().toInt()

    repeat(m) {
        val (s, f) = reader.readLine().split(" ").map { it.toInt() }
        // Если начало и конец совпадают, подъём равен 0
        if (s == f) {
            writer.write("0")
            writer.newLine()
        } else if (s < f) {
            // При движении слева направо суммарный подъём = forward[f-1] - forward[s-1]
            writer.write("${forward[f - 1] - forward[s - 1]}")
            writer.newLine()
        } else {
            // При движении справа налево суммарный подъём = rev[s-1] - rev[f - 1]
            writer.write("${rev[s - 1] - rev[f - 1]}")
            writer.newLine()
        }
    }

    writer.flush()
    reader.close()
    writer.close()
}