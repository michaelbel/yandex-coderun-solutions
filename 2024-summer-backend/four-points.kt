import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import java.util.Arrays
import java.util.ArrayList

class Point(val x: Long, val y: Long) : Comparable<Point> {
    override fun compareTo(other: Point): Int {
        if (this.x != other.x) return this.x.compareTo(other.x)
        return this.y.compareTo(other.y)
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false
        return x == other.x && y == other.y
    }
    
    override fun hashCode(): Int {
        return 31 * x.hashCode() + y.hashCode()
    }
}

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

    fun nextLong(): Long? {
        val s = next()
        if (s == "") return null
        return s.toLong()
    }

    val nStr = next()
    if (nStr == "") return
    val n = nStr.toInt()

    val rawPoints = Array(n) {
        val x = nextLong() ?: 0L
        val y = nextLong() ?: 0L
        Point(x, y)
    }

    Arrays.sort(rawPoints)
    val uniquePoints = ArrayList<Point>(n)
    if (n > 0) {
        uniquePoints.add(rawPoints[0])
        for (i in 1 until n) {
            val curr = rawPoints[i]
            val prev = rawPoints[i - 1]
            if (curr.x != prev.x || curr.y != prev.y) {
                uniquePoints.add(curr)
            }
        }
    }

    if (uniquePoints.size < 4) {
        println("No")
        return
    }

    val layer1 = buildStrictConvexHull(uniquePoints)

    if (layer1.size >= 4) {
        println("Yes")
        return
    }
    
    if (layer1.size <= 2) {
        println("No")
        return
    }

    val layer1Set = HashSet(layer1)
    val remainingPoints = ArrayList<Point>()
    
    for (p in uniquePoints) {
        if (!layer1Set.contains(p)) {
            remainingPoints.add(p)
        }
    }

    if (remainingPoints.isEmpty()) {
        println("No")
        return
    }

    val layer2 = buildStrictConvexHull(remainingPoints)

    if (layer2.size >= 3) {
        println("Yes")
        return
    }

    val candidates = ArrayList<Point>()
    candidates.addAll(layer1)
    candidates.addAll(layer2)

    if (checkSmallSet(candidates)) {
        println("Yes")
    } else {
        println("No")
    }
}

fun crossProduct(a: Point, b: Point, c: Point): Long {
    return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)
}

fun buildStrictConvexHull(points: List<Point>): List<Point> {
    val n = points.size
    if (n <= 2) return points
    
    val h = ArrayList<Point>(n * 2)
    
    for (p in points) {
        while (h.size >= 2) {
            val p1 = h[h.size - 2]
            val p2 = h[h.size - 1]
            if (crossProduct(p1, p2, p) <= 0) {
                h.removeAt(h.size - 1)
            } else {
                break
            }
        }
        h.add(p)
    }

    val lowerHullSize = h.size
    for (i in n - 2 downTo 0) {
        val p = points[i]
        while (h.size > lowerHullSize) {
            val p1 = h[h.size - 2]
            val p2 = h[h.size - 1]
            if (crossProduct(p1, p2, p) <= 0) {
                h.removeAt(h.size - 1)
            } else {
                break
            }
        }
        h.add(p)
    }

    if (h.size > 1) {
        h.removeAt(h.size - 1)
    }
    
    return h
}

fun checkSmallSet(pts: List<Point>): Boolean {
    val k = pts.size
    if (k < 4) return false
    
    for (i in 0 until k) {
        for (j in i + 1 until k) {
            for (l in j + 1 until k) {
                for (m in l + 1 until k) {
                    val subset = listOf(pts[i], pts[j], pts[l], pts[m]).sorted()
                    val hull = buildStrictConvexHull(subset)
                    if (hull.size == 4) return true
                }
            }
        }
    }
    return false
}
