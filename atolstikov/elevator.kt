import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.StringTokenizer
import java.util.Arrays
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

// --- Основной класс решения ---
fun main() {
    val reader = FastReader()
    val out = PrintWriter(System.out)

    // Чтение n и t
    val nStr = reader.next()
    if (nStr == null) return
    val n = nStr.toInt()
    val tMove = reader.next()!!.toLong()

    // Массивы данных всех пассажиров
    val tArr = LongArray(n)
    val sArr = LongArray(n)
    val dArr = LongArray(n)
    val dirArr = IntArray(n) // 1 для вверх, -1 для вниз

    // Списки для индексов пассажиров по направлениям
    // Храним кортеж (floor, key, id) в упакованном виде или через параллельные массивы
    // Для экономии памяти и удобства сортировки создадим Entry класс, но для скорости - массивы
    val upEntries = ArrayList<Entry>(n)
    val downEntries = ArrayList<Entry>(n)

    for (i in 0 until n) {
        val ti = reader.next()!!.toLong()
        val si = reader.next()!!.toLong()
        val di = reader.next()!!.toLong()

        tArr[i] = ti
        sArr[i] = si
        dArr[i] = di

        if (di > si) {
            dirArr[i] = 1
            val key = ti - si * tMove
            upEntries.add(Entry(si, key, i))
        } else {
            dirArr[i] = -1
            val key = ti + si * tMove
            downEntries.add(Entry(si, key, i))
        }
    }

    // Глобальные массивы состояния
    val served = BooleanArray(n)
    val waitTimes = LongArray(n)
    
    // Позиции пассажиров в листьях дерева отрезков
    val upFloorPos = IntArray(n) { -1 }
    val downFloorPos = IntArray(n) { -1 }

    // Построение индексов
    val upIndex = if (upEntries.isNotEmpty()) DirIndex(upEntries, upFloorPos) else null
    val downIndex = if (downEntries.isNotEmpty()) DirIndex(downEntries, downFloorPos) else null

    var curTime: Long = 0
    var curFloor: Long = 1
    var ptrTime = 0

    // Симуляция
    while (true) {
        // Пропускаем уже обслуженных пассажиров, чтобы найти инициатора следующего рейса
        while (ptrTime < n && served[ptrTime]) {
            ptrTime++
        }
        if (ptrTime >= n) break

        val initiator = ptrTime
        
        // Лифт свободен, едет к инициатору
        val callTime = tArr[initiator]
        // Если лифт освободился раньше вызова, ждем вызова. Иначе время = текущее
        if (curTime < callTime) {
            curTime = callTime
        }

        val startFloor = sArr[initiator]
        val destFloor = dArr[initiator]

        // Время поездки пустого лифта до пассажира
        val travelEmpty = abs(startFloor - curFloor) * tMove
        val pickupTime = curTime + travelEmpty
        
        // Обслуживаем инициатора
        waitTimes[initiator] = pickupTime - callTime
        served[initiator] = true
        
        // Обновляем дерево отрезков: убираем инициатора из очереди
        if (dirArr[initiator] == 1) {
            upIndex?.refreshFloor(upFloorPos[initiator], served, upEntries)
        } else {
            downIndex?.refreshFloor(downFloorPos[initiator], served, downEntries)
        }

        curTime = pickupTime
        curFloor = startFloor

        // Начинаем рейс с пассажиром
        if (destFloor > startFloor) {
            // Едем ВВЕРХ
            var currentEnd = destFloor
            val t0 = curTime
            val f0 = curFloor
            val limitC = t0 - f0 * tMove

            if (upIndex != null) {
                // Ищем попутчиков в диапазоне [f0, currentEnd] у которых key <= limitC
                val l = upIndex.lowerBound(f0)
                while (true) {
                    val r = upIndex.upperBound(currentEnd) - 1
                    if (l > r) break
                    
                    // Извлекаем одного подходящего пассажира
                    val id = upIndex.extract(l, r, limitC, served, upEntries) ?: break
                    
                    // Вычисляем время посадки попутчика
                    val pTime = t0 + (sArr[id] - f0) * tMove
                    waitTimes[id] = pTime - tArr[id]
                    served[id] = true
                    
                    // Если попутчик едет дальше, удлиняем маршрут
                    if (dArr[id] > currentEnd) {
                        currentEnd = dArr[id]
                    }
                }
            }
            curTime = t0 + (currentEnd - f0) * tMove
            curFloor = currentEnd

        } else {
            // Едем ВНИЗ
            var currentEnd = destFloor
            val t0 = curTime
            val f0 = curFloor
            val limitC = t0 + f0 * tMove

            if (downIndex != null) {
                // Ищем попутчиков в диапазоне [currentEnd, f0] (в индексе floors возрастают, поэтому диапазон по индексу)
                // Диапазон этажей [currentEnd, f0], в массиве floors ищем индексы
                val r = downIndex.upperBound(f0) - 1
                while (true) {
                    val l = downIndex.lowerBound(currentEnd)
                    if (l > r) break

                    val id = downIndex.extract(l, r, limitC, served, downEntries) ?: break

                    val pTime = t0 + (f0 - sArr[id]) * tMove
                    waitTimes[id] = pTime - tArr[id]
                    served[id] = true

                    if (dArr[id] < currentEnd) {
                        currentEnd = dArr[id]
                    }
                }
            }
            curTime = t0 + (f0 - currentEnd) * tMove
            curFloor = currentEnd
        }
    }

    // Вывод результатов
    for (i in 0 until n) {
        out.println(waitTimes[i])
    }
    out.flush()
}

