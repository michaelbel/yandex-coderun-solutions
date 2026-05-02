import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.PriorityQueue

fun gcd(x: Long, y: Long): Long {
    var a = x
    var b = y
    while (b != 0L) {
        val t = b
        b = a % b
        a = t
    }
    return a
}

data class Node(val v: Int, val d: Long) : Comparable<Node> {
    override fun compareTo(other: Node) = d.compareTo(other.d)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val N = reader.readLine().toLong()
    val parts = reader.readLine().split(" ")
    val A = parts[0].toLong()
    val B = parts[1].toLong()
    val C = parts[2].toLong()

    val g = gcd(gcd(A, B), C)
    val Np = (N - 1) / g

    val a1 = A / g
    val b1 = B / g
    val c1 = C / g
    val coins = listOf(a1, b1, c1).sorted()
    val a = coins[0]
    val b = coins[1]
    val c = coins[2]

    val aInt = a.toInt()
    val modb = (b % a).toInt()
    val modc = (c % a).toInt()

    val dist = LongArray(aInt) { Long.MAX_VALUE }
    dist[0] = 0L
    val pq = PriorityQueue<Node>()
    pq.add(Node(0, 0L))

    while (pq.isNotEmpty()) {
        val node = pq.poll()
        val u = node.v
        val du = node.d
        if (du != dist[u]) continue

        // relax edge +b
        var v = u + modb
        if (v >= aInt) v -= aInt
        var nd = du + b
        if (nd < dist[v]) {
            dist[v] = nd
            pq.add(Node(v, nd))
        }

        // relax edge +c
        v = u + modc
        if (v >= aInt) v -= aInt
        nd = du + c
        if (nd < dist[v]) {
            dist[v] = nd
            pq.add(Node(v, nd))
        }
    }

    var total = 0L
    for (r in 0 until aInt) {
        val dr = dist[r]
        if (dr > Np) continue
        total += ((Np - dr) / a + 1)
    }

    writer.write(total.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
