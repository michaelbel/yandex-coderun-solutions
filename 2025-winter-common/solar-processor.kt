import java.io.BufferedInputStream
import java.lang.StringBuilder

private const val SPLIT = 19
private const val LOW_MASK = (1 shl SPLIT) - 1
private const val GROUPS = 1 shl (30 - SPLIT)
private const val MAX_MEX = 1 shl SPLIT

private class FastScanner {
    private val input = BufferedInputStream(System.`in`)
    private val buffer = ByteArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readByte(): Int {
        if (ptr >= len) {
            len = input.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].toInt()
    }

    fun nextInt(): Int {
        var c: Int
        do c = readByte() while (c <= 32 && c >= 0)
        var sign = 1
        var res = 0
        var ch = c
        if (ch == '-'.code) {
            sign = -1
            ch = readByte()
        }
        while (ch > 32 && ch >= 0) {
            res = res * 10 + (ch - '0'.code)
            ch = readByte()
        }
        return if (sign == 1) res else -res
    }
}

private class IntIntMap(initialCap: Int) {
    private var cap = 1
    private var mask: Int
    private var keys: IntArray
    private var vals: IntArray
    private var used = 0
    private var threshold: Int

    init {
        while (cap < initialCap) cap = cap shl 1
        mask = cap - 1
        keys = IntArray(cap)
        vals = IntArray(cap)
        threshold = (cap * 7) / 10
    }

    private fun mix(x: Int): Int {
        var h = x * -0x7a143595
        h = h xor (h ushr 16)
        return h
    }

    private fun rehash() {
        val oldKeys = keys
        val oldVals = vals
        cap = cap shl 1
        mask = cap - 1
        keys = IntArray(cap)
        vals = IntArray(cap)
        used = 0
        threshold = (cap * 7) / 10
        for (i in oldKeys.indices) {
            val k = oldKeys[i]
            if (k != 0) {
                val key = k - 1
                val v = oldVals[i]
                put(key, v)
            }
        }
    }

    private fun put(key: Int, value: Int) {
        var idx = mix(key) and mask
        val stored = key + 1
        while (true) {
            val k = keys[idx]
            if (k == 0) {
                keys[idx] = stored
                vals[idx] = value
                used++
                return
            }
            if (k == stored) {
                vals[idx] = value
                return
            }
            idx = (idx + 1) and mask
        }
    }

    fun add(key: Int, delta: Int): Int {
        if (used >= threshold) rehash()
        var idx = mix(key) and mask
        val stored = key + 1
        while (true) {
            val k = keys[idx]
            if (k == 0) {
                keys[idx] = stored
                vals[idx] = delta
                used++
                return delta
            }
            if (k == stored) {
                val nv = vals[idx] + delta
                vals[idx] = nv
                return nv
            }
            idx = (idx + 1) and mask
        }
    }

    fun get(key: Int): Int {
        var idx = mix(key) and mask
        val stored = key + 1
        while (true) {
            val k = keys[idx]
            if (k == 0) return 0
            if (k == stored) return vals[idx]
            idx = (idx + 1) and mask
        }
    }
}

private class LowTrie {
    private var left = IntArray(GROUPS + 5)
    private var right = IntArray(GROUPS + 5)
    private var cnt = IntArray(GROUPS + 5)
    private var dp = IntArray(GROUPS + 5)
    private var nodeCnt = GROUPS

    fun reset() {
        nodeCnt = GROUPS
        for (i in 1..GROUPS) {
            left[i] = 0
            right[i] = 0
            cnt[i] = 0
            dp[i] = 0
        }
    }

    private fun ensureCap(need: Int) {
        if (need < left.size) return
        var ncap = left.size
        while (need >= ncap) ncap = ncap shl 1
        left = left.copyOf(ncap)
        right = right.copyOf(ncap)
        cnt = cnt.copyOf(ncap)
        dp = dp.copyOf(ncap)
    }

    private fun newNode(): Int {
        val id = ++nodeCnt
        ensureCap(id + 1)
        left[id] = 0
        right[id] = 0
        cnt[id] = 0
        dp[id] = 0
        return id
    }

