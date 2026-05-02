import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import kotlin.math.min
import kotlin.math.max

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    var tokenizer = StringTokenizer("")

    fun next(): String {
        while (!tokenizer.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer.nextToken()
    }

    fun nextInt(): Int? {
        val s = try { next() } catch (e: Exception) { return null }
        if (s == "") return null
        return s.toInt()
    }

    val tStr = next()
    if (tStr == "") return
    val t = tStr.toInt()
    
    val out = StringBuilder()

    for (i in 0 until t) {
        val n = nextInt()!!
        val d = nextInt()!!
        solve(n, d, out)
    }
    print(out)
}

fun solve(n: Int, d: Int, out: StringBuilder) {
    val N = n * n
    
    // Краевой случай для n=1
    if (n == 1) {
        if (d == 0) {
            out.append("Won\n")
            out.append("o\n")
        } else {
            out.append("Lost\n")
        }
        return
    }

    // Проверка допустимого диапазона диаметра
    // Максимальный диаметр для графа из N вершин - это путь длины N-1
    if (d < 1 || d > N - 1) {
        out.append("Lost\n")
        return
    }

    // Минимальный возможный диаметр для решетки n*n
    // Для нечетных n центр в (n/2, n/2), макс расстояние (n-1) + (n-1) = 2n-2
    // Для четных n "центр" смещен, макс расстояние 2n-1
    val minD = if (n % 2 == 1) (2 * n - 2) else (2 * n - 1)
    if (d < minD) {
        out.append("Lost\n")
        return
    }

    // Построение пути "змейкой" для удобного выбора связной цепи длины d
    val snake = IntArray(N)
    var idx = 0
    for (r in 0 until n) {
        if (r % 2 == 0) {
            for (c in 0 until n) {
                snake[idx++] = r * n + c
            }
        } else {
            for (c in n - 1 downTo 0) {
                snake[idx++] = r * n + c
            }
        }
    }

    // Массивы для BFS и построения дерева
    val posInPath = IntArray(N)
    val slack = IntArray(N)
    val parent = IntArray(N)
    val q = IntArray(N * 2 + 100) // Буфер для очереди

    // Функция попытки построения дерева с заданным началом главной цепи
    fun tryBuild(startIdx: Int): Boolean {
        // Сброс массивов
        for (k in 0 until N) {
            posInPath[k] = -1
            slack[k] = -1
            parent[k] = -1
        }
        
        var qHead = 0
        var qTail = 0
        
        // Инициализация главной цепи (хребта) длины d
        for (i in 0..d) {
            val u = snake[startIdx + i]
            posInPath[u] = i
            // Вычисляем допустимый "запас" (slack) для ответвлений.
            // Если мы отойдем от вершины u на расстояние k, то
            // расстояние до начала цепи станет i + k, до конца (d-i) + k.
            // Оба должны быть <= d => k <= d - i и k <= i.
            val s = min(i, d - i)
            slack[u] = s
            q[qTail++] = u
        }
        
        // BFS для присоединения остальных вершин
        while (qHead < qTail) {
            val u = q[qHead++]
            val s = slack[u]
            
            // Если запаса нет, дальше идти нельзя
            if (s <= 0) continue
            
            val ns = s - 1
            val r = u / n
            val c = u % n
            
            // Проверка 4-х соседей
            // Верх
            if (r > 0) {
                val v = u - n
                if (posInPath[v] == -1) {
                    // Если вершина еще не в дереве или нашли путь с большим запасом (для жадности)
                    // В данном случае, так как BFS идет по уровням запаса, первый заход оптимален
                    if (slack[v] == -1) {
                        slack[v] = ns
                        parent[v] = u
                        q[qTail++] = v
                    }
                }
            }
            // Низ
            if (r < n - 1) {
                val v = u + n
                if (posInPath[v] == -1) {
                    if (slack[v] == -1) {
                        slack[v] = ns
                        parent[v] = u
                        q[qTail++] = v
                    }
                }
            }
            // Лево
            if (c > 0) {
                val v = u - 1
                if (posInPath[v] == -1) {
                    if (slack[v] == -1) {
                        slack[v] = ns
                        parent[v] = u
                        q[qTail++] = v
                    }
                }
            }
            // Право
            if (c < n - 1) {
                val v = u + 1
                if (posInPath[v] == -1) {
                    if (slack[v] == -1) {
                        slack[v] = ns
                        parent[v] = u
                        q[qTail++] = v
                    }
                }
            }
        }
        
        // Проверяем, все ли вершины присоединены
        for (k in 0 until N) {
            if (posInPath[k] == -1 && parent[k] == -1) return false
        }
        
        // Решение найдено, формируем вывод
        out.append("Won\n")
        
        val dim = 2 * n - 1
        val canvas = CharArray(dim * dim) { '.' }
        
        // Рисуем комнаты
        for (r in 0 until n) {
            for (c in 0 until n) {
                canvas[(2 * r) * dim + (2 * c)] = 'o'
            }
        }
        
        // Функция отрисовки ребра
        fun connect(u: Int, v: Int) {
            val r1 = u / n
            val c1 = u % n
            val r2 = v / n
            val c2 = v % n
            
            val rMin = min(r1, r2)
            val cMin = min(c1, c2)
            
            if (r1 == r2) {
                // Горизонтальное ребро
                canvas[(2 * r1) * dim + (2 * cMin + 1)] = '-'
            } else {
                // Вертикальное ребро
                canvas[(2 * rMin + 1) * dim + (2 * c1)] = '|'
            }
        }
        
        // Рисуем ребра цепи
        for (i in 0 until d) {
            connect(snake[startIdx + i], snake[startIdx + i + 1])
        }
        
        // Рисуем ребра к родителям для остальных вершин
        for (k in 0 until N) {
            if (posInPath[k] == -1) {
                connect(k, parent[k])
            }
        }
        
        // Записываем результат в буфер вывода
        for (r in 0 until dim) {
            for (c in 0 until dim) {
                out.append(canvas[r * dim + c])
            }
            out.append('\n')
        }
        
        return true
    }

    // Оптимизация перебора стартовой позиции
    val maxStart = N - 1 - d
    // Начинаем поиск с середины доступного диапазона, так как там наиболее вероятно
    // успешное распределение "запаса" во все стороны
    val centerStart = max(0, min(maxStart, (N / 2) - (d / 2)))
    
    // Ограничиваем окно поиска эвристически, чтобы не делать O(N^2) проходов,
    // хотя при N=50 это допустимо, но тесты могут быть жесткими.
    val W = min(maxStart, 4 * n + 80) 
    
    if (tryBuild(centerStart)) return
    
    for (delta in 1..W) {
        val low = centerStart - delta
        if (low >= 0) {
            if (tryBuild(low)) return
        }
        val high = centerStart + delta
        if (high <= maxStart) {
            if (tryBuild(high)) return
        }
    }
    
    out.append("Lost\n")
}
