import java.util.ArrayDeque
import java.util.HashMap
import java.util.HashSet

fun findPath(from: String, to: String, fetchFlights: (String) -> List<String>): List<String> {
    if (from == to) return listOf(from)

    val parent = HashMap<String, String>(1024)
    val visited = HashSet<String>(1024)
    val q: ArrayDeque<String> = ArrayDeque()

    visited.add(from)
    q.addLast(from)

    while (q.isNotEmpty()) {
        val u = q.removeFirst()
        val next = fetchFlights(u)
        for (v in next) {
            if (visited.add(v)) {
                parent[v] = u
                if (v == to) {
                    val path = ArrayList<String>()
                    var cur = to
                    path.add(cur)
                    while (cur != from) {
                        val p = parent[cur] ?: return emptyList()
                        cur = p
                        path.add(cur)
                    }
                    path.reverse()
                    return path
                }
                q.addLast(v)
            }
        }
    }

    return emptyList()
}