    private fun recompute(node: Int, bitIdx: Int) {
        var l = left[node]
        var r = right[node]
        if (l != 0 && cnt[l] == 0) {
            left[node] = 0
            l = 0
        }
        if (r != 0 && cnt[r] == 0) {
            right[node] = 0
            r = 0
        }

        val size = 1 shl bitIdx
        val dpL = if (l == 0) 0 else dp[l]
        val dpR = if (r == 0) 0 else dp[r]
        val fullL = l != 0 && cnt[l] == size
        val fullR = r != 0 && cnt[r] == size

        val res0 = if (fullL) size + dpR else dpL
        val res1 = if (fullR) size + dpL else dpR
        dp[node] = if (res0 >= res1) res0 else res1
    }

    fun applyDistinct(group: Int, low: Int, delta: Int) {
        val root = group + 1
        val path = IntArray(SPLIT + 1)
        var pLen = 0
        var v = root
        path[pLen++] = v

        var bit = SPLIT - 1
        while (bit >= 0) {
            val dir = (low ushr bit) and 1
            var nxt = if (dir == 0) left[v] else right[v]
            if (nxt == 0) {
                if (delta < 0) return
                nxt = newNode()
                if (dir == 0) left[v] = nxt else right[v] = nxt
            }
            v = nxt
            path[pLen++] = v
            bit--
        }

        for (i in 0 until pLen) cnt[path[i]] += delta

        var depth = pLen - 2
        while (depth >= 0) {
            val node = path[depth]
            val bitIdx = SPLIT - 1 - depth
            recompute(node, bitIdx)
            depth--
        }
    }

    fun groupBest(group: Int): Int = dp[group + 1]
}

fun main() {
    val fs = FastScanner()
    val t = fs.nextInt()
    val out = StringBuilder()

    val trie = LowTrie()

    repeat(t) {
        val n = fs.nextInt()
        val q = fs.nextInt()
        val a = IntArray(n)
        for (i in 0 until n) a[i] = fs.nextInt()

        trie.reset()

        val est = (n + q) * 2 + 8
        val freq = IntIntMap(est)

        val best = IntArray(GROUPS)
        val freqBest = IntArray(MAX_MEX + 1)
        freqBest[0] = GROUPS
        var curMax = 0

        fun updateGroupBest(g: Int) {
            val old = best[g]
            val nw = trie.groupBest(g)
            if (old == nw) return
            freqBest[old]--
            freqBest[nw]++
            best[g] = nw
            if (nw > curMax) {
                curMax = nw
            } else {
                while (curMax > 0 && freqBest[curMax] == 0) curMax--
            }
        }

        for (i in 0 until n) {
            val v = a[i]
            val before = freq.get(v)
            val after = freq.add(v, 1)
            if (before == 0 && after == 1) {
                val g = v ushr SPLIT
                val low = v and LOW_MASK
                trie.applyDistinct(g, low, 1)
                updateGroupBest(g)
            }
        }

        out.append(curMax).append('\n')

        repeat(q) {
            val j = fs.nextInt() - 1
            val v = fs.nextInt()
            val oldVal = a[j]
            if (oldVal != v) {
                val oldCnt = freq.get(oldVal)
                val newOldCnt = freq.add(oldVal, -1)
                if (oldCnt == 1 && newOldCnt == 0) {
                    val g = oldVal ushr SPLIT
                    val low = oldVal and LOW_MASK
                    trie.applyDistinct(g, low, -1)
                    updateGroupBest(g)
                }

                val beforeNew = freq.get(v)
                val afterNew = freq.add(v, 1)
                if (beforeNew == 0 && afterNew == 1) {
                    val g = v ushr SPLIT
                    val low = v and LOW_MASK
                    trie.applyDistinct(g, low, 1)
                    updateGroupBest(g)
                }

                a[j] = v
            }
            out.append(curMax).append('\n')
        }
    }

    print(out.toString())
}
