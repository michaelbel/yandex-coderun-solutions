import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import java.util.Arrays
import kotlin.math.min

// Константы для оптимизации памяти
// Максимальное кол-во элементов N=200,000. Глубина дерева ~18.
// Суммарное количество линий во всех узлах Segment Tree <= N * log N.
// 200,000 * 18 ~ 3,600,000. Берем с запасом 4,500,000.
private const val MAX_HULL_SIZE = 4_500_000
private const val MAX_TREE_NODES = 800_005 // 4 * N

// Глобальные массивы для хранения Convex Hulls (вместо объектов)
private val treeK = LongArray(MAX_HULL_SIZE)
private val treeB = LongArray(MAX_HULL_SIZE)
private val nodeStart = IntArray(MAX_TREE_NODES) // Индекс начала hull для узла в treeK/treeB
private val nodeSize = IntArray(MAX_TREE_NODES)  // Размер hull для узла
private var globalPoolIndex = 0 // Указатель свободного места в treeK/treeB

// Данные складов (отсортированные)
private lateinit var sortedS: LongArray
private lateinit var sortedP: LongArray

// Бесконечность
private const val INF = Long.MAX_VALUE

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    var tokenizer = StringTokenizer("")
    fun next(): String {
        while (!tokenizer.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer.nextToken()
    }
    fun nextInt(): Int = next().toInt()
    fun nextLong(): Long = next().toLong()

    val nStr = next()
    if (nStr == "") return
    val n = nStr.toInt()
    val m = nextInt()

    // Класс для хранения исходных данных для сортировки
    class Warehouse(val s: Long, val p: Long, val originalIdx: Int)

    val warehouses = Array(n) { i ->
        Warehouse(nextLong(), nextLong(), i)
    }

    // Сортируем склады по координате S.
    // Это критически важно: slope = -2*s будет строго убывать.
    // distinct coordinates -> строгое убывание.
    warehouses.sortBy { it.s }

    // Заполняем массивы и карту индексов
    sortedS = LongArray(n)
    sortedP = LongArray(n)
    val originalToSorted = IntArray(n)

    for (i in 0 until n) {
        sortedS[i] = warehouses[i].s
        sortedP[i] = warehouses[i].p
        originalToSorted[warehouses[i].originalIdx] = i
    }

    // Функция пересечения прямых для CHT
    // Возвращает X-координату пересечения.
    // k1 > k2 (так как slopes убывают)
    // x = (b2 - b1) / (k1 - k2)
    fun intersect(k1: Long, b1: Long, k2: Long, b2: Long): Double {
        return (b2 - b1).toDouble() / (k1 - k2).toDouble()
    }

    // Построение дерева отрезков
    fun build(v: Int, tl: Int, tr: Int) {
        if (tl == tr) {
            // Лист
            val sVal = sortedS[tl]
            val pVal = sortedP[tl]
            // Формула: cost = p + (s-c)^2 = p + s^2 - 2sc + c^2
            // Line: y = kx + b
            // k = -2s
            // b = p + s^2
            // x = c
            // Потом добавим c^2 к ответу
            val k = -2 * sVal
            val b = pVal + sVal * sVal
            
            nodeStart[v] = globalPoolIndex
            nodeSize[v] = 1
            treeK[globalPoolIndex] = k
            treeB[globalPoolIndex] = b
            globalPoolIndex++
        } else {
            val tm = (tl + tr) / 2
            val leftChild = 2 * v
            val rightChild = 2 * v + 1
            
            build(leftChild, tl, tm)
            build(rightChild, tm + 1, tr)

            // Merge (Слияние)
            // Так как мы отсортировали склады по S (возрастание),
            // то наклоны k = -2S убывают.
            // В левом поддереве индексы меньше -> S меньше -> k больше.
            // В правом поддереве индексы больше -> S больше -> k меньше.
            // Значит, все линии левого ребенка имеют наклон БОЛЬШЕ, чем линии правого.
            // Нам нужно просто склеить списки и пройтись алгоритмом Monotone Chain (построение оболочки).
            
            val startL = nodeStart[leftChild]
            val sizeL = nodeSize[leftChild]
            val startR = nodeStart[rightChild]
            val sizeR = nodeSize[rightChild]
            
            // Резервируем место под результат
            val myStart = globalPoolIndex
            nodeStart[v] = myStart
            
            // Указатель текущего размера новой оболочки
            var top = 0 
            // Временный указатель в глобальном массиве (куда пишем)
            var ptr = myStart

            // Helper для добавления линии
            fun addLine(nk: Long, nb: Long) {
                while (top >= 2) {
                    val k1 = treeK[ptr - 2]
                    val b1 = treeB[ptr - 2]
                    val k2 = treeK[ptr - 1]
                    val b2 = treeB[ptr - 1]
                    
                    // Проверка: нужна ли линия 2?
                    // x1 (пересечение 1 и 2)
                    // x2 (пересечение 2 и new)
                    // Если x1 >= x2, то линия 2 не нужна (она покрыта 1 и new)
                    
                    // Используем double для скорости и избежания переполнения при умножении
                    val x1 = (b2 - b1).toDouble() / (k1 - k2).toDouble()
                    val x2 = (nb - b2).toDouble() / (k2 - nk).toDouble()
                    
                    if (x1 >= x2) {
                        top--
                        ptr--
                    } else {
                        break
                    }
                }
                treeK[ptr] = nk
                treeB[ptr] = nb
                ptr++
                top++
            }

            // Сначала добавляем линии левого ребенка
            for (i in 0 until sizeL) {
                addLine(treeK[startL + i], treeB[startL + i])
            }
            // Потом правого
            for (i in 0 until sizeR) {
                addLine(treeK[startR + i], treeB[startR + i])
            }
            
            nodeSize[v] = top
            globalPoolIndex += top
        }
    }

    build(1, 0, n - 1)

    // Запрос минимума в конкретном узле для точки x
    fun queryNode(v: Int, x: Long): Long {
        val start = nodeStart[v]
        val size = nodeSize[v]
        if (size == 0) return INF // Не должно случаться
        
        // Тернарный поиск или Бинарный поиск по производной
        // Функция выпуклая (hull lines sorted by slope descending -> lower envelope).
        // Значения сначала уменьшаются, потом растут. Нам нужен минимум.
        
        var l = 0
        var r = size - 1
        while (l < r) {
            val mid = (l + r) / 2
            val val1 = treeK[start + mid] * x + treeB[start + mid]
            val val2 = treeK[start + mid + 1] * x + treeB[start + mid + 1]
            if (val1 < val2) {
                r = mid
            } else {
                l = mid + 1
            }
        }
        return treeK[start + l] * x + treeB[start + l]
    }

    // Запрос на диапазоне [l, r]
    // qL, qR - границы запроса
    fun queryTree(v: Int, tl: Int, tr: Int, qL: Int, qR: Int, x: Long): Long {
        if (qL > qR) return INF
        if (qL == tl && qR == tr) {
            return queryNode(v, x)
        }
        val tm = (tl + tr) / 2
        // Оптимизация: не заходим в ветки, которые не пересекаются
        val minLeft = if (qL <= tm) 
            queryTree(2 * v, tl, tm, qL, min(qR, tm), x) 
        else INF
        
        val minRight = if (qR > tm) 
            queryTree(2 * v + 1, tm + 1, tr, kotlin.math.max(qL, tm + 1), qR, x) 
        else INF
        
        return if (minLeft < minRight) minLeft else minRight
    }

    // Обработка запросов
    // Для оптимизации не будем создавать массивы каждый раз,
    // но excludedIdx зависит от M.
    // Max excluded count sum is 400,000.
    
    // Массив для сортировки запрещенных индексов
    val forbidden = IntArray(n + 1) // с запасом

    for (i in 0 until m) {
        val c = nextLong()
        val k = nextInt()
        
        // Считываем запрещенные индексы и переводим их в sorted-space
        for (j in 0 until k) {
            val rawIdx = nextInt() - 1 // 0-based
            forbidden[j] = originalToSorted[rawIdx]
        }
        
        // Сортируем только используемую часть
        Arrays.sort(forbidden, 0, k)
        
        var currentMin = INF
        
        var lastAllowed = -1
        
        // Итерируемся по запрещенным и запрашиваем промежутки
        for (j in 0 until k) {
            val badIdx = forbidden[j]
            if (badIdx > lastAllowed + 1) {
                val res = queryTree(1, 0, n - 1, lastAllowed + 1, badIdx - 1, c)
                if (res < currentMin) currentMin = res
            }
            lastAllowed = badIdx
        }
        // Хвост после последнего запрещенного
        if (lastAllowed < n - 1) {
            val res = queryTree(1, 0, n - 1, lastAllowed + 1, n - 1, c)
            if (res < currentMin) currentMin = res
        }
        
        // Итоговая стоимость: min(kx + b) + c^2
        val ans = currentMin + c * c
        writer.write(ans.toString())
        writer.newLine()
    }

    writer.flush()
}
