import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

// Константа для представления бесконечности (большое число)
private const val INF = Int.MAX_VALUE / 2 // Используем половину MAX_VALUE во избежание переполнения при сложении

// Класс для представления состояния в очереди (строка, столбец, направление прибытия)
private data class State(val r: Int, val c: Int, val dir: Int)

fun main(args: Array<String>) {
    // Используем BufferedReader для быстрого чтения
    val reader = BufferedReader(InputStreamReader(System.`in`))
    // Используем BufferedWriter для быстрого вывода
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем размеры поля H (высота) и W (ширина)
    val (h, w) = reader.readLine().split(" ").map { it.toInt() }

    // Создаем 2D массив для хранения информации о блокированных клетках
    // true - клетка заблокирована ('X'), false - свободна ('.')
    val isBlocked = Array(h) { BooleanArray(w) }
    for (i in 0 until h) {
        val line = reader.readLine()
        for (j in 0 until w) {
            isBlocked[i][j] = line[j] == 'X'
        }
    }

    // Читаем координаты старта (sx, sy)
    // Входные координаты (sx, sy) 1-based, от нижнего левого угла
    val (startColInput, startRowInput) = reader.readLine().split(" ").map { it.toInt() }
    // Переводим в 0-based координаты (row, col) от верхнего левого угла
    val startRow = h - startRowInput
    val startCol = startColInput - 1

    // Читаем координаты цели (tx, ty)
    val (targetColInput, targetRowInput) = reader.readLine().split(" ").map { it.toInt() }
    // Переводим в 0-based координаты (row, col) от верхнего левого угла
    val targetRow = h - targetRowInput
    val targetCol = targetColInput - 1

    // Массивы для смещений по 8 направлениям (горизонтали, вертикали, диагонали)
    // Порядок: СЗ, С, СВ, З, В, ЮЗ, Ю, ЮВ
    val dr = intArrayOf(-1, -1, -1, 0, 0, 1, 1, 1)
    val dc = intArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)

    // 3D массив для хранения минимального количества поворотов
    // dist[r][c][d] - мин. поворотов, чтобы прибыть в клетку (r, c) с направлением d
    val dist = Array(h) { Array(w) { IntArray(8) { INF } } }

    // Используем двустороннюю очередь (Deque) для 0-1 BFS
    val deque = ArrayDeque<State>()

    // Инициализация BFS:
    // Трактор в начале стоит на месте. Первый ход в любом направлении требует 1 поворота.
    // Добавляем все возможные первые шаги в очередь.
    for (d in 0 until 8) {
        val nr = startRow + dr[d]
        val nc = startCol + dc[d]

        // Проверяем, что новая клетка (nr, nc) находится в пределах поля
        if (nr >= 0 && nr < h && nc >= 0 && nc < w) {
            // Проверяем, что новая клетка не заблокирована
            if (!isBlocked[nr][nc]) {
                // Стоимость первого шага - 1 поворот
                if (dist[nr][nc][d] > 1) {
                    dist[nr][nc][d] = 1
                    // Добавляем состояние в конец очереди (вес ребра = 1)
                    deque.addLast(State(nr, nc, d))
                }
            }
        }
    }

    // Основной цикл 0-1 BFS
    while (deque.isNotEmpty()) {
        // Извлекаем состояние из начала очереди
        val (r, c, arrivedDir) = deque.pollFirst()
        // Получаем текущее количество поворотов для этого состояния
        val currentTurns = dist[r][c][arrivedDir]

        // Исследуем следующие возможные ходы из клетки (r, c)

        // 1. Двигаться прямо (в том же направлении arrivedDir)
        val nextDirStraight = arrivedDir
        val nrStraight = r + dr[nextDirStraight]
        val ncStraight = c + dc[nextDirStraight]

        // Проверяем границы и препятствия для прямого хода
        if (nrStraight >= 0 && nrStraight < h && ncStraight >= 0 && ncStraight < w && !isBlocked[nrStraight][ncStraight]) {
            // Стоимость хода прямо - 0 дополнительных поворотов
            val newTurnsStraight = currentTurns
            // Если нашли путь с меньшим или равным числом поворотов (важно для 0-1 BFS)
            if (newTurnsStraight < dist[nrStraight][ncStraight][nextDirStraight]) {
                dist[nrStraight][ncStraight][nextDirStraight] = newTurnsStraight
                // Добавляем состояние в НАЧАЛО очереди (вес ребра = 0)
                deque.addFirst(State(nrStraight, ncStraight, nextDirStraight))
            }
        }

        // 2. Повернуть и двигаться (в направлениях, отличных от arrivedDir)
        for (nextDirTurn in 0 until 8) {
            if (nextDirTurn == arrivedDir) continue // Пропускаем движение прямо

            val nrTurn = r + dr[nextDirTurn]
            val ncTurn = c + dc[nextDirTurn]

            // Проверяем границы и препятствия для хода с поворотом
            if (nrTurn >= 0 && nrTurn < h && ncTurn >= 0 && ncTurn < w && !isBlocked[nrTurn][ncTurn]) {
                // Стоимость хода с поворотом - 1 дополнительный поворот
                val newTurnsTurn = currentTurns + 1
                 // Если нашли путь с меньшим числом поворотов
                if (newTurnsTurn < dist[nrTurn][ncTurn][nextDirTurn]) {
                    dist[nrTurn][ncTurn][nextDirTurn] = newTurnsTurn
                     // Добавляем состояние в КОНЕЦ очереди (вес ребра = 1)
                    deque.addLast(State(nrTurn, ncTurn, nextDirTurn))
                }
            }
        }
    }

    // Ищем минимальное количество поворотов для достижения целевой клетки (targetRow, targetCol)
    // не важно, с каким направлением мы туда прибыли
    var minTurns = INF
    for (d in 0 until 8) {
        minTurns = minOf(minTurns, dist[targetRow][targetCol][d])
    }

    // Выводим результат
    if (minTurns == INF) {
        // Если минимальное число поворотов осталось бесконечным, путь не найден
        writer.write("-1")
    } else {
        // Иначе выводим минимальное найденное количество поворотов
        writer.write(minTurns.toString())
    }
    writer.newLine() // Не забываем перевод строки в конце вывода
    writer.flush() // Сбрасываем буфер вывода
    reader.close() // Закрываем ридер
    writer.close() // Закрываем врайтер
}
