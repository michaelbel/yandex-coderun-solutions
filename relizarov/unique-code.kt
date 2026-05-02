import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import java.util.Arrays
import kotlin.math.abs

// Глобальные константы для ускорения (избегаем аллокаций внутри цикла)
const val MAX_LETTERS = 10
const val ALPHABET_SIZE = 26

fun main() {
    val scanner = FastScanner()
    
    // Чтение входных данных
    val w1 = scanner.next() ?: return
    val w2 = scanner.next() ?: return
    val n = scanner.nextInt()
    
    // Предварительный расчет весов для W1 и W2
    // weight[char] += 10^pos
    val baseWeights = LongArray(ALPHABET_SIZE)
    val basePresent = BooleanArray(ALPHABET_SIZE)
    val leadingLetterFlags = BooleanArray(ALPHABET_SIZE) // true, если буква стоит первой
    
    // Функция для добавления весов слова
    fun addWeights(word: String, sign: Long) {
        var powerOf10 = 1L
        for (i in word.length - 1 downTo 0) {
            val charIdx = word[i] - 'A'
            baseWeights[charIdx] += sign * powerOf10
            basePresent[charIdx] = true
            powerOf10 *= 10
        }
        if (word.isNotEmpty()) {
            leadingLetterFlags[word[0] - 'A'] = true
        }
    }

    addWeights(w1, 1L)
    addWeights(w2, 1L)

    val maxLenInput = maxOf(w1.length, w2.length)
    val results = ArrayList<String>()
    
    // Переиспользуемые структуры данных для Solver
    val charMap = IntArray(ALPHABET_SIZE)
    val currentWeights = LongArray(MAX_LETTERS + 2) // + запас
    val currentLeading = BooleanArray(MAX_LETTERS + 2)
    val suffixMax = LongArray(MAX_LETTERS + 2)
    val nodes = Array(MAX_LETTERS + 2) { Node() }
    
    // Обработка словаря
    for (i in 0 until n) {
        val candidate = scanner.next() ?: break
        val candLen = candidate.length
        
        // Фильтр 1: Длина суммы двух чисел может быть равна maxLen или maxLen + 1
        if (candLen < maxLenInput || candLen > maxLenInput + 1) continue
        
        // Быстрая проверка на количество уникальных букв и формирование локальных данных
        if (solveForCandidate(
                candidate, 
                w1, w2, // нужны только для проверки leading char, веса уже в baseWeights
                baseWeights, basePresent, leadingLetterFlags,
                charMap, currentWeights, currentLeading, suffixMax, nodes
            )) {
            results.add(candidate)
        }
    }

    // Вывод результата
    val out = StringBuilder()
    out.append(results.size).append('\n')
    for (res in results) {
        out.append(res).append('\n')
    }
    print(out)
}

// Вспомогательный класс для сортировки букв
class Node {
    var originalCharIdx: Int = 0
    var weight: Long = 0L
}

// --- Логика решения ---

