fun main() {
    val n = readln().trim().toInt()
    val out = StringBuilder()

    repeat(n) {
        val (s, t) = readln().trim().split(' ', limit = 2)

        val cntS = IntArray(26)
        val cntT = IntArray(26)
        for (ch in s) cntS[ch - 'A']++
        for (ch in t) cntT[ch - 'A']++

        var ok = true
        for (i in 0 until 26) {
            if (cntT[i] > cntS[i]) { ok = false; break }
        }
        if (!ok) {
            out.appendLine("NO")
            return@repeat
        }

        val need = cntT.clone()
        val keep = BooleanArray(s.length)
        for (i in s.lastIndex downTo 0) {
            val idx = s[i] - 'A'
            if (need[idx] > 0) {
                keep[i] = true
                need[idx]--
            }
        }

        val built = StringBuilder()
        for (i in s.indices) if (keep[i]) built.append(s[i])

        out.appendLine(if (built.toString() == t) "YES" else "NO")
    }

    print(out.toString())
}
