import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s = reader.readLine() ?: ""
    val n = s.length

    // Узлы: позиция по течению (0..n) и берег: 0 — левый, 1 — правый
    // id = pos * 2 + side
    val nodeCount = (n + 1) * 2
    val graph = Array(nodeCount) { mutableListOf<IntArray>() }

    fun addEdge(from: Int, to: Int, w: Int) {
        graph[from].add(intArrayOf(to, w))
    }

    // Переходы через основную реку на каждом "срезе" (позиции)
    for (pos in 0..n) {
        val left = pos * 2
        val right = pos * 2 + 1
        // переходы L <-> R, стоимость 1
        addEdge(left, right, 1)
        addEdge(right, left, 1)
    }

    // Переходы вдоль берега между притоками
    for (i in 0 until n) {
        val c = s[i]
        val posFrom = i
        val posTo = i + 1

        val leftFrom = posFrom * 2
        val rightFrom = leftFrom + 1
        val leftTo = posTo * 2
        val rightTo = leftTo + 1

        when (c) {
            'L' -> {
                // Приток слева: по левому берегу нужно переправиться (1),
                // по правому можно пройти бесплатно (0)
                addEdge(leftFrom, leftTo, 1)
                addEdge(rightFrom, rightTo, 0)
            }
            'R' -> {
                // Приток справа: по правому берегу нужно переправиться (1),
                // по левому можно пройти бесплатно (0)
                addEdge(rightFrom, rightTo, 1)
                addEdge(leftFrom, leftTo, 0)
            }
            'B' -> {
                // Притоки с обоих берегов: и слева, и справа надо переправляться (1)
                addEdge(leftFrom, leftTo, 1)
                addEdge(rightFrom, rightTo, 1)
            }
        }
    }

    // 0-1 BFS от (позиция 0, левый берег) до (позиция n, правый берег)
    val INF = Int.MAX_VALUE / 4
    val dist = IntArray(nodeCount) { INF }
    val deque = ArrayDeque<Int>()

    val start = 0              // pos 0, left
    val target = n * 2 + 1     // pos n, right

    dist[start] = 0
    deque.add(start)

    while (deque.isNotEmpty()) {
        val v = deque.removeFirst()
        val dv = dist[v]
        if (v == target) continue

        for (edge in graph[v]) {
            val to = edge[0]
            val w = edge[1]
            val nd = dv + w
            if (nd < dist[to]) {
                dist[to] = nd
                if (w == 0) {
                    deque.addFirst(to)
                } else {
                    deque.addLast(to)
                }
            }
        }
    }

    writer.write(dist[target].toString())
    writer.newLine()
    writer.flush()
}
