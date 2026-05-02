import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.min
import kotlin.math.max

// Функция решета для поиска простых чисел до maxValue.
fun sieve(maxValue: Int): IntArray {
    val isPrime = BooleanArray(maxValue + 1) { true }
    isPrime[0] = false
    isPrime[1] = false
    for (i in 2..maxValue) {
        if (isPrime[i]) {
            var j = 2 * i
            while (j <= maxValue) {
                isPrime[j] = false
                j += i
            }
        }
    }
    val primes = ArrayList<Int>()
    for (i in 2..maxValue) {
        if (isPrime[i]) {
            primes.add(i)
        }
    }
    return primes.toIntArray()
}

// Функция разложения числа на простые множители
fun factorize(num: Int, primes: IntArray): ArrayList<Pair<Int, Int>> {
    val factors = ArrayList<Pair<Int, Int>>()
    var x = num
    if (x <= 1) return factors
    for (p in primes) {
        if (p * p > x) break
        if (x % p == 0) {
            var cnt = 0
            while (x % p == 0) {
                cnt++
                x /= p
            }
            factors.add(Pair(p, cnt))
        }
    }
    if (x > 1) {
        factors.add(Pair(x, 1))
    }
    return factors
}

// Простая реализация сегментного дерева для диапазонного минимума по массиву CA
class SegmentTree(val n: Int) {
    val size: Int
    val tree: LongArray

    init {
        var sz = 1
        while (sz < n) sz *= 2
        size = sz
        tree = LongArray(2 * size) { Long.MAX_VALUE }
    }

    fun build(arr: LongArray) {
        for (i in 0 until n) {
            tree[size + i] = arr[i]
        }
        for (i in size - 1 downTo 1) {
            tree[i] = min(tree[2 * i], tree[2 * i + 1])
        }
    }

    // Запрос минимума на полуоткрытом отрезке [l, r)
    fun query(l: Int, r: Int): Long {
        var res = Long.MAX_VALUE
        var left = l + size
        var right = r + size
        while (left < right) {
            if (left and 1 == 1) {
                res = min(res, tree[left])
                left++
            }
            if (right and 1 == 1) {
                right--
                res = min(res, tree[right])
            }
            left /= 2
            right /= 2
        }
        return res
    }
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val sb = StringBuilder()
    val st = StringTokenizer(reader.readLine())
    val n = st.nextToken().toInt()
    val m = st.nextToken().toInt()
    val q = st.nextToken().toInt()

    // Считываем массив A длины n+1 и B длины m+1
    val A = IntArray(n + 1)
    val lineA = reader.readLine().split(" ")
    for (i in 0..n) {
        A[i] = lineA[i].toInt()
    }
    val B = IntArray(m + 1)
    val lineB = reader.readLine().split(" ")
    for (j in 0..m) {
        B[j] = lineB[j].toInt()
    }
    // Считываем стоимости выбора префиксов из A и B
    val CA = LongArray(n + 1)
    val lineCA = reader.readLine().split(" ")
    for (i in 0..n) {
        CA[i] = lineCA[i].toLong()
    }
    val CB = LongArray(m + 1)
    val lineCB = reader.readLine().split(" ")
    for (j in 0..m) {
        CB[j] = lineCB[j].toLong()
    }

    // Предвычисляем простые числа для факторизации (до sqrt(1e9))
    val primes = sieve(31623)