fun solveForCandidate(
    cand: String,
    w1: String, w2: String,
    baseWeights: LongArray,
    basePresent: BooleanArray,
    baseLeading: BooleanArray,
    // Переиспользуемые буферы:
    charMap: IntArray,
    currentWeights: LongArray,
    currentLeading: BooleanArray,
    suffixMax: LongArray,
    nodes: Array<Node>
): Boolean {
    
    // 1. Идентифицируем уникальные буквы и считаем итоговые веса
    // Сброс маппинга
    Arrays.fill(charMap, -1)
    
    var uniqueCount = 0
    
    // Добавляем буквы из W1 и W2 (те, что есть в базе)
    for (c in 0 until ALPHABET_SIZE) {
        if (basePresent[c]) {
            charMap[c] = uniqueCount++
        }
    }
    
    // Добавляем буквы из кандидата и вычитаем их веса
    // Вес кандидата считается "на лету", веса W1/W2 берутся из baseWeights
    var powerOf10 = 1L
    for (i in cand.length - 1 downTo 0) {
        val charIdx = cand[i] - 'A'
        if (charMap[charIdx] == -1) {
            charMap[charIdx] = uniqueCount++
        }
        powerOf10 *= 10
    }
    
    // Фильтр 2: Слишком много уникальных букв
    if (uniqueCount > 10) return false

    // Заполняем веса и флаги leading для текущего набора букв
    // Сначала копируем базовые веса
    for (c in 0 until ALPHABET_SIZE) {
        val id = charMap[c]
        if (id != -1) {
            currentWeights[id] = baseWeights[c]
            currentLeading[id] = baseLeading[c]
        }
    }
    
    // Вычитаем веса кандидата
    powerOf10 = 1L
    for (i in cand.length - 1 downTo 0) {
        val charIdx = cand[i] - 'A'
        val id = charMap[charIdx]
        currentWeights[id] -= powerOf10
        powerOf10 *= 10
    }
    if (cand.isNotEmpty()) {
        val leadIdx = charMap[cand[0] - 'A']
        currentLeading[leadIdx] = true
    }

    // 2. Сортировка букв (эвристика: сначала самые "тяжелые" по модулю веса)
    for (i in 0 until uniqueCount) {
        nodes[i].originalCharIdx = i // здесь i - это локальный id (0..uniqueCount-1)
        nodes[i].weight = currentWeights[i]
    }
    
    // Сортировка пузырьком или вставками (N <= 10, это быстрее чем QuickSort)
    // Сортируем массив ссылок/объектов nodes
    for (i in 0 until uniqueCount) {
        for (j in i + 1 until uniqueCount) {
            if (abs(nodes[j].weight) > abs(nodes[i].weight)) {
                val tempIdx = nodes[i].originalCharIdx
                val tempW = nodes[i].weight
                nodes[i].originalCharIdx = nodes[j].originalCharIdx
                nodes[i].weight = nodes[j].weight
                nodes[j].originalCharIdx = tempIdx
                nodes[j].weight = tempW
            }
        }
    }

    // Перекладываем отсортированные данные обратно в линейные массивы для быстрого доступа в DFS
    // Используем suffixMax как временное хранилище порядка, чтобы не аллоцировать int[]
    // Но лучше просто обновить currentWeights и currentLeading согласно новому порядку
    // Чтобы не ломать логику, просто используем nodes внутри DFS косвенно или перестроим массивы.
    // Эффективнее перестроить массивы, чтобы доступ был прямым по индексу глубины рекурсии.
    
    val sortedWeights = LongArray(uniqueCount)
    val sortedLeading = BooleanArray(uniqueCount)
    
    for (i in 0 until uniqueCount) {
        sortedWeights[i] = nodes[i].weight
        val originalId = nodes[i].originalCharIdx
        sortedLeading[i] = currentLeading[originalId]
    }

    // 3. Расчет суффиксных границ (Suffix Bounds)
    // suffixMax[i] = максимальная сумма, которую можно получить оставшимися буквами (i..end)
    // если все они возьмут цифру 9.
    suffixMax[uniqueCount] = 0
    for (i in uniqueCount - 1 downTo 0) {
        suffixMax[i] = suffixMax[i + 1] + abs(sortedWeights[i]) * 9
    }

    // 4. Запуск DFS
    var solutionCount = 0

    fun dfs(idx: Int, usedMask: Int, currentSum: Long) {
        if (solutionCount >= 2) return // Early exit
        
        if (idx == uniqueCount) {
            if (currentSum == 0L) {
                solutionCount++
            }
            return
        }

        // Отсечение (Pruning)
        // Если модуль текущей суммы больше, чем теоретически возможная коррекция оставшимися весами,
        // то мы никогда не вернемся к нулю.
        if (abs(currentSum) > suffixMax[idx]) return

        val weight = sortedWeights[idx]
        val isLeading = sortedLeading[idx]
        
        // Пробуем цифры
        val startDigit = if (isLeading) 1 else 0
        
        for (digit in startDigit..9) {
            if ((usedMask shr digit) and 1 == 0) { // Если цифра не использована
                dfs(idx + 1, usedMask or (1 shl digit), currentSum + weight * digit)
                if (solutionCount >= 2) return
            }
        }
    }

    dfs(0, 0, 0L)
    return solutionCount == 1
}

// --- Класс для быстрого ввода ---
class FastScanner {
    var tokenizer = StringTokenizer("")
    var reader = BufferedReader(InputStreamReader(System.`in`))

    fun next(): String? {
        while (!tokenizer.hasMoreTokens()) {
            val line = reader.readLine() ?: return null
            tokenizer = StringTokenizer(line)
        }
        return tokenizer.nextToken()
    }

    fun nextInt(): Int {
        return next()?.toInt() ?: 0
    }
}