// --- Вспомогательные структуры ---

class Entry(val floor: Long, val key: Long, val id: Int) : Comparable<Entry> {
    override fun compareTo(other: Entry): Int {
        if (this.floor != other.floor) {
            return this.floor.compareTo(other.floor)
        }
        return this.key.compareTo(other.key)
    }
}

class DirIndex(entriesRaw: ArrayList<Entry>, globalPos: IntArray) {
    private val INF: Long = Long.MAX_VALUE / 2
    
    val floors: LongArray
    private val ptrs: IntArray
    private val ends: IntArray
    private val m: Int
    
    // Дерево отрезков
    private val size: Int
    private val tree: LongArray

    init {
        // Сортируем записи: сначала по этажу, потом по ключу (времени)
        entriesRaw.sort()
        
        // Сжимаем этажи: находим уникальные этажи и диапазоны для них в sortedEntries
        val tempFloors = ArrayList<Long>()
        val tempStarts = ArrayList<Int>()
        val tempEnds = ArrayList<Int>()
        
        if (entriesRaw.isNotEmpty()) {
            var currentFloor = entriesRaw[0].floor
            var startIdx = 0
            for (i in 1 until entriesRaw.size) {
                if (entriesRaw[i].floor != currentFloor) {
                    tempFloors.add(currentFloor)
                    tempStarts.add(startIdx)
                    tempEnds.add(i)
                    currentFloor = entriesRaw[i].floor
                    startIdx = i
                }
            }
            tempFloors.add(currentFloor)
            tempStarts.add(startIdx)
            tempEnds.add(entriesRaw.size)
        }
        
        m = tempFloors.size
        floors = LongArray(m)
        ptrs = IntArray(m)
        ends = IntArray(m)
        
        for (i in 0 until m) {
            floors[i] = tempFloors[i]
            ptrs[i] = tempStarts[i]
            ends[i] = tempEnds[i]
        }
        
        // Заполняем globalPos (mapping id -> leaf index) для быстрого обновления
        for (i in 0 until m) {
            for (k in ptrs[i] until ends[i]) {
                globalPos[entriesRaw[k].id] = i
            }
        }

        // Строим дерево отрезков
        var sz = 1
        while (sz < m) sz = sz shl 1
        size = sz
        tree = LongArray(2 * size) { INF }

        // Инициализация листьев: берем ключ ПЕРВОГО пассажира на этаже
        for (i in 0 until m) {
            val p = ptrs[i]
            if (p < ends[i]) {
                tree[size + i] = entriesRaw[p].key
            }
        }
        // Строим дерево
        for (i in size - 1 downTo 1) {
            tree[i] = min(tree[2 * i], tree[2 * i + 1])
        }
    }