    // Для массивов A и B будем сохранять для каждого простого p и требуемой степени e (1..maxE)
    // наименьший индекс, когда накопленный показатель для p достигается.
    val MAX_EXP = 31
    val thresholdA = HashMap<Int, IntArray>()
    val thresholdB = HashMap<Int, IntArray>()
    // Инициализируем «накопленные счетчики»
    val countA = HashMap<Int, Int>()
    for (i in 0..n) {
        if (A[i] != 1) {
            val facs = factorize(A[i], primes)
            for ((p, exp) in facs) {
                val prev = countA.getOrDefault(p, 0)
                val now = prev + exp
                countA[p] = now
                // Инициализируем массив порогов для p, если ещё не создан
                if (!thresholdA.containsKey(p)) {
                    thresholdA[p] = IntArray(MAX_EXP + 1) { n + 1 }
                }
                val arr = thresholdA[p]!!
                // Для всех новых степеней от prev+1 до now (если не превышают MAX_EXP)
                var newE = prev + 1
                while (newE <= now && newE <= MAX_EXP) {
                    if (arr[newE] == n + 1) {
                        arr[newE] = i
                    }
                    newE++
                }
            }
        }
    }
    val countB = HashMap<Int, Int>()
    for (j in 0..m) {
        if (B[j] != 1) {
            val facs = factorize(B[j], primes)
            for ((p, exp) in facs) {
                val prev = countB.getOrDefault(p, 0)
                val now = prev + exp
                countB[p] = now
                if (!thresholdB.containsKey(p)) {
                    thresholdB[p] = IntArray(MAX_EXP + 1) { m + 1 }
                }
                val arr = thresholdB[p]!!
                var newE = prev + 1
                while (newE <= now && newE <= MAX_EXP) {
                    if (arr[newE] == m + 1) {
                        arr[newE] = j
                    }
                    newE++
                }
            }
        }
    }

    // Предвычисляем массив bestB: bestB[r] = минимум по CB[0..r-1] для r от 1 до m+1
    val bestB = LongArray(m + 2)
    bestB[1] = CB[0]
    for (r in 2..(m + 1)) {
        bestB[r] = min(bestB[r - 1], CB[r - 1])
    }

    // Построение сегментного дерева по массиву CA для запросов минимума
    val segTree = SegmentTree(n + 1)
    segTree.build(CA)

    // Для каждого запроса:
    for (qi in 1..q) {
        val line = reader.readLine()
        val stq = StringTokenizer(line)
        val k = stq.nextToken().toInt()
        // Для запрета будем сохранять прямоугольники в виде (I, J)
        val rects = ArrayList<Pair<Int, Int>>()
        for (t in 1..k) {
            val x = stq.nextToken().toInt()
            val facs = factorize(x, primes)
            var Ix = 0
            var Jx = 0
            // Для каждого простого, требуем, чтобы накопленный показатель в A и в B был не меньше
            for ((p, exp) in facs) {
                val tA = thresholdA[p]?.get(exp) ?: (n + 1)
                val tB = thresholdB[p]?.get(exp) ?: (m + 1)
                Ix = max(Ix, tA)
                Jx = max(Jx, tB)
            }
            // Если хотя бы для одного простого нужный показатель не достигнут – число x не может делить gcd,
            // поэтому такое x не порождает запрет.
            if (Ix <= n && Jx <= m) {
                rects.add(Pair(Ix, Jx))
            }
        }
        // Сортируем запретные прямоугольники по I (а при равных по J)
        rects.sortWith(Comparator { a, b ->
            if (a.first != b.first) a.first - b.first else a.second - b.second
        })
        // Построим функцию L(i) = min{J из всех прямоугольников с I<=i} с помощью разбиения [0,n] на отрезки,
        // на которых L(i) постоянна. Если для i нет запрета, L(i)=m+1.
        val segments = ArrayList<Triple<Int, Int, Int>>() // Triple(l, r, value)
        var currentMin = m + 1
        var prev = 0
        var idx = 0
        while (idx < rects.size) {
            val pos = rects[idx].first
            if (pos > prev) {
                segments.add(Triple(prev, pos, currentMin))
                prev = pos
            }
            // Обрабатываем все прямоугольники с одинаковым I
            while (idx < rects.size && rects[idx].first == pos) {
                currentMin = min(currentMin, rects[idx].second)
                idx++
            }
        }
        if (prev <= n) {
            segments.add(Triple(prev, n + 1, currentMin))
        }
        // Теперь перебираем отрезки, для каждого отрезка [l, r) значение L(i) = allowedJ.
        // Кандидат в ответ = (минимум CA на [l, r)) + bestB[allowedJ]
        var answer = Long.MAX_VALUE
        for ((l, r, allowedJ) in segments) {
            // Если область по j пуста (allowedJ == 0) – пропускаем
            if (allowedJ <= 0) continue
            val minAInSeg = segTree.query(l, r)
            val candidate = minAInSeg + bestB[allowedJ]
            answer = min(answer, candidate)
        }
        sb.append(answer).append("\n")
    }
    writer.write(sb.toString())
    writer.flush()
    writer.close()
    reader.close()
}
