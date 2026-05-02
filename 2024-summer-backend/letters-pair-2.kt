import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

const val MOD = 998244353L

// Быстрое возведение в степень по модулю.
fun modPow(a: Long, b: Long, mod: Long): Long {
    var base = a % mod
    var exp = b
    var result: Long = 1
    while(exp > 0) {
        if(exp and 1L == 1L) result = (result * base) % mod
        base = (base * base) % mod
        exp = exp shr 1
    }
    return result
}

// Поиск в глубину для проверки связности графа.
fun dfs(v: Int, graph: Array<MutableList<Int>>, visited: BooleanArray) {
    visited[v] = true
    for (w in graph[v]) {
        if (!visited[w]) dfs(w, graph, visited)
    }
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    // Считываем n и k.
    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val k = firstLine[1].toLong()

    // Значения для выбора четного и нечётного требования:
    // Рассматриваем числа от 1 до k.
    // Если число чётное, то оно попадает в группу "even", иначе – "odd".
    val evenCount: Long
    val oddCount: Long
    if(k % 2L == 0L) {
        evenCount = k / 2
        oddCount = k / 2
    } else {
        oddCount = (k + 1) / 2
        evenCount = (k - 1) / 2
    }
    
    // Списки для разбиения подстрок по типу:
    // symmetric: строки вида AA (одинаковые символы)
    // asymm: пары, где первый символ отличается от второго.
    var symmetricCount = 0
    val asymmEdges = ArrayList<Pair<Int, Int>>()
    
    // Для проверки связности будем строить неориентированный граф на 20 вершинах (буквы A..T).
    // Для каждой асимметричной пары добавляем ребро между соответствующими вершинами.
    // Для симметричных пар (например, AA) отмечаем, что вершина A присутствует.
    val used = BooleanArray(20) { false }  // метка, что буква участвует хотя бы в одной строке
    val graph = Array(20) { mutableListOf<Int>() }
    
    val parts = reader.readLine().split(" ")
    for (s in parts) {
        // Перевод символов в индексы: 'A' -> 0, 'B' -> 1, …, 'T' -> 19.
        val u = s[0] - 'A'
        val v = s[1] - 'A'
        used[u] = true
        used[v] = true
        if(u == v) {
            // Симметричная пара – не влияет на разность степеней, но даёт множитель k
            symmetricCount++
        } else {
            // Асимметричная пара.
            asymmEdges.add(Pair(u, v))
            // Для неориентированного графа добавляем ребро в обе стороны.
            graph[u].add(v)
            graph[v].add(u)
        }
    }
    
    // Для проверки связности рассматривать будем все вершины,
    // в которых встречается хотя бы одна допустимая подстрока.
    // Если таких вершин больше одной, то они должны быть все связаны асимметричными ребрами.
    // Заметим, что симметричные пары (циклы) не соединяют вершины,
    // поэтому если в несколько вершин встречаются только симметричные подстроки,
    // то нельзя объединить их в одну строку.
    val verticesUsed = (0 until 20).filter { used[it] }
    if(verticesUsed.isNotEmpty()) {
        val visited = BooleanArray(20) { false }
        // Запускаем DFS от первой используемой вершины.
        dfs(verticesUsed[0], graph, visited)
        // Если найдётся вершина, используемая в каком-либо требовании, которая не достижима,
        // то граф не связен – ответ 0.
        for(v in verticesUsed) {
            if (!visited[v]) {
                writer.write("0")
                writer.newLine()
                writer.flush()
                return
            }
        }
    }
    
    // Теперь считаем количество способов выбрать требования для асимметричных пар.
    // Для каждой асимметричной пары требуемое число c выбирается из [1,k].
    // При этом, если c чётное – «вес» выбора равен evenCount, если нечётное – oddCount.
    // При формировании строки w (которая должна иметь все и только данные подстроки)
    // условие существования Эйлерова (трейла) зависит только от чётности
    // количества "odd" выборов, поскольку вклад для каждой пары равен:
    //    2*x - c, что является чётным, если c чётное, и нечётным если c нечётное.
    // Для каждого асимметричного ребра при «переключении» мы будем менять биты для обеих вершин.
    // Используем DP по битовым маскам на 20 вершин. 
    // Состояние dp[mask] хранит суммарный вес (с учётом модулей) всех способов выбрать для уже обработанных рёбер такой набор,
    // что для каждой вершины v бит mask[v] = 1, если число асимметричных требований с нечётным c (инцидентных v)
    // нечетно, и 0 иначе.
    
    val fullSize = 1 shl 20  // 2^20
    var dp = LongArray(fullSize)
    dp[0] = 1L
    
    // Для каждого асимметричного ребра (u,v) обновляем состояния:
    // Если выбираем для ребра c чётное, то вклад E и состояние не переключается.
    // Если выбираем нечётное c, то вес умножается на oddCount и состояние меняется:
    // переключаются у соответствующих вершин (т.е. mask XOR ((1<<u) OR (1<<v))).
    for (edge in asymmEdges) {
        val u = edge.first
        val v = edge.second
        val toggle = (1 shl u) or (1 shl v)
        val newDp = LongArray(fullSize)
        for (mask in 0 until fullSize) {
            val ways = dp[mask]
            if(ways != 0L) {
                // Не менять бит: выбираем c чётное (E = evenCount)
                newDp[mask] = (newDp[mask] + ways * evenCount) % MOD
                // Переключить: выбираем c нечётное (O = oddCount)
                newDp[mask xor toggle] = (newDp[mask xor toggle] + ways * oddCount) % MOD
            }
        }
        dp = newDp
    }
    
    // Теперь нас интересуют только те маски, при которых условие Эйлерова тропы выполняется.
    // Возможны два случая:
    // 1) Эйлеров цикл: для всех вершин разность степеней равна 0 – это соответствует mask == 0.
    // 2) Эйлерова путь (не цикл): ровно одна вершина имеет излишек +1, а одна – -1,
    //    что (в терминах чётности) означает, что ровно две вершины имеют нечетное число "odd" требований.
    var waysAsymm: Long = dp[0]
    for(mask in 0 until fullSize) {
        // Если в маске ровно 2 единицы – это кандидат для Эйлерова пути.
        if (Integer.bitCount(mask) == 2) {
            waysAsymm = (waysAsymm + dp[mask]) % MOD
        }
    }
    
    // Для симметричных пар независимо можно выбрать любое требование от 1 до k,
    // поэтому для каждой такой строки множитель равен k.
    val waysSym = modPow(k, symmetricCount.toLong(), MOD)
    
    val ans = (waysAsymm * waysSym) % MOD
    writer.write(ans.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