    // Обновить лист (когда пассажир обслужен)
    // Передвигаем указатель ptrs[floorIdx] к следующему необслуженному
    fun refreshFloor(floorIdx: Int, served: BooleanArray, entries: ArrayList<Entry>) {
        var p = ptrs[floorIdx]
        val e = ends[floorIdx]
        
        // Пропускаем обслуженных (хотя обычно вызываем для конкретного id, но массив общий)
        while (p < e && served[entries[p].id]) {
            p++
        }
        ptrs[floorIdx] = p
        
        val value = if (p < e) entries[p].key else INF
        updateLeaf(floorIdx, value)
    }

    private fun updateLeaf(idx: Int, value: Long) {
        var pos = size + idx
        tree[pos] = value
        pos = pos shr 1
        while (pos >= 1) {
            val newVal = min(tree[2 * pos], tree[2 * pos + 1])
            if (tree[pos] == newVal) break
            tree[pos] = newVal
            pos = pos shr 1
        }
    }

    private fun getMin(l: Int, r: Int): Long {
        var res = INF
        var left = l + size
        var right = r + size
        while (left <= right) {
            if (left % 2 == 1) {
                res = min(res, tree[left])
                left++
            }
            if (right % 2 == 0) {
                res = min(res, tree[right])
                right--
            }
            left = left shr 1
            right = right shr 1
        }
        return res
    }

    // Найти индекс листа в диапазоне [ql, qr], значение в котором <= limit
    // Возвращает -1, если такого нет
    private fun findFirstValid(node: Int, nl: Int, nr: Int, ql: Int, qr: Int, limit: Long): Int {
        if (nl > qr || nr < ql) return -1
        if (tree[node] > limit) return -1
        if (nl == nr) return nl
        
        val mid = (nl + nr) shr 1
        // Сначала пытаемся найти слева
        val leftRes = findFirstValid(2 * node, nl, mid, ql, qr, limit)
        if (leftRes != -1) return leftRes
        return findFirstValid(2 * node + 1, mid + 1, nr, ql, qr, limit)
    }

    // Извлекает одного пассажира, подходящего под условие
    fun extract(l: Int, r: Int, limit: Long, served: BooleanArray, entries: ArrayList<Entry>): Int? {
        if (m == 0 || l > r) return null
        
        // Быстрая проверка минимума на диапазоне
        if (getMin(l, r) > limit) return null

        // Ищем конкретный лист
        val leafIdx = findFirstValid(1, 0, size - 1, l, r, limit)
        if (leafIdx == -1) return null

        // Мы нашли лист, где minKey <= limit.
        // Берем текущего пассажира
        val p = ptrs[leafIdx]
        // На всякий случай проверка (хотя логика refresh гарантирует актуальность)
        if (p >= ends[leafIdx]) return null // Не должно случаться при корректном tree
        
        val id = entries[p].id
        
        // Сразу обновляем структуру, сдвигая указатель на этом этаже
        // Мы помечаем served снаружи, но здесь сдвигаем локальный указатель
        ptrs[leafIdx] = p + 1
        refreshFloor(leafIdx, served, entries)
        
        return id
    }
    
    // Бинарный поиск: первый индекс, где floor >= x
    fun lowerBound(x: Long): Int {
        var l = 0
        var r = m
        while (l < r) {
            val mid = (l + r) ushr 1
            if (floors[mid] < x) {
                l = mid + 1
            } else {
                r = mid
            }
        }
        return l
    }
    
    // Бинарный поиск: первый индекс, где floor > x
    fun upperBound(x: Long): Int {
        var l = 0
        var r = m
        while (l < r) {
            val mid = (l + r) ushr 1
            if (floors[mid] <= x) {
                l = mid + 1
            } else {
                r = mid
            }
        }
        return l
    }
}

class FastReader {
    var br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
    var st: StringTokenizer? = null

    fun next(): String? {
        while (st == null || !st!!.hasMoreTokens()) {
            val line = br.readLine() ?: return null
            st = StringTokenizer(line)
        }
        return st!!.nextToken()
    }
}
