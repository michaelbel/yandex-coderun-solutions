fun solution(n: Int, words: Array<String>): Array<IntArray> {
    val m = words.size
    val next = IntArray(26 * (sumLen(words) + 1)) { -1 }
    var nodes = 1
    val termHead = IntArray(sumLen(words) + m + 5) { -1 }
    val termNext = IntArray(m) { -1 }
    val nodeForTerm = IntArray(m)
    for (i in 0 until m) {
        var v = 0
        val s = words[i]
        for (ch in s) {
            val c = ch.code - 97
            var to = next[v * 26 + c]
            if (to == -1) {
                to = nodes++
                next[v * 26 + c] = to
            }
            v = to
        }
        nodeForTerm[i] = v
        termNext[i] = termHead[v]
        termHead[v] = i
    }

    class IntBag {
        private var a = IntArray(4)
        var size = 0
            private set
        fun add(x: Int) {
            if (size == a.size) a = a.copyOf(a.size shl 1)
            a[size++] = x
        }
        fun pop(): Int = a[--size]
        fun appendFrom(other: IntBag) {
            if (other.size == 0) return
            val need = size + other.size
            if (need > a.size) {
                var cap = a.size
                while (cap < need) cap = cap shl 1
                a = a.copyOf(cap)
            }
            java.lang.System.arraycopy(other.a, 0, a, size, other.size)
            size += other.size
            other.size = 0
        }
    }

    val bags = arrayOfNulls<IntBag>(nodes)
    val stackNode = IntArray(nodes * 2)
    val stackState = IntArray(nodes * 2)
    var sp = 0
    stackNode[sp] = 0
    stackState[sp] = 0
    sp++
    val answer = Array(n) { IntArray(2) }
    var ansCnt = 0

    while (sp > 0) {
        sp--
        val v = stackNode[sp]
        val st = stackState[sp]
        if (st == 0) {
            stackNode[sp] = v
            stackState[sp] = 1
            sp++
            var c = 25
            while (c >= 0) {
                val to = next[v * 26 + c]
                if (to != -1) {
                    stackNode[sp] = to
                    stackState[sp] = 0
                    sp++
                }
                c--
            }
        } else {
            var base: IntBag? = null
            var c = 0
            while (c < 26) {
                val to = next[v * 26 + c]
                if (to != -1) {
                    val childBag = bags[to]
                    if (childBag != null && childBag.size > 0) {
                        if (base == null || childBag.size > base.size) {
                            val tmp = base
                            base = childBag
                            if (tmp != null && tmp.size > 0) base.appendFrom(tmp)
                        } else {
                            base.appendFrom(childBag)
                        }
                    }
                }
                c++
            }
            if (base == null) base = IntBag()
            var t = termHead[v]
            while (t != -1) {
                if (base.size > 0) {
                    val fullIdx = base.pop()
                    answer[ansCnt][0] = t + 1
                    answer[ansCnt][1] = fullIdx + 1
                    ansCnt++
                } else {
                    base.add(t)
                }
                t = termNext[t]
            }
            bags[v] = base
        }
    }
    return answer
}

private fun sumLen(words: Array<String>): Int {
    var s = 0
    for (w in words) s += w.length
    return s
}
