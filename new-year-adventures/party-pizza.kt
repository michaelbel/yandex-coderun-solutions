import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`), 1 shl 20)
    val writer = BufferedWriter(OutputStreamWriter(System.out), 1 shl 20)

    val stNM = StringTokenizer(reader.readLine())
    val n = stNM.nextToken().toInt()
    val m = stNM.nextToken().toInt()

    val a = IntArray(n + 1)
    val stA = StringTokenizer(reader.readLine())
    for (i in 1..n) {
        a[i] = stA.nextToken().toInt()
    }

    val pos = Array(n + 1) { mutableListOf<Int>() }
    for (i in 1..n) {
        pos[a[i]].add(i)
    }

    val size = 1 shl (32 - Integer.numberOfLeadingZeros(n - 1))
    val treeSize = size shl 1
    val cand = IntArray(treeSize)
    val cnt = IntArray(treeSize)

    for (i in 0 until n) {
        cand[size + i] = a[i + 1]
        cnt[size + i] = 1
    }
    for (i in size - 1 downTo 1) {
        val lc = i shl 1
        val rc = lc or 1
        val c1 = cand[lc]; val c2 = cand[rc]
        val k1 = cnt[lc]; val k2 = cnt[rc]
        if (c1 == c2) {
            cand[i] = c1
            cnt[i] = k1 + k2
        } else if (k1 > k2) {
            cand[i] = c1
            cnt[i] = k1 - k2
        } else {
            cand[i] = c2
            cnt[i] = k2 - k1
        }
    }

    fun query(l0: Int, r0: Int): Int {
        var l = l0 + size - 1
        var r = r0 + size - 1
        var lcand = 0; var lcount = 0
        var rcand = 0; var rcount = 0

        while (l <= r) {
            if ((l and 1) == 1) {
                val vc = cand[l]; val vk = cnt[l]
                if (lcand == vc) {
                    lcount += vk
                } else if (lcount > vk) {
                    lcount -= vk
                } else {
                    lcand = vc
                    lcount = vk - lcount
                }
                l++
            }
            if ((r and 1) == 0) {
                val vc = cand[r]; val vk = cnt[r]
                if (rcand == vc) {
                    rcount += vk
                } else if (vk > rcount) {
                    rcand = vc
                    rcount = vk - rcount
                } else {
                    rcount -= vk
                }
                r--
            }
            l = l shr 1
            r = r shr 1
        }
        return if (lcand == rcand) {
            lcand
        } else if (lcount > rcount) {
            lcand
        } else {
            rcand
        }
    }

    fun lowerBound(list: List<Int>, key: Int): Int {
        var lo = 0
        var hi = list.size
        while (lo < hi) {
            val mid = (lo + hi) ushr 1
            if (list[mid] < key) lo = mid + 1 else hi = mid
        }
        return lo
    }
    fun upperBound(list: List<Int>, key: Int): Int {
        var lo = 0
        var hi = list.size
        while (lo < hi) {
            val mid = (lo + hi) ushr 1
            if (list[mid] <= key) lo = mid + 1 else hi = mid
        }
        return lo
    }

    repeat(m) {
        val st = StringTokenizer(reader.readLine())
        val l = st.nextToken().toInt()
        val r = st.nextToken().toInt()
        val c = query(l, r)
        if (c == 0) {
            writer.write("0\n")
        } else {
            val occ = upperBound(pos[c], r) - lowerBound(pos[c], l)
            writer.write(if (occ * 2 > r - l + 1) "$c\n" else "0\n")
        }
    }

    writer.flush()
}
